package com.epam.training.toto.service;

import com.epam.training.toto.domain.Hit;
import com.epam.training.toto.domain.Outcome;
import com.epam.training.toto.domain.Round;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TotoService {
    public List<Round> getRoundsFromFile(String filePath, String csvSplitBy) {
        List<Round> roundList = new ArrayList<>();
        RoundParser roundParser = new RoundParser();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                Round round = roundParser.parseRound(scanner.nextLine(), csvSplitBy);
                if (round != null) {
                    roundList.add(round);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return roundList;
    }

    public int getLargestPrize(List<Round> roundList) throws ParseException {
        final Comparator<Hit> compHits = Comparator.comparingInt(Hit::getPrize);
        return roundList.stream().map(round -> round
                .getHits()
                .stream()
                .max(compHits)
                .get()
                .getPrize())
                .max(Comparator.comparingInt(a -> a))
                .get();
    }

    public List<double[]> getDistributionOfResults(List<Round> roundList) {
        return roundList.stream().map(round -> {
            double[] distribution = {0, 0, 0};
            round.getRoundOutcome().forEach(outcome -> {
                if (outcome.equals(Outcome.TEAM_ONE_WIN)) ++distribution[0];
                else if (outcome.equals(Outcome.TEAM_TWO_WIN)) ++distribution[1];
                else if (outcome.equals(Outcome.DRAW)) ++distribution[2];
            });
            return convertToPercentDistribution(distribution);
        }).collect(Collectors.toList());
    }

    private double[] convertToPercentDistribution(double[] array) {
        double totalNumber = Arrays.stream(array).reduce(0, (a, b) -> a + b);
        return Arrays.stream(array).map(element -> Math.round(element / totalNumber * 10000) / 100d).toArray();
    }

    public Optional<Hit> checkStake(List<Round> roundList, LocalDate date, List<Outcome> stake) {
        Optional<Hit> stakeHit = Optional.empty();
        Optional<Round> stakeRound = roundList.stream().filter(round -> round.getDate().equals(date)).findAny();
        if (stakeRound.isPresent()) {
            Round round = stakeRound.get();
            int stakeResult = compareOutcomes(stake, round.getRoundOutcome());
            stakeHit = round.getHits().stream().filter(hit -> hit.getHitsNumber() == stakeResult).findAny();
            if (!stakeHit.isPresent()) {
                stakeHit = Optional.of(Hit.builder().setHitsNumber(stakeResult).build());
            }
        }
        return stakeHit;
    }

    private int compareOutcomes(List<Outcome> stake, List<Outcome> roundOutcomes) {
        int stakeResult = 0;
        for (int i = 0; i < stake.size(); i++) {
            if (stake.get(i) == roundOutcomes.get(i)) ++stakeResult;
        }
        return stakeResult;
    }
}
