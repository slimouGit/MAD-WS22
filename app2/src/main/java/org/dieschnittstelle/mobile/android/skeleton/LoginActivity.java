package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.dieschnittstelle.mobile.android.skeleton.util.ConectivitySingleton;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private TextView usernameErrorMessage;
    private TextView pwdErrorMessage;
    private TextView loginErrorMessage;
    boolean emailIsValid = false;
    boolean passwordIsValid = false;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(!ConectivitySingleton.getInstance().isConectionAvailable()){
            this.redirectToListView();
        };
        username = findViewById(R.id.username);
        usernameErrorMessage = findViewById(R.id.usernameErrorMessage);
        password = findViewById(R.id.password);
        pwdErrorMessage = findViewById(R.id.pwdError);
        login = findViewById(R.id.login);
        loginErrorMessage = findViewById(R.id.loginError);
        loginErrorMessage.setVisibility(View.GONE);
        login.setEnabled(false);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2}";
                    if (username.getText().toString().trim().matches(emailPattern)) {
                        usernameErrorMessage.setText("valid email");
                        usernameErrorMessage.setTextColor(Color.GREEN);
                        emailIsValid = true;
                        checkLoginButtonState();
                    } else {
                        usernameErrorMessage.setTextColor(Color.RED);
                        usernameErrorMessage.setText("invalid email");
                        emailIsValid = false;
                        login.setEnabled(false);
                    }
                } else {
                    usernameErrorMessage.setText("");
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String passwordPattern = "[0-9]{6}";
                    if (password.getText().toString().trim().matches(passwordPattern)) {
                        pwdErrorMessage.setText("valid password");
                        pwdErrorMessage.setTextColor(Color.GREEN);
                        passwordIsValid = true;
                        checkLoginButtonState();
                    } else {
                        pwdErrorMessage.setTextColor(Color.RED);
                        pwdErrorMessage.setText("invalid password");
                        passwordIsValid = false;
                        login.setEnabled(false);
                    }
                } else {
                    pwdErrorMessage.setText("");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (this.loginIsValid(username, password)) {
                    spinner=(ProgressBar)findViewById(R.id.progressBar);
                    spinner.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "You have authenticated successfully", Toast.LENGTH_LONG).show();
                    redirectToListView();
                } else {
                    loginErrorMessage.setVisibility(View.VISIBLE);
                }
            }

            private boolean loginIsValid(EditText username, EditText password) {
                final String USERNAME = "a@b.de";
                final String PASSWORD = "123456";
                return (Objects.equals(username.getText().toString(), USERNAME) && Objects.equals(password.getText().toString(), PASSWORD));
            }
        });
    }

    private void redirectToListView() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void checkLoginButtonState() {
        if(emailIsValid && passwordIsValid){
            login.setEnabled(true);
        }
    }
}
