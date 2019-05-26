package com.project.spender.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.fns.api.NetworkManager;

public class LoginActivity extends AppCompatActivity {

    private EditText password;
    private EditText number;
    private Button catButton;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password = findViewById(R.id.password);
        number = findViewById(R.id.number);
        catButton = findViewById(R.id.im_cat);
        catButton.setOnClickListener(v -> {
            ChecksRoller.saveAccountInfo(NetworkManager.DEFAULT_LOGIN, NetworkManager.DEFAULT_PASSWORD);
            finish();
        });

        enter = findViewById(R.id.enter);
        enter.setOnClickListener(v -> {
            String newPassword = password.getText().toString();
            String newNumber = password.getText().toString();

            if (newNumber == null && newPassword == null) {
                Toast.makeText(LoginActivity.this, "At least one field must be filled", Toast.LENGTH_SHORT).show();
            } else {
                ChecksRoller.saveAccountInfo(newNumber, newPassword);
                finish();
            }
        });
    }
}
