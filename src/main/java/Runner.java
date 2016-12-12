import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bartek on 12/12/16.
 */
public class Runner {

    public static void main(String[] args) {
        System.out.println(run(args));
    }

    private static Deputies.DeputiesOptions parseOption = Deputies.DeputiesOptions.any;
    private static Deputies deputies;
    private static List<Integer> IDs;
    private static HashMap<String, Integer> deputyByName;
    private static StringBuilder res = new StringBuilder();



    public static String run(String[] args){


        Options options = OptionsCreator.create();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);


            if (cmd.getOptions().length == 0){
                throw new IllegalArgumentException("Nie podano argumentów");
            }

            for (Option o: cmd.getOptions()) {
                prepareData(o);
            }

            for (Option o: cmd.getOptions()) {
                perform(o);
            }


        } catch (ParseException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        }


        return res.toString();
    }

    private static void perform(Option o) {
        switch (o.getOpt()){
            case "wydatkiSUM":
//                TODO
                break;
            case "wydatki_naprawy":
//                TODO
                 break;
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
            case "all":
                res.append("Średnia wydatków:  ").append(deputies.getAvgSpends()).append("\n");
                res.append("Najwiecej podróży odbył:  ").append(deputies.getMostTrip()).append("\n");
                res.append("Najdłużej za granicą pozostawał:  ").append(deputies.getMostExpensive()).append("\n");
                res.append("Najdroższą podróż odbył:  ").append(deputies.getMostExpensive()).append("\n");
                res.append("We włoszech byli:  ").append(deputies.getVisitedItaly()).append("\n");
                break;
        }
    }

    private static void prepareData(Option o) throws IOException {
        boolean needAll = false;
        switch (o.getOpt()){
            case "wydatkiSUM":
                break;
            case "wydatki_naprawy":
                if(!parseOption.equals(Deputies.DeputiesOptions.WithTrips) && (!parseOption.equals(Deputies.DeputiesOptions.any)))
                    parseOption = Deputies.DeputiesOptions.WithSpends;
                else parseOption = Deputies.DeputiesOptions.WithTripsAndSpends;
                break;
            case "wydatkiAVG":
            case "najwiecejpod":
            case "najdluzej":
            case "najdrozsza":
            case "wlochy":
                if(!parseOption.equals(Deputies.DeputiesOptions.WithSpends) && (!parseOption.equals(Deputies.DeputiesOptions.any)))
                    parseOption = Deputies.DeputiesOptions.WithTrips;
                else parseOption = Deputies.DeputiesOptions.WithTripsAndSpends;
                needAll = true;
                break;
            case "all":
                parseOption = Deputies.DeputiesOptions.WithTripsAndSpends;
                needAll = true;
                break;
        }


        if(needAll){
            IDs = JsonParser.getIDs();
            deputies = new Deputies(IDs, parseOption);
            deputyByName = JsonParser.getDeputyByName();
        }
    }



}
