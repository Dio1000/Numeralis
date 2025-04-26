package me.dariansandru.numeralis.activities.logic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.activities.MainActivity;
import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.utils.algorithms.LogicHelper;
import me.dariansandru.numeralis.utils.algorithms.SATSolver;
import me.dariansandru.numeralis.utils.structures.logic.TruthTable;

public class SatisfiabilityActivity extends AppCompatActivity {

    private EditText formulaInput;
    private EditText clauseInput;
    private LinearLayout resultContainer;
    private TextView resultValue;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_satisfiability);
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

        formulaInput = findViewById(R.id.formulaInput);
        clauseInput = findViewById(R.id.clausesInput);
        resultContainer = findViewById(R.id.resultContainer);
        resultValue = findViewById(R.id.resultValue);
        errorMessage = findViewById(R.id.errorMessage);

        findViewById(R.id.btnAnd).setOnClickListener(v -> formulaInput.append("∧"));
        findViewById(R.id.btnOr).setOnClickListener(v -> formulaInput.append("∨"));
        findViewById(R.id.btnImplies).setOnClickListener(v -> formulaInput.append("⇒"));
        findViewById(R.id.btnIff).setOnClickListener(v -> formulaInput.append("⇔"));
        findViewById(R.id.btnNot).setOnClickListener(v -> formulaInput.append("¬"));

        findViewById(R.id.btnAnd).setOnLongClickListener(v -> { clauseInput.append("∧"); return true; });
        findViewById(R.id.btnOr).setOnLongClickListener(v -> { clauseInput.append("∨"); return true; });
        findViewById(R.id.btnImplies).setOnLongClickListener(v -> { clauseInput.append("⇒"); return true; });
        findViewById(R.id.btnIff).setOnLongClickListener(v -> { clauseInput.append("⇔"); return true; });
        findViewById(R.id.btnNot).setOnLongClickListener(v -> { clauseInput.append("¬"); return true; });

        Button computeFormulaButton = findViewById(R.id.checkFormulaButton);
        Button computeClauseButton = findViewById(R.id.checkClausesButton);

        computeFormulaButton.setOnClickListener(v -> checkFormula());
        computeClauseButton.setOnClickListener(v -> checkClauses());
    }

    private void checkFormula() {
        String expressionString = formulaInput.getText().toString().trim();
        if (expressionString.isEmpty()) {
            showError("Please enter a formula.");
            return;
        }

        try {
            Expression expression = new Expression(expressionString);
            TruthTable truthTable = new TruthTable(expression);

            Expression cnfExpression = LogicHelper.truthTableToCNF(truthTable);
            boolean isSatisfiable = SATSolver.isSatisfiable(LogicHelper.getClausesFromCNF(cnfExpression));
            showResult(isSatisfiable);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Invalid formula syntax.");
        }
    }

    private void checkClauses() {
        String clausesString = clauseInput.getText().toString().trim();
        if (clausesString.isEmpty()) {
            showError("Please enter clauses.");
            return;
        }
        try {
            boolean isSatisfiable = SATSolver.isSatisfiable(SATSolver.parseClauses(clausesString));
            showResult(isSatisfiable);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Invalid clauses format.");
        }
    }

    private void showResult(boolean satisfiable) {
        resultContainer.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);

        if (satisfiable) {
            resultValue.setText("SATISFIABLE");
            resultValue.setTextColor(getColor(R.color.green));
        }
        else {
            resultValue.setText("UNSATISFIABLE");
            resultValue.setTextColor(getColor(R.color.red));
        }
    }

    private void showError(String message) {
        resultContainer.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }
}
