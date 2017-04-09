package barkr.barkr;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class AlarmActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private MoveDetector mMoveDetector;

    Uri alarmURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private Button mGiantRedButton;

    public void emailNotify () {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"barkr@kd8zev.net"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AlarmActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        mGiantRedButton = (Button)findViewById(R.id.BIGREDBUTTON);
        mGiantRedButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            toBarkorNotToBark();
            }
        });
        mGiantRedButton.setText("ARM");
    }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMoveDetector = new MoveDetector();
        mMoveDetector.setOnMoveListener(new MoveDetector.OnMoveListener() {
            @Override
            public void onMove(int count) {
                Toast.makeText(AlarmActivity.this, "WOWWEE!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SHAKE SHAKE SHAKE: " + count);
                //emailNotify();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mMoveDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mMoveDetector);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "ON RESTART");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "---ON DESTROY---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "ON PAUSE");
    }

    protected void onStart(){
        super.onStart();
        Log.d(TAG, "ON START");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "ON STOP");
    }

    protected void toBarkorNotToBark(){
        if(mGiantRedButton.getText().equals("ARM")){
            mGiantRedButton.setText("DISARM");
            Intent alarmIntent	=	new	Intent(AlarmActivity.this, BarkrAlarmPlaybackService.class);
            alarmIntent.putExtra("URIString",	alarmURI.toString());
            startService(alarmIntent);
        }else{
            mGiantRedButton.setText("ARM");
            stopService(new	Intent(AlarmActivity.this,	BarkrAlarmPlaybackService.class));
        }
    }
}
