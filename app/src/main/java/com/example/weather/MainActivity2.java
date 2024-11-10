package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    private EditText locationEditText;
    private RadioGroup unitRadioGroup;
    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        EditText locationInput = findViewById(R.id.location_input);
        RadioGroup unitGroup = findViewById(R.id.unit_group);
        Button submitButton = findViewById(R.id.submit_button);

        // Retrieve location and unit from the intent, if available
        Intent intent = getIntent();
        if (intent != null) {
            String location = intent.getStringExtra("location");
            String unit = intent.getStringExtra("unit");

            if (location != null && !location.isEmpty()) {
                locationEditText.setText(location);
            }

            if (unit != null) {
                if (unit.equals("imperial")) {
                    unitRadioGroup.check(R.id.fahrenheit_radio);
                } else {
                    unitRadioGroup.check(R.id.celsius_radio);
                }
            }
        }

        submitButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            int selectedUnitId = unitGroup.getCheckedRadioButtonId();
            String unit = "metric"; // Default to Celsius

            if (selectedUnitId == R.id.fahrenheit_radio) {
                unit = "imperial"; // Fahrenheit
            }

            Intent resultIntent = new Intent(MainActivity2.this, MainActivity.class);
            resultIntent.putExtra("location", location);
            resultIntent.putExtra("unit", unit);
            startActivity(resultIntent);
        });
    }

}