package Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import henrik.mau.assignments.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment {
    private String activeFragment;

    public DataFragment() {
        // Required empty public constructor
    }

    /*
       This fragment is used for saving the current active fragment.
       It also calls setRetainInstance to true, if the activity under runtime sometimes destroy itself
       to remember what fragment that was active before destroying itself.
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setActiveFragment(String activeFragment){
        this.activeFragment = activeFragment;
    }

    public String getActiveFragment(){
        return activeFragment;
    }
}
