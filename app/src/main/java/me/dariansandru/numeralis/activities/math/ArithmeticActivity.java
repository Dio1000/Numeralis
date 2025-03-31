package me.dariansandru.numeralis.activities.math;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.parser.Evaluator;
import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.utils.algorithms.Splitter;

public class ArithmeticActivity extends AppCompatActivity {

    private EditText expressionInput;
    private Button buttonEvaluate;
    private TextView outputView;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_arithmetic);

        expressionInput = findViewById(R.id.expressionInput);
        buttonEvaluate = findViewById(R.id.buttonEvaluate);
        outputView = findViewById(R.id.outputView);
        Button devModeButton = findViewById(R.id.devModeButton);
        TextView splitView = findViewById(R.id.splitView);
        View devPanel = findViewById(R.id.devPanel);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        devModeButton.setOnClickListener(v -> {
            if (devPanel.getVisibility() == View.VISIBLE) {
                devPanel.setVisibility(View.GONE);
            } else {
                devPanel.setVisibility(View.VISIBLE);
            }
        });

        buttonEvaluate.setOnClickListener(v -> {
            String expression = expressionInput.getText().toString().trim();

            if (expression.isEmpty()) {
                Toast.makeText(ArithmeticActivity.this, "Please enter a valid expression", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Expression expr = new Expression(expression);
                List<Object> result = Splitter.recursiveSplit(expr);
                outputView.setText("Result: " + Evaluator.evaluate(result));

                splitView.setText("Split Version: " + result.toString());
            } catch (Exception e) {
                outputView.setText("Error: " + e.getMessage());
            }
        });
    }

}