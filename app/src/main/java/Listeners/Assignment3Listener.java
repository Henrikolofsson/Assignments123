package Listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import henrik.mau.assignments.Controller;

/**
 * @Assignment3Listener
 * @author Henrik Olofsson
 * This class is used for assignment 3 to listen on the Proximity and Light sensor.
 * What should it do: It is supposed to turn the flash light on if the proximity sensor is
 * feeling that an object is close enough given a threshold. It is also supposed to
 * set the value of the light sensor in the controller.
 */

public class Assignment3Listener implements SensorEventListener {
    private Controller controller;
    private Sensor proximitySensor;

    public Assignment3Listener(Controller controller, Sensor proximitySensor){
        this.controller = controller;
        this.proximitySensor = proximitySensor;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_PROXIMITY:
                float distanceToPhone = event.values[0];
                if(distanceToPhone < 3){
                    if(!controller.getIsFlashLightOn()){
                        Log.d("INNE", "inne");
                        controller.turnFlashLightOn();
                    }
                } else {
                    if(controller.getIsFlashLightOn()){
                        controller.turnFlashLightOff();
                    }
                }
                break;

            case Sensor.TYPE_LIGHT:
                float light = event.values[0];
                if(light > 0 && light < 100){
                    controller.setLight(light);
                }
                break;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
