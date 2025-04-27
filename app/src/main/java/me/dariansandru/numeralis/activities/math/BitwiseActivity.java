package me.dariansandru.numeralis.activities.math;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.activities.MainActivity;
import me.dariansandru.numeralis.utils.algorithms.BaseConverter;
import me.dariansandru.numeralis.utils.algorithms.logic.BitwiseHelper;
import me.dariansandru.numeralis.utils.structures.BaseNumber;

public class BitwiseActivity extends AppCompatActivity {

    private TextView binaryResult, decimalResult, hexResult;
    private EditText inputNumber1, inputNumber2;
    private LinearLayout resultSection;
    private TextView resultLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bitwise_calculator);

        inputNumber1 = findViewById(R.id.inputNumber1);
        inputNumber2 = findViewById(R.id.inputNumber2);
        binaryResult = findViewById(R.id.resultBinary);
        decimalResult = findViewById(R.id.resultDecimal);
        hexResult = findViewById(R.id.resultHex);
        resultSection = findViewById(R.id.resultSection);
        resultLabel = findViewById(R.id.resultLabel);

        resultLabel.setText("Result will appear here");
        binaryResult.setText("");
        decimalResult.setText("");
        hexResult.setText("");
        resultSection.setVisibility(View.VISIBLE);

        findViewById(R.id.andButton).setOnClickListener(v -> computeAND());
        findViewById(R.id.orButton).setOnClickListener(v -> computeOR());
        findViewById(R.id.xorButton).setOnClickListener(v -> computeXOR());

        findViewById(R.id.backButton).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void computeAND() {
        computeOperation(BitwiseHelper::bitwiseAND);
    }

    private void computeOR() {
        computeOperation(BitwiseHelper::bitwiseOR);
    }

    private void computeXOR() {
        computeOperation(BitwiseHelper::bitwiseXOR);
    }

    private void computeOperation(BitwiseOperation operation) {
        try {
            String num1 = inputNumber1.getText().toString();
            String num2 = inputNumber2.getText().toString();

            if (num1.isEmpty() || num2.isEmpty()) {
                showError("Please enter both binary numbers");
                return;
            }

            if (!BitwiseHelper.isBinaryNumber(num1)) {
                showError("First number is not valid binary");
                return;
            }

            if (!BitwiseHelper.isBinaryNumber(num2)) {
                showError("Second number is not valid binary");
                return;
            }

            String binary = operation.apply(num1, num2);
            BaseNumber binaryNum = new BaseNumber(binary, 2);
            BaseNumber decimalNum = new BaseNumber(String.valueOf(BaseConverter.convertToDecimal(binaryNum)), 10);
            BaseNumber hexNum = new BaseNumber(String.valueOf(BaseConverter.convertToBase(decimalNum, 16)), 16);

            showResult(binary, decimalNum.getRepresentation(), hexNum.getRepresentation());

        } catch (Exception e) {
            showError("Invalid operation: " + e.getMessage());
        }
    }

    private void showResult(String binary, String decimal, String hex) {
        resultSection.setVisibility(View.VISIBLE);
        resultLabel.setText("Result");
        binaryResult.setText(binary);
        decimalResult.setText(decimal);
        hexResult.setText(hex);

        inputNumber1.setError(null);
        inputNumber2.setError(null);
    }

    private void showError(String message) {
        resultSection.setVisibility(View.VISIBLE);
        resultLabel.setText(message);
        binaryResult.setText("");
        decimalResult.setText("");
        hexResult.setText("");

        if (inputNumber1.getText().toString().isEmpty()) inputNumber1.setError("Required");
        if (inputNumber2.getText().toString().isEmpty()) inputNumber2.setError("Required");

    }

    @FunctionalInterface
    private interface BitwiseOperation {
        String apply(String num1, String num2);
    }
}