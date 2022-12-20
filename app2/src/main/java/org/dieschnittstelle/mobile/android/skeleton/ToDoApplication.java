package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitRemoteDataItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RoomLocalTodoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.SyncedToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.CachedToDoCRUDOperations;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ToDoApplication extends Application {

    private ToDoCRUDOperations crudOperations;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if (new Conectivity().checkConnectivity().get()) {
                ToDoCRUDOperations crudOperations = new SyncedToDoCRUDOperations(
                        new RoomLocalTodoCRUDOperations(this),
                        new RetrofitRemoteDataItemCRUDOperations());
                this.crudOperations = new CachedToDoCRUDOperations(crudOperations);
                Toast.makeText(this,"Using synced data access ...", Toast.LENGTH_LONG).show();
            } else {
                this.crudOperations = new RoomLocalTodoCRUDOperations(this);
                Toast.makeText(this,"Remote api is not accessible ...", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            throw new RuntimeException("got exception" + e  ,e);
        }
    }

    public ToDoCRUDOperations getCrudOperations() {
        return this.crudOperations;
    }

}
