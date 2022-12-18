package org.dieschnittstelle.mobile.android.skeleton;

import static org.dieschnittstelle.mobile.android.skeleton.MainActivity.LOGGER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBindingImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.util.MADAsyncOperationRunner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

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
    private String expiryDateTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview);

        this.selectContactLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
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
            this.item.setExpiry(String.valueOf(System.currentTimeMillis()));
            this.binding.setViewmodel(this);
        } else {
            this.binding.setViewmodel(this);
        }
        //        this.binding.setItem(this.item);
    }

    public void showDatePickerDialog() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a Date");
        MaterialDatePicker<Long> picker = builder.build();

        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(Calendar.HOUR_OF_DAY)
                .setMinute(Calendar.MINUTE)
                .build();

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("MEZ"));
                calendar.setTimeInMillis(selection);
                Timestamp ts = new Timestamp(calendar.getTimeInMillis());
                ts.setHours(materialTimePicker.getHour());
                ts.setMinutes(materialTimePicker.getMinute());
                String time = String.valueOf(ts.getTime());
                getItem().setExpiry(time);
            }
        });
        picker.show(getSupportFragmentManager(), picker.toString());
        materialTimePicker.show(getSupportFragmentManager(), materialTimePicker.toString());
    }

    private void addTime(Calendar calendar) {



    }


    public ToDo getItem() {
        return this.item;
    }

    public void onGoBack() {
        startActivity(new Intent(DetailViewActivity.this, MainActivity.class));
    }

    public void onSaveItem() {
        Intent returnIntent = new Intent();

        int resultCode = item.getId() > 0 ? STATUS_UPDATED : STATUS_CREATED;
        if (null == item.getExpiry()) {
            item.setExpiry(String.valueOf(System.currentTimeMillis()));
        }
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
        showContactDetails(resultData.getData());
    }

    private Uri latestSelectedContactUri;
    private static int REQUEST_CONTACT_PERMISSIONS_REQUES_CODE = 42;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CONTACT_PERMISSIONS_REQUES_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (latestSelectedContactUri != null) {
                    showContactDetails(latestSelectedContactUri);
                } else {
                    Toast.makeText(this, "Cannot continue", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Contacts cannot be accassed", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void showContactDetails(Uri contactUri) {

        int hasReadContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT_PERMISSIONS_REQUES_CODE);
            return;
        }

        Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
        if (cursor.moveToFirst()) {
            Log.i(LOGGER, "Moved to first element of query result");
            int contactNamePosition = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String contactName = cursor.getString(contactNamePosition);
            Log.i(LOGGER, "contactname " + contactName);
            int internalIdPosition = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            long internalId = cursor.getLong(internalIdPosition);
            Log.i(LOGGER, "internalId " + internalId);
            showContactDetailsForInternalId(internalId);
        }
    }

    public void showContactDetailsForInternalId(long internalId) {
        Cursor cursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                ContactsContract.Contacts._ID + "=?",
                new String[]{String.valueOf(internalId)}, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Log.i(LOGGER, "displayed Name " + displayName);

        } else {
            Toast.makeText(this, "No Contacts found for internalId " + internalId + "...", Toast.LENGTH_LONG).show();
            return;
        }

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                new String[]{String.valueOf(internalId)},
                null
        );
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            @SuppressLint("Range") int phoneNumberType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            Log.i(LOGGER, "got Number " + number + " of type " + (phoneNumberType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE ? "mobile" : "not mobil"));
        }
    }
}
