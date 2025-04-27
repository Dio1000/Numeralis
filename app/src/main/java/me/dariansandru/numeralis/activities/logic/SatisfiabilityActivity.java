package me.dariansandru.numeralis.activities.logic;

import android.content.Intent;
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
import me.dariansandru.numeralis.activities.MainActivity;
import me.dariansandru.numeralis.parser.Evaluator;
import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.utils.algorithms.logic.LogicHelper;
import me.dariansandru.numeralis.utils.algorithms.logic.SATSolver;
import me.dariansandru.numeralis.utils.structures.logic.TruthTable;

public class SatisfiabilityActivity extends AppCompatActivity {

    private EditText formulaInput;
    private EditText clauseInput;
    private TextView resultDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_satisfiability);

        formulaInput = findViewById(R.id.formulaInput);
        clauseInput = findViewById(R.id.clausesInput);
        resultDisplay = findViewById(R.id.resultDisplay);

        resultDisplay.setVisibility(View.GONE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SatisfiabilityActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        View.OnClickListener insertSymbolListener = v -> {
            View currentFocus = getCurrentFocus();
            if (currentFocus instanceof EditText) {
                EditText editText = (EditText) currentFocus;
                Button button = (Button) v;
                String symbol = button.getText().toString();
                int start = Math.max(editText.getSelectionStart(), 0);
                int end = Math.max(editText.getSelectionEnd(), 0);
                editText.getText().replace(Math.min(start, end), Math.max(start, end),
                        symbol, 0, symbol.length());
            }
        };

        findViewById(R.id.btnAnd).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnOr).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnNot).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnImplies).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnIff).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnClrFormula).setOnClickListener(v -> formulaInput.setText(""));

        findViewById(R.id.btnAndClause).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnOrClause).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnNotClause).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnImpliesClause).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnIffClause).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnClrClause).setOnClickListener(v -> clauseInput.setText(""));

        Button computeFormulaButton = findViewById(R.id.checkFormulaButton);
        Button computeClauseButton = findViewById(R.id.checkClausesButton);

        computeFormulaButton.setOnClickListener(v -> checkFormula());
        computeClauseButton.setOnClickListener(v -> checkClauses());
    }

    private void checkFormula() {
        String expressionString = formulaInput.getText().toString().trim();
        if (expressionString.isEmpty()) {
            showError("Please enter a formula");
            return;
        }

        try {
            if (!Evaluator.isValidLogicExpression(expressionString)) showError("Invalid formula syntax");
            else {
                Expression expression = new Expression(expressionString);
                TruthTable truthTable = new TruthTable(expression);

                Expression cnfExpression = LogicHelper.truthTableToCNF(truthTable);
                boolean isSatisfiable = SATSolver.isSatisfiable(LogicHelper.getClausesFromCNF(cnfExpression));
                showResult(isSatisfiable);
            }

        } catch (Exception e) {
            showError("Invalid formula syntax");
        }
    }

    private void checkClauses() {
        String clausesString = clauseInput.getText().toString().trim();
        if (clausesString.isEmpty()) {
            showError("Please enter clauses");
            return;
        }
        try {
            if (!SATSolver.isValidClauseFormat(clausesString))showError("Invalid clauses format");
            else {
                boolean isSatisfiable = SATSolver.isSatisfiable(SATSolver.parseClauses(clausesString));
                showResult(isSatisfiable);
            }
        } catch (Exception e) {
            showError("Invalid clauses format");
        }
    }

    private void showResult(boolean satisfiable) {
        resultDisplay.setVisibility(View.VISIBLE);
        if (satisfiable) {
            resultDisplay.setText("SATISFIABLE");
            resultDisplay.setTextColor(getColor(R.color.green));
        } else {
            resultDisplay.setText("UNSATISFIABLE");
            resultDisplay.setTextColor(getColor(R.color.red));
        }
    }

    private void showError(String message) {
        resultDisplay.setVisibility(View.VISIBLE);
        resultDisplay.setText(message);
        resultDisplay.setTextColor(getColor(android.R.color.holo_orange_light));
    }
}
