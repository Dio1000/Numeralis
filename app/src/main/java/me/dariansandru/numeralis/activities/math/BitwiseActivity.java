package me.dariansandru.numeralis.activities.math;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        findViewById(R.id.andButton).setOnClickListener(v -> computeAND());
        findViewById(R.id.orButton).setOnClickListener(v -> computeOR());
        findViewById(R.id.xorButton).setOnClickListener(v -> computeXOR());

        findViewById(R.id.backButton).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void computeAND() {
        try {
            String num1 = inputNumber1.getText().toString();
            String num2 = inputNumber2.getText().toString();

            if (!BitwiseHelper.isBinaryNumber(num1)) {
                throw new IllegalArgumentException("First number is not binary");
            }
            if (!BitwiseHelper.isBinaryNumber(num2)) {
                throw new IllegalArgumentException("Second number is not binary");
            }

            String binary = BitwiseHelper.bitwiseAND(num1, num2);
            BaseNumber binaryNum = new BaseNumber(binary, 2);
            BaseNumber decimalNum = new BaseNumber(String.valueOf(BaseConverter.convertToDecimal(binaryNum)), 10);
            BaseNumber hexNum = new BaseNumber(String.valueOf(BaseConverter.convertToBase(decimalNum, 16)), 16);

            resultSection.setVisibility(View.VISIBLE);
            binaryResult.setText(binary);
            decimalResult.setText(decimalNum.getRepresentation());
            hexResult.setText(hexNum.getRepresentation());
        }
        catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void computeOR() {
        try {
            String num1 = inputNumber1.getText().toString();
            String num2 = inputNumber2.getText().toString();

            if (!BitwiseHelper.isBinaryNumber(num1)) {
                throw new IllegalArgumentException("First number is not binary");
            }
            if (!BitwiseHelper.isBinaryNumber(num2)) {
                throw new IllegalArgumentException("Second number is not binary");
            }

            String binary = BitwiseHelper.bitwiseOR(num1, num2);
            BaseNumber binaryNum = new BaseNumber(binary, 2);
            BaseNumber decimalNum = new BaseNumber(String.valueOf(BaseConverter.convertToDecimal(binaryNum)), 10);
            BaseNumber hexNum = new BaseNumber(String.valueOf(BaseConverter.convertToBase(decimalNum, 16)), 16);

            resultSection.setVisibility(View.VISIBLE);
            binaryResult.setText(binary);
            decimalResult.setText(decimalNum.getRepresentation());
            hexResult.setText(hexNum.getRepresentation());
        }
        catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void computeXOR() {
        try {
            String num1 = inputNumber1.getText().toString();
            String num2 = inputNumber2.getText().toString();

            if (!BitwiseHelper.isBinaryNumber(num1)) {
                throw new IllegalArgumentException("First number is not binary");
            }
            if (!BitwiseHelper.isBinaryNumber(num2)) {
                throw new IllegalArgumentException("Second number is not binary");
            }

            String binary = BitwiseHelper.bitwiseXOR(num1, num2);
            BaseNumber binaryNum = new BaseNumber(binary, 2);
            BaseNumber decimalNum = new BaseNumber(String.valueOf(BaseConverter.convertToDecimal(binaryNum)), 10);
            BaseNumber hexNum = new BaseNumber(String.valueOf(BaseConverter.convertToBase(decimalNum, 16)), 16);

            resultSection.setVisibility(View.VISIBLE);
            binaryResult.setText(binary);
            decimalResult.setText(decimalNum.getRepresentation());
            hexResult.setText(hexNum.getRepresentation());
        }
        catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}