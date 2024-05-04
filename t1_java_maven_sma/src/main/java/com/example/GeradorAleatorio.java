package com.example;

public class GeradorAleatorio {
    private long a = 1664525L;
    private long c = 1013904223L;
    private long M = 4294967296L; // 2^32
    private long previous;
    private int limit;
    private int count;

    public GeradorAleatorio(long seed, int limit) {
        this.previous = seed;
        this.limit = limit;
        this.count = 0;
    }

    public boolean hasNext() {
        return this.count < this.limit;
    }

    public double nextRandom() {
        if (!hasNext()) {
            throw new IllegalStateException("No more random numbers available.");
        }
        previous = (a * previous + c) % M;
        count++;
        return (double) previous / M;
    }

    public double nextRandomInRange(double min, double max) {
        double randomValue = nextRandom();
        return min + (max - min) * randomValue;
    }
}
