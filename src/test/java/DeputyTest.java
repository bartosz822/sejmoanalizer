import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bartek on 12/12/16.
 */
public class DeputyTest {
    private static Deputy testDeputy = DeputyBuilder.buildWithTripsAndSpends(197);

    @Test
    public void getSumOfSpends() throws Exception {
        assertEquals(291868.23, testDeputy.getSumOfSpends(), 0.01);
    }

    @Test
    public void getSpendsOnRepairs() throws Exception {
        assertEquals(0.0, testDeputy.getSpendsOnRepairs(), 0.01);
    }

    @Test
    public void getTripsAmount() throws Exception {
        assertEquals(new Integer(5), testDeputy.getTripsAmount());
    }

    @Test
    public void getLongestTripDays() throws Exception {
        assertEquals(new Integer(9), testDeputy.getLongestTripDays());
    }

    @Test
    public void getMostExpensiveTrip() throws Exception {
        assertEquals(15263.97, testDeputy.getMostExpensiveTrip(), 0.01);
    }

    @Test
    public void visitedItaly() throws Exception {
        assertTrue(!testDeputy.visitedItaly());
    }

}