package com.example.mymap;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.io.IOException;


public class Marker_Fill extends Fragment {

    EditText input_marker_info;
    Spinner input_marker_color, input_sensor_type;
    Button submit_marker_button;

    public Marker_Fill() {
        // Required empty public constructor
    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker__fill, container, false);

        input_marker_info = view.findViewById(R.id.input_marker_info);
        input_marker_color = view.findViewById(R.id.input_marker_color);
        input_sensor_type = view.findViewById(R.id.input_sensor_type);
        submit_marker_button = view.findViewById(R.id.submit_marker_button);

        // Set Up Spinner and Adapter for Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.marker_colors, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        input_marker_color.setAdapter(adapter);

        input_marker_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set Up Spinner and Adapter for Spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.sensor_type, R.layout.support_simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        input_sensor_type.setAdapter(adapter2);

        input_sensor_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // If click button submit_marker
        submit_marker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_marker_info.getText().toString().equals(""))
                {
                    // Show message Fill all fields.
                    Toast.makeText(getActivity(), "Fill info field!", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    try {
                        // Create List<Address> with location from geocoder
                        MapsActivity.addressList = MapsActivity.geocoder.getFromLocation(MapsActivity.latLng.latitude, MapsActivity.latLng.longitude, 1);
                        // Get locality
                        String locality = MapsActivity.addressList.get(0).getLocality();
                        // Get country name
                        String countryName = MapsActivity.addressList.get(0).getCountryName();
                        // Selected color marker
                        String selected_color_marker = input_marker_color.getSelectedItem().toString();
                        String color = selected_color_marker;
                        // Creating Marker
                        MarkerOptions markerOptions = new MarkerOptions();
                        // Set Marker Potition
                        markerOptions.position(MapsActivity.latLng);
                        // Set Latitude And Longitude On Marker
                        String title = locality + " , " + countryName;
                        markerOptions.title(title);
                        // Color The Marker
                        switch (color)
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
                        // Get Info
                        String info = input_marker_info.getText().toString();
                        // Selected sensor type
                        String selected_sensor_type = input_sensor_type.getSelectedItem().toString();
                        MapsActivity.sensor = selected_sensor_type;
                        // Get Value of sensor
                        if(MapsActivity.sensor != null) {
                            // Set up sensor_value
                            if (MapsActivity.sensor.equals("Accelerometer")) {
                                MapsActivity.sensor_value = MapsActivity.accelerometer_value;
                            } else if (MapsActivity.sensor.equals("Light")) {
                                MapsActivity.sensor_value = MapsActivity.light_value;
                            } else if (MapsActivity.sensor.equals("Barometer")) {
                                MapsActivity.sensor_value = MapsActivity.gravity_value;
                            } else if (MapsActivity.sensor.equals("Thermometer")) {
                                MapsActivity.sensor_value = MapsActivity.temperature_value;
                            } else if (MapsActivity.sensor.equals("Humidity")) {
                                MapsActivity.sensor_value = MapsActivity.humidity_value;
                            } else if (MapsActivity.sensor.equals("Gyroscope")) {
                                MapsActivity.sensor_value = MapsActivity.gyroscope_value;
                            }
                        }
                        // Info_And_Sensor The Marker
                        String info_and_sensor = "Info: " + info + "\nSensor Type: " + MapsActivity.sensor + "\nSensor Value: " + MapsActivity.sensor_value;
                        markerOptions.snippet(info_and_sensor);

                        // Add Marker On Map
                        MapsActivity.mMap.addMarker(markerOptions);
                        // Create a object "MarkerPoint" for save informations
                        MarkerPoint markerPoint = new MarkerPoint();
                        // Set all attributes
                        markerPoint.setCountryName(countryName);
                        markerPoint.setLocality(locality);
                        markerPoint.setLatitude(String.valueOf(MapsActivity.latLng.latitude));
                        markerPoint.setLongitude(String.valueOf(MapsActivity.latLng.longitude));
                        markerPoint.setColorMarker(color);
                        markerPoint.setInfo(info);
                        markerPoint.setSensor(MapsActivity.sensor);
                        markerPoint.setSensor_value(MapsActivity.sensor_value);
                        // Save in firebase database this object
                        MapsActivity.db.collection("locations").
                                add(markerPoint).
                                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getContext(), "Add it!", Toast.LENGTH_LONG).show();
                                    }
                                }).
                                addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Show message Add it
                    Toast.makeText(getActivity(), "Marker Added!", Toast.LENGTH_SHORT).show();
                    // Open MapsActivity
                    startActivity(new Intent(getContext(), MapsActivity.class));
                }
            }
        });
        return view;
    }
}
