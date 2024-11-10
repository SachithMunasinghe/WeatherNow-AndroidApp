package com.example.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemData;
    private final String[] itemTemp;
    private final Integer[] icons;
    private String unit;

    public CustomListAdapter(Activity context, String[] itemData, String[] itemTemp, Integer[] icons, String unit) {
        super(context, R.layout.my_list, itemData);
        this.context = context;
        this.itemData = itemData;
        this.itemTemp = itemTemp;
        this.icons = icons;
        this.unit = unit != null ? unit : "metric"; // Default to metric if unit is null
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.my_list, null, true);

        TextView itemText = rowView.findViewById(R.id.txt_date);
        TextView tempText = rowView.findViewById(R.id.txt_temp);
        ImageView icon = rowView.findViewById(R.id.icon);

        itemText.setText(itemData[position]);
        String tempUnit = unit.equals("imperial") ? " °F" : " °C";
        tempText.setText(itemTemp[position] + tempUnit);

        // Set the icon resource
        Integer iconResId = icons[position];
        if (iconResId != null) {
            icon.setImageResource(iconResId);
        }

        return rowView;
    }
}
