package com.epam.training.toto.domain;

import java.util.Arrays;
import java.util.Optional;

public enum Outcome {
    TEAM_ONE_WIN("1"),
    TEAM_TWO_WIN("2"),
    DRAW("X"),
    UNDEFINED("?");

    private String outcomeValue;

    Outcome(String outcomeValue) {
        this.outcomeValue = outcomeValue;
    }

    public static Outcome getOutcome(String fileData) {
        Outcome outcome = Outcome.UNDEFINED;
        String preparedFileData = fileData.replace("+", "")
                .trim()
                .toUpperCase();

        Optional<Outcome> filteredOutcome = Arrays.stream(Outcome.values())
                .filter(probableOutcome -> probableOutcome.getOutcomeValue().equals(preparedFileData))
                .findAny();

        if (filteredOutcome.isPresent()) {
            outcome = filteredOutcome.get();
        }
        return outcome;
    }

    public String getOutcomeValue() {
        return outcomeValue;
    }
}
