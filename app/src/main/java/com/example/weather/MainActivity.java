package com.example.weather;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    String[] date_list = new String[20];
    String[] temp_list = new String[20];
    String[] humidity_list = new String[20]; // Added humidity_list array
    Integer[] icon_list = new Integer[20];
    private HttpURLConnection urlConnection;
    private static final String TAG = "MainActivity";
    private String forecastJsonStr;
    private String location = "Colombo"; // Default location
    private String unit = "metric"; // Default unit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleMarginTop(0);
        toolbar.setTitleMarginBottom(0);

        // Get data from MainActivity2
        Intent intent = getIntent();
        if (intent != null) {
            location = intent.getStringExtra("location");
            unit = intent.getStringExtra("unit");
            if (location == null || location.isEmpty()) {
                location = "Colombo"; // Default location if not provided
            }
            if (unit == null || unit.isEmpty()) {
                unit = "metric"; // Default unit if not provided
            }
            Log.d(TAG, "Location: " + location + ", Unit: " + unit); // Log the values
        } else {
            location = "Colombo"; // Default location
            unit = "metric"; // Default unit
        }
        TextView locationView = findViewById(R.id.location_view);
        locationView.setText(location);
        new FetchData().execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, spanString.length(), 0);
            item.setTitle(spanString);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.item1){
            Intent intent = new Intent(this,MainActivity2.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.item2){
            Intent intent = new Intent(this,MainActivity3.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchData extends AsyncTask<String,Void,String>{

        private BufferedReader reader;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "Fetching data for location: " + location + " and unit: " + unit);
            try {
                final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=" + location + "&cnt=20&appid=&units=" + unit;
                URL url = new URL(BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line1;

                while ((line1 = reader.readLine()) != null) {
                    buffer.append(line1).append("\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();
                return forecastJsonStr;

            }catch (IOException e){
                Log.e("Hi","Error",e);
                return null;
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    } catch (final IOException e){
                        Log.e("Hi","Error closing stream",e);
                    }
                }
            }

        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject weatherObject = new JSONObject(result);
                    JSONArray dataList = weatherObject.getJSONArray("list");

                    for (int i = 0; i < dataList.length(); i++) {
                        JSONObject valueObject = dataList.getJSONObject(i);
                        date_list[i] = valueObject.getString("dt_txt");

                        JSONObject mainObject = valueObject.getJSONObject("main");
                        temp_list[i] = mainObject.getString("temp");
                        humidity_list[i] = mainObject.getString("humidity");

                        JSONArray weatherArray = valueObject.getJSONArray("weather");
                        JSONObject weatherArrayObject = weatherArray.getJSONObject(0);
                        icon_list[i] = getApplicationContext().getResources().getIdentifier("pic_" + weatherArrayObject.getString("icon"), "drawable", getApplicationContext().getPackageName());
                    }

                    CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, date_list, temp_list, icon_list,unit);
                    ListView listView = findViewById(R.id.list_view);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            try {
                                Intent detailActivity = new Intent(MainActivity.this, detail_activity.class);
                                detailActivity.putExtra("date", date_list[position]);
                                detailActivity.putExtra("temperature", temp_list[position]);
                                detailActivity.putExtra("humidity", humidity_list[position]); // Pass humidity data
                                detailActivity.putExtra("icon", icon_list[position]);
                                detailActivity.putExtra("unit", unit); // Pass the unit
                                startActivity(detailActivity);
                            } catch (Exception e) {
                                Log.e(TAG, "Error in item click", e);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
