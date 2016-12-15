import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;


/**
 * Created by bartek on 12/12/16.
 */
class Deputies {

    private List<Deputy> deputies = new ArrayList<>();

    Deputies(List<Integer> IDs, DeputiesOptions o) throws IOException, ExecutionException, InterruptedException {

        if (o.equals(DeputiesOptions.WithSpends))
            this.deputies = IDs.parallelStream().map(DeputyBuilder::buildWithSpends).collect(Collectors.toList());
        else if (o.equals(DeputiesOptions.WithTrips))
            this.deputies = IDs.parallelStream().map(DeputyBuilder::buildWithTrips).collect(Collectors.toList());
        else {
            this.deputies = IDs.parallelStream().parallel().map(DeputyBuilder::buildWithTripsAndSpends).collect(Collectors.toList());
        }

    }

    double getAvgSpends() {
        return deputies.parallelStream().collect(Collectors.summingDouble(Deputy::getSumOfSpends)) / deputies.size();
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
        Optional<Deputy> deputy = deputies.stream().collect(Collectors.maxBy(comparator));
        if (deputy.isPresent()) {
            return deputy.get().getName();
        } else return "";
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
