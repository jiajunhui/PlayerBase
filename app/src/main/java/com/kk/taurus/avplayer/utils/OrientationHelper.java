package com.kk.taurus.avplayer.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by JiaJunHui on 2018/4/12.
 */

public class OrientationHelper {

    private final int MSG_CODE_SENSOR_ORIENTATION = 1;

    private Context mContext;

    private int mSensorOrientation;

    private int mActivityOrientation;

    private OnOrientationListener mOnOrientationListener;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_SENSOR_ORIENTATION:
                    int orientation = msg.arg1;
                    boolean reverse = false;
                    if (orientation > 45 && orientation < 135) {
                        //landscape
                        mSensorOrientation = Configuration.ORIENTATION_LANDSCAPE;
                        reverse = true;
                    } else if (orientation > 135 && orientation < 225) {
                        //portrait
                        mSensorOrientation = Configuration.ORIENTATION_PORTRAIT;
                        reverse = true;
                    } else if (orientation > 225 && orientation < 315) {
                        //landscape
                        mSensorOrientation = Configuration.ORIENTATION_LANDSCAPE;
                        reverse = false;
                    } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                        //portrait
                        mSensorOrientation = Configuration.ORIENTATION_PORTRAIT;
                        reverse = false;
                    }
                    if(mSensorOrientation != Configuration.ORIENTATION_UNDEFINED && mSensorOrientation==mActivityOrientation){
                        if(mOnOrientationListener!=null)
                            mOnOrientationListener.onSensorUserAgreement();
                    }
                    if(mOnOrientationListener!=null)
                        mOnOrientationListener.onOrientationChange(reverse, mSensorOrientation, orientation);
                    break;
            }
        }
    };
    private SensorManager sensorManager;
    private OrientationSensorListener listener;

    public OrientationHelper(Context context, OnOrientationListener onOrientationListener){
        this.mContext = context;
        this.mOnOrientationListener = onOrientationListener;
        initSensorListener();
    }

    private void initSensorListener() {
        // 注册重力感应器,监听屏幕旋转
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(mHandler);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void destroy(){
        sensorManager.unregisterListener(listener);
    }

    public void onActivityConfigChanged(Configuration newConfig){
        mActivityOrientation = newConfig.orientation;
    }

    /**
     * 重力感应监听者
     */
    public class OrientationSensorListener implements SensorEventListener {
        private static final int _DATA_X = 0;
        private static final int _DATA_Y = 1;
        private static final int _DATA_Z = 2;

        public static final int ORIENTATION_UNKNOWN = -1;

        private Handler rotateHandler;

        public OrientationSensorListener(Handler handler) {
            rotateHandler = handler;
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float X = -values[_DATA_X];
            float Y = -values[_DATA_Y];
            float Z = -values[_DATA_Z];
            float magnitude = X * X + Y * Y;
            // Don't trust the angle if the magnitude is small compared to the y
            // value
            if (magnitude * 4 >= Z * Z) {
                // 屏幕旋转时
                float OneEightyOverPi = 57.29577957855f;
                float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                orientation = 90 - (int) Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }
            if (rotateHandler != null) {
                rotateHandler.obtainMessage(MSG_CODE_SENSOR_ORIENTATION, orientation, 0).sendToTarget();
            }
        }
    }

    public interface OnOrientationListener{
        void onOrientationChange(boolean reverse, int orientation, int angle);
        void onSensorUserAgreement();
    }

}
