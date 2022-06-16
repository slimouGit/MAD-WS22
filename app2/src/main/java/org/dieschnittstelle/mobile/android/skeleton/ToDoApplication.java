package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.util.Log;

import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitRemoteDataItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RoomLocalTodoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoCRUDOperations;

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
                this.crudOperations = new RetrofitRemoteDataItemCRUDOperations();
            } else {
                this.crudOperations = new RoomLocalTodoCRUDOperations(this);
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
