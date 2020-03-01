package com.example.greentrip;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Maps extends FragmentActivity implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    double OrigionalLatitude = 20;
    double OrigionalLongtitude = 20;
    double tempK;
    double wind;
    String weather;
    double temp;
    float zoomLevel = 15.0f;
    private GoogleMap mMap;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        TextView temperature = (TextView) findViewById(R.id.temp);
        TextView windSpeed = (TextView) findViewById(R.id.wind);
        TextView weahterData = (TextView) findViewById(R.id.weather);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra("Latitude")) {
                OrigionalLatitude = extras.getDouble("Latitude");
            }
            if (getIntent().hasExtra("Longitude")) {
                OrigionalLongtitude = extras.getDouble("Longitude");
            }

            if (getIntent().hasExtra("temp")) {
                //In Kelvin
                tempK = Double.parseDouble(Objects.requireNonNull(extras.getString("temp")));
                temp = tempK - 273.15;
                temperature.setText("Temperature: " + String.valueOf(temp));
            }
            if (getIntent().hasExtra("wind")) {
                wind = Double.parseDouble(Objects.requireNonNull(extras.getString("wind")));
                windSpeed.setText("Wind Speed: " + String.valueOf(wind));
            }
            if (getIntent().hasExtra("weather")) {
                weather = extras.getString("weather");
                weahterData.setText("Weather: " + weather);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //getNearByMarker();

        //Implement our travel stuff
        //Log.d("DDD", "COOR");
        Coordinate coordinate = new Coordinate(37.4419, -122.1430);

        PlacesNearby currentPlace = new PlacesNearby(coordinate);

        ArrayList<Place> places = currentPlace.getPlacesNearby();
        if(places == null){
            Log.d("DDD", "NULL");
        }else{
            Log.d("DDD", "NO");
        }

        getNearByMarker();


    }
    private void getNearByMarker() {
        String APIKEY = "5ae2e3f221c38a28845f05b69371569391d28e8ad62d7f28fac24e6f";
        Integer RADIUS = 5000;
        Integer NUMBER_OF_OBJECTS = 500;

        Coordinate coordinate;
        String url = String.format("https://api.opentripmap.com/0.1/en/places/radius?radius="+RADIUS+"&lon="+OrigionalLongtitude+"&lat="+OrigionalLatitude+"&limit="+NUMBER_OF_OBJECTS+"&format=geojson&apikey="+APIKEY);
        //coordinate = new Coordinate(OrigionalLatitude, OrigionalLongtitude);

        JsonObjectRequest que = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray array = response.getJSONArray("features");


                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        JSONObject prop = obj.getJSONObject("properties");
                        JSONObject geo = obj.getJSONObject("geometry");
                        JSONArray coords = geo.getJSONArray("coordinates");

                        Double[] realCoods = new Double[2];

                        if (coords != null) {
                            for (int j=0;j<=1;j++){
                                realCoods[j] = Double.parseDouble(coords.get(i).toString());
                            }
                        }

                        double lon = realCoods[0];
                        double lat = realCoods[1];
                        String title = "i";
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title(title));
                    }

                } catch (JSONException e) {
                    //txt.setText(String.valueOf(e));
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        RequestQueue lala = Volley.newRequestQueue(this);
        lala.add(que);
    }

    private void recommendTravelPlan(String weather, double wind, double temp) {
        /*
        if(weather.equals("Thunderstorm") || weather.equals("Rain") || weather.equals("Snow") || weather.equals("Tornado") ||
                weather.equals("Haze") || weather.equals("Smoke") || weather.equals("Dust") || weather.equals("Sand") || weather.equals("Squall") ||
                weather.equals("Ash") || temp <= -20 || temp >= 45 || wind >= 20){
            //Weather is super bad all indoor
            //100% indoor
        }
        else if(weather.equals("Drizzle") || weather.equals("Mist") || temp <= 0 || temp >= 35 || wind >= 10){
            //weather is meh, most indoor, no attraction
            //80% in 20% out
        }else if(weather.equals("Clear") || weather.equals("Cloud")){
            //Weather is good, attraction included
            //40% in 60% out
        }else{
            //In case we forget some of the index, balance both indoor and outdoor
        }
        */
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        LatLng latLng = new LatLng(OrigionalLatitude, OrigionalLongtitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

        //Implement

    }
    public void markerplacer (String[] names, double[] latitude, double[] longitude, String[] descrip){
        for (int i =0;i<names.length;i++){
            mMap.addMarker(new MarkerOptions().position(new LatLng(longitude[i],latitude[i]))
                    .title(names[i])
                    .flat(false)
                    .snippet(descrip[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            com.example.greentrip.PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Localizing your position", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (com.example.greentrip.PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        com.example.greentrip.PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}