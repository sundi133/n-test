import java.io.Serializable;

/**
 * Created by jsundi on 2/15/17.
 */
public class Data implements Serializable {
    String city = "";
    String pop = "";
    String state = "";
    String _id = "";

    public Data(String city) {
        this.city = city;
    }
}
