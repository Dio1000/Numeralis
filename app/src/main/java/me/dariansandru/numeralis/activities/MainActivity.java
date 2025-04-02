package me.dariansandru.numeralis.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import me.dariansandru.numeralis.R;
import me.dariansandru.numeralis.activities.logic.BooleanFunctionsActivity;
import me.dariansandru.numeralis.activities.logic.SatisfiabilityActivity;
import me.dariansandru.numeralis.activities.logic.TruthTableActivity;
import me.dariansandru.numeralis.activities.math.ArithmeticActivity;
import me.dariansandru.numeralis.activities.math.BaseConversionActivity;
import me.dariansandru.numeralis.activities.math.CalculusActivity;
import me.dariansandru.numeralis.activities.math.ProbabilityActivity;
import me.dariansandru.numeralis.utils.db.DBConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBConnection.init(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnArithmetic).setOnClickListener(v -> openActivity(ArithmeticActivity.class));
        findViewById(R.id.btnBaseConversion).setOnClickListener(v -> openActivity(BaseConversionActivity.class));
        findViewById(R.id.btnCalculus).setOnClickListener(v -> openActivity(CalculusActivity.class));
        findViewById(R.id.btnProbability).setOnClickListener(v -> openActivity(ProbabilityActivity.class));

        findViewById(R.id.btnBooleanFunctions).setOnClickListener(v -> openActivity(BooleanFunctionsActivity.class));
        findViewById(R.id.btnTruthTable).setOnClickListener(v -> openActivity(TruthTableActivity.class));
        findViewById(R.id.btnSatisfiability).setOnClickListener(v -> openActivity(SatisfiabilityActivity.class));
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}

// TODO: Fix an issue where expressions of the form (x1 op y1) op (x2 op y2) are not split correctly
