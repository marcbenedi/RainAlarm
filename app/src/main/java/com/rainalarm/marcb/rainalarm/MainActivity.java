package com.rainalarm.marcb.rainalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    SharedPreferences preferences;
    public static final String PREFS_NAME = "userSettings";
    String city;
    int RefreshRatePos;
    int LanguagePos;
    int UnitsPos;
    boolean NotificationCheck;
    EditText cityText;
    int rate[] = {1,3,4,5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Preferences
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //Load user values
        city = preferences.getString("city", "Barcelona");
        RefreshRatePos = preferences.getInt("refreshRate", 0);
        LanguagePos = preferences.getInt("language", 0);
        UnitsPos = preferences.getInt("units", 0);
        NotificationCheck = preferences.getBoolean("notification",false);

        //City EditText
        cityText = (EditText)findViewById(R.id.editText1);
        cityText.setText(city);

        //Refresh Rate Spinner
        final Spinner refreshRate = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.refresh_rate,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        refreshRate.setAdapter(adapter);
        refreshRate.setSelection(RefreshRatePos);
        refreshRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = refreshRate.getSelectedItemPosition();
                RefreshRatePos = index;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Language Spinner
        final Spinner language = (Spinner) findViewById(R.id.spinner2);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.language,android.R.layout.simple_dropdown_item_1line);
        language.setAdapter(adapter);
        language.setSelection(LanguagePos);
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = language.getSelectedItemPosition();
                LanguagePos = index;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Units Spinner
        final Spinner units = (Spinner) findViewById(R.id.spinner3);
        adapter = ArrayAdapter.createFromResource(this,R.array.units,
                android.R.layout.simple_dropdown_item_1line);
        units.setAdapter(adapter);
        units.setSelection(UnitsPos);
        units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = units.getSelectedItemPosition();
                UnitsPos = index;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Notification CheckBox
        final CheckBox notificationBox = (CheckBox)findViewById(R.id.checkBox);
        notificationBox.setChecked(NotificationCheck);
        notificationBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notificationBox.isChecked()){
                    NotificationCheck = true;
                }
                else{
                    NotificationCheck = false;
                }
            }
        });

        Intent alarmIntent = new Intent(this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startAlarm(new View(this));
            return true;
        }
        else if (id == R.id.action_settings2){
            cancelAlarm(new View(this));
            return true;
        }
        else if (id == R.id.action_settings3){
            saveSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAlarm(View view){
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 60000*60*rate[RefreshRatePos];
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),interval,pendingIntent);
        Toast.makeText(this,"alarm set",Toast.LENGTH_LONG).show();
    }

    public void cancelAlarm(View view)
    {
        if(manager == null){
            //Si no tenim l'alarma definida perquè l'hem perduda de memòria només
            //l'hem de sobrescriure i cancelar-la
            manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            int interval = 60000*60*rate[RefreshRatePos];
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), interval, pendingIntent);
            Toast.makeText(this,"alarm set from cancell",Toast.LENGTH_LONG).show();
        }
        manager.cancel(pendingIntent);
        Utility.makeToast(view, "Alarm Canceled");
    }

    public void saveSettings()
    {
        city = cityText.getText().toString();
        SharedPreferences.Editor editor = preferences.edit();
        //Save CityName
        editor.putString("city", city);
        //Save RefreshRate
        editor.putInt("refreshRate", RefreshRatePos);
        //Save Language
        editor.putInt("language",LanguagePos);
        //Save Units
        editor.putInt("units", UnitsPos);
        //Save Notification
        editor.putBoolean("notification", NotificationCheck);
        //Commit
        editor.commit();
        Toast.makeText(this,"Settings saved",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        saveSettings();
    }
}