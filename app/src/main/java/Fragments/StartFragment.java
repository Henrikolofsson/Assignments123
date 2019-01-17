package Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import henrik.mau.assignments.Controller;
import henrik.mau.assignments.R;

/**
 * A simple {@link Fragment} subclass.
 * A very basic fragment just made to look good.
 */

public class StartFragment extends Fragment {
    private Controller controller;

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        return view;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

}
