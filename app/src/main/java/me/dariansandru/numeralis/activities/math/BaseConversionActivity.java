package me.dariansandru.numeralis.activities.math;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.utils.structures.BaseNumber;
import me.dariansandru.numeralis.utils.algorithms.BaseConverter;

public class BaseConversionActivity extends AppCompatActivity {

    private EditText inputNumber;
    private Spinner fromBaseSpinner, toBaseSpinner;
    private Button convertButton;
    private TextView outputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_conversion);

        inputNumber = findViewById(R.id.inputNumber);
        fromBaseSpinner = findViewById(R.id.fromBaseSpinner);
        toBaseSpinner = findViewById(R.id.toBaseSpinner);
        convertButton = findViewById(R.id.convertButton);
        outputView = findViewById(R.id.outputView);

        setupSpinners();
        convertButton.setOnClickListener(v -> performConversion());
    }

    private void setupSpinners() {
        Integer[] baseValues = new Integer[35];
        for (int i = 0; i < 35; i++) {
            baseValues[i] = i + 2;
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, baseValues);
        fromBaseSpinner.setAdapter(adapter);
        toBaseSpinner.setAdapter(adapter);

        fromBaseSpinner.setSelection(8);
        toBaseSpinner.setSelection(14);
    }

    @SuppressLint("SetTextI18n")
    private void performConversion() {
        String number = inputNumber.getText().toString();
        if (number.isEmpty()) {
            Toast.makeText(this, "Please enter a number!", Toast.LENGTH_SHORT).show();
            return;
        }

        int fromBase = (int) fromBaseSpinner.getSelectedItem();
        int toBase = (int) toBaseSpinner.getSelectedItem();

        try {
            BaseNumber baseNumber = new BaseNumber(number, fromBase);
            BaseNumber decimalNumber = new BaseNumber(String.valueOf(baseNumber.toDecimal()), 10);
            BaseNumber convertedNumber = BaseConverter.convertToBase(decimalNumber, toBase);

            outputView.setText("Result: " + convertedNumber.getRepresentation());
        } catch (Exception e) {
            outputView.setText("Invalid input for the selected base.");
        }
    }
}
