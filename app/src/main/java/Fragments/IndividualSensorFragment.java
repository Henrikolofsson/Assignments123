package Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import henrik.mau.assignments.Controller;
import henrik.mau.assignments.R;

/**
 * A simple {@link Fragment} subclass.
 * Basically this is a class that shows the information about a specific sensor.
 */

public class IndividualSensorFragment extends Fragment {
    private Controller controller;
    private TextView tvSensorTitle;
    private TextView tvSensorValues;
    private Button btnRegisterListener;
    private Button btnUnregisterListener;

    public IndividualSensorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invidivual_sensor, container, false);
        initializeComponents(view);
        initializeTitle();
        initializeButtonText();
        registerListener();
        return view;
    }

    private void initializeComponents(View view){
        tvSensorTitle = (TextView) view.findViewById(R.id.tvSensorTitle);
        btnRegisterListener = (Button) view.findViewById(R.id.btnRegisterSensorListener);
        tvSensorValues = (TextView) view.findViewById(R.id.tvValues);
        btnUnregisterListener = (Button) view.findViewById(R.id.btnUnregisterSensorListener);
    }

    private void initializeTitle(){
        tvSensorTitle.setText(controller.getIndividualSensorClickedOn());
    }

    private void initializeButtonText(){
        btnRegisterListener.setText("REGISTER LISTENER ON " + controller.getIndividualSensorClickedOn());
        btnUnregisterListener.setText("UNREGISTER LISTENER ON " + controller.getIndividualSensorClickedOn());
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    private void registerListener(){
        btnRegisterListener.setOnClickListener(new RegisterIndividualSensorListener());
        btnUnregisterListener.setOnClickListener(new UnregisterIndividualSensorListener());
    }

    public void setTvSensorValues(String text){
        tvSensorValues.setText(text);
    }


    private class RegisterIndividualSensorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            controller.registerSensor();
        }
    }

    private class UnregisterIndividualSensorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            controller.unregisterSensor();
            tvSensorValues.setText("Here is where the values will appear.");
        }
    }

}
