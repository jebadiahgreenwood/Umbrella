package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    String[] units = { "Fahrenheit", "Celsius" };
    String[] units_conv = { "imperial", "metric" };
    int selected_units = 0;
    Spinner spinner_select_units;
    Toolbar toolbar_settings;
    EditText et_zip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        et_zip = findViewById(R.id.et_set_zip);
        toolbar_settings = findViewById(R.id.toolbar_settings);
        toolbar_settings.setOnClickListener(view -> {
            if (!et_zip.getText().toString().isEmpty())
            {
                Intent intent = getIntent();
                intent.putExtra("units", units_conv[selected_units]);
                intent.putExtra("zip", et_zip.getText().toString());
                setResult(RESULT_OK, intent);
            }
            finish();
        });
        spinner_select_units = findViewById(R.id.spinner_units);
        spinner_select_units.setAdapter(new ArrayAdapter(this, R.layout.spinner_layout, units));
        spinner_select_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_units = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




}
