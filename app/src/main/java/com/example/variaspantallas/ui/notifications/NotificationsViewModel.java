package com.example.variaspantallas.ui.notifications;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.variaspantallas.R;

import java.text.DecimalFormat;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorListener;


    public NotificationsViewModel(Context context) {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");

        // Inicializar SensorManager y el acelerómetro
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Verificar si el dispositivo tiene acelerómetro
        if (accelerometer != null) {
            sensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // Valores del acelerómetro en m/s^2
                    float xAcc = sensorEvent.values[0];
                    float yAcc = sensorEvent.values[1];
                    float zAcc = sensorEvent.values[2];

                    // Actualizar los TextView con los valores del acelerómetro
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    xAcc = Float.parseFloat(decimalFormat.format(xAcc));
                    yAcc = Float.parseFloat(decimalFormat.format(yAcc));
                    zAcc = Float.parseFloat(decimalFormat.format(zAcc));

                    // Actualizar el valor del LiveData con los valores del acelerómetro
                    String accelerometerValues = "X Acc: " + xAcc + ", Y Acc: " + yAcc + ", Z Acc: " + zAcc;
                    mText.postValue(accelerometerValues);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // Puedes ignorar esta callback por el momento
                }
            };

            // Registramos el listener para capturar los eventos del sensor
            sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void unregisterSensorListener() {
        if (sensorManager != null && sensorListener != null) {
            sensorManager.unregisterListener(sensorListener);
        }
    }
}
