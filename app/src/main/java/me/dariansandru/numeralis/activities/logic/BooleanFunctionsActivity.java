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

import java.util.List;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.activities.MainActivity;
import me.dariansandru.numeralis.parser.Evaluator;
import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.parser.OperatorRegistry;
import me.dariansandru.numeralis.utils.algorithms.Splitter;

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

        findViewById(R.id.btnAnd).setOnClickListener(v -> expressionInput.append("∧"));
        findViewById(R.id.btnOr).setOnClickListener(v -> expressionInput.append("∨"));
        findViewById(R.id.btnImplies).setOnClickListener(v -> expressionInput.append("⇒"));
        findViewById(R.id.btnIff).setOnClickListener(v -> expressionInput.append("⇔"));
        findViewById(R.id.btnNot).setOnClickListener(v -> expressionInput.append("¬"));

        Button computeButton = findViewById(R.id.computeButton);
        EditText resultBox = findViewById(R.id.arithmeticResult);

        computeButton.setOnClickListener(v -> {
            String input = expressionInput.getText().toString().trim();
            if (!input.isEmpty()) {
                List<String> operators = OperatorRegistry.getLogicalOperatorSymbols();
                Expression expression = new Expression(input);
                List<Object> result = Splitter.recursiveSplit(expression, operators);

                String arithmeticExpression = Evaluator.transform(result);
                resultBox.setText(arithmeticExpression.substring(1, arithmeticExpression.length() - 1));
            } else {
                resultBox.setText("Please enter an expression.");
            }
        });
    }
}
