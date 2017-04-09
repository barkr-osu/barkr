package barkr.barkr;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Daniel Stelson on 4/8/2017.
 */

public class BarkrAlarmPlaybackService extends Service {

    MediaPlayer player;

    public IBinder onBind(Intent intent) {return null;}
    Uri alert;

    public void onCreate(){
        alert = Uri.parse("android.resource://"+getPackageName()+"/raw/dogsringtone");
        if(alert == null){
            Log.d("Alarm Service","uri null");
        }
        player = MediaPlayer.create(this, alert);
        player.setLooping(true);
    }

    public void onStart(Intent intent, int startid){
            try{
                player.reset();
                player.setDataSource(this.getApplicationContext(),alert );
                player.prepare();
            }catch(Exception e){
                e.printStackTrace();
            }
        player.start();
    }

    public void onDestroy(){
        player.stop();
    }
}
