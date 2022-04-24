package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.R;

public class MainActivity extends AppCompatActivity {

    private ViewGroup viewRoot;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewRoot = findViewById(R.id.viewRoot);
        welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setOnClickListener(view -> showMessage(getResources().getString(R.string.welcomeMessage2)));
    }

    private void showMessage(String msg) {
//        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(viewRoot, msg,Snackbar.LENGTH_SHORT).show();
    }
}
