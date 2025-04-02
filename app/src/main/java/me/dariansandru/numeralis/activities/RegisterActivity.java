package me.dariansandru.numeralis.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.domain.Admin;
import me.dariansandru.numeralis.domain.Scientist;
import me.dariansandru.numeralis.utils.db.DBConnection;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextPassword, editTextConfirmPassword;
    private RadioButton radioButtonScientist, radioButtonAdmin;
    private TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        radioButtonScientist = findViewById(R.id.radioButtonScientist);
        radioButtonAdmin = findViewById(R.id.radioButtonAdmin);
        textViewMessage = findViewById(R.id.textViewMessage);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        DBConnection.init(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    textViewMessage.setText("All fields are required.");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    textViewMessage.setText("Passwords do not match.");
                    return;
                }

                if (radioButtonScientist.isChecked()) {
                    Scientist scientist = new Scientist(name, password);
                    DBConnection.saveScientist(scientist.getName(), scientist.getPassword());
                    Toast.makeText(RegisterActivity.this, "Scientist registered successfully!", Toast.LENGTH_SHORT).show();
                }
                else if (radioButtonAdmin.isChecked()) {
                    Admin admin = new Admin(name, password, null);
                    DBConnection.saveAdmin(admin);
                    Toast.makeText(RegisterActivity.this, "Admin registered successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    textViewMessage.setText("Please select a user type.");
                    return;
                }

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
