package com.epam.training.toto.domain;

import java.util.Objects;

public class Hit {

    private int hitsNumber;
    private int numberOfGames;
    private int prize;

    public Hit() {
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public int getPrize() {
        return prize;
    }

    public int getHitsNumber() {
        return hitsNumber;
    }

    public static Builder builder() {
        return new Hit().new Builder();
    }

    @Override
    public int hashCode() {

        return Objects.hash(hitsNumber, numberOfGames, prize);
    }

    public class Builder {
        private Builder() {

        }

        public Builder setHitsNumber(int hitsNumber) {
            Hit.this.hitsNumber = hitsNumber;

            return this;
        }

        public Builder setNumberOfGames(int numberOfGames) {
            Hit.this.numberOfGames = numberOfGames;

            return this;
        }

        public Builder setPrize(int prize) {
            Hit.this.prize = prize;

            return this;
        }

        public Hit build() {
            return Hit.this;
        }
    }
}
