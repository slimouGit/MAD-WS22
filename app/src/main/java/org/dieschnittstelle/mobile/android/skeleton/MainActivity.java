package org.dieschnittstelle.mobile.android.skeleton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOGGER = "OverviewActivity";
    private ViewGroup viewRoot;
    private FloatingActionButton addNewItemButton;

    private ListView listView;
    private ArrayAdapter<ToDo> listViewAdapter;
    private List<ToDo> listViewItems = new ArrayList<>();


    private ActivityResultLauncher<Intent> detailviewForNewActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewRoot = findViewById(R.id.viewRoot);
        listView = findViewById(R.id.listView);
        addNewItemButton = findViewById(R.id.fab_add);
        addNewItemButton.setOnClickListener(v -> onAddNewItemButton());

        listViewAdapter = initialiseListViewAdapter();
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                ToDo selectedItem = listViewAdapter.getItem(pos);
                onListitemSelected(selectedItem);
            }
        });

        InitialiseActivityResultLauncher();

        Arrays.asList("Lorem", "Ipsum", "Olar", "Pipsum", "Enfis").
                stream()
                .map(name -> new ToDo(name))
                .forEach(item -> this.addListItemView(item));
    }

    @NonNull
    private ArrayAdapter<ToDo> initialiseListViewAdapter() {
        return new ArrayAdapter<>(this, R.layout.activity_main_listitem_view, listViewItems) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ToDo item = super.getItem(position);
                ViewGroup itemView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_main_listitem_view, null);
                TextView itemNameText = itemView.findViewById(R.id.itemName);
                CheckBox itemChecked = itemView.findViewById(R.id.itemChecked);
                //bind data to view elements
                itemNameText.setText(item.getName());
                itemChecked.setChecked(item.isChecked());
                return itemView;
            }
        };
    }

    private void InitialiseActivityResultLauncher() {
        this.detailviewForNewActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            ToDo item = (ToDo) result.getData().getSerializableExtra(DetailViewActivity.ARG_ITEM);
                            addListItemView(item);
                        }
                    }
                }
        );
    }

    private void addListItemView(ToDo item) {
//        TextView listItemView = (TextView) getLayoutInflater().inflate(R.layout.activity_main_listitem_view, null);
//        listItemView.setText(item);
//        listView.addView(listItemView);
//        listItemView.setOnClickListener(v -> onListitemSelected(((TextView) v).getText().toString()));
        listViewAdapter.add(item);
    }

    private void onListitemSelected(ToDo item) {
        Intent detailviewIntent = new Intent(this, DetailViewActivity.class);
        detailviewIntent.putExtra(DetailViewActivity.ARG_ITEM, item);
        startActivity(detailviewIntent);
    }

    private void showMessage(String msg) {
//        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(viewRoot, msg, Snackbar.LENGTH_SHORT).show();
    }

    private static int CALL_DETAILVIEW_FOR_NEW_ITEM = 1;

    private void onAddNewItemButton() {
        Intent detailviewIntentForAddItem = new Intent(this, DetailViewActivity.class);
//        startActivityForResult(detailviewIntentForAddItem, CALL_DETAILVIEW_FOR_NEW_ITEM);
        detailviewForNewActivityLauncher.launch(detailviewIntentForAddItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(LOGGER, "requestCode: " + requestCode);
        Log.i(LOGGER, "resultCode: " + resultCode);
        Log.i(LOGGER, "data: " + data);
        if (requestCode == CALL_DETAILVIEW_FOR_NEW_ITEM) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra(DetailViewActivity.ARG_ITEM);
//                showMessage("revieved: " + name);
//                addListItemView(name);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
