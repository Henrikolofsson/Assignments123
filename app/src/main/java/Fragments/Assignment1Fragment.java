package Fragments;


import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import Adapters.SensorAdapter;
import henrik.mau.assignments.Controller;
import henrik.mau.assignments.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Assignment1Fragment extends Fragment {
    private Controller controller;
    private List<Sensor> sensorList;
    private ArrayList<String> sensorNames = new ArrayList<>();
    private RecyclerView rvSensors;
    private SensorAdapter adapter;

    public Assignment1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment1, container, false);
        initializeComponents(view);
        populate();
        return view;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    /*
       Initializes the components, but also adds a DividerItemDecoration to the recycler view.
       What this does, is that it separates every individual item with a line, for a smoother looking recycler view.
     */

    private void initializeComponents(View view){
        rvSensors = (RecyclerView) view.findViewById(R.id.rvSensors);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        rvSensors.addItemDecoration(itemDecoration);
        rvSensors.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SensorAdapter(getActivity(), sensorNames);
        adapter.setController(controller);
        rvSensors.setAdapter(adapter);

    }

    /*
       It fills up an array with strings, with the names of all the sensors.
       This is because you need to have an string array refered to in the adapter.
       After it notifies the adapter that it got new content.
       It also prevents the array to keep on filling up with new components if it is already filled once.
     */

    private void populate(){
        sensorList = controller.getSensorList();
        if(!(sensorNames.size() > 1)) {
            for (int i = 0; i < sensorList.size(); i++) {
                sensorNames.add(sensorList.get(i).getName());
            }
        }
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
