package com.example.greentrip;

import android.util.Log;
import android.widget.TextView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Welcome extends AppCompatActivity {

    Location currentLocation;
    FusedLocationProviderClient client;
    private RequestQueue queue;
    double longitude;
    double latitude;
    private static final int REQUEST_CODE = 101;
    String[] allData = new String[3];

    //private TextView weather;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button start = (Button) findViewById(R.id.startTrip);
        //The placeholder will greet based on your current location
        final TextView greeting = (TextView) findViewById(R.id.greeting);


        //Find current location
        client = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(Welcome.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(Welcome.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = client.getLastLocation();

        //Detect the location and show on the interface
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                findWeather();
                Geocoder geocoder = new Geocoder(Welcome.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String cityName = addresses.get(0).getSubAdminArea();
                    String countryName = addresses.get(0).getCountryName();

                    greeting.setText("Hi we know you are in " + cityName + " right now!" );

                } catch (IOException e) {
                    greeting.setText(e.toString());
                    greeting.setText("Hi You are in Edinburgh in UK now!");
                }
            }
        });



        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openWelcome();
            }
        });

    }

    public void openWelcome(){
        intent = new Intent(getBaseContext(), Maps.class);
        intent.putExtra("Latitude", latitude);
        intent.putExtra("Longitude", longitude);
        intent.putExtra("weather", allData[0]);
        intent.putExtra("temp", allData[1]);
        intent.putExtra("wind", allData[2]);
        startActivity(intent);
    }

    private void findWeather(){
        int lati = (int) latitude;
        int longti = (int) longitude;
        String key = "cc0f792d36bf210e4b27018738d42901";
        final String url ="https://api.openweathermap.org/data/2.5/weather?lat="+lati+"&lon="+longti+"&appid="+key;

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject main = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject wind = response.getJSONObject("wind");
                    JSONObject obj = array.getJSONObject(0);

                    String temp = String.valueOf(main.getDouble("temp"));
                    String weatherData = obj.getString("main");
                    String windSpeed = wind.getString("speed");

                    allData[0] = weatherData;
                    allData[1] = temp;
                    allData[2] = windSpeed;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);

    }

    private void addMarker(GoogleMap map, double lat, double lon,
                           int title, int snippet, boolean flat,
                           float rotation) {
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(getString(title))
                .snippet(getString(snippet))
                .flat(flat).rotation(rotation));
    }
}