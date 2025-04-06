package com.example.projectmodel;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TensorFlowClassifier classifier;
    private TextView activityTextView;
    private TextView textView;
    private SensorManager sensorManager;
    private Button btnGoToDashboard;
    private float[] sensorData = new float[1152];
    private int dataIndex = 0;
    taskDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityTextView = findViewById(R.id.activity_text_view);
        textView = findViewById(R.id.textView);
        btnGoToDashboard = findViewById(R.id.button2);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        try {
            classifier = new TensorFlowClassifier(this);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing TensorFlow classifier.", e);
        }
    }

    public void getTask(View view) {
        dao = new taskDAO();
        dao.getAllTasks(taskList -> new Handler(Looper.getMainLooper()).post(() -> {
            String str = taskList.toString();
            textView.setText(str);
        }));
    }

    public void go_to_dashboard(View view) {
        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Ensure we don't try to copy data past the end of the array
            int length = event.values.length;
            if ((dataIndex * length) + length > sensorData.length) {
                // Not enough space left in the array; adjust the length we copy
                length = sensorData.length - (dataIndex * length);
            }

            // Copy the data to the sensorData array
            System.arraycopy(event.values, 0, sensorData, dataIndex * event.values.length, length);
            dataIndex++;

            // When the buffer is full, perform inference and reset
            if (dataIndex * event.values.length >= sensorData.length) {
                float[] results = classifier.predictProbabilities(sensorData);
                displayActivity(results);
                dataIndex = 0; // Reset index
                Arrays.fill(sensorData, 0); // Clear the sensor data array for new data
            }
        }
    }


    private void displayActivity(float[] results) {
        int maxIndex = 0;
        for (int i = 1; i < results.length; i++) {
            if (results[i] > results[maxIndex]) {
                maxIndex = i;
            }
        }
        String[] activities = {"Downstairs", "Jogging", "Sitting", "Standing", "Upstairs", "Walking"};
        activityTextView.setText("Detected Activity: " + activities[maxIndex]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not implemented
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
