package de.fh_dortmund.throwit.menu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import de.fh_dortmund.throwit.R;
import de.fh_dortmund.throwit.menu.calculations.ThrowCalculator;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThrowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ThrowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThrowFragment extends Fragment implements SensorEventListener {

    private TextView value;
    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private double throwstart;
    private OnFragmentInteractionListener mListener;
    private ThrowCalculator tc;

    private boolean stopListening = false;

    public ThrowFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment ThrowFragment.
     */
    public static ThrowFragment newInstance() {
        ThrowFragment fragment = new ThrowFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_throw, container, false);
        FrameLayout mainLayout = (FrameLayout) v.findViewById(R.id.ThrowFrame);
        mainLayout.setBackgroundColor(getResources().getColor(R.color.colorThemeBackground));
        value = v.findViewById(R.id.lbl_value);
        Button start = v.findViewById(R.id.btn_start);
        mSensorManager = (SensorManager) inflater.getContext().getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSensor();
            }
        });

        return v;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    private void initSensor() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        value.setText(String.valueOf("~"));
        tc = new ThrowCalculator();
        throwstart = System.nanoTime();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && !stopListening) {
                // Invertieren da negative Werte Erhöhung der y Koordinate indizieren &
                // g-Kraft abziehen
                double verticalAcceleration = (event.values[2] -9.81d);
                double[] values = {event.values[0], event.values[1], verticalAcceleration};

                Log.i("VertAccelleration: ", ""+verticalAcceleration);
                value.setText(String.format(Locale.getDefault(),"%.3f", verticalAcceleration));

                if(!tc.add(values, (long)(System.nanoTime() - throwstart))){
                    mSensorManager.unregisterListener(this);
                    value.setText(String.format(Locale.getDefault(), "%.2f", tc.calculateHeight()));
                }
            }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
