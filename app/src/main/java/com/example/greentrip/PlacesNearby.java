package com.example.greentrip;

import com.example.greentrip.Coordinate;
import com.example.greentrip.JSON;
import com.example.greentrip.OpenTripMapAPI;
import com.example.greentrip.Place;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlacesNearby {
    private Coordinate position;
    private ArrayList<Place> placesNearby;


    /**
     * Constructor for PlacesNearby object.
     *
     * @param position current position of user
     */
    public PlacesNearby(Coordinate position) {
        this.position = position;
        findPlacesNearby();
    }

    /**
     * Assign nearby places to placesNearby field.
     */
    private void findPlacesNearby() {
        JSON placesJSON = OpenTripMapAPI.getPlacesNearbyJSON(position);
        placesNearby = placesJSON.jsonToPlaces();
    }

    public void setCoordinate (Coordinate position) {
        this.position = position;
    }

    public Coordinate getPosition() {
        return position;
    }

    public ArrayList<Place> getPlacesNearby() {
        return placesNearby;
    }

    public ArrayList<Place> getPlacesNearbyIf (Predicate<Place> condition) {
        return (ArrayList<Place>) placesNearby.stream().filter(condition).collect(Collectors.toList());
    }

}
