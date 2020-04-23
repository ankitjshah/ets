package com.msd.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.msd.finalproject.helper.DataBaseHelper;
import com.msd.finalproject.model.User;

/**
 * LoginActivity to support login functionality
 * Users have already been hardcoded for login purpose
 * If user has logged in, then he will be redirected to Next page
 * Otherwise he will be asked to logged in to the system
 */
public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    DataBaseHelper dbh = null;
    User user = null;

    // SharedPrerence is declared to get stored user' login details
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        populateUserTable();

        sp = getSharedPreferences("login", MODE_PRIVATE);

        // Checking if user's login details exists
        if (sp.getBoolean("logged", false)) {
            String userName = sp.getString("userName", "");
            String password = sp.getString("password", "");
            User user = dbh.validateUserCredentials(userName, password);
            // Opening main activity if user has logged in
            openMainActivity(user);
        }

        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validating user's details on login button clicked
                validateUser(etEmail.getText().toString(), etPassword.getText().toString());

            }
        });

    }

    /**
     * Open main activity, once user has logged in
     *
     * @param user
     */
    public void openMainActivity(User user) {
        Intent i = new Intent(LoginActivity.this, CameraActivity.class);

        i.putExtra("loggedInUser", String.valueOf(user.getId()));

        startActivity(i);
    }

    private void validateUser(String userName, String password) {
        User user = dbh.validateUserCredentials(userName, password);

        // On successful authorization of user's details, login details will be stored in sharedpreference
        if (user.getId() > 0) {

            sp.edit().putString("userName", userName).apply();
            sp.edit().putString("password", password).apply();
            sp.edit().putBoolean("logged", true).apply();

            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
            openMainActivity(user);

        } else {

            Toast.makeText(getApplicationContext(), "Invalid Login Data", Toast.LENGTH_SHORT).show();

        }
        dbh.close();
    }


    private void populateUserTable() {
        dbh = new DataBaseHelper(this);
        dbh.populateUserData();
        dbh.close();
    }
}
