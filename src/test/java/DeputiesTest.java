import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * Created by bartek on 12/12/16.
 */
public class DeputiesTest {

    private static Deputies deputies;


    static {
        try {
            deputies = new Deputies(JsonParser.getIDs("https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=8"), Deputies.DeputiesOptions.WithTripsAndSpends);
        } catch (IOException | InterruptedException | ExecutionException e ) {
            e.getMessage();
        }
    }

       @Test
    public void getAvgSpends() throws Exception {
        assertEquals(149331.12, deputies.getAvgSpends(), 0.01);
    }

    @Test
    public void getMostTrip() throws Exception {
        assertEquals("Jan Dziedziczak", deputies.getMostTrip());
    }

    @Test
    public void getLongestTrip() throws Exception {
        assertEquals("Killion Munyama", deputies.getLongestTrip());
    }

    @Test
    public void getMostExpensive() throws Exception {
        assertEquals("Witold Waszczykowski",deputies.getMostExpensive());
    }

    @Test
    public void getVisitedItaly() throws Exception {
        String italy = "[Michał Jaros, Robert Tyszkiewicz, Roman Jacek Kosecki, Sławomir Neumann, Grzegorz Raniewicz, "
                + "Jacek Falfus, Cezary Tomczyk, Rafał Grupiński, Marek Matuszewski, Andrzej Czerwiński, Jan Dziedziczak, Ewa Kopacz, Cezary "
                + "Grabarczyk, Adam Abramowicz, Stefan Niesiołowski, Anna Nemś, Grzegorz Schetyna, Antoni Mężydło, Jakub Rutnicki, Krystyna "
                + "Skowrońska, Wojciech Ziemniak, Marek Rząsa, Joanna Fabisiak, Agnieszka Pomaska, Ireneusz Raś]";
        assertEquals(italy, deputies.getVisitedItaly());
    }

}