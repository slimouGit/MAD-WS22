package org.dieschnittstelle.mobile.android.skeleton.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
        return id == toDo.id && checked == toDo.checked && favourite == toDo.favourite && Objects.equals(name, toDo.name) && Objects.equals(description, toDo.description) && Objects.equals(expiry, toDo.expiry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, checked, favourite, expiry);
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
        String formattedDate = "";
        if (expiry.matches("[0-9]+")) {
            Long unixTime = Long.valueOf(expiry);
            String formats = "dd.MM.yyyy HH:mm";
            formattedDate = new SimpleDateFormat(formats, Locale.GERMANY).format(new Date(unixTime));
        } else {
            formattedDate = expiry;
        }
        return formattedDate;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public boolean isOverdue() {
        boolean showExclamationMark = false;
        Date expiryDate = null;
        if (expiry.matches("[0-9]+")) {
            expiryDate = new Date(Long.valueOf(expiry));
        } else {
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
            try {
                expiryDate = formatter.parse(expiry);
            } catch (ParseException e) {
                e.printStackTrace();
                showExclamationMark = false;
            }
        }
        Date currentDate = new java.util.Date(System.currentTimeMillis());
        int result = expiryDate.compareTo(currentDate);
        if (result == 0 || result < 0 && !checked) {
            showExclamationMark = true;
        } else if (result > 0) {
            showExclamationMark = false;
        }
        return showExclamationMark;
    }

}
