import java.util.ArrayList;

public class Attributes {
    private ArrayList<String> attributes;

    /**
     * Constructor for attributes object.
     *
     * @param attributes place's attributes
     */
    public Attributes(ArrayList<String> attributes){
        this.attributes = attributes;
    }

    /**
     * get attributes as ArrayList<String>
     *
     * @return ArrayList<String> of attributes
     */
    public ArrayList<String> asList() {
        return attributes;
    }

}
