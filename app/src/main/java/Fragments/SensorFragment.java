package Fragments;


import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Adapters.SensorAdapter;
import henrik.mau.assignments.Controller;
import henrik.mau.assignments.R;

/**
 * A simple {@link Fragment} subclass.
 * Simply a class that represents the sensor in the recycle view, inside Assignment1Fragment.
 * It has a very simple layout.
 */
public class SensorFragment extends Fragment {
    private Controller controller;



    public SensorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        return view;
    }


    public void setController(Controller controller){
        this.controller = controller;
    }

}
