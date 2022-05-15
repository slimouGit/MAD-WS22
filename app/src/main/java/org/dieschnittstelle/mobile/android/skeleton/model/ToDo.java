package org.dieschnittstelle.mobile.android.skeleton.model;

import java.io.Serializable;

public class ToDo implements Serializable {

    private String name;
    private String description;
    private boolean checked;

    public ToDo() {
    }

    public ToDo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
