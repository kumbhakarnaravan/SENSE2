package com.example.dell1.sense;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "MainActivity";
    private static final String filename="Accelerometer.txt";
    private static final String filename1="Gyroscope.txt";
    private static final String filename2="Magnetometer.txt";
    String s,s1,s2;
    String w;

    private SensorManager sensorManager;

    private Sensor sAcc,sGyro,sMagno,sLight,sPressure,sTemp,sHumi;

    TextView Xval,Yval,Zval,XGval,YGval,ZGval,XMval,YMval,ZMval,light,pressure,temp,humi;
    File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),filename);
    File f1=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),filename1);
    File f2=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),filename2);


    public void click(View v){

        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Xval=(TextView)findViewById(R.id.Xval);
        Yval=(TextView)findViewById(R.id.Yval);
        Zval=(TextView)findViewById(R.id.Zval);

        XGval=(TextView)findViewById(R.id.XGval);
        YGval=(TextView)findViewById(R.id.YGval);
        ZGval=(TextView)findViewById(R.id.ZGval);

        XMval=(TextView)findViewById(R.id.MXval);
        YMval=(TextView)findViewById(R.id.MYval);
        ZMval=(TextView)findViewById(R.id.MZval);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&& checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);

        }


        Log.d(TAG, "onCreate: Intializing Sensor Services");
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        sAcc=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sAcc!=null){
            sensorManager.registerListener(MainActivity.this,sAcc,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered accelerometer listner");
        }
        else{
            Xval.setText("Accelerometer sensor not supported");
            Yval.setText("Accelerometer sensor not supported");
            Zval.setText("Accelerometer sensor not supported");
        }

        sGyro=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(sGyro!=null){
            sensorManager.registerListener(MainActivity.this,sGyro,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered gyro listner");
        }
        else{
            XGval.setText("Gyrometer sensor not supported");
            YGval.setText("Gyrometer sensor not supported");
            ZGval.setText("Gyrometer sensor not supported");
        }

        sMagno=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(sMagno!=null){
            sensorManager.registerListener(MainActivity.this,sMagno,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered magno listner");
        }
        else{
            XMval.setText("Magnetometer sensor not supported");
            YMval.setText("Magnetometer sensor not supported");
            ZMval.setText("Magnetometer sensor not supported");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void saveT(String content ,File f1){


        try (FileOutputStream fos = new FileOutputStream(f1)) {
            fos.write(content.getBytes());

            fos.close();
            //  Toast.makeText(this, "saved to"+f1.getAbsolutePath() , Toast.LENGTH_SHORT).show();

            w="saved to"+f1.getAbsolutePath();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
        catch (IOException e) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1000:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)

                {Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();}
                else{
                    Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
    public String getS(){
        return s;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor=sensorEvent.sensor;


        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "onSensorChanged: X:" + sensorEvent.values[0] + "Y:" + sensorEvent.values[1] + "Z:" + sensorEvent.values[2]);
            s=s+"\n X:" + sensorEvent.values[0] + "     Y:" + sensorEvent.values[1] + "     Z:" + sensorEvent.values[2];
            Xval.setText("XAcc_Value: " + sensorEvent.values[0]);
            Yval.setText("YAcc_Value: " + sensorEvent.values[1]);
            Zval.setText("ZAcc_Value: " + sensorEvent.values[2]);

            saveT(s,f);
        }else if(sensor.getType() == Sensor.TYPE_GYROSCOPE){
            Log.d(TAG, "onSensorChanged: X:" + sensorEvent.values[0] + "Y:" + sensorEvent.values[1] + "Z:" + sensorEvent.values[2]);
            s1=s1+"\n X:" + sensorEvent.values[0] + "     Y:" + sensorEvent.values[1] + "     Z:" + sensorEvent.values[2];
            XGval.setText("XGyro_Value: " + sensorEvent.values[0]);
            YGval.setText("YGyro_Value: " + sensorEvent.values[1]);
            ZGval.setText("ZGyro_Value: " + sensorEvent.values[2]);

            saveT(s1,f1);
        }else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Log.d(TAG, "onSensorChanged: X:" + sensorEvent.values[0] + "Y:" + sensorEvent.values[1] + "Z:" + sensorEvent.values[2]);
            s2 = s2+"\n X:" + sensorEvent.values[0] + "     Y:" + sensorEvent.values[1] + "     Z:" + sensorEvent.values[2];
            XMval.setText("XMagno_Value: " + sensorEvent.values[0]);
            YMval.setText("YMagno_Value: " + sensorEvent.values[1]);
            ZMval.setText("ZMagno_Value: " + sensorEvent.values[2]);

            saveT(s2, f2);
        }
    }
}