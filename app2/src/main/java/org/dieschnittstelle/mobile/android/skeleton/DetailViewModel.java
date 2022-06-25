package org.dieschnittstelle.mobile.android.skeleton;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

public interface DetailViewModel {

    public ToDo getItem();

    public void onSaveItem();

    public boolean checkedFieldInputCompleted(View v, int actionId, boolean hasFocus, boolean isCalledFromOnFocusChange);

    public String getErrorStatus();

    public boolean onNameFieldInputChanged();

}
