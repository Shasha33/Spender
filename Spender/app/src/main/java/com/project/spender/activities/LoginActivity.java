package com.project.spender.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.project.spender.controllers.ChecksRoller;
import com.project.spender.R;
import com.project.spender.fns.api.NetworkManager;
import com.project.spender.fns.api.data.Status;
import com.project.spender.fns.api.data.StatusWithResponse;

/**
 * Activity for login, register and password restore actions
 */
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
            LiveData<StatusWithResponse> result = ChecksRoller.getInstance().registration(newName, newEmail, newNumber);
            result.observeForever(status -> {
                Toast.makeText(LoginActivity.this, status.getUserReadableMassage(), Toast.LENGTH_LONG).show();
                if (status.getStatus().equals(Status.SUCCESS)) {
                    setLoginMode();
                }
            });
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
            if (!newNumber.matches("\\+\\d{11}")) {
                Toast.makeText(LoginActivity.this, "invalid number format", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.matches("\\d{6}")) {
                Toast.makeText(LoginActivity.this, "invalid password format", Toast.LENGTH_SHORT).show();
            } else {
                ChecksRoller.getInstance().saveAccountInfo(newNumber, newPassword);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        restore.setOnClickListener(v -> {
            String num = number.getText().toString();
            LiveData<StatusWithResponse> result = ChecksRoller.getInstance().restore(num);
            result.observeForever(status -> Toast.makeText(LoginActivity.this,
                    status.getUserReadableMassage(), Toast.LENGTH_SHORT).show());

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
