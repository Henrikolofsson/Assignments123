package Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import henrik.mau.assignments.R;

/**
 * A simple {@link Fragment} subclass.
 * A class for the fragments in the recycler view contained inside Assignment2Fragment.
 */
public class WeatherRVFragment extends Fragment {
    private TextView tvWeatherType;
    private TextView tvWeatherResult;

    public WeatherRVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_rv, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view){
        tvWeatherType = (TextView) view.findViewById(R.id.tvWeatherType);
        tvWeatherResult = (TextView) view.findViewById(R.id.tvWeatherResult);
    }

    public void setTvWeatherType(String text){
        tvWeatherType.setText(text);

    }

    public void setTvWeatherResult(String text){
        tvWeatherResult.setText(text);
    }

    public String getTvWeatherType(){
        return tvWeatherType.getText().toString();
    }

    public String getTvWeatherResult(){
        return tvWeatherResult.getText().toString();
    }



}
