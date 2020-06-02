package com.example.mymap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, SensorEventListener {

    SensorManager deviceSensorManager;
    Sensor accelerometer, light, barometer, thermometer, humidity, gyroscope;
    // Sensor Name
    public static String sensor;
    // Sensor Value
    public static String sensor_value;
    // Values of sensors
    public static String  accelerometer_value;
    public static String  light_value;
    public static String  gravity_value;
    public static String  temperature_value;
    public static String  humidity_value;
    public static String  gyroscope_value;

    LocationManager locationManager;
    public static FragmentManager fragmentManager;
    public static GoogleMap mMap;
    public static FirebaseFirestore db;
    public static CollectionReference reference;

    public static LatLng latLng;
    public static Geocoder geocoder;
    public static List<Address> addressList;

    private final long MIN_TIME = 0;
    private final long MIN_DISTANCE = 0;
    private final float ZOOM_LEVEL = 10.2f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Set Up DeviceSensorManager
        deviceSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Create fragmentManager
        fragmentManager = getSupportFragmentManager();

        // Instance firestore
        db = FirebaseFirestore.getInstance();
        // Get collection
        reference = db.collection("locations");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Load Data from Firebase Firestore
        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                 @Override
                 public void onSuccess(QuerySnapshot documentSnapshots) {
                     if (documentSnapshots.isEmpty()) {
                         Toast.makeText(getApplicationContext(), "onSuccess: LIST EMPTY\"", Toast.LENGTH_LONG).show();
                         return;
                     } else {
                         System.out.println("hello");
                         // Convert the whole Query Snapshot to a list
                         // of objects directly! No need to fetch each
                         // document.
                         List<MarkerPoint> markerPoints = documentSnapshots.toObjects(MarkerPoint.class);

                        // Loop for all List
                        for(MarkerPoint child : markerPoints)
                        {
                            MarkerOptions markerOptions = new MarkerOptions();
                            String latitude2 = child.getLatitude();
                            String longitude2 = child.getLongitude();
                            double loclatitude2 = Double.parseDouble(latitude2);
                            double loclongitude2 = Double.parseDouble(longitude2);
                            LatLng cod2 = new LatLng(loclatitude2, loclongitude2);
                            String colorMarker2 = child.getColorMarker();
                            String info2 = child.getInfo();
                            String sensor2 = child.getSensor();
                            String sensor_value2 = child.getSensor_value();
                            String info_and_sensor2 = "Info: " + info2 + "\n Sensor Type: " + sensor2 + "\n Sensor Value: " + sensor_value2;
                            String countryName2 = child.getCountryName();
                            String locality2 = child.getLocality();
                            String title2 = locality2 + " , " + countryName2;
                            switch (colorMarker2)
                            {
                                case "AZURE": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                    break;
                                case "BLUE": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                    break;
                                case "CYEAN": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                    break;
                                case "GREEN": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    break;
                                case "MAGENTA": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                    break;
                                case "ORANGE": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                    break;
                                case "RED": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                    break;
                                case "ROSE": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                                    break;
                                case "VIOLET": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                                    break;
                                case "YELLOW": markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                    break;
                            }
                            markerOptions.position(cod2);
                            markerOptions.title(title2);
                            markerOptions.snippet(info_and_sensor2);
                            mMap.addMarker(markerOptions);
                        }

                         Toast.makeText(getApplicationContext(), "onSuccess!!!", Toast.LENGTH_LONG).show();
                     }
                 }
             }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
             });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Get Latitude
                    double latitude = location.getLatitude();
                    // Get Longitude
                    double longitude = location.getLongitude();
                    // Set LatLng with latitude and longitude
                    latLng = new LatLng(latitude, longitude);
                    // Create geocoder
                    geocoder = new Geocoder(getApplicationContext());
                    // Zoom The Marker
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
                    // Open Custom Adapter for Info
                    mMap.setInfoWindowAdapter(new Custom_Info_Window_Adapter(MapsActivity.this));
                    // If click on map
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                        @Override
                        public void onMapClick(LatLng latLng2)
                        {
                            // Call Fragment for fill user: color and info
                            fragmentManager.beginTransaction().replace(R.id.map, new Marker_Fill()).commit();
                        }
                    });
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(String provider) {}
                @Override
                public void onProviderDisabled(String provider) {}
            });
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Get Latitude
                    double latitude = location.getLatitude();
                    // Get Longitude
                    double longitude = location.getLongitude();
                    // Set LatLng with latitude and longitude
                    latLng = new LatLng(latitude, longitude);
                    // Create geocoder
                    geocoder = new Geocoder(getApplicationContext());
                    // Zoom The Marker
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
                    // Open Custom Adapter for Info
                    mMap.setInfoWindowAdapter(new Custom_Info_Window_Adapter(MapsActivity.this));
                    // If click on map
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                        @Override
                        public void onMapClick(LatLng latLng2)
                        {
                            // Call Fragment for fill user: color and info
                            fragmentManager.beginTransaction().replace(R.id.map, new Marker_Fill()).commit();
                        }
                    });
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(String provider) {}
                @Override
                public void onProviderDisabled(String provider) {}
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Show blue point for my potition.
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onMarkerClick(Marker marker) { return false; }

    // Go back in main page
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, MapsActivity.class));
        finish();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        deviceSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        deviceSensorManager.unregisterListener(this);

        // Set up sensors
        accelerometer = deviceSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = deviceSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        barometer = deviceSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        thermometer = deviceSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humidity = deviceSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        gyroscope = deviceSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // accelerometer
        if (accelerometer != null)
        {
            deviceSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            Toast.makeText(this, "No accelerometer sensor.", Toast.LENGTH_LONG).show();
        }
        // light
        if (light != null)
        {
            deviceSensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            Toast.makeText(this, "No light sensor.", Toast.LENGTH_LONG).show();
        }
        // barometer
        if (barometer != null)
        {
            deviceSensorManager.registerListener(this, barometer, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            Toast.makeText(this, "No barometer sensor.", Toast.LENGTH_LONG).show();
        }
        // thermometer
        if (thermometer != null)
        {
            deviceSensorManager.registerListener(this, thermometer, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            Toast.makeText(this, "No thermometer sensor.", Toast.LENGTH_LONG).show();
        }
        // humidity
        if (humidity != null)
        {
            deviceSensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            Toast.makeText(this, "No humidity sensor.", Toast.LENGTH_LONG).show();
        }
        // gyroscope
        if (gyroscope != null)
        {
            deviceSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            Toast.makeText(this, "No gyroscope sensor.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Change values with change event
        switch (event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometer_value = "x: " + Float.toString(event.values[0])+" y: " + Float.toString(event.values[1])+" z: " + Float.toString(event.values[2]);
                break;
            case Sensor.TYPE_LIGHT:
                light_value = Float.toString(event.values[0]);
                break;
            case Sensor.TYPE_GRAVITY:
                gravity_value = "x: " + Float.toString(event.values[0])+" y: " + Float.toString(event.values[1])+" z: " + Float.toString(event.values[2]);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                temperature_value = Float.toString(event.values[0]);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                humidity_value = Float.toString(event.values[0]);
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroscope_value = "x: " + Float.toString(event.values[0])+" y: " + Float.toString(event.values[1])+" z: " + Float.toString(event.values[2]);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

//    Timer timer = new Timer();
//    timer.scheduleAtFixedRate(new TimerTask() {
//          @Override
//          public void run() {
//
//          }
//      }, 0, 60000);   // 1000 Millisecond  = 1 second
