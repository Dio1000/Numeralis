package me.dariansandru.numeralis.activities.logic;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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

import java.util.List;
import java.util.Map;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.activities.MainActivity;
import me.dariansandru.numeralis.parser.Expression;
import me.dariansandru.numeralis.utils.structures.logic.TruthTable;

public class TruthTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_truth_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TruthTableActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        EditText expressionInput = findViewById(R.id.expressionInput);
        findViewById(R.id.btnAnd).setOnClickListener(v -> expressionInput.append("∧"));
        findViewById(R.id.btnOr).setOnClickListener(v -> expressionInput.append("∨"));
        findViewById(R.id.btnImplies).setOnClickListener(v -> expressionInput.append("⇒"));
        findViewById(R.id.btnIff).setOnClickListener(v -> expressionInput.append("⇔"));
        findViewById(R.id.btnNot).setOnClickListener(v -> expressionInput.append("¬"));

        Button generateButton = findViewById(R.id.generateButton);
        LinearLayout truthTableContent = findViewById(R.id.truthTableContent);
        TextView variablesCount = findViewById(R.id.variablesCount);
        TextView errorMessage = findViewById(R.id.errorMessage);

        generateButton.setOnClickListener(v -> {
            try {
                truthTableContent.removeAllViews();
                errorMessage.setVisibility(View.GONE);

                String expressionStr = expressionInput.getText().toString().trim();
                if (expressionStr.isEmpty()) {
                    throw new IllegalArgumentException("Please enter a logical expression");
                }

                Expression expression = new Expression(expressionStr);
                TruthTable truthTable = new TruthTable(expression);
                variablesCount.setText("Variables detected: " + truthTable.getLiterals().size());

                displayTruthTable(truthTableContent, truthTable);

            }
            catch (Exception e) {
                errorMessage.setText(e.getMessage());
                errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void displayTruthTable(LinearLayout container, TruthTable truthTable) {
        List<String> literals = truthTable.getLiterals();
        List<Map<String, String>> table = truthTable.getTable();

        LinearLayout headerRow = createTableRow(true);
        for (String literal : literals) {
            TextView headerText = createTableCell(literal, true);
            headerRow.addView(headerText);
        }
        container.addView(headerRow);

        for (Map<String, String> row : table) {
            LinearLayout dataRow = createTableRow(false);
            for (String literal : literals) {
                TextView cellText = createTableCell(row.get(literal), false);
                dataRow.addView(cellText);
            }
            container.addView(dataRow);
        }
    }

    private LinearLayout createTableRow(boolean isHeader) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        if (!isHeader) row.setBackgroundColor(getResources().getColor(R.color.dark_gray));
        return row;
    }

    private TextView createTableCell(String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(isHeader ? R.color.purple_200 : R.color.white));
        textView.setTextSize(14);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(16, 8, 16, 8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textView.setLayoutParams(params);
        return textView;
    }
}