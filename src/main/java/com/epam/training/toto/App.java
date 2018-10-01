package com.epam.training.toto;

import com.epam.training.toto.domain.Hit;
import com.epam.training.toto.domain.Outcome;
import com.epam.training.toto.domain.Round;
import com.epam.training.toto.service.InputService;
import com.epam.training.toto.service.RoundParser;
import com.epam.training.toto.service.TotoService;
import com.epam.training.toto.service.UACurrencyFormatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

public class App {
    private static final String FILE_PATH = "materials\\toto.csv";
    private static final String CSV_SEPARATOR = ";";
    private static final String LARGEST_PRIZE_STRING = "Largest prize ever recorded: %s%n";
    private static final String DISTRIBUTION_STRING = "Round %s: team#1 won: %.2f%%, team#2 won: %.2f%%, draw: %.2f%%%n";
    private static final String ON_DATE_INPUT = "Enter date in format yyyy.MM.d. (e.g. 2015.10.29.):";
    private static final String ON_OUTCOMES_INPUT = "Enter outcomes (inline 1/2/X values; max:14; e.g. 2X11222121X112):";
    private static final String STAKE_RESULT_LOSE = "Result: hits: %d, better luck next time%n";
    private static final String STAKE_RESULT_WIN = "Result: hits: %d, amount: %s%n";

    private static TotoService totoService = new TotoService();
    private static InputService inputService = new InputService();
    private static RoundParser roundParser = new RoundParser();
    private static UACurrencyFormatter uaCurrencyFormatter = UACurrencyFormatter.getInstance();

    public static void main(String[] args) throws ParseException {
        List<Round> roundList = totoService.getRoundsFromFile(FILE_PATH, CSV_SEPARATOR);

        int prize = totoService.getLargestPrize(roundList);
        out.printf(LARGEST_PRIZE_STRING, uaCurrencyFormatter.format(prize));
        out.println();

        List<double[]> distributionList = totoService.getDistributionOfResults(roundList);
        for (int i = 0; i < distributionList.size(); i++) {
            double[] distribution = distributionList.get(i);
            LocalDate date = roundList.get(i).getDate();
            out.printf(DISTRIBUTION_STRING, date, distribution[0], distribution[1], distribution[2]);
        }
        out.println();


        LocalDate date = inputService.readInput(ON_DATE_INPUT, roundParser::parseRoundDate);
        List<Outcome> outcomes = inputService.readInput(ON_OUTCOMES_INPUT, roundParser::parseRoundOutcome);

        Optional<Hit> hit = totoService.checkStake(roundList, date, outcomes);
        if (hit.isPresent()) {
            Hit stakeHit = hit.get();
            if (stakeHit.getHitsNumber() < 10) {
                out.printf(STAKE_RESULT_LOSE, stakeHit.getHitsNumber());
            } else {
                out.printf(STAKE_RESULT_WIN, stakeHit.getHitsNumber(), uaCurrencyFormatter.format(stakeHit.getPrize()));
            }
        } else {
            out.println("No rounds on this date");
        }
    }

}
