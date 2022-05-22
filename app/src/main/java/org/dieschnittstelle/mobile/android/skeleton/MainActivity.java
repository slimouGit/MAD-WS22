package org.dieschnittstelle.mobile.android.skeleton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityMainListitemViewBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.util.MADAsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.util.MADAsyncTask;

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

    private ToDoCRUDOperations crudOperations;
    private ProgressBar progressBar;
    private MADAsyncOperationRunner operationRunner;


    private ActivityResultLauncher<Intent> detailviewForNewActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewRoot = findViewById(R.id.viewRoot);
        listView = findViewById(R.id.listView);
        addNewItemButton = findViewById(R.id.fab_add);
        addNewItemButton.setOnClickListener(v -> onAddNewItemButton());
        progressBar = findViewById(R.id.progressBar);
        operationRunner = new MADAsyncOperationRunner(this, progressBar);
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

        crudOperations = ToDoCRUDOperationsImpl.getInstance();
        operationRunner.run(
                () -> crudOperations.readAllToDos(),
                items -> {
//                    items.forEach(item -> this.addListItemView(item));
                    items.forEach(this::addListItemView);
                });
//        progressBar.setVisibility(View.VISIBLE);
//        new Thread(() -> {
//            List<ToDo> items = crudOperations.readAllToDos();
//            runOnUiThread(() -> {
//                items.forEach(this::addListItemView);
//                progressBar.setVisibility(View.GONE);
//            });
//        }).start();

//        new MADAsyncTask<Void, Void, List<ToDo>>(){
//
//            @Override
//            protected void onPreExecute(){
//                progressBar.setVisibility(View.VISIBLE);
//            }
//            @Override
//            protected List<ToDo> doInBackground(Void... voids) {
//                return crudOperations.readAllToDos();
//            }
//            @Override
//            protected void onPostExecute(List<ToDo> toDos) {
//                toDos.forEach(item -> MainActivity.this.addListItemView(item));
//                progressBar.setVisibility(View.GONE);
//            }
//        }.execute();
    }

    @NonNull
    private ArrayAdapter<ToDo> initialiseListViewAdapter() {
        return new ArrayAdapter<>(this, R.layout.activity_main_listitem_view, listViewItems) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View existingListitemView, @NonNull ViewGroup parent) {
                ToDo item = super.getItem(position);

                ActivityMainListitemViewBinding itemBinding = existingListitemView != null
                        ? (ActivityMainListitemViewBinding) existingListitemView.getTag()
                        : DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main_listitem_view, null, false);

                itemBinding.setItem(item);
                View itemView = itemBinding.getRoot();
                itemView.setTag(itemBinding);
                return itemView;
            }
        };
    }

//    private void InitialiseActivityResultLauncher() {
//        this.detailviewForNewActivityLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            long itemId = result.getData().getLongExtra(DetailViewActivity.ARG_ITEM_ID, -1);
//                            ToDo item = crudOperations.readToDo(itemId);
//                            addListItemView(item);
//                        }
//                    }
//                }
//        );
//    }

    private void InitialiseActivityResultLauncher() {
        detailviewForNewActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                (result) -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        long itemId = result.getData().getLongExtra(DetailViewActivity.ARG_ITEM_ID, -1);
                        operationRunner.run(
                                () -> crudOperations.readToDo(itemId),
                                item -> addListItemView(item)
                        );
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
        listView.setSelection(listViewAdapter.getPosition(item));
    }

    private void onListitemSelected(ToDo item) {
        Intent detailviewIntent = new Intent(this, DetailViewActivity.class);
        detailviewIntent.putExtra(DetailViewActivity.ARG_ITEM_ID, item.getId());
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
                String name = data.getStringExtra(DetailViewActivity.ARG_ITEM_ID);
//                showMessage("revieved: " + name);
//                addListItemView(name);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
