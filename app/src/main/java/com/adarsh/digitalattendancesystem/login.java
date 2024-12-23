package com.adarsh.digitalattendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    EditText  usernameEditText;
    EditText  passwordEditText;
     Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.ID);
        passwordEditText = findViewById(R.id.Pass);
        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Example login conditions for three different users
                if (username.equals("admin") && password.equals("admin123")) {
                    // Start Class 1
                    Intent intent = new Intent(login.this, Class1.class);
                    startActivity(intent);
                } else if (username.equals("user1") && password.equals("user123")) {
                    // Start Class 2
                    Intent intent = new Intent(login.this, Analytics.class);
                    startActivity(intent);
                    finish();
                } else if (username.equals("user2") && password.equals("user2123")) {
                    // Start Class 3
                    Intent intent = new Intent(login.this, Class3.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Show error message
                    Toast.makeText(login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
