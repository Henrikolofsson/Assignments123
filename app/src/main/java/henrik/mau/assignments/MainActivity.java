package henrik.mau.assignments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import Listeners.Assignment2Listener;
import Listeners.Assignment3Listener;

/**
 * @MainActivity
 * @author Henrik Olofsson
 * This class is used to communicate with android OS, and many of these things is controlled by the controller.
 * What does it do: It initiates the controller and a fragment manager to exchange fragments dynamically in the
 * "Main container". After that is done it will initiate the sensors, first deliver a list of all sensors to controller.
 * This is for the assignment1 task. After that it will initiate the Temperature-, Humidity- and Proximity sensors. Assignment2 task.
 * Also instances a listener for assignment 3 that is registered when clicking into the assignment3 inside the drawer layout.
 * When you use the back button it unregisters it. HOWEVER it does not yet unregister the sensors if the click is made inside the drawer layout.
 * Adds a camera manager for the torch light. This is controlled by the proximity sensor
 * in Assignment2Listener, and a boolean called isFlashlightOn inside controller.
 */

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Controller controller;
    private FragmentManager fm;

    private SensorManager mSensorManager;
    private Assignment2Listener assignment2Listener;
    private Assignment3Listener assignment3Listener;
    private Sensor mTemperatureSensor;
    private Sensor mLocalPressureSensor;
    private Sensor mHumiditySensor;
    private Sensor mProximitySensor;
    private Sensor mLightSensor;
    private boolean isTemperatureSensorPresent;
    private boolean isLocalPressureSensorPresent;
    private boolean isHumiditySensorPresent;
    private boolean isProximitySensorPresent;
    private boolean isLightSensorPresent;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private CameraManager mCameraManager;
    private String cameraID;
    private CameraCharacteristics cameraCharacteristics;

    private boolean isFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeSystem();
        initializeSensors();
        initializeComponents();
        initializeCamera();
        checkPermission();
    }

    private void initializeSystem(){
        fm = getSupportFragmentManager();
        controller = new Controller(this);
    }

    private void initializeSensors(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        controller.setSensorList(mSensorManager.getSensorList(Sensor.TYPE_ALL));
        assignment2Listener = new Assignment2Listener(controller);
        initializeTemperatureSensor();
        initializeHumiditySensor();
        initializeLocalPressureSensor();
        initializeProximitySensor();
        assignment3Listener = new Assignment3Listener(controller, mProximitySensor);
        initializeLightSensor();
    }

    private void initializeTemperatureSensor(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            mTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTemperatureSensorPresent = true;
        } else {
            Toast.makeText(this, "Temperature sensor is not available!", Toast.LENGTH_SHORT).show();
            isTemperatureSensorPresent = false;
        }
    }

    private void initializeHumiditySensor(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null){
            mHumiditySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            isHumiditySensorPresent = true;
        } else {
            Toast.makeText(this, "Humidity sensor is not available!", Toast.LENGTH_SHORT).show();
            isHumiditySensorPresent = false;

        }
    }

    private void initializeLocalPressureSensor(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){
            mLocalPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            isLocalPressureSensorPresent = true;
        } else {
            Toast.makeText(this, "Pressure sensor is not available!", Toast.LENGTH_SHORT).show();
            isLocalPressureSensorPresent = false;
        }
    }

    private void initializeProximitySensor(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            isProximitySensorPresent = true;
        } else {
            Toast.makeText(this, "Proximity sensor is not available!", Toast.LENGTH_SHORT).show();
            isProximitySensorPresent = false;
        }
    }

    private void initializeLightSensor(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            isLightSensorPresent = true;
        } else {
            Toast.makeText(this, "Light sensor is not available!", Toast.LENGTH_SHORT).show();
            isLightSensorPresent = false;
        }
        controller.initScreenBrightness();
    }

    /*
       Sets up a toolbar and navigation view listener. Depending on what item you click
       in the menu it will set given fragment in controller.
     */
    private void initializeComponents(){
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_assignment1:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        controller.setAssignment1Fragment();
                        return true;

                    case R.id.nav_assignment2:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        controller.setAssignment2Fragment();
                        return true;

                    case R.id.nav_assignment3:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        controller.setAssignment3Fragment();
                        registerAssignment3Listener();
                        return true;
                }
                return false;
            }
        });
    }

    /*
       Initializes the camera for flash light purposes.
     */
    private void initializeCamera(){
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraID = mCameraManager.getCameraIdList()[0];
            cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraID);
        } catch(CameraAccessException e){
            e.printStackTrace();
        }
    }

    /*
       Used to dynamically change fragments.
     */
    public void setFragment(Fragment fragment, String tag){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_container, fragment, tag);
        ft.commit();
    }

    /*
       Used by dataFragment to add and store fragments in it.
     */
    public void addFragment(Fragment fragment, String tag){
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(fragment, tag);
        ft.commit();
    }

    /*
       Calls the controllers onBackPressed method to control
       what fragments is exchanged to when pressed.
     */
    @Override
    public void onBackPressed() {
        if(controller.onBackPressed()){
            super.onBackPressed();
        }
    }

    public Fragment getFragment(String tag){
        return fm.findFragmentByTag(tag);
    }


    /*
       If the button in the toolbar is pressed it opens the drawer layout
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
       Register listener for Assignment1.
     */
    protected void registerListener(Sensor sensor){
        mSensorManager.registerListener(this, sensor, mSensorManager.SENSOR_DELAY_NORMAL);
        Toast.makeText(this, "REGISTERED LISTENER ON " + sensor.getName(),Toast.LENGTH_SHORT).show();
    }

    /*
       Unregister listener for Assignment1,
     */
    protected void unregisterListener(Sensor sensor){
        mSensorManager.unregisterListener(this);
        Toast.makeText(this, "UNREGISTERED LISTENER ON " + sensor.getName(),Toast.LENGTH_SHORT).show();
    }

    /*
       Register listener for Assignment2.
     */
    protected void startReadingSensors(){
        mSensorManager.registerListener(assignment2Listener, mTemperatureSensor, mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(assignment2Listener, mHumiditySensor, mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(assignment2Listener, mLocalPressureSensor, mSensorManager.SENSOR_DELAY_NORMAL);
        Toast.makeText(this, "REGISTERED LISTENER ON TEMPERATURE, HUMIDITY AND PRESSURE SENSORS", Toast.LENGTH_LONG).show();
    }

    protected void stopReadingSensors(){
        mSensorManager.unregisterListener(assignment2Listener);
        Toast.makeText(this, "UNREGISTERED LISTENER ON TEMPERATURE, HUMIDITY AND PRESSURE SENSORS", Toast.LENGTH_LONG).show();
    }

    protected void registerAssignment3Listener(){
        mSensorManager.registerListener(assignment3Listener, mProximitySensor, mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(assignment3Listener, mLightSensor, mSensorManager.SENSOR_DELAY_NORMAL);
        Toast.makeText(this, "REGISTERED LISTENER ON LIGHT AND PROXIMITY SENSORS", Toast.LENGTH_LONG).show();
    }

    protected void unregisterAssignment3Listener(){
        mSensorManager.unregisterListener(assignment3Listener, mProximitySensor);
        mSensorManager.unregisterListener(assignment3Listener, mLightSensor);
        Toast.makeText(this, "UNREGISTERED LISTENER ON LIGHT AND PROXIMITY SENSORS", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        controller.presentValues(event.values, event.accuracy, event.timestamp);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void turnFlashLightOn(){
        if(cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)){
            try {
                mCameraManager.setTorchMode(cameraID, true);
                isFlashOn = true;
                Log.d("TAG", "TURNING ON FLASH");
                Toast.makeText(this, "TorchLight is on", Toast.LENGTH_SHORT).show();
            } catch(CameraAccessException e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "This phone does not have a flash light", Toast.LENGTH_SHORT).show();
        }
    }

    public void turnFlashLightOff(){
        if(isFlashOn) {
            try {
                mCameraManager.setTorchMode(cameraID, false);
                isFlashOn = false;
                Toast.makeText(this, "TorchLight is off", Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /*
       Checks if this application(or more correctly MainActivity) has the write to write settings in the system.
       If it is denied, it asks for permission.
     */
    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, 101 );
        }
    }





}
