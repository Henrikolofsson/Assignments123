package Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Adapters.SensorAPIViewPageAdapter;
import henrik.mau.assignments.Controller;
import henrik.mau.assignments.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Assignment2Fragment extends Fragment {
    private Controller controller;
    private SensorAPIViewPageAdapter sensorViewPageAdapter;
    private ViewPager viewPager;
    private TabSensorFragment sensorFragment;
    private TabAPIFragment apiFragment;


    public Assignment2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment2, container, false);
        initializeComponents(view);
        setupViewPageAdapter(viewPager);
        initializeTabLayouts(view);
        return view;
    }

    private void initializeComponents(View view) {
        sensorViewPageAdapter = new SensorAPIViewPageAdapter(getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.vp_weather);
    }

    private void setupViewPageAdapter(final ViewPager viewPager){
        sensorViewPageAdapter.addFragment(sensorFragment, "Sensor");
        sensorViewPageAdapter.addFragment(apiFragment, "API");
        viewPager.setAdapter(sensorViewPageAdapter);
    }

    private void initializeTabLayouts(View view){
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayoutWeather);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void setSensorFragment(TabSensorFragment fragment){
        this.sensorFragment = fragment;
    }

    public void setApiFragment(TabAPIFragment fragment){
        this.apiFragment = fragment;

    }
}
