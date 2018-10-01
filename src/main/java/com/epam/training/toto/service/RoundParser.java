package com.epam.training.toto.service;

import com.epam.training.toto.domain.Hit;
import com.epam.training.toto.domain.Outcome;
import com.epam.training.toto.domain.Round;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RoundParser {
    private static final String DATE_FORMAT = "yyyy.MM.d.";
    private static final int MAX_ROUND_OUTCOMES = 14;

    public Round parseRound(String csvLine, String csvSplitBy) {
        if (csvLine == null) {
            return null;
        }

        String[] roundValues = csvLine.split(csvSplitBy);
        int year = roundValues[0].isEmpty() ? 0 : Integer.parseInt(roundValues[0]);
        int week = roundValues[1].isEmpty() ? 0 : Integer.parseInt(roundValues[1]);

        return Round.builder()
                .setYear(year)
                .setWeek(week)
                .setRoundNumber(parseRoundNumber(roundValues[2]))
                .setDate(parseRoundDate(roundValues[3], year, week))
                .setHits(parseHits(Arrays.copyOfRange(roundValues, 4, 14)))
                .setRoundOutcome(parseRoundOutcome(Arrays.copyOfRange(roundValues, 14, roundValues.length)))
                .build();
    }

    private int parseRoundNumber(String roundNumberString) {
        int roundNumber;
        try {
            roundNumber = Integer.parseInt(roundNumberString);
        } catch (NumberFormatException e) {
            roundNumber = 1;
        }
        return roundNumber;
    }

    private LocalDate parseRoundDate(String dateString, int year, int week) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            date = getRandomDateFromWeek(year, week);
        }
        return date;
    }

    public LocalDate parseRoundDate(String dateString) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(dateString, formatter);
    }

    private LocalDate getRandomDateFromWeek(int year, int week) {
        int dayOfWeek = 1 + (int) Math.floor(7 * Math.random());
        WeekFields weekFields = WeekFields.ISO;

        LocalDate date = LocalDate.now()
                .withYear(year)
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), dayOfWeek);

        LocalDate firstDayOfYear = LocalDate.now().withYear(year).with(TemporalAdjusters.firstDayOfYear());
        LocalDate lastDayOfYear = LocalDate.now().withYear(year).with(TemporalAdjusters.lastDayOfYear());

        if (date.isBefore(firstDayOfYear)) {
            date = firstDayOfYear;
        } else if (date.isAfter(lastDayOfYear)) {
            date = lastDayOfYear;
        }
        return date;
    }

    private List<Hit> parseHits(String[] hitsString) {
        List<Hit> hits = new ArrayList<>();
        int hitsNumber = 14;

        for (int i = 0; i < hitsString.length; i += 2, hitsNumber--) {
            Hit hit = Hit.builder()
                    .setHitsNumber(hitsNumber)
                    .setNumberOfGames(Integer.parseInt(hitsString[i]))
                    .setPrize(UACurrencyFormatter.getInstance().parse(hitsString[i + 1]))
                    .build();
            hits.add(hit);
        }
        return hits;
    }

    private List<Outcome> parseRoundOutcome(String[] outcomesArray) {
        return Arrays.stream(outcomesArray).map(Outcome::getOutcome).collect(Collectors.toList());
    }

    public List<Outcome> parseRoundOutcome(String outcomesString) {
        List<Outcome> outcomes = parseRoundOutcome(outcomesString.split(""));
        if (outcomes.contains(Outcome.UNDEFINED) || outcomes.size() > MAX_ROUND_OUTCOMES) {
            throw new RuntimeException();
        }
        return outcomes;
    }
}
