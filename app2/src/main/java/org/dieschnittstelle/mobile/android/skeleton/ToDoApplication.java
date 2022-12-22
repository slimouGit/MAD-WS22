package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitRemoteDataItemCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RoomLocalTodoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.SyncedToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.CachedToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.util.ConectivitySingleton;

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
                ConectivitySingleton.getInstance().setConectionAvailable(true);
                this.crudOperations = new CachedToDoCRUDOperations(crudOperations);
                Toast.makeText(this,"Using synced data access ...", Toast.LENGTH_LONG).show();
            } else {
                ConectivitySingleton.getInstance().setConectionAvailable(false);
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
