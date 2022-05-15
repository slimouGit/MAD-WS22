package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBindingImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

public class DetailViewActivity extends AppCompatActivity implements DetailViewModel {
    public static String ARG_ITEM = "item";
    private ToDo item;
    private ActivityDetailviewBindingImpl binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detailview);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview);
        this.item = (ToDo) getIntent().getSerializableExtra(ARG_ITEM);
        if (this.item == null){
            this.item = new ToDo();
        }
        this.binding.setViewmodel(this);
    }

    public ToDo getItem(){
        return this.item;
    }

    public void onSaveItem() {
        Intent returnIntent = new Intent();


        returnIntent.putExtra(ARG_ITEM, item);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
