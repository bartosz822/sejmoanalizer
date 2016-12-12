import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Created by bartek on 12/12/16.
 */
public class DeputyBuilder {

    private static String url = "https://api-v3.mojepanstwo.pl/dane/poslowie/";

    static Deputy buildWithSpends(int ID) {
        try {
            return new Deputy(ID, url + ID + ".json" + "?layers[]=wydatki");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

   static Deputy buildWithTrips(int ID) {
       try {
           return new Deputy(ID, url + ID + ".json" + "?layers[]=wyjazdy");
       } catch (IOException e) {
           throw new UncheckedIOException(e);
       }
    }

    static Deputy buildWithTripsAndSpends(int ID) {
        try {
            return new Deputy(ID, url + ID + ".json" + "?layers[]=wyjazdy&layers[]=wydatki");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
