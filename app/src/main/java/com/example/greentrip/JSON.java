import java.util.ArrayList;
import java.util.Arrays;

public class JSON {

    /** Name identifier.*/
    private static final String NAME_FIELD = "name";
    /** Space between identifier to value.*/
    private static final int FIELD_TO_NAME = 7;

    /** Attribute identifier.*/
    private static final String ATTRIBUTE_FIELD = "kinds";
    /** Space between identifier to value.*/
    private static final int FIELD_TO_ATTRIBUTE = 8;
    /** Attribute separator in JSON string.*/
    private static final String ATTRIBUTE_SEPARATOR = ",";

    /** Latitude identifier.*/
    private static final String LATITUDE_FIELD = "lat";
    /** Space between identifier to value.*/
    private static final int FIELD_TO_LATITUDE = 6;

    /** Longitude identifier.*/
    private static final String LONGITUDE_FIELD = "lon";
    /** Space between identifier to value.*/
    private static final int FIELD_TO_LONGITUDE = 5;

    /** JSON string field.*/
    private String json;

    /**
     * Constructor class for JSON strings.
     *
     * @param json JSON string
     */
    public JSON(String json) {
        this.json = json;
    }

    /**
     * Get all places from the OpenTripMapAPI JSON string.
     *
     * @return list of places
     */
    public ArrayList<Place> jsonToPlaces() {
        ArrayList<Place> places = new ArrayList<>();
        addPlaces(places);
        return places;
    }

    /**
     * Add places to list from OpenTripMapAPI JSON string .
     *
     * @param places list to add places too
     */
    private void addPlaces(ArrayList<Place> places) {
        while(getNextIndexOf(NAME_FIELD) != -1) {
            String name = getNextName();

            // Exclude places without names.
            if (name.length() == 0) {
                continue;
            }

            Attributes attributes = new Attributes(getNextAttributes());
            double latitude = getNextLatitude();
            double longitude = getNextLongitude();

            places.add(new Place(name, attributes, latitude, longitude));
        }
    }

    /**
     * Cutoff a part of the JSON file.
     *
     * @param index index to cutoff JSON string at.
     */
    private void cutOff(int index) {
        json = json.substring(index);
    }

    /**
     * Find first occurence of an identifier.
     *
     * @param identifier string identifier
     * @return first index of identifier, or -1
     */
    private int getNextIndexOf(String identifier) {
        return json.indexOf(identifier);
    }

    /**
     * get the next name in the OpenTripMapAPI JSON string.
     *
     * @return next name in the OpenTripMapAPI JSON string.
     */
    private String getNextName() {
        int nameStart = getNextIndexOf(NAME_FIELD) + FIELD_TO_NAME;

        // Cut off everything before attributes.
        cutOff(nameStart);

        // Double quotes signify end of name.
        int nameEnd = json.indexOf("\"");
        String name = json.substring(0,nameEnd);

        // After getting the attributes, cut it off the String.
        cutOff(nameEnd);

        return name;
    }

    /**
     * get the next attribute in the OpenTripMapAPI JSON string.
     *
     * @return next attribute in the OpenTripMapAPI JSON string.
     */
    private ArrayList<String> getNextAttributes() {
        int attributeStart = getNextIndexOf(ATTRIBUTE_FIELD) + FIELD_TO_ATTRIBUTE;

        // Cut off everything before name.
        cutOff(attributeStart);

        int attributeEnd = json.indexOf("\"");
        String[] attributes = json.substring(0, attributeEnd).split(ATTRIBUTE_SEPARATOR);

        // After getting the name, cut it off the String.
        cutOff(attributeEnd);

        return new ArrayList<> (Arrays.asList(attributes));
    }

    /**
     * get the next latitude in the OpenTripMapAPI JSON string.
     *
     * @return next latitude in the OpenTripMapAPI JSON string.
     */
    private double getNextLatitude() {
        int latitudeStart = getNextIndexOf(LATITUDE_FIELD) + FIELD_TO_LATITUDE;

        // Cut off everything before latitude.
        cutOff(latitudeStart);

        int latitudeEnd = json.indexOf(",");
        String latitudeString = json.substring(0, latitudeEnd);

        // Check for braces in string.
        latitudeString = latitudeString.contains("}") ? latitudeString.replaceAll("}","") : latitudeString;
        double latitude = Double.parseDouble(latitudeString);

        // After getting the latitude, cut it off the String.
        cutOff(latitudeEnd);

        return latitude;
    }

    /**
     * get the next longitude in the OpenTripMapAPI JSON string.
     *
     * @return next longitude in the OpenTripMapAPI JSON string.
     */
    private double getNextLongitude() {
        int longitudeStart = getNextIndexOf(LONGITUDE_FIELD) + FIELD_TO_LONGITUDE;

        // Cut off everything before longitude.
        cutOff(longitudeStart);

        int longitudeEnd = json.indexOf(",");
        double longitude = Double.parseDouble(json.substring(0, longitudeEnd));

        // After getting the longitude, cut it off the String.
        cutOff(longitudeEnd);

        return longitude;
    }
}
