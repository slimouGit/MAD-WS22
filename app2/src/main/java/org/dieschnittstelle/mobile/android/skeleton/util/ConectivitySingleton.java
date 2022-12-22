package org.dieschnittstelle.mobile.android.skeleton.util;

public class ConectivitySingleton {
    private static ConectivitySingleton INSTANCE;
    private boolean conectionAvailable = false;

    private ConectivitySingleton() {
    }

    public static ConectivitySingleton getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ConectivitySingleton();
        }

        return INSTANCE;
    }

    public boolean isConectionAvailable() {
        return conectionAvailable;
    }

    public void setConectionAvailable(boolean conectionAvailable) {
        this.conectionAvailable = conectionAvailable;
    }
}
