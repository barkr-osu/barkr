package barkr.barkr;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MoveDetector implements SensorEventListener {
    private static final double MOVE_THRESHOLD_GRAVITY = 2.7;
    private static final int MOVE_SLOP_TIME_MS = 500;
    private static final int MOVE_COUNT_RESET_TIME_MS = 3000;

    private OnMoveListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public void setOnShakeListener(OnMoveListener listener) {
        this.mListener = listener;
    }

    public interface OnMoveListener {
        public void onShake(int count);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mListener != null) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            double gX = x / SensorManager.GRAVITY_EARTH;
            double gY = y / SensorManager.GRAVITY_EARTH;
            double gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > MOVE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + MOVE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + MOVE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;

                mListener.onShake(mShakeCount);
            }
        }
    }
}