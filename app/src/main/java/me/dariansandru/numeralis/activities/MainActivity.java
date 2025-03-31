package me.dariansandru.numeralis.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.utils.algorithms.Splitter;

public class MainActivity extends AppCompatActivity {

    private EditText expressionInput;
    private Button buttonEvaluate;
    private TextView outputView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        expressionInput = findViewById(R.id.expressionInput);
        buttonEvaluate = findViewById(R.id.buttonEvaluate);
        outputView = findViewById(R.id.outputView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonEvaluate.setOnClickListener(v -> {
            String expression = expressionInput.getText().toString().trim();

            if (expression.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a valid expression", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Expression expr = new Expression(expression);
                List<Object> result = Splitter.recursiveSplit(expr);
                outputView.setText(result.toString());
            } catch (Exception e) {
                outputView.setText("Error processing the expression: " + e.getMessage());
            }
        });
    }
}
