package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class detail_activity extends AppCompatActivity {
    private static final String DEFAULT_DATE = "No date available";
    private static final String DEFAULT_TEMP = "No temperature available";
    private static final String DEFAULT_HUMIDITY = "No humidity available";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Intent intent = getIntent();

        TextView txtDate = findViewById(R.id.txtDate2);
        TextView txtTemp = findViewById(R.id.txtTemp2);
        TextView txtHumidity = findViewById(R.id.txtHumidity2);
        ImageView imgIcon = findViewById(R.id.imgIcon2);

        if (intent != null) {
            String date = intent.getStringExtra("date");
            String temperature = intent.getStringExtra("temperature");
            String humidity = intent.getStringExtra("humidity");
            int iconResId = intent.getIntExtra("icon", -1); // Default to -1 if not found
            String unit = intent.getStringExtra("unit");

            txtDate.setText(date != null ? date : DEFAULT_DATE);
            txtTemp.setText(temperature != null ? temperature + (unit != null && unit.equals("imperial") ? " °F" : " °C") : DEFAULT_TEMP);
            txtHumidity.setText(humidity != null ? "Humidity: " + humidity + "%" : DEFAULT_HUMIDITY);
            imgIcon.setImageResource(iconResId);
        }
    }

}
