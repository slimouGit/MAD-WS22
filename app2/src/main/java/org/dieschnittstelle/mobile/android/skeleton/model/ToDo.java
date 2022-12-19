package org.dieschnittstelle.mobile.android.skeleton.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

@Entity
public class ToDo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    @SerializedName("done")
    private boolean checked;
    @SerializedName("favourite")
    private boolean favourite;
    private String expiry;
    private String readableExpiry;
    private boolean overdue;

    public ToDo() {
    }

    public ToDo(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDo toDo = (ToDo) o;
        return id == toDo.id && checked == toDo.checked && favourite == toDo.favourite && overdue == toDo.overdue && Objects.equals(name, toDo.name) && Objects.equals(description, toDo.description) && Objects.equals(expiry, toDo.expiry) && Objects.equals(readableExpiry, toDo.readableExpiry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, checked, favourite, expiry, readableExpiry, overdue);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getReadableExpiry() {
        Long unixTime = Long.valueOf(expiry);
        String formats = "dd.MM.yyyy HH:mm";
        String date = new SimpleDateFormat(formats, Locale.GERMANY).format(new Date(unixTime));
        return date;
    }

    public void setReadableExpiry(String readableExpiry) {
        this.readableExpiry = readableExpiry;
    }

    public boolean isOverdue() {
        boolean showExclamationMark = false;
        Date expiryDate = new Date(Long.valueOf(expiry));
        Date currentDate = new java.util.Date(System.currentTimeMillis());
        int result = expiryDate.compareTo(currentDate);
        if (result == 0 || result < 0) {
            showExclamationMark = true;
        } else if (result > 0) {
            showExclamationMark = false;
        } else {
        }
        return showExclamationMark;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }
}
