package Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import Adapters.WeatherRVAdapter;
import Entities.Weather;
import henrik.mau.assignments.Controller;
import henrik.mau.assignments.R;

/**
 * A simple {@link Fragment} subclass.
 * A class that reads the sensors input from the Humidity-, Temperature- and Pressure-sensor.
 */
public class TabSensorFragment extends Fragment {
    private final static String TAG = "Sensor";
    private Controller controller;
    private RecyclerView recyclerView;
    private WeatherRVAdapter adapter;
    private List<Weather> content = new ArrayList<>();
    private Button btnStartReading;
    private Button btnStopReading;

    public TabSensorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_sensor, container, false);
        initializeComponents(view);
        registerListeners();
        return view;
    }

    private void initializeComponents(View view){
        adapter = new WeatherRVAdapter(getActivity(), content);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_sensor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnStartReading = (Button) view.findViewById(R.id.btnStartListenOnSensor);
        btnStopReading = (Button) view.findViewById(R.id.btnStopListenOnSensor);
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void setContent(List<Weather> content){
        this.content = content;
        adapter.setContent(content);
        adapter.notifyDataSetChanged();
    }

    private void registerListeners(){
        btnStartReading.setOnClickListener(new StartReadingSensors());
        btnStopReading.setOnClickListener(new StopReadingSensors());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(content!=null){
            adapter.setContent(content);
        }
    }

    private class StartReadingSensors implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            controller.startReadingSensors();
        }
    }

    private class StopReadingSensors implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            controller.stopReadingSensors();
        }
    }

}
