package com.example.variaspantallas.ui.giroscopio;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.variaspantallas.R;

import java.text.DecimalFormat;

/**
 * A simple {@link NewFragment} subclass.
 * Use the {@link NewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Parameters
    private String mParam1;
    private String mParam2;


    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorListener;

    private long lastClickTime = 0;

    public NewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewFragment.
     */
    public static NewFragment newInstance(String param1, String param2) {
        NewFragment fragment = new NewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Inicialización del SensorManager y del acelerómetro
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
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

                    // Actualizar los TextView con los valores del acelerómetro
                    updateTextView(R.id.textViewXAcc, "X Acc: " + xAcc);
                    updateTextView(R.id.textViewYAcc, "Y Acc: " + yAcc);
                    updateTextView(R.id.textViewZAcc, "Z Acc: " + zAcc);

                    // Procesamiento o visualización adicional si es necesario...
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Desregistramos el listener para evitar posibles fugas de memoria
        if (sensorManager != null && sensorListener != null) {
            sensorManager.unregisterListener(sensorListener);
        }
    }

    private void updateTextView(int textViewId, String text) {
        View view = getView();
        if (view != null) {
            TextView textView = view.findViewById(textViewId);
            if (textView != null) {
                textView.setText(text);
            }
        }
    }

    // Método para verificar el doble clic
    private void checkDoubleClick() {
        long clickTime = System.currentTimeMillis();

        if (clickTime - lastClickTime < 500) {
            showToast("Has dado doble click!");
        }

        lastClickTime = clickTime;
    }

    // Método para mostrar un Toast
    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
