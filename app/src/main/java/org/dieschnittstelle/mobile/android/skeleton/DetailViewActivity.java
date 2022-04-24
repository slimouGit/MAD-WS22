package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        saveNewItemButton = findViewById(R.id.fab_detail);
        saveNewItemButton.setOnClickListener(s -> onSaveItem());

        String item = getIntent().getStringExtra(ARG_ITEM);
        if (item != null) {
            itemNameText.setText(item);
        }
    }

    private void onSaveItem() {
        Intent returnIntent = new Intent();
        String item = itemNameText.getText().toString();
        returnIntent.putExtra(ARG_ITEM, item);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
