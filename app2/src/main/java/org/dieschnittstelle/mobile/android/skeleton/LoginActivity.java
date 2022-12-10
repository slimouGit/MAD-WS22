package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private TextView errorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        errorMessage = findViewById(R.id.text);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setEnabled(false);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!username.toString().isEmpty() && username.getText().toString().trim().matches(emailPattern) && !password.toString().isEmpty()) {
                    errorMessage.setText("valid email");
                    errorMessage.setTextColor(Color.GREEN);
                    login.setEnabled(true);
                } else {
                    errorMessage.setText("invalid email");
                    errorMessage.setTextColor(Color.RED);
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(username.getText().toString(), "a@b.cd") && Objects.equals(password.getText().toString(), "admin")) {
                    Toast.makeText(LoginActivity.this, "You have Authenticated Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
