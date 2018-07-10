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

import java.util.Locale;

import de.fh_dortmund.throwit.R;
import de.fh_dortmund.throwit.menu.calculations.ThrowCalculator;
import de.fh_dortmund.throwit.menu.calculations2.ThrowCalculator2;
import de.fh_dortmund.throwit.menu.dummy.UserScoreContent;

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
    private TextView value2;
    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private Sensor mLinear = null;
    private double throwstart;
    private OnFragmentInteractionListener mListener;
    private ThrowCalculatorI tc1;
    private ThrowCalculatorI tc2;

    private boolean stopListening = false;
    private boolean startCount;
    private boolean startCount2;

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
        value2 = v.findViewById(R.id.lbl_value2);
        Button start = v.findViewById(R.id.btn_start);
        mSensorManager = (SensorManager) inflater.getContext().getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLinear = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

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
        mSensorManager.registerListener(this, mLinear, SensorManager.SENSOR_DELAY_GAME);

        value.setText(String.valueOf("~"));
        value2.setText(String.valueOf("~"));

        tc1 = new ThrowCalculator();
        tc2 = new ThrowCalculator2();

        throwstart = System.nanoTime();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && !stopListening) {
                // Invertieren da negative Werte Erhöhung der y Koordinate indizieren &
                // g-Kraft abziehen
                if(System.nanoTime()-throwstart < 2000000000L && !startCount)
                    value.setText("1"); value2.setText("1");

                if(System.nanoTime()-throwstart < 1000000000L && !startCount)
                    value.setText("2"); value2.setText("2");
                if(System.nanoTime()-throwstart < 3000000000L)
                    return;
                if(!startCount){
                    throwstart = System.nanoTime();
                    startCount = true;
                    startCount2 = true;

                }


                double verticalAcceleration = -1*(event.values[2]);
                double[] values = {event.values[0], event.values[1], verticalAcceleration};


                Log.i("VertAccelleration: ", ""+verticalAcceleration);


                value.setText(String.format(Locale.getDefault(),"%.3f", verticalAcceleration));
                if(!tc1.add(values, (long)(System.nanoTime() - throwstart))){
                    mSensorManager.unregisterListener(this);
                    double height = tc1.calculateHeight();
                    value.setText(String.format(Locale.getDefault(), "%.2f", height));
                    startCount = false;
                    UserScoreContent.newScore("",height, height);
                }


                value2.setText(String.format(Locale.getDefault(),"%.3f", verticalAcceleration));
                if(!tc2.add(values, (long)(System.nanoTime() - throwstart))) {
                    mSensorManager.unregisterListener(this);
                    double height = tc2.calculateHeight();
                    value2.setText(String.format(Locale.getDefault(), "%.2f", height));
                    startCount2 = false;
                    UserScoreContent.newScore("",height, height);
                }
            }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
