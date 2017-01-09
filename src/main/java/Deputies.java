import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by bartek on 12/12/16.
 */
class Deputies {

    private List<Deputy> deputies = new ArrayList<>();

    Deputies(List<Integer> IDs, DeputiesOptions o) throws IOException, ExecutionException, InterruptedException {

        Function<Integer,Deputy> buildfunction = DeputyBuilder::buildWithTripsAndSpends;

        if (o.equals(DeputiesOptions.WithSpends))
            buildfunction = DeputyBuilder::buildWithSpends;
        else if (o.equals(DeputiesOptions.WithTrips))
            buildfunction = DeputyBuilder::buildWithTrips;
            this.deputies = Runner.streamBuilder
                    .from(IDs)
                    .map(buildfunction)
                    .toQueue()
                    .stream()
                    .collect(Collectors.toList());
        }

    double getAvgSpends() {
        double avgSum = deputies.parallelStream().mapToDouble(Deputy::getSumOfSpends).sum() / deputies.size();
        return Math.round(avgSum * 100.0) / 100.0;
    }

    String getMostTrip() {
        return getNameOfMax(new Deputy.DeputyMostTripsComparator());
    }

    String getLongestTrip() {
        return getNameOfMax(new Deputy.DeputyLongestTripsComparator());
    }

    String getMostExpensive() {
        return getNameOfMax(new Deputy.DeputyMostExpensiveComparator());
    }

    private String getNameOfMax(Comparator<Deputy> comparator) {
        Optional<Deputy> deputy = deputies.stream().max(comparator);
        return deputy.map(Deputy::getName).orElse("");
    }

    String getVisitedItaly() {
        return deputies.stream().filter(Deputy::visitedItaly).map(Deputy::getName).collect(Collectors.toList()).toString();
    }

    enum DeputiesOptions {
        WithSpends,
        WithTrips,
        WithTripsAndSpends,
        any
    }

}
