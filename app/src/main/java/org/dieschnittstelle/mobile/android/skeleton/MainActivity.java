package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ViewGroup viewRoot;
    private ViewGroup listView;
    private FloatingActionButton addNewItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewRoot = findViewById(R.id.viewRoot);
        listView = findViewById(R.id.listView);
        addNewItemButton = findViewById(R.id.fab_add);
        addNewItemButton.setOnClickListener(v -> onAddNewItemButton());

        Arrays.asList("Lorem", "Ipsum", "Olar", "Pipsum", "Enfis").forEach(
                item -> {
                    this.addListItemView(item);
                });
    }

    private void addListItemView(String item) {
        TextView listItemView = (TextView) getLayoutInflater().inflate(R.layout.activity_main_listitem_view, null);
        listItemView.setText(item);
        listView.addView(listItemView);
        listItemView.setOnClickListener(v -> onListitemSelected(((TextView) v).getText().toString()));
    }

    private void onListitemSelected(String item) {
        Intent detailviewIntent = new Intent(this, DetailViewActivity.class);
        detailviewIntent.putExtra(DetailViewActivity.ARG_ITEM, item);
        startActivity(detailviewIntent);
    }

    private void showMessage(String msg) {
//        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(viewRoot, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void onAddNewItemButton() {
        Intent detailviewIntentForAddItem = new Intent(this, DetailViewActivity.class);
        startActivity(detailviewIntentForAddItem);
    }
}
