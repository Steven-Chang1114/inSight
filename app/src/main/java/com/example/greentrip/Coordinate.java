public class Coordinate {

    /** absolute longitude maximum.*/
    private static Integer LONGITUDE_RANGE = 180;
    /** absolute latitude maximum.*/
    private static Integer LATITUDE_RANGE = 90;

    /** Coordinate latitude.*/
    private double latitude;
    /** Coordinate longitude.*/
    private double longitude;

    /**
     * Constructor for Coordinate objects.
     *
     * @param latitude Coordinate object's latitude
     * @param longitude Coordinate object's longitude
     * @throws IllegalArgumentException if invalid latitude range
     * @throws IllegalArgumentException if invalid longitude range
     */
    public Coordinate(double latitude, double longitude) {
        // Exception checking.
        latitudeRangeCheck(latitude);
        longitudeRangeCheck(longitude);

        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Exception check for a given latitude.
     *
     * @param latitude Coordinate object's longitude
     * @throws IllegalArgumentException if invalid latitude range
     */
    private void latitudeRangeCheck(double latitude) {
        if ((Math.abs(latitude) > LATITUDE_RANGE)) {
            throw new IllegalArgumentException("ERROR: Invalid latitude range: " + latitude);
        }
    }

    /**
     * Exception check for a given longitude.
     *
     * @param longitude Coordinate object's longitude
     * @throws IllegalArgumentException if invalid longitude range
     */
    private void longitudeRangeCheck(double longitude) {
        if ((Math.abs(longitude) > LONGITUDE_RANGE)) {
            throw new IllegalArgumentException("ERROR: Invalid longitude range: " + longitude);
        }
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }
}
