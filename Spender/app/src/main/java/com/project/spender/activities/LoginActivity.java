package com.project.spender.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.spender.controllers.ChecksRoller;
import com.project.spender.R;
import com.project.spender.fns.api.NetworkManager;

public class LoginActivity extends AppCompatActivity {

    private EditText password;
    private EditText number;
    private EditText name;
    private EditText email;
    private Button catButton;
    private Button enter;
    private Button register;
    private Button restore;

    private void setRegisterMode() {
        catButton.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        restore.setVisibility(View.GONE);

        enter.setOnClickListener(v -> {
            String newEmail = email.getText().toString();
            String newNumber = number.getText().toString();
            String newName = name.getText().toString();
            String result = ChecksRoller.getInstance().register(newName, newEmail, newNumber);
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            if (result.equals("Success")) {
                setLoginMode();
            }
        });
    }


    private void setLoginMode() {
        if (ChecksRoller.getInstance().getCatMode()) {
            catButton.setVisibility(View.VISIBLE);
        } else {
            catButton.setVisibility(View.GONE);
        }

        register.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        restore.setVisibility(View.VISIBLE);
        name.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        enter.setOnClickListener(v -> {
            String newPassword = password.getText().toString();
            String newNumber = number.getText().toString();

            if (newNumber == null && newPassword == null) {
                Toast.makeText(LoginActivity.this, "Both fields must be filled", Toast.LENGTH_SHORT).show();
            } else {
                ChecksRoller.getInstance().saveAccountInfo(newNumber, newPassword);
                finish();
            }
        });

        restore.setOnClickListener(v -> {
            String num = number.getText().toString();
            Toast.makeText(this, ChecksRoller.getInstance().restore(num), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        password = findViewById(R.id.password);
        number = findViewById(R.id.number);
        catButton = findViewById(R.id.im_cat);
        catButton.setOnClickListener(v -> {
            ChecksRoller.getInstance().saveAccountInfo(NetworkManager.DEFAULT_LOGIN, NetworkManager.DEFAULT_PASSWORD);
            finish();
        });

        restore = findViewById(R.id.restore_password);
        enter = findViewById(R.id.enter);

        name = findViewById(R.id.new_name);
        email = findViewById(R.id.email);

        register = findViewById(R.id.register);
        register.setOnClickListener(v -> setRegisterMode());

        setLoginMode();
    }
}
