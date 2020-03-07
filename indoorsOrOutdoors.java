import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class indoorsOrOutdoors {
    //converts the list received from the other file to an array
    private static String[] convert(List<String> list){
        return list.toArray(new String[list.size()]);
    }

    //returns whether a location/activity is outdoors or not; the result will be stored in the app
    public static boolean isOutdoors(ArrayList<String> attributes){
// TODO: generalize for any arguments
        Boolean finalAnswer = true;
        String[] argsArray = convert(attributes);
        switch(argsArray.length){
            case 2:
                finalAnswer = inOrOutTwoDim(argsArray);
                break;
            case 3:
                finalAnswer = inOrOutThreeDim(argsArray);
                break;
            case 4:
                finalAnswer = inOrOutFourDim(argsArray);
                break;
            case 5:
                finalAnswer = false;
                break;
        }
        return finalAnswer;

        }

    private static Boolean inOrOutTwoDim(String[] argsArray){
        Boolean indoorsOrOutdoors;
        if (Arrays.asList(argsArray).contains("accommodations") || Arrays.asList(argsArray).contains("adult")){
            indoorsOrOutdoors = false;
        } else {
            indoorsOrOutdoors = true;
        }
        return indoorsOrOutdoors;
    }

    private static Boolean inOrOutThreeDim(String[] argsArray){
        Boolean indoorsOrOutdoors;
        if (Arrays.asList(argsArray).contains("sport") || Arrays.asList(argsArray).contains("industrial_facilities")
                || Arrays.asList(argsArray).contains("other") || Arrays.asList(argsArray).contains("lighthouses")
                || Arrays.asList(argsArray).contains("glaciers") || Arrays.asList(argsArray).contains("outdoor")
                || Arrays.asList(argsArray).contains("picnic_site")){
            indoorsOrOutdoors = true;
        } else {
            indoorsOrOutdoors = false;
        }
        return indoorsOrOutdoors;
    }

    private static Boolean inOrOutFourDim(String[] argsArray){
        Boolean indoorsOrOutdoors;
        if (Arrays.asList(argsArray).contains("historic") || Arrays.asList(argsArray).contains("natural")
                || Arrays.asList(argsArray).contains("other") || Arrays.asList(argsArray).contains("bridges")
                || Arrays.asList(argsArray).contains("towers") || Arrays.asList(argsArray).contains("urban_environment")
                || Arrays.asList(argsArray).contains("amphitheatres") || Arrays.asList(argsArray).contains("destroyed_objects")
                || Arrays.asList(argsArray).contains("farms") || Arrays.asList(argsArray).contains("pyramids")
                || Arrays.asList(argsArray).contains("triumphal_archs") || Arrays.asList(argsArray).contains("wineries")){
            indoorsOrOutdoors = true;
        } else {
            indoorsOrOutdoors = false;
        }
        return indoorsOrOutdoors;
    }

    public static void main(String[] args){
        ArrayList<String> test = new ArrayList<>();

        for(int i = 0; i < args.length; i++){
            test.add(args[i]);
        }

        System.out.println(isOutdoors(test));
    }

}
