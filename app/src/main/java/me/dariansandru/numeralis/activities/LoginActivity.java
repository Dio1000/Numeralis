package me.dariansandru.numeralis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.utils.db.DBConnection;

public class LoginActivity extends AppCompatActivity {
    private static final String ADMIN_SECRET = "1234";

    private RadioGroup userTypeGroup;
    private RadioButton radioAdmin;
    private EditText adminSecretPassword, inputName, inputPassword;
    private Button btnLogin, btnRegister, btnDevMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DBConnection.init(this);

        try {
            userTypeGroup = findViewById(R.id.userTypeGroup);
            radioAdmin = findViewById(R.id.radioAdmin);
            adminSecretPassword = findViewById(R.id.adminSecretPassword);
            inputName = findViewById(R.id.inputName);
            inputPassword = findViewById(R.id.inputPassword);
            btnLogin = findViewById(R.id.btnLogin);
            btnRegister = findViewById(R.id.btnRegister);
            btnDevMode = findViewById(R.id.btnDevMode);
        }
        catch (Exception e) {
            Toast.makeText(this, "UI initialization failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnDevMode.setOnClickListener(v -> {
            DBConnection.logAllUsers();
            Toast.makeText(LoginActivity.this, "DevMode activated. Here are the users.", Toast.LENGTH_LONG).show();
        });

        userTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            adminSecretPassword.setVisibility(checkedId == R.id.radioAdmin ? View.VISIBLE : View.GONE);
        });

        btnLogin.setOnClickListener(v -> {
            try {
                String name = inputName.getText().toString().trim();
                String password = inputPassword.getText().toString();

                if (name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Name and password are required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (radioAdmin.isChecked()) {
                    String adminPass = adminSecretPassword.getText().toString();
                    if (!adminPass.equals(ADMIN_SECRET)) {
                        showError("Incorrect admin password");
                        return;
                    }

                    String adminPassword = DBConnection.getAdminPassword(name);
                    if (adminPassword != null && adminPassword.equals(password)) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        showError("Invalid admin credentials");
                    }
                }
                else {
                    String scientistPassword = DBConnection.getScientistPassword(name);
                    if (scientistPassword != null && scientistPassword.equals(password)) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    else {
                        showError("Invalid scientist credentials");
                    }
                }
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        inputName.setText("");
        inputPassword.setText("");
        inputName.requestFocus();
    }
}
