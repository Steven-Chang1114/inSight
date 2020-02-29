import java.util.List;
import java.util.ArrayList;


public class indoorsOrOutdoors {
    public static String[] convert(List<String> list){
        return list.toArray(new String[list.size()]);
    }

    public static boolean isOutdoors(ArrayList<String> attributes){

        Boolean finalAnswer = true;
        String[] argsArray = convert(attributes);
        switch(argsArray.length){
            case 2:
                finalAnswer = inOrOutTwoDim(argsArray[0]);
                break;
            case 3:
                finalAnswer = inOrOutThreeDim(argsArray[0], argsArray[1], argsArray[2]);
                break;
            case 4:
                finalAnswer = inOrOutFourDim(argsArray[1], argsArray[2], argsArray[3]);
                break;
            case 5:
                finalAnswer = false;
                break;
        }
        return finalAnswer;

        }

    public static Boolean inOrOutTwoDim(String args0){
        Boolean indoorsOrOutdoors;
        if (args0.equals("accommodations") || args0.equals("adult")){
            indoorsOrOutdoors = false;
        } else {
            indoorsOrOutdoors = true;
        }
        return indoorsOrOutdoors;
    }

    public static Boolean inOrOutThreeDim(String args0, String args1, String args2){
        Boolean indoorsOrOutdoors;
        if (args0.equals("sport") || args1.equals("industrial_facilities") || args1.equals("other")
                || args2.equals("lighthouses") || args2.equals("glaciers") || args2.equals("outdoor")
                || args2.equals("picnic_site")){
            indoorsOrOutdoors = true;
        } else {
            indoorsOrOutdoors = false;
        }
        return indoorsOrOutdoors;
    }

    public static Boolean inOrOutFourDim(String args1, String args2, String args3){
        Boolean indoorsOrOutdoors;
        if (args1.equals("historic") || args1.equals("natural") || args1.equals("other")
                || args2.equals("bridges") || args2.equals("towers") || args2.equals("urban_environment")
                || args3.equals("amphitheatres") || args3.equals("destroyed_objects") || args3.equals("farms")
                || args3.equals("pyramids") || args3.equals("triumphal_archs") || args3.equals("wineries")){
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
