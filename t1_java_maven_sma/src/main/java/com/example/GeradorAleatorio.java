package com.example;

public class GeradorAleatorio {
    private long a = 1664525L;
    private long c = 1013904223L;
    private long M = 4294967296L;
    private long previous;
    private int limit;
    private int count;
    private long initialSeed;

    public GeradorAleatorio(long seed, int limit) {
        this.previous = seed;
        this.initialSeed = seed;
        this.limit = limit;
        this.count = 0;
    }

    public boolean hasNext() {
        return this.count < this.limit;
    }

    public double nextRandom() {
        if (!hasNext()) {
            reset();
        }
        previous = (a * previous + c) % M;
        count++;
        return (double) previous / M;
    }

    public double nextRandomInRange(double min, double max) {
        double randomValue = nextRandom();
        return min + (max - min) * randomValue;
    }

    private void reset() {
        this.previous = this.initialSeed;
        this.count = 0;
    }
}