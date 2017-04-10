package barkr.barkr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Stephen on 4/8/2017.
 */

public class MyAlertDialogFragment extends DialogFragment {
    private final String TAG = this.getClass().getSimpleName();
    public static MyAlertDialogFragment newInstance(int message) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int message = getArguments().getInt("message");

        return new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.d(TAG, "Error message closed!");
                            }
                        }
                )
                .create();
    }
}
