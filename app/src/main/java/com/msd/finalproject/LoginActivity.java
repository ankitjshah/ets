package com.msd.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.msd.finalproject.helper.DataBaseHelper;
import com.msd.finalproject.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    DataBaseHelper dbh = null;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        populateUserTable();
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateUser(etEmail.getText().toString(), etPassword.getText().toString());

            }
        });

    }

    public void openMainActivity(User user) {
        Intent i = new Intent(LoginActivity.this, CameraActivity.class);

        i.putExtra("loggedInUser", String.valueOf(user.getId()));

        startActivity(i);
    }

    private void validateUser(String userName, String password) {
        User user = dbh.validateUserCredentials(userName, password);

        if (user.getId() > 0) {

            Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
            openMainActivity(user);

        } else {

            Toast.makeText(getApplicationContext(), "Invalid Login Data", Toast.LENGTH_SHORT).show();

        }
    }


    private void populateUserTable() {
        dbh = new DataBaseHelper(this);
        dbh.populateUserData();
    }
}
