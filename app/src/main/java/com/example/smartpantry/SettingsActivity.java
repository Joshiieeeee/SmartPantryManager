package com.example.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

public class SettingsActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        nav();

        Switch alert = findViewById(R.id.alertSwitch);
        SharedPreferences preferences = getSharedPreferences("options", MODE_PRIVATE);
        alert.setChecked(preferences.getBoolean("expiry_alert", false));
        alert.setOnCheckedChangeListener((view, checked) ->
                preferences.edit().putBoolean("expiry_alert", checked).apply());
    }
}
