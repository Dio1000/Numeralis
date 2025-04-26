package me.dariansandru.numeralis.activities.logic;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import me.dariansandru.numeralis.R;

public class LogicalEquivalenceActivity extends AppCompatActivity {

    private EditText formulaInput1, formulaInput2;
    private Button backButton;
    private Button btnAnd, btnOr, btnNot, btnImplies, btnIff;
    private Button btnClr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logical_equivalence);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        formulaInput1 = findViewById(R.id.formulaInput1);
        formulaInput2 = findViewById(R.id.formulaInput2);
        backButton = findViewById(R.id.backButton);

        btnAnd = findViewById(R.id.btnAnd);
        btnOr = findViewById(R.id.btnOr);
        btnNot = findViewById(R.id.btnNot);
        btnImplies = findViewById(R.id.btnImplies);
        btnIff = findViewById(R.id.btnIff);
        btnClr = findViewById(R.id.btnClr);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        backButton.setOnClickListener(v -> finish());

        btnAnd.setOnClickListener(v -> insertSymbolIntoFocusedEditText("∧"));
        btnOr.setOnClickListener(v -> insertSymbolIntoFocusedEditText("∨"));
        btnNot.setOnClickListener(v -> insertSymbolIntoFocusedEditText("¬"));
        btnImplies.setOnClickListener(v -> insertSymbolIntoFocusedEditText("⇒"));
        btnIff.setOnClickListener(v -> insertSymbolIntoFocusedEditText("⇔"));
        btnClr.setOnClickListener(v -> clearFocusedEditText());
    }

    private void insertSymbolIntoFocusedEditText(String symbol) {
        View currentFocus = getCurrentFocus();
        if (currentFocus instanceof EditText) {
            EditText editText = (EditText) currentFocus;
            int start = Math.max(editText.getSelectionStart(), 0);
            int end = Math.max(editText.getSelectionEnd(), 0);
            editText.getText().replace(Math.min(start, end), Math.max(start, end), symbol, 0, symbol.length());
        }
    }

    private void clearFocusedEditText() {
        View currentFocus = getCurrentFocus();
        if (currentFocus instanceof EditText) {
            ((EditText) currentFocus).setText("");
        }
    }
}
