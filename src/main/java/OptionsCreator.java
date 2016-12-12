import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Created by bartek on 12/12/16.
 */
public class OptionsCreator {

    static Options create(){

        Options options = new Options();

        Option wydatkiSUM  = Option.builder("wydatkiSUM")
                .hasArgs()
                .numberOfArgs(2)
                .build();

        Option wydatki_naprawy  = Option.builder("wydatki_naprawy")
                .hasArgs()
                .numberOfArgs(2)
                .build();

        options.addOption(wydatkiSUM);
        options.addOption(wydatki_naprawy);
        options.addOption("wydatkiAVG", false, "opcja odzytu średniej wartości sumy wydatków wszystkich posłów");
        options.addOption("najwiecejpod", false, "opcja odzytu posła/posłanki, który wykonał najwięcej podróży zagranicznych\n");
        options.addOption("najdluzej", false, "opcja odzytu posła/posłanki, który najdłużej przebywał za granicą");
        options.addOption("najdrozsza", false, "opcja odzytu posła/posłanki, który odbył najdroższą podróż zagraniczną");
        options.addOption("wlochy", false, "opcja odzytu listy wszystkich posłów, którzy odwiedzili Włochy\n");
        options.addOption("all", false, "opcja odzytu wszystkich opcji bezparametrowych");

        return options;
    }
}
