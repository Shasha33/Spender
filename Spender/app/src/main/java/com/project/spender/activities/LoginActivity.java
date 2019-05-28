package com.project.spender.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Check;
import com.project.spender.fns.api.NetworkManager;

import java.io.IOException;

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

            int result;
            try {
                result = ChecksRoller.getInstance().register(newName, newEmail, newNumber);
            } catch (IOException e) {
                Toast.makeText(LoginActivity.this, "Failed connect to server", Toast.LENGTH_LONG).show();
                Log.i(ChecksRoller.LOG_TAG, "Failed connect to server");
                return;
            }
            //(todo) introduce constants
            Log.i(ChecksRoller.LOG_TAG, "Try to register, answer is" + result);
            switch (result) {
                case 204:
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                    setRegisterMode();
                    break;
                case 409:
                    Toast.makeText(LoginActivity.this, "Such a user already exist", Toast.LENGTH_LONG).show();
                    break;
                case 500:
                    Toast.makeText(LoginActivity.this, "Incorrect number", Toast.LENGTH_LONG).show();
                    break;
                case 400:
                    Toast.makeText(LoginActivity.this, "Incorrect email", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "Unknown error\n Try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoginMode() {
        catButton.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        restore.setVisibility(View.VISIBLE);
        name.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        enter.setOnClickListener(v -> {
            String newPassword = password.getText().toString();
            String newNumber = number.getText().toString();

            if (newNumber == null && newPassword == null) {
                Toast.makeText(LoginActivity.this, "At least one field must be filled", Toast.LENGTH_SHORT).show();
            } else {
                ChecksRoller.getInstance().saveAccountInfo(newNumber, newPassword);
                finish();
            }
        });
        restore.setOnClickListener(v -> {
            String num = number.getText().toString();
            System.out.println(num);
            if (num == null) {
                Toast.makeText(LoginActivity.this, "Empty number field", Toast.LENGTH_SHORT).show();
                return;
            }
            int res = ChecksRoller.getInstance().remindPassword(num);
            Log.i(ChecksRoller.LOG_TAG, "Trying to restore password, answer is" + res);
            switch (res) {
                case -100:
                    Toast.makeText(LoginActivity.this, "Failed to connect server", Toast.LENGTH_SHORT).show();
                    break;
                case 204:
                    Toast.makeText(LoginActivity.this, "Success\nWait for sms", Toast.LENGTH_SHORT).show();
                    break;
                case 404:
                    Toast.makeText(LoginActivity.this, "Unknown or incorrect number", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
