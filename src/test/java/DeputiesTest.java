import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by bartek on 12/12/16.
 */
public class DeputiesTest {

    private static Deputies deputies;

    static {
        try {
            deputies = new Deputies(JsonParser.getIDs(), Deputies.DeputiesOptions.WithTripsAndSpends);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

       @Test
    public void getAvgSpends() throws Exception {
        assertEquals(153386.6574, deputies.getAvgSpends(), 0.01);
    }

    @Test
    public void getMostTrip() throws Exception {
        assertEquals("Witold Waszczykowski", deputies.getMostTrip());
    }

    @Test
    public void getLongestTrip() throws Exception {
        assertEquals("Jan Krzysztof Ardanowski", deputies.getLongestTrip());
    }

    @Test
    public void getMostExpensive() throws Exception {
        assertEquals("Witold Waszczykowski",deputies.getMostExpensive());
    }

    @Test
    public void getVisitedItaly() throws Exception {
        assertEquals("[Joanna Fabisiak, Rafał Grupiński, Andrzej Czerwiński]", deputies.getVisitedItaly());
    }

}