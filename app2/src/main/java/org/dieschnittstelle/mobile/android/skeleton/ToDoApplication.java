package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitRemoteDataItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RoomLocalTodoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.SyncedToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoCRUDOperationsImpl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ToDoApplication extends Application {

    private ToDoCRUDOperations crudOperations;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if (checkConnectivity().get()) {
                ToDoCRUDOperations crudOperations = new SyncedToDoCRUDOperations(
                        new RoomLocalTodoCRUDOperations(this),
                        new RetrofitRemoteDataItemCRUDOperations());
                this.crudOperations = new ToDoCRUDOperationsImpl(crudOperations);
                Toast.makeText(this,"Using synced data access ...", Toast.LENGTH_LONG).show();
            } else {
                this.crudOperations = new ToDoCRUDOperationsImpl(
                        new RoomLocalTodoCRUDOperations(this));
                Toast.makeText(this,"Remote api is not accessible ...", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            throw new RuntimeException("got exception" + e  ,e);
        }
    }

    public ToDoCRUDOperations getCrudOperations() {
        return this.crudOperations;
    }

    public Future<Boolean> checkConnectivity() {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("http://10.0.2.2:8080/api/todos").openConnection();
                conn.setConnectTimeout(500);
                conn.setReadTimeout(500);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                conn.getInputStream();
                result.complete(true);
            } catch (Exception e) {
                Log.e("ToDoException", "No connection" + e, e);
                result.complete(false);
            }
        }).start();
        return result;
    }
}
