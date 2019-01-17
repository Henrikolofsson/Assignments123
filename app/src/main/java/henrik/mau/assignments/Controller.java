package henrik.mau.assignments;

import android.content.ContentResolver;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Entities.Weather;
import Fragments.Assignment1Fragment;
import Fragments.Assignment2Fragment;
import Fragments.Assignment3Fragment;
import Fragments.DataFragment;
import Fragments.StartFragment;
import Fragments.IndividualSensorFragment;
import Fragments.TabAPIFragment;
import Fragments.TabSensorFragment;

/**
 * @Controller
 * @author Henrik Olofsson
 * This class is used to communicate with the main activity and to be handling the majority of the logic.
 * Firstly it initiates all the fragments. It after that sets the fragment to be shown in application start up.
 * I had to also make it initiate a couple of arrays, because I could not find a way to just add the three first items
 * and after that just exchange them to new values. The problem: It would end up either just keep on filling it with
 * endless of sensor values or none at all.
 */

public class Controller {
    private MainActivity activity;

    private DataFragment dataFragment;
    private StartFragment startFragment;
    private Assignment1Fragment assignment1Fragment;
    private IndividualSensorFragment individualSensorFragment;
    private Assignment2Fragment assignment2Fragment;
    private TabSensorFragment tabSensorFragment;
    private TabAPIFragment tabAPIFragment;
    private Assignment3Fragment assignment3Fragment;

    private List<Sensor> sensorList;
    private String individualSensorClickedOn;
    private final static String API_KEY = "a268f0547f91575628dc6c1121b1b54e";
    private final static String malmo_url =
            "http://api.openweathermap.org/data/2.5/weather?id=2692969&appid=a268f0547f91575628dc6c1121b1b54e";
    private List<Weather> sensorContent;
    private List<Weather> APIContent;

    private boolean sendSensorContent = false;
    private boolean isFlashLightOn = false;
    private float brightness;

    private ContentResolver mContentResolver;
    private Window mWindow;
    private float light;


    public Controller(MainActivity activity){
        this.activity = activity;
        initializeFragments();
        setFragment("StartFragment");
        initEmptyArrayLists();
    }

    /*
       Initializes all the fragments.
     */
    private void initializeFragments(){
        initializeDataFragment();
        initializeStartFragment();
        initializeAssignment1Fragment();
        initializeIndividualSensorFragment();
        initializeTabSensorFragment();
        initializeTabAPIFragment();
        initializeAssignment2Fragment();
        initializeAssignment3Fragment();
    }

    private void initializeDataFragment(){
        dataFragment = (DataFragment) activity.getFragment("DataFragment");
        if(dataFragment == null){
            dataFragment = new DataFragment();
            activity.addFragment(dataFragment, "DataFragment");
            dataFragment.setActiveFragment("StartFragment");
        }
    }

    private void initializeStartFragment(){
        startFragment = (StartFragment) activity.getFragment("StartFragment");
        if(startFragment == null){
            startFragment = new StartFragment();
        }
        startFragment.setController(this);
    }

    private void initializeAssignment1Fragment(){
        assignment1Fragment = (Assignment1Fragment) activity.getFragment("Assignment1Fragment");
        if(assignment1Fragment == null){
            assignment1Fragment = new Assignment1Fragment();
        }
        assignment1Fragment.setController(this);
    }

    private void initializeIndividualSensorFragment(){
        individualSensorFragment = (IndividualSensorFragment) activity.getFragment("IndividualSensorFragment");
        if(individualSensorFragment == null){
            individualSensorFragment = new IndividualSensorFragment();
        }
        individualSensorFragment.setController(this);
    }

    private void initializeTabSensorFragment(){
        tabSensorFragment = (TabSensorFragment) activity.getFragment("TabSensorFragment");
        if(tabSensorFragment == null){
            tabSensorFragment = new TabSensorFragment();
        }
        tabSensorFragment.setController(this);
    }

    private void initializeTabAPIFragment(){
        tabAPIFragment = (TabAPIFragment) activity.getFragment("TabAPIFragment");
        if(tabAPIFragment == null){
            tabAPIFragment = new TabAPIFragment();
        }
        tabAPIFragment.setController(this);
    }

    private void initializeAssignment2Fragment(){
        assignment2Fragment = (Assignment2Fragment) activity.getFragment("Assignment2Fragment");
        if(assignment2Fragment == null){
            assignment2Fragment = new Assignment2Fragment();
        }
        assignment2Fragment.setController(this);
        assignment2Fragment.setSensorFragment(tabSensorFragment);
        assignment2Fragment.setApiFragment(tabAPIFragment);
    }

    private void initializeAssignment3Fragment(){
        assignment3Fragment = (Assignment3Fragment) activity.getFragment("Assignment3Fragment");
        if(assignment3Fragment == null){
            assignment3Fragment = new Assignment3Fragment();
        }
        assignment3Fragment.setController(this);
    }

    /*
       Initializes the arrays to be filled with empty nonsense.
       Just so that I could exchange these items later on with real values.
     */
    private void initEmptyArrayLists(){
        sensorContent = new ArrayList<>();
        sensorContent.add(new Weather("nothing", 0));
        sensorContent.add(new Weather("nothing", 0));
        sensorContent.add(new Weather("nothing", 0));
        APIContent = new ArrayList<>();
        APIContent.add(new Weather("nothing", 0));
        APIContent.add(new Weather("nothing", 0));
        APIContent.add(new Weather("nothing", 0));
    }

    /*
       Used to dynamically be able to change the fragments.
     */
    private void setFragment(String tag) {
        switch (tag) {
            case "StartFragment":
                setFragment(startFragment, tag);
                break;

            case "Assignment1Fragment":
                setFragment(assignment1Fragment, tag);
                break;

            case "IndividualSensorFragment":
                setFragment(individualSensorFragment, tag);
                break;

            case "Assignment2Fragment":
                setFragment(assignment2Fragment, tag);
                break;

            case "Assignment3Fragment":
                setFragment(assignment3Fragment, tag);
                break;
        }
    }

    /*
       Sends the fragment to the main activity to make a fragment transaction, and also
       store the TAG of the active fragment in DataFragment to easily get the active fragment.
     */
    private void setFragment(Fragment fragment, String tag) {
        activity.setFragment(fragment, tag);
        dataFragment.setActiveFragment(tag);
    }

    /*
       When androids back button pressed this method is used to change
       the current fragment to the fragment that was before the active fragment.
     */
    public boolean onBackPressed() {
        String activeFragment = dataFragment.getActiveFragment();

        switch(activeFragment){
            case "Assignment1Fragment":
                setFragment("StartFragment");
                break;

            case "IndividualSensorFragment":
                setFragment("Assignment1Fragment");
                break;

            case "Assignment2Fragment":
                setFragment("StartFragment");
                break;

            case "Assignment3Fragment":
                activity.unregisterAssignment3Listener();
                setFragment("StartFragment");
                break;
        }
        return false;
    }

    public void setSensorList(List<Sensor> sensorList){
        this.sensorList = sensorList;
    }

    public List<Sensor> getSensorList(){
        return sensorList;
    }

    public void setAssignment1Fragment(){
        setFragment("Assignment1Fragment");
    }

    public void setAssignment2Fragment(){
        setFragment("Assignment2Fragment");
    }

    public void setAssignment3Fragment(){
        setFragment("Assignment3Fragment");
    }

    /*
       Sets an active fragment clicked on in the Assignment1Fragment.
     */
    public void setIndividualSensorFragment(String sensor){
        setFragment("IndividualSensorFragment");
        setIndividualSensorClickedOn(sensor);
    }

    public void setIndividualSensorClickedOn(String sensorName){
        this.individualSensorClickedOn = sensorName;
    }

    public String getIndividualSensorClickedOn(){
        return individualSensorClickedOn;
    }

    /*
       Register a sensor in a list of sensors based on the name of the clicked sensor.
     */
    public void registerSensor(){
        for(Sensor s : sensorList){
            if(s.getName().equals(individualSensorClickedOn)){
                activity.registerListener(s);
            }
        }
    }

    /*
       Calls the MainActivity to read the Humidity-, Pressure- and Temperature-sensors.
       Sending their input to Assignment2Fragment.
     */
    public void startReadingSensors(){
        activity.startReadingSensors();
        sendSensorContent();
    }

    /*
       Calls the MainActivity to stop reading the Humidity-, Pressure- and Temperature-sensors.
     */
    public void stopReadingSensors(){
        activity.stopReadingSensors();
        sendSensorContent = false;
    }

    /*
       Unregister the sensor in Assignment1Fragment that is currently listened on.
     */
    public void unregisterSensor(){
        for(Sensor s : sensorList){
            if(s.getName().equals(individualSensorClickedOn)){
                activity.unregisterListener(s);
            }
        }
    }

    public void setIsFlashLightOn(){
        isFlashLightOn = true;
    }

    public void setIsFlashLightOff(){
        isFlashLightOn = false;
    }

    public boolean getIsFlashLightOn(){
        return isFlashLightOn;
    }

    /*
       Turns boolean isLightOn to true. After that request the MainActivity to turn on the flash light.
       This method is called from the Assignment3Listener, depending on proximity sensor.
     */
    public void turnFlashLightOn(){
        Log.d("TAG", "turning on flash");
        setIsFlashLightOn();
        activity.turnFlashLightOn();
    }

    /*
       Turns boolean isLightOn to false. After that request MainActivity to turn off the flash light.
     */
    public void turnFlashLightOff(){
        setIsFlashLightOff();
        activity.turnFlashLightOff();
    }

    /*
       Sends the values from the chosen sensor in Assignment1Fragment.
       Depending on how many values there is, it sets the TextView:s text in the individualSensorFragment.
     */
    public void presentValues(float[] values, int accuracy, long timestamp) {
        Log.d("VALUES", ""+values.length);
        if(values.length == 1){
            individualSensorFragment.setTvSensorValues("The value is \n" + values[0] +
                    "\nAccuracy: " + accuracy +
                    "\nTimestamp: " + timestamp);
        }
        else if(values.length == 2){
            individualSensorFragment.setTvSensorValues("The values is \n" + values[0] +
                    "\n " + values[1] +
                    "\nAccuracy: " + accuracy +
                    "\nTimestamp: " + timestamp);
        }
        else if(values.length == 3){
            individualSensorFragment.setTvSensorValues("The values is \n" + values[0] +
                    "\n" + values[1] +
                    "\n" + values[2] +
                    "\nAccuracy: " + accuracy +
                    "\nTimestamp: " + timestamp);
        }
        else if(values.length == 4){
            individualSensorFragment.setTvSensorValues("The values is \n" + values[0] +
                    "\n" + values[1] +
                    "\n" + values[2] +
                    "\n" + values[3] +
                    "\nAccuracy: " + accuracy +
                    "\nTimestamp: " + timestamp);
        }
        else if(values.length == 5){
            individualSensorFragment.setTvSensorValues("The values is \n" + values[0] +
                    "\n" + values[1] +
                    "\n" + values[2] +
                    "\n" + values[3] +
                    "\n" + values[4] +
                    "\nAccuracy: " + accuracy +
                    "\nTimestamp: " + timestamp);
        }
        else if(values.length == 6){
            individualSensorFragment.setTvSensorValues("The values is \n" + values[0] +
                    "\n " + values[1] +
                    "\n " + values[2] +
                    "\n " + values[3] +
                    "\n " + values[4] +
                    "\n" + values[5] +
                    "\nAccuracy: " + accuracy +
                    "\nTimestamp: " + timestamp);
        }
    }

    /*
       If the radio button Volley is checked in Assignment2Fragment, this method will be called to send a
       volley weather request to OpenWeatherAPI.
     */
    public void sendVolleyRequest(){
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, malmo_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handleResponse(response, "VOLLEY");
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error occurred ", error);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(jor);
    }

    /*
       If Async-Task is checked in Assignment2Fragment this method will be called to send
       an async task to OpenWeatherAPI.
     */
    public void sendAsyncRequest(){
        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute();
    }

    /*
       This method handles the response from the API, and also LOG:s
       what values returned and kind of method was used to send the request.
     */
    private void handleResponse(JSONObject response, String log) {
        try {
            JSONObject main_object = response.getJSONObject("main");

            String temperature = main_object.getString("temp");
            Double doubleTemp = Double.parseDouble(temperature) - 273.15;
            Float floatTemp = doubleTemp.floatValue();
            Weather temp = new Weather("TYPE_AMBIENT_TEMPERATURE", floatTemp);

            String humidity = main_object.getString("humidity");
            Weather humi = new Weather("TYPE_RELATIVE_HUMIDITY", Float.parseFloat(humidity));

            String pressure = main_object.getString("pressure");
            Weather press = new Weather("TYPE_PRESSURE", Float.parseFloat(pressure));

            Log.d("Information", "\nTemperature: " + temperature + ", As float: " + floatTemp
                    + "\nHumidity: " + humidity + ", As float: " + Float.parseFloat(humidity) +
                    "\nPressure: " + pressure + ", As float: " + Float.parseFloat(pressure) +
                    "\nLOG: " + log);

            APIContent.set(0, temp);
            APIContent.set(1, humi);
            APIContent.set(2, press);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tabAPIFragment.setContent(APIContent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
       A custom made async task used to open up a connection with OpenWeatherAPI and send a weather request
       specifically for Malmö.
     */
    private class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(malmo_url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    return br.readLine();

                } finally {
                    urlConnection.disconnect();
                }

            } catch(MalformedURLException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
            return "Finished!";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject response = new JSONObject(s);
                handleResponse(response, "ASYNC");
            } catch(JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    /*
       Depending on the sensor input it will fill the arrays that earlier was filled with nonsense
       with new real values to be used inside the TabSensorFragment.
     */
    public void sensorInput(String sensorType, float value){
        Weather weather = new Weather(sensorType, value);
        switch(sensorType){
            case "TYPE_AMBIENT_TEMPERATURE":
                sensorContent.set(0, weather);
                break;

            case "TYPE_RELATIVE_HUMIDITY":
                sensorContent.set(1, weather);
                break;

            case "TYPE_PRESSURE":
                sensorContent.set(2, weather);
                break;
        }
    }

    /*
       An awful way of sending content to TabSensorFragment with the array of values.
       However this was the only way I could think of doing it, since I got a lot of
       errors. The errors was basically because this class is not the parent of the views,
       and is not allowed to touch the child views of the activity.
       It does however, not look good with a thread inside a thread. How should this be done instead?
     */
    private void sendSensorContent(){
        sendSensorContent = true;
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                while(sendSensorContent) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tabSensorFragment.setContent(sensorContent);
                        }
                    });
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 5000);
    }

    /*
       Used for writing settings to the system.
     */
    public void initScreenBrightness(){
        mContentResolver = activity.getContentResolver();
        mWindow = activity.getWindow();
    }

    /*
       Light set by the Assignment3Listener.
     */
    public void setLight(float light){
        this.light = light;
    }

    public float getLight(){
        return light;
    }

    /*
       Depending on what preset the user have chosen, it will
       set the SystemBrightness. Brightness * 255.
       This is done by writing the system settings, but only if the user have allowed so.
     */
    public void setSystemBrightness(String preference){
        if(preference.toLowerCase().equals("low")){
            if(light > 0 && light < 21){
                brightness = 0.1f;
            }
            else if(light > 21 && light < 41){
                brightness = 0.2f;
            }
            else if(light > 41 && light < 61){
                brightness = 0.3f;
            }
            else if(light > 61 && light < 81){
                brightness = 0.4f;
            }
            else if(light > 81 && light <= 100){
                brightness = 0.5f;
            }
        }
        else if(preference.toLowerCase().equals("medium")){
            if(light > 0 && light < 21){
                brightness = 0.2f;
            }
            else if(light > 21 && light < 41){
                brightness = 0.3f;
            }
            else if(light > 41 && light < 61){
                brightness = 0.4f;
            }
            else if(light > 61 && light < 81){
                brightness = 0.5f;
            }
            else if(light > 81 && light <= 100){
                brightness = 0.6f;
            }
        }
        else if(preference.toLowerCase().equals("high")){
            if(light > 0 && light < 21){
                brightness = 0.3f;
            }
            else if(light > 21 && light < 41){
                brightness = 0.4f;
            }
            else if(light > 41 && light < 61){
                brightness = 0.5f;
            }
            else if(light > 61 && light < 81){
                brightness = 0.6f;
            }
            else if(light > 81 && light <= 100){
                brightness = 0.7f;
            }
        }
        else if(preference.toLowerCase().equals("damn you want new eyes son?")){
            if(light > 0 && light < 21){
                brightness = 0.4f;
            }
            else if(light > 21 && light < 41){
                brightness = 0.5f;
            }
            else if(light > 41 && light < 61){
                brightness = 0.6f;
            }
            else if(light > 61 && light < 81){
                brightness = 0.7f;
            }
            else if(light > 81 && light <= 100){
                brightness = 0.8f;
            }
        }
        else {
            brightness = 1.0f;
        }

        if(!Settings.System.canWrite(activity)){
            Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            activity.startActivity(i);
        }
        else {
            Log.d("VALUE:", Integer.toString((int)(brightness*255)));
            assignment3Fragment.setTvSystemValue("System value: " + Integer.toString((int)(brightness*255)) + " / 255");
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) (brightness*255));
        }
    }

    /*
       Sets the ScreenBrightness depending on the preference of the user.
       I separated the methods setSystemBrightness and setScreenBrightness, because both methods is
       going to be called if the preference is to set system brightness. However if only screen brightness
       is chosen in Assignment3Fragment, I only want setScreenBrightness to be called.
     */
    public void setScreenBrightness(String preference){
        float screenBrightness;
        if(preference.toLowerCase().equals("low")){
            screenBrightness = 0.2f;
        }
        else if(preference.toLowerCase().equals("medium")){
            screenBrightness = 0.4f;
        }
        else if(preference.toLowerCase().equals("high")){
            screenBrightness = 0.6f;
        }
        else if(preference.toLowerCase().equals("damn you want new eyes son?")){
            screenBrightness = 0.8f;
        }
        else {
            screenBrightness = 1;
        }

        assignment3Fragment.setTvScreenValue("Screen Brightness: " + Float.toString(screenBrightness) + " / " +
                "1");

        WindowManager.LayoutParams mLayoutParams = activity.getWindow().getAttributes();
        mLayoutParams.screenBrightness = screenBrightness;
        activity.getWindow().setAttributes(mLayoutParams);
    }


}
