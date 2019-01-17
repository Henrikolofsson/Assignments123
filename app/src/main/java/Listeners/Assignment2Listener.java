package Listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import henrik.mau.assignments.Controller;

public class Assignment2Listener implements SensorEventListener {
    private Controller controller;

    public Assignment2Listener(Controller controller){
        this.controller = controller;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            float temperature = event.values[0];
            controller.sensorInput("TYPE_AMBIENT_TEMPERATURE", temperature);
        }

        else if(event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
            float humidity = event.values[0];
            controller.sensorInput("TYPE_RELATIVE_HUMIDITY", humidity);
        }

        else if(event.sensor.getType() == Sensor.TYPE_PRESSURE){
         float pressure = event.values[0];
         controller.sensorInput("TYPE_PRESSURE", pressure);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
