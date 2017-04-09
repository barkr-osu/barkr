package barkr.barkr;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class AlarmActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    Uri alarmURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private Button mGiantRedButton;

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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ON RESUME");
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

    @Override
    protected void onStart() {
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
