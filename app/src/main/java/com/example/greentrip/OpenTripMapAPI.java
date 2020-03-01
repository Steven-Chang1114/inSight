package com.example.greentrip;

import java.io.IOException;

public class OpenTripMapAPI {

    public static final String APIKEY = "";
    public static final Integer RADIUS = 5000;
    public static final Integer NUMBER_OF_OBJECTS = 500;
    public static final Coordinate PALO_ALTO = new Coordinate(37.4419, -122.1430);

    /**
     * create the url for the given coordinate formatted for the API.
     *
     * @param position current position of user
     * @param radius the scope of the search
     * @param numberOfObjects the maximum number of objects in the search
     * @param apiKey the API key for OpenTripMapAPI
     * @return url for accessing the nearby objects
     */
    private static String createNearbyPlacesUrl(Coordinate position, Integer radius, Integer numberOfObjects, String apiKey){
        return String.format("https://api.opentripmap.com/0.1/en/places/radius?"
                                + "radius=" + radius
                                + "&lon=" + position.getLongitude()
                                + "&lat=" + position.getLatitude()
                                + "&limit=" + numberOfObjects
                                + "&format=json"
                                + "&apikey=" + apiKey);
    }

    /**
     * Get a string of the places nearby in string with JSON format.

     * @param position current position of user
     * @return string of places nearby in JSON format
     * @throws IOException if invalid url created
     */
    public static JSON getPlacesNearbyJSON(Coordinate position) {
        String positionUrl = createNearbyPlacesUrl( position
                                                  , RADIUS
                                                  , NUMBER_OF_OBJECTS
                                                  , APIKEY);
        //new UrlHandling.getPlacesTask.execute(positionUrl);
        String placesJSON = UrlHandling.getUrlContents(positionUrl);
        return new JSON (placesJSON);
    }



}
