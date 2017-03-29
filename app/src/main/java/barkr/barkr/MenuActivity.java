package barkr.barkr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Button mAlarmButton;
    private Button mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAlarmButton = (Button)findViewById(R.id.alarm_button);
        mAlarmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent toAlarm = new Intent(MenuActivity.this, AlarmActivity.class);
                startActivity(toAlarm);
            }
        });

        mSettingsButton = (Button)findViewById(R.id.settings_button);
        mSettingsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent toSettings = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(toSettings);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "ON RESUME");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG, "ON RESTART");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "---ON DESTROY---");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "ON PAUSE");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "ON START");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "ON STOP");
    }

}
