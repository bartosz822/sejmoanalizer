import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;

/**
 * Created by bartek on 12/12/16.
 */
class Deputy {

    private final int ID;
    private final JSONObject body;

    Deputy(int ID, String url) throws IOException {
        this.ID = ID;
        try (InputStream in =
                     new URL(url).openStream()
        ) {
            body = new JSONObject(IOUtils.toString(in, "UTF-8"));
        }
    }


    double getSumOfSpends(){
        JSONArray years = body.getJSONObject("layers").getJSONObject("wydatki").getJSONArray("roczniki");
        Double sum = 0.0;
        for (Object year:years){
            JSONObject jsonYear = (JSONObject) year;
            for (Object amountString : jsonYear.getJSONArray("pola")) {
                double amount = Double.parseDouble((String) amountString);
                sum += amount;
            }
        }

        return sum;
    }


    Double getSpendsOnRepairs(){
        JSONArray years = body.getJSONObject("layers").getJSONObject("wydatki").getJSONArray("roczniki");
        Double sum = 0.0;
        for (Object year:years){
            JSONObject jsonYear = (JSONObject) year;
            sum += jsonYear.getJSONArray("pola").getDouble(12);
            }
        return sum;
    }

    Integer getTripsAmount(){
        if (body.getJSONObject("layers").optJSONArray("wyjazdy") == null) return 0;
        return body.getJSONObject("layers").getJSONArray("wyjazdy").length();
    }

    Integer getLongestTripDays(){
        if (body.getJSONObject("layers").optJSONArray("wyjazdy") == null) return 0;
        JSONArray trips = body.getJSONObject("layers").getJSONArray("wyjazdy");
        int max = 0;
        for (Object trip: trips) {
            JSONObject jsonTrip = (JSONObject) trip;
            int days = jsonTrip.getInt("liczba_dni");
            if (days > max) max = days;
        }
        return max;
    }

    Double getMostExpensiveTrip(){
        if (body.getJSONObject("layers").optJSONArray("wyjazdy") == null) return 0d;
        JSONArray trips = body.getJSONObject("layers").getJSONArray("wyjazdy");
        double max = 0.0;
        for (Object trip: trips) {
            JSONObject jsonTrip = (JSONObject) trip;
            double cost = Double.parseDouble(jsonTrip.getString("koszt_suma"));
            if (cost > max) max = cost;
        }
        return max;
    }

    Boolean visitedItaly(){
        if (body.getJSONObject("layers").optJSONArray("wyjazdy") == null) return false;
        JSONArray trips = body.getJSONObject("layers").getJSONArray("wyjazdy");
        for (Object trip: trips) {
            JSONObject jsonTrip = (JSONObject) trip;
            if (jsonTrip.getString("kraj").equals("Włochy")) return true;
        }
        return false;
    }

    String getName() {
        return body.getJSONObject("data").getString("ludzie.nazwa");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deputy deputy = (Deputy) o;

        return ID == deputy.ID;

    }

    @Override
    public int hashCode() {
        return ID;
    }


    static class DeputyMostTripsComparator implements Comparator<Deputy>{
        @Override
        public int compare(Deputy d1, Deputy t2) {
            return d1.getTripsAmount().compareTo(t2.getTripsAmount());
        }
    }

    static class DeputyLongestTripsComparator implements Comparator<Deputy>{
        @Override
        public int compare(Deputy d1, Deputy t2) {
            return d1.getLongestTripDays().compareTo(t2.getLongestTripDays());
        }
    }

    static class DeputyMostExpensiveComparator implements Comparator<Deputy>{
        @Override
        public int compare(Deputy d1, Deputy t2) {
            return d1.getMostExpensiveTrip().compareTo(t2.getMostExpensiveTrip());
        }
    }
}
