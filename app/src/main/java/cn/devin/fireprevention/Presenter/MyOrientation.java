package cn.devin.fireprevention.Presenter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import cn.devin.fireprevention.MyApplication;

/**
 * Created by Devin on 2017/12/19.
 * To get Orientation from sensor
 */

public class MyOrientation implements SensorEventListener{
    private static final String TAG = "MyOrientation";
    private float bias = 0;

    private SensorManager mSensorManager;
    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器

    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private float lastOrient = 0;

    /*
    Singleton Pattern
    get Singleton by getInstance()
     */
    private static MyOrientation myOrientation = new MyOrientation();
    public static MyOrientation getInstance(){
        return myOrientation;
    }
    private MyOrientation(){
        mSensorManager = (SensorManager) MyApplication.getContext().getSystemService(Context.SENSOR_SERVICE);

        // init the accelerometer sensor
        accelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // init the magnetic sensor
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // register this class to listener
        registerLis();
    }


    /**
     * register/un
     */
    public void registerLis(){
        mSensorManager.registerListener(this, accelerometer, Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, magnetic, Sensor.TYPE_MAGNETIC_FIELD);
    }
    public void unRegisterLis(){
        mSensorManager.unregisterListener(this);
    }


    /**
     * calculate the Orientation
     */
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
        // Switch the arc to an angle
        values[0] = (float) Math.toDegrees(values[0]);

        if (values[0] < 0){
            values[0] += 360;
        }

        myOrientationListener.onOrientationChange(values[0] + bias);

        lastOrient = values[0];
    }

    /**
     * callback of sensor
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = sensorEvent.values;
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = sensorEvent.values;
        }
        calculateOrientation();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    /**
     * interface
     */
    private MyOrientationListener myOrientationListener;

    public interface MyOrientationListener{
        void onOrientationChange(float rotate);
    }
    public void setOnOrientationChangeListener(MyOrientationListener myOrientationListener){
        this.myOrientationListener = myOrientationListener;
    }

}
