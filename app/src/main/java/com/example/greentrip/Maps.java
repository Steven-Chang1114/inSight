package com.example.greentrip;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class Maps extends AppCompatActivity implements
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

    double OrigionalLatitude;
    double OrigionalLongtitude;
    double tempK;
    double wind;
    String wea;
    double temp;
    float zoomLevel = 15.0f;
    private GoogleMap mMap;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                tempK = Double.parseDouble(extras.getString("temp"));
                temp = tempK - 273.15;
                temperature.setText("Temperature: " + String.valueOf(temp));
            }
            if (getIntent().hasExtra("wind")) {
                wind = Double.parseDouble(Objects.requireNonNull(extras.getString("wind")));
                windSpeed.setText("Wind Speed: " + String.valueOf(wind));
            }
            if (getIntent().hasExtra("weather")) {
                wea = extras.getString("weather");
                weahterData.setText("Weather: " + wea);
            }
        }
        //getNearByMarker();

        getNearByMarker();


    }
    private void getNearByMarker() {
        String APIKEY = "5ae2e3f221c38a28845f05b69371569391d28e8ad62d7f28fac24e6f";
        Integer RADIUS = 10000;
        Integer NUMBER_OF_OBJECTS = 500;

        String url = String.format("https://api.opentripmap.com/0.1/en/places/radius?radius="+RADIUS+"&lon="+OrigionalLongtitude+"&lat="+OrigionalLatitude+"&limit="+NUMBER_OF_OBJECTS+"&format=geojson&apikey="+APIKEY);

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

                        int rate = prop.getInt("rate");
                        String kind = prop.getString("kinds");

                        Double[] realCoods = new Double[2];

                        if (coords != null) {
                            for (int j=0;j<=1;j++){
                                realCoods[j] = Double.parseDouble(coords.get(j).toString());
                            }
                        }

                        double lon = realCoods[0];
                        double lat = realCoods[1];
                        String title = prop.getString("name");
                        String name = formatString(title);

                        boolean isIndoor = isIndoor(kind);
                        boolean forIndoor = recommendTravelPlan(wea, wind, temp);
                        boolean isForTravel = isAttraction(title);
                        boolean res;

                        res = forIndoor == isIndoor && isForTravel;


                        String emission = emission_checker(OrigionalLatitude, OrigionalLongtitude, lon, lat);

                        boolean a = forIndoor;
                        if(res) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(name)
                                    .snippet(String.valueOf(isIndoor))
                                    .visible(true));
                        }
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

    private String formatString(String title) {
        if(title.length() > 20){
            String[] list = title.split(",");
            String res = list[list.length - 1];
            return res;
        }else{
            return title;
        }
    }

    private boolean isAttraction(String title) {
        if (title.contains("Street")) {
            return false;
        }else{
            return true;
        }
    }

    public String emission_checker(double originlat, double originlng, double destlng, double destlat){
        float[] results = new float[10];
        Location.distanceBetween(originlat,originlng,destlat,destlng,results);
        int cf = (int)((results[0])* 0.186);
        String CarbonFootprint;
        if(cf <= 50) {
            CarbonFootprint = "The Carbon Emission for Driving is " + cf + "g so WALK!!!";
        }else{
            CarbonFootprint = "The Carbon Emission for Driving is " + cf + "g but you can cycle tho";
        }
        return CarbonFootprint;

    }



    private boolean isIndoor(String kind) {
        if(kind.contains("accommodations") || kind.contains("adult") || kind.contains("lighthouses") || kind.contains("architecture")){
            return true;
        }
        //else if(kind.contains("sport") || kind.contains("industrial_facilities") || kind.contains("other") || kind.contains("glaciers") || kind.contains("gardens") ||
        //        kind.contains("outdoor")|| kind.contains("picnic_site") || kind.contains("historic") || kind.contains("natural") || kind.contains("bridges") ||
          //      kind.contains("towers") || kind.contains("urban_environment") || kind.contains("amphitheatres") || kind.contains("destroyed_objects") ||
            //    kind.contains("farms") || kind.contains("pyramids") || kind.contains("triumphal_archs") || kind.contains("wineries")){
            //return false;}
        else{
            return false;
        }
    }

    private boolean recommendTravelPlan(String weather, double wind, double temp) {
        //Return the rate of introducing indoor event
        if(weather.equals("Thunderstorm") || weather.equals("Rain") || weather.equals("Snow") || weather.equals("Tornado") ||
                weather.equals("Haze") || weather.equals("Smoke") || weather.equals("Dust") || weather.equals("Sand") || weather.equals("Squall") ||
                weather.equals("Ash") || temp <= -20 || temp >= 45 || wind >= 20){
            //Weather is super bad all indoor
            //100% indoor
            return true;
        }
        else if(weather.equals("Drizzle") || weather.equals("Mist") || temp <= 0 || temp >= 35 || wind >= 10){
            //weather is meh, most indoor, no attraction
            //80% in 20% out
            double percentage = Math.random();
            if(percentage > 0.2){
                return true;
            }else{
                return false;
            }

        }else if(weather.equals("Clear") || weather.equals("Cloud")){
            //Weather is good, attraction included
            //40% in 60% out
            double percentage = Math.random();
            if(percentage > 0.6){
                return true;
            }else{
                return false;
            }

        }else{
            //In case we forget some of the index, balance both indoor and outdoor
            double percentage = Math.random();
            if(percentage >= 0.5){
                return true;
            }else{
                return false;
            }
        }

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
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);



        //Implement

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