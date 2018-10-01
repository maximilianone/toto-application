package com.epam.training.toto.domain;

import java.time.LocalDate;
import java.util.List;

public class Round {

    private int year;
    private int week;
    private int roundNumber;
    private LocalDate date;
    private List<Hit> hits;
    private List<Outcome> roundOutcome;

    public Round() {
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public List<Outcome> getRoundOutcome() {
        return roundOutcome;
    }

    public static Builder builder() {
        return new Round().new Builder();
    }

    public class Builder {
        private Builder() {

        }

        public Builder setYear(int year) {
            Round.this.year = year;

            return this;
        }

        public Builder setWeek(int week) {
            Round.this.week = week;

            return this;
        }

        public Builder setRoundNumber(int roundNumber) {
            Round.this.roundNumber = roundNumber;

            return this;
        }

        public Builder setDate(LocalDate date) {
            Round.this.date = date;

            return this;
        }

        public Builder setHits(List<Hit> hits) {
            Round.this.hits = hits;

            return this;
        }

        public Builder setRoundOutcome(List<Outcome> roundOutcome) {
            Round.this.roundOutcome = roundOutcome;

            return this;
        }

        public Round build() {
            return Round.this;
        }
    }
}
