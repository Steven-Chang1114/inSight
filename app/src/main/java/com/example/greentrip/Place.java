import java.util.ArrayList;

public class Place {
    private String name;
    private Attributes attributes;
    private Coordinate coordinate;

    /**
     * Constructor for place objects.
     *
     * @param name place's name
     * @param attributes place's attributes
     * @param latitude place's latitude
     * @param longitude place's longitude
     * @throws IllegalArgumentException if invalid latitude range
     * @throws IllegalArgumentException if invalid longitude range
     */
    public Place(String name, Attributes attributes, double latitude, double longitude){
        this.coordinate = new Coordinate(latitude, longitude);
        this.name = name;
        this.attributes = attributes;
    }


    public String getName(){
        return name;
    }

    public Attributes getAttributes(){
        return attributes;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
