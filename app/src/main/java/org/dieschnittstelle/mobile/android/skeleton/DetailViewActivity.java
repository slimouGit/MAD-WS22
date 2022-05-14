package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

public class DetailViewActivity extends AppCompatActivity {
    public static String ARG_ITEM = "item";
    private EditText itemNameText;
    private EditText itemDescriptionText;
    private FloatingActionButton saveNewItemButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailview);
        itemNameText = findViewById(R.id.itemName);
        itemDescriptionText = findViewById(R.id.itemDescription);
        saveNewItemButton = findViewById(R.id.fab);
        saveNewItemButton.setOnClickListener(s -> onSaveItem());

        ToDo item = (ToDo) getIntent().getSerializableExtra(ARG_ITEM);
        if (item != null) {
            itemNameText.setText(item.getName());
            itemDescriptionText.setText(item.getDescription());
        }
    }

    private void onSaveItem() {
        Intent returnIntent = new Intent();
        String name = itemNameText.getText().toString();
        String description = itemDescriptionText.getText().toString();
        ToDo item = new ToDo(name);
        item.setDescription(description);

        returnIntent.putExtra(ARG_ITEM, item);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
