package barkr.barkr;

import android.content.Intent;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static final String SHARED_PREF_FILE = "BarkrSettings";
    private final String TAG = this.getClass().getSimpleName();
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Get values from SharedPreferences, if they exist.
        sharedPreferences=getSharedPreferences(SHARED_PREF_FILE,0);
        String savedEmail=sharedPreferences.getString(getString(R.string.email_key),getString(R.string.no_saved_email));
        String savedPhoneNum=sharedPreferences.getString(getString(R.string.phone_num_key),getString(R.string.no_saved_phone_num));
        String savedPin=sharedPreferences.getString(getString(R.string.pin_key),getString(R.string.no_saved_pin));
        EditText emailInput = (EditText) findViewById(R.id.emailInput);
        EditText phoneNumberInput = (EditText) findViewById(R.id.phoneNumberInput);
        EditText pinInput = (EditText) findViewById(R.id.pinInput);
        if(!savedEmail.equals(getString(R.string.no_saved_email))){
            emailInput.setText(savedEmail, TextView.BufferType.EDITABLE);
        }
        if(!savedPhoneNum.equals(getString(R.string.no_saved_phone_num))){
            phoneNumberInput.setText(savedPhoneNum, TextView.BufferType.EDITABLE);
        }
        if(!savedPin.equals(getString(R.string.no_saved_pin))){
            pinInput.setText(savedPin, TextView.BufferType.EDITABLE);
        }
        //Set up help button.
        Button helpButton=(Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment helpFragment=MyAlertDialogFragment.newInstance(R.string.help);
                helpFragment.show(getFragmentManager(),"help");
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

    public void saveSettings(View v) {
        boolean error=false;
        Log.d(TAG, "Save button clicked!");
        //Get text fields.
        EditText emailInput = (EditText) findViewById(R.id.emailInput);
        EditText phoneNumberInput = (EditText) findViewById(R.id.phoneNumberInput);
        EditText pinInput = (EditText) findViewById(R.id.pinInput);
        //Print text to terminal.
        String emailText=emailInput.getText().toString();
        String phoneNumberText=phoneNumberInput.getText().toString();
        String pinText=pinInput.getText().toString();
        Log.d(TAG,"The email address is "+emailText);
        Log.d(TAG,"The phone number is "+phoneNumberText);
        Log.d(TAG,"The PIN is "+pinText);
        if(pinText.length()<4||(emailText.length()<1&&phoneNumberText.length()<1)) {
            DialogFragment errorFragment;
            if(pinText.length()<1){
                errorFragment=MyAlertDialogFragment.newInstance(R.string.no_pin);
            }else if(pinText.length()<4){
                errorFragment=MyAlertDialogFragment.newInstance(R.string.pin_too_short);
            }else{
                errorFragment=MyAlertDialogFragment.newInstance(R.string.no_email_or_phone);
            }
            errorFragment.show(getFragmentManager(), "fieldEmpty");
            error = true;
        }
        if(!error) {
            sharedPreferences=getSharedPreferences(SHARED_PREF_FILE,0);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString(getString(R.string.email_key),emailText);
            editor.putString(getString(R.string.phone_num_key),phoneNumberText);
            editor.putString(getString(R.string.pin_key),pinText);
            editor.commit();
            if((!sharedPreferences.getString(getString(R.string.email_key),getString(R.string.no_saved_email)).equals(getString(R.string.no_saved_email))||
                    !sharedPreferences.getString(getString(R.string.phone_num_key),getString(R.string.no_saved_phone_num)).equals(getString(R.string.no_saved_phone_num)))&&
                    !sharedPreferences.getString(getString(R.string.pin_key),getString(R.string.no_saved_pin)).equals(getString(R.string.no_saved_pin))){
                Toast toast=Toast.makeText(getApplicationContext(),"Settings saved",Toast.LENGTH_SHORT);
                toast.show();
            }
            Intent toMenu = new Intent(SettingsActivity.this, MenuActivity.class);
            startActivity(toMenu);
            finish();
        }
    }
}
