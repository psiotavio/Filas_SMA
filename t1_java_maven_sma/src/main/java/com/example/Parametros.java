package com.example;

import java.util.List;
import java.util.Map;

public class Parametros {
    private Map<String, BaseFila> Filas;
    private List<Destino> network;
    private Map<String, Double> arrivals;
    private int seed;
    private int qtyOfRandomNumbers;

    // Getters
    public Map<String, BaseFila> getFilas() {
        return Filas;
    }

    public List<Destino> getNetwork() {
        return network;
    }

    public Map<String, Double> getArrivals() {
        return arrivals;
    }

    public int getSeed() {
        return seed;
    }

    public int getQtyOfRandomNumbers() {
        return qtyOfRandomNumbers;
    }
}
