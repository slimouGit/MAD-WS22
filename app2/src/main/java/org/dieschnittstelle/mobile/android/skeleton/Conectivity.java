package org.dieschnittstelle.mobile.android.skeleton;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Conectivity {
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
