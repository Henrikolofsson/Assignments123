package Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import henrik.mau.assignments.Controller;
import henrik.mau.assignments.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Assignment3Fragment extends Fragment {
    private Controller controller;
    private SeekBar sbBrightness;
    private RadioGroup rgBrightness;
    private RadioButton rbSystem;
    private RadioButton rbScreen;
    private TextView tvPreference;
    private TextView tvShowSystemValue;
    private TextView tvShowScreenValue;
    private String preference = "Low";

    public Assignment3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment3, container, false);
        initializeComponents(view);
        registerListeners();
        return view;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    private void initializeComponents(View view){
        sbBrightness = (SeekBar) view.findViewById(R.id.sbBrightness);
        tvPreference = (TextView) view.findViewById(R.id.tvPreference);
        tvShowSystemValue = (TextView) view.findViewById(R.id.tvShowSystem);
        tvShowScreenValue = (TextView) view.findViewById(R.id.tvShowScreen);
        rgBrightness = (RadioGroup) view.findViewById(R.id.radioGroup);
        rbSystem = (RadioButton) view.findViewById(R.id.rbSystemBrightness);
        rbScreen = (RadioButton) view.findViewById(R.id.rbScreenBrightness);
    }

    public String getPreference(){
        return preference;
    }

    public void setTvSystemValue(String text){
        tvShowSystemValue.setText(text);
    }

    public void setTvScreenValue(String text){
        tvShowScreenValue.setText(text);
    }

    /*
       Register a listener to the SeekBar in the Assignment3Fragment.
       It sets the persons preference depending on what value the SeekBar returns.
       Depending on what radio button that is checked it sends the preference to the controller
       to set the brightness of either the screen or both system and screen.
     */
    private void registerListeners(){
        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress > 0 && progress < 21){
                    preference = "Low";
                }
                else if(progress > 21 && progress < 41){
                    preference = "Medium";
                }
                else if(progress > 41 && progress < 61){
                    preference = "High";
                }
                else if(progress > 61 && progress < 81){
                    preference = "Damn you want new eyes son?";
                }
                else if(progress > 81 && progress <= 100){
                    preference = "Bruh we can finally stop using solar energy and use your cell phone as a primary source of power";
                }
                tvPreference.setText("Your preference is: " + preference);

                switch(rgBrightness.getCheckedRadioButtonId()){
                    case R.id.rbSystemBrightness:
                        controller.setSystemBrightness(preference);
                        controller.setScreenBrightness(preference);
                        break;

                    case R.id.rbScreenBrightness:
                        controller.setScreenBrightness(preference);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
