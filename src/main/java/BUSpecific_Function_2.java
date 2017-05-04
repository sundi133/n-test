import com.google.gson.Gson;

/**
 * Created by jsundi on 2/15/17.
 */
public class BUSpecific_Function_2 {

    static public String function(String w)
    {
        Gson g = new Gson();
        Data map = g.fromJson(w,Data.class);
        return map.city.concat("\t").concat(map.state).concat("\t").concat(map._id).concat("\t").concat(map.pop);
    }

    public static void main(String[] args) {

        String expectedInput = "AGAWAM,MA,01001,15338";

        String inputTuple = "{ \"city\" : \"AGAWAM\", \"loc\" : [ -72.622739, 42.070206 ], \"pop\" : 15338, \"state\" : \"MA\", \"_id\" : \"01001\" }";

        String expectedOuput = function(inputTuple);

        boolean test = expectedInput.contentEquals(expectedOuput) == true ? true : false;

        System.out.println(test);
        System.out.println(expectedInput);
        System.out.println(expectedOuput);


    }
}
