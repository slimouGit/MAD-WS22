package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBindingImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.util.MADAsyncOperationRunner;

public class DetailViewActivity extends AppCompatActivity implements DetailViewModel {
    public static String ARG_ITEM_ID = "itemId";
    public static int STATUS_CREATED = 42;
    public static int STATUS_UPDATED = -42;

    private String errorStatus;
    private ToDo item;
    private ActivityDetailviewBindingImpl binding;
    private MADAsyncOperationRunner operationRunner;
    private ToDoCRUDOperations crudOperations;
    private ActivityResultLauncher<Intent> selectContactLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview);

        this.selectContactLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        onContactSelected(result.getData());
                    }
                }
        );
        crudOperations = ((ToDoApplication) getApplication()).getCrudOperations();

        this.operationRunner = new MADAsyncOperationRunner(this, null);
        long itemId = getIntent().getLongExtra(ARG_ITEM_ID, -1);
        if (itemId != -1) {
            operationRunner.run(
                    () -> this.crudOperations.readToDo(itemId),
                    item -> {
                        this.item = item;
                        this.binding.setViewmodel(this);
                    });
            ;
        }

        if (this.item == null) {
            this.item = new ToDo();
            this.binding.setViewmodel(this);
        } else {
            this.binding.setViewmodel(this);
        }
        //        this.binding.setItem(this.item);
    }



    public ToDo getItem() {
        return this.item;
    }

    public void onSaveItem() {
        Intent returnIntent = new Intent();

        int resultCode = item.getId() > 0 ? STATUS_UPDATED : STATUS_CREATED;

        operationRunner.run(
                () -> item.getId() > 0 ? crudOperations.updateToDo(item) : crudOperations.createToDo(item),
                item -> {
                    this.item = item;
                    returnIntent.putExtra(ARG_ITEM_ID, this.item.getId());
                    setResult(resultCode, returnIntent);
                    finish();
                }
        );
    }

    @Override
    public boolean checkedFieldInputCompleted(View v, int actionId, boolean hasFocus, boolean isCalledFromOnFocusChange) {
        if (isCalledFromOnFocusChange
                ? !hasFocus
                : (actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_NEXT)) {
            if (item.getName().length() <= 3) {
                errorStatus = "Name too short";
                this.binding.setViewmodel(this);
            }
        }
        return false;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    @Override
    public boolean onNameFieldInputChanged() {
        if (this.errorStatus != null) {
            this.errorStatus = null;
            this.binding.setViewmodel(this);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.selecttContact) {
            selectContact();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void selectContact() {
        Intent selectContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        this.selectContactLauncher.launch(selectContactIntent);
    }

    public void onContactSelected(Intent resultData) {

    }
}
