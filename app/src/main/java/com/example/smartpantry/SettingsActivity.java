package com.example.smartpantry;
import android.content.*;import android.os.Bundle;import android.widget.*;
public class SettingsActivity extends BaseActivity {
 @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_settings);nav();
  Switch alert=findViewById(R.id.alertSwitch);android.content.SharedPreferences prefs=getSharedPreferences("options",MODE_PRIVATE);
  alert.setChecked(prefs.getBoolean("expiry_alert",false));alert.setOnCheckedChangeListener((v,checked)->prefs.edit().putBoolean("expiry_alert",checked).apply());
  ((TextView)findViewById(R.id.settingsNote)).setText("Expiry alert preference is saved locally. Automated notifications are not included in this version.");
 }
}
