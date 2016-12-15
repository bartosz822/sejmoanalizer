import org.apache.commons.cli.*;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
/**
 * Created by bartek on 12/12/16.
 */
public class Runner {

// TODO config file


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(run(args));
        System.out.println("\n\nczas:" + (System.currentTimeMillis() - start));
    }

    private static Deputies.DeputiesOptions parseOption = Deputies.DeputiesOptions.any;
    private static Deputies deputies;
    private static HashMap<String, Integer> deputyByName;
    private static StringBuilder res = new StringBuilder();

    private static String UrlDefault = "https://api-v3.mojepanstwo.pl/dane/poslowie.json";
    private static String UrlKad8 = "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=8";
    private static String UrlKad7 = "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=7";
    private static String prop1 = "java.util.concurrent.ForkJoinPool.common.parallelism";
    private static String threads = "50";


    private static String run(String[] args) {

        System.setProperty(prop1, threads);


        Options options = OptionsCreator.create();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);


            if (cmd.getOptions().length == 0) {
                throw new IllegalArgumentException("Nie podano argumentów");
            }

            prepareData(cmd.getOptions());

            for (Option o : cmd.getOptions()) {
                perform(o);
            }


        } catch (UnknownHostException e){
            System.out.println("Host " + e.getMessage() + " nie odpowiada");
        } catch (MalformedURLException e){
            System.out.println("Prawdopodobnie coś jest nie tak z serwerem ");
        }
        catch (ParseException | IOException | ExecutionException | InterruptedException | IllegalArgumentException | JSONException e) {
            System.out.println(e.getMessage());
        }


        return res.toString();
    }


    private static void perform(Option o) {
        switch (o.getOpt()) {
            case "wydatkiSUM": {
                String name = String.join(" ", (CharSequence[]) o.getValues());
                if (deputyByName.containsKey(name)) {
                    int id = deputyByName.get(name);
                    res.append(DeputyBuilder.buildWithSpends(id).getSumOfSpends());
                } else throw new IllegalArgumentException("Taki poseł nie istnieje");
                break;
            }
            case "wydatki_naprawy": {
                String name = String.join(" ", (CharSequence[]) o.getValues());
                if (deputyByName.containsKey(name)) {
                    int id = deputyByName.get(name);
                    res.append(DeputyBuilder.buildWithSpends(id).getSpendsOnRepairs());
                } else throw new IllegalArgumentException("Taki poseł nie istnieje");
                break;
            }
            case "wydatkiAVG":
                res.append("Średnia wydatków:  ").append(deputies.getAvgSpends()).append("\n");
                break;
            case "najwiecejpod":
                res.append("Najwiecej podróży odbył:  ").append(deputies.getMostTrip()).append("\n");
                break;
            case "najdluzej":
                res.append("Najdłużej za granicą pozostawał:  ").append(deputies.getMostExpensive()).append("\n");
                break;
            case "najdrozsza":
                res.append("Najdroższą podróż odbył:  ").append(deputies.getMostExpensive()).append("\n");
                break;
            case "wlochy":
                res.append("We włoszech byli:  ").append(deputies.getVisitedItaly()).append("\n");
                break;
            case "all": {
                res.append("Średnia wydatków:  ").append(deputies.getAvgSpends()).append("\n");
                res.append("Najwiecej podróży odbył:  ").append(deputies.getMostTrip()).append("\n");
                res.append("Najdłużej za granicą pozostawał:  ").append(deputies.getMostExpensive()).append("\n");
                res.append("Najdroższą podróż odbył:  ").append(deputies.getMostExpensive()).append("\n");
                res.append("We włoszech byli:  ").append(deputies.getVisitedItaly()).append("\n");
                break;
            }
        }
    }


    private static void prepareData(Option[] opt) throws IOException, ExecutionException, InterruptedException {
        String choosenUrl = UrlDefault; //default
        boolean needAll = false;

        for (Option o: opt) {
            switch (o.getOpt()) {
                case "wydatkiSUM":
                    break;
                case "wydatki_naprawy": {
                    if (!parseOption.equals(Deputies.DeputiesOptions.WithTrips) && (!parseOption.equals(Deputies.DeputiesOptions.any)))
                        parseOption = Deputies.DeputiesOptions.WithSpends;
                    else parseOption = Deputies.DeputiesOptions.WithTripsAndSpends;
                    break;
                }
                case "wydatkiAVG":
                case "najwiecejpod":
                case "najdluzej":
                case "najdrozsza":
                case "wlochy": {
                    if (!parseOption.equals(Deputies.DeputiesOptions.WithSpends) && (!parseOption.equals(Deputies.DeputiesOptions.any)))
                        parseOption = Deputies.DeputiesOptions.WithTrips;
                    else parseOption = Deputies.DeputiesOptions.WithTripsAndSpends;
                    needAll = true;
                    break;
                }
                case "all": {
                    parseOption = Deputies.DeputiesOptions.WithTripsAndSpends;
                    needAll = true;
                    break;
                }
                case "kadencja": {
                    if (o.getValue().equals("7")) choosenUrl = UrlKad7;
                    else if (o.getValue().equals("8")) choosenUrl = UrlKad8;
                    else throw new IllegalArgumentException("Podano złą kadencję");
                    break;
                }
            }
        }
        if (needAll) {
            List<Integer> IDs = JsonParser.getIDs(choosenUrl);
            deputies = new Deputies(IDs, parseOption);
        }
        deputyByName = JsonParser.getDeputyByName(choosenUrl);
    }


}
