package barkr.barkr;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private MoveDetector mMoveDetector;

    private static final String SHARED_PREF_FILE = "BarkrSettings";
    private SharedPreferences sharedPreferences;

    Uri alarmURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private Button mGiantRedButton;

    public void emailNotify () {
        sharedPreferences=getSharedPreferences(SHARED_PREF_FILE,0);
        String savedEmail=sharedPreferences.getString(getString(R.string.email_key),getString(R.string.no_saved_email));
        String savedPhoneNum=sharedPreferences.getString(getString(R.string.phone_num_key),getString(R.string.no_saved_phone_num));
        String savedPin=sharedPreferences.getString(getString(R.string.pin_key),getString(R.string.no_saved_pin));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd 'at' h:mm a");
        String current_time_string = format.format(calendar.getTime());

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(savedPhoneNum, null, "BARKR - Potential break-in at " + current_time_string, null, null);
        } catch (Exception e) {
            Toast.makeText(AlarmActivity.this, "Unable to send SMS", Toast.LENGTH_SHORT).show();
        }

        // TODO - Better way to do this
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , savedEmail);
        i.putExtra(Intent.EXTRA_SUBJECT, "BARKR - Break-in Detected!");
        i.putExtra(Intent.EXTRA_TEXT   , "A potential room break-in was detected at " + current_time_string);
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
            toBarkOrNotToBark();
            }
        });
        mGiantRedButton.setText("ARM");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMoveDetector = new MoveDetector();
        mMoveDetector.setOnMoveListener(new MoveDetector.OnMoveListener() {
            @Override
            public void onMove(int count) {
                if (mGiantRedButton.getText() == "DISARM") {
                    Toast.makeText(AlarmActivity.this, "Movement Detected!", Toast.LENGTH_SHORT).show();
                    Intent alarmIntent	=	new	Intent(AlarmActivity.this, BarkrAlarmPlaybackService.class);
                    alarmIntent.putExtra("URIString",	alarmURI.toString());
                    startService(alarmIntent);
                    emailNotify();

                }
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
    
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "ON START");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "ON STOP");
    }

    protected void toBarkOrNotToBark(){
        if(mGiantRedButton.getText().equals("ARM")){
            mGiantRedButton.setText("DISARM");
            //Intent alarmIntent	=	new	Intent(AlarmActivity.this, BarkrAlarmPlaybackService.class);
            //alarmIntent.putExtra("URIString",	alarmURI.toString());
            //startService(alarmIntent);
        }else{
            mGiantRedButton.setText("ARM");
            stopService(new	Intent(AlarmActivity.this,	BarkrAlarmPlaybackService.class));
        }
    }
}
