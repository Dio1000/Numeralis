package me.dariansandru.numeralis.activities.logic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.activities.MainActivity;
import me.dariansandru.numeralis.parser.Evaluator;
import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.parser.OperatorRegistry;
import me.dariansandru.numeralis.utils.algorithms.LogicHelper;
import me.dariansandru.numeralis.utils.algorithms.Splitter;
import me.dariansandru.numeralis.utils.structures.logic.TruthTable;

public class BooleanFunctionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_boolean_functions);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(BooleanFunctionsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        EditText expressionInput = findViewById(R.id.expressionInput);

        findViewById(R.id.btnClr).setOnClickListener(v -> {
            View currentFocus = getCurrentFocus();
            if (currentFocus instanceof EditText) {
                ((EditText) currentFocus).setText("");
            }
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
        findViewById(R.id.btnImplies).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnIff).setOnClickListener(insertSymbolListener);
        findViewById(R.id.btnNot).setOnClickListener(insertSymbolListener);

        Button computeButton = findViewById(R.id.computeButton);
        EditText resultBox = findViewById(R.id.arithmeticResult);

        computeButton.setOnClickListener(v -> {
            String input = expressionInput.getText().toString().trim();
            if (!input.isEmpty()) {
                List<String> operators = OperatorRegistry.getLogicalOperatorSymbols();
                Expression expression = new Expression(input);
                List<Object> result = Splitter.recursiveSplit(expression, operators);

                String arithmeticExpression = Evaluator.transform(result);
                resultBox.setText(arithmeticExpression);
            } else {
                resultBox.setText("Please enter an expression.");
            }
        });

        Button clearButton = findViewById(R.id.btnClr);
        clearButton.setOnClickListener(v -> {
            expressionInput.setText("");
        });

        Button cnfButton = findViewById(R.id.cnfButton);
        EditText cnfBox = findViewById(R.id.cnfResult);

        cnfButton.setOnClickListener(v -> {
            String input = expressionInput.getText().toString().trim();
            if (!input.isEmpty()) {
                Expression expression = new Expression(input);
                TruthTable truthTable = new TruthTable(expression);
                Expression cnfExpression = LogicHelper.truthTableToCNF(truthTable);

                cnfBox.setText(cnfExpression.toString());
            } else {
                cnfBox.setText("Please enter an expression.");
            }
        });

        Button dnfButton = findViewById(R.id.dnfButton);
        EditText dnfBox = findViewById(R.id.dnfResult);

        dnfButton.setOnClickListener(v -> {
            String input = expressionInput.getText().toString().trim();
            if (!input.isEmpty()) {
                Expression expression = new Expression(input);
                TruthTable truthTable = new TruthTable(expression);
                Expression dnfExpression = LogicHelper.truthTableToDNF(truthTable);

                dnfBox.setText(dnfExpression.toString());
            } else {
                dnfBox.setText("Please enter an expression.");
            }
        });
    }
}
