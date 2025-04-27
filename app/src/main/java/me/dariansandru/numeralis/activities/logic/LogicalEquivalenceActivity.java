package me.dariansandru.numeralis.activities.logic;

import me.dariansandru.numeralis.parser.Evaluator;
import me.dariansandru.numeralis.utils.algorithms.logic.LogicHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.utils.structures.logic.TruthTable;

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

        EditText formulaInput1 = findViewById(R.id.formulaInput1);
        EditText formulaInput2 = findViewById(R.id.formulaInput2);

        findViewById(R.id.checkEquivalenceButton).setOnClickListener(v -> {
            TextView resultView = findViewById(R.id.equivalenceResult);
            try {
                String expressionString1 = formulaInput1.getText().toString().trim();
                String expressionString2 = formulaInput2.getText().toString().trim();

                if (expressionString1.isEmpty() || expressionString2.isEmpty()) {
                    throw new IllegalArgumentException("Both formulas must be entered");
                }

                if (!Evaluator.isValidLogicExpression(expressionString1)) {
                    resultView.setText("First expression is invalid.");
                    resultView.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                    return;
                }

                if (!Evaluator.isValidLogicExpression(expressionString2)) {
                    resultView.setText("Second expression is invalid.");
                    resultView.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                    return;
                }

                Expression expression1 = new Expression(expressionString1);
                Expression expression2 = new Expression(expressionString2);

                TruthTable truthTable1 = new TruthTable(expression1);
                TruthTable truthTable2 = new TruthTable(expression2);

                boolean isEquivalent = LogicHelper.areEquivalent(truthTable1, truthTable2);

                if (isEquivalent) {
                    resultView.setText("EQUIVALENT");
                    resultView.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                } else {
                    resultView.setText("NOT EQUIVALENT");
                    resultView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }

            } catch (Exception e) {
                resultView.setText("Error: " + e.getMessage());
                resultView.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
            }
        });
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
