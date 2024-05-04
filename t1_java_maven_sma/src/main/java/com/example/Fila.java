package com.example;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Fila {
    private String id;
    private ConfiguracaoFila environment;
    private int population = 0;
    private int maxPopulation = Integer.MAX_VALUE;
    private int servers = -1;
    private double minArrival = -1.0;
    private double maxArrival = -1.0;
    private double minDeparture = -1.0;
    private double maxDeparture = -1.0;
    private List<FilaDestination> destinations = new ArrayList<>();
    private Map<Integer, Double> statistics = new HashMap<>();
    private int lost = 0;

    public Fila(String id, ConfiguracaoFila env, BaseFila FilaData) {
        this.id = id;
        this.environment = env;
        this.maxPopulation = FilaData.getCapacity() != null ? FilaData.getCapacity() : this.maxPopulation;
        this.servers = FilaData.getServers() != null ? FilaData.getServers() : this.servers;
        this.minArrival = FilaData.getMinArrival() != null ? FilaData.getMinArrival() : this.minArrival;
        this.maxArrival = FilaData.getMaxArrival() != null ? FilaData.getMaxArrival() : this.maxArrival;
        this.minDeparture = FilaData.getMinService() != null ? FilaData.getMinService() : this.minDeparture;
        this.maxDeparture = FilaData.getMaxService() != null ? FilaData.getMaxService() : this.maxDeparture;
    }

    public String getId() {
        return this.id;
    }

    public void in() {
        this.population++;
    }

    public void out() {
        this.population--;
    }

    public void loss() {
        this.lost++;
    }

    public void arrival() {
        this.environment.recordTime();
        if (this.population < this.maxPopulation) {
            this.in();
            if (this.population <= this.servers) {
                this.scheduleExit();
            }
        } else {
            this.loss();
        }

        this.environment.scheduleArrival(this, maxArrival);
    }

    public void departure() {
        this.environment.recordTime();
        this.out();
        if (this.population >= this.servers) {
            this.scheduleExit();
        }
    }

    public void passing(Fila destination) {
        this.environment.recordTime();
        this.out();
        if (this.population >= this.servers) {
            this.scheduleExit();
        }
        if (destination.population < destination.maxPopulation) {
            destination.in();
            if (destination.population <= destination.servers) {
                destination.scheduleExit();
            }
        } else {
            destination.loss();
        }
    }

    public void scheduleExit() {
        Fila destination = this.getDestination();
        if (destination != null) {
            this.environment.schedulePassing(this, destination);
        } else {
            this.environment.scheduleDeparture(this);
        }
    }

    public void addDestination(Fila Fila, double probability) {
        FilaDestination destination = new FilaDestination(Fila, probability);
        this.destinations.add(destination);
        this.destinations.sort((a, b) -> Double.compare(a.probability, b.probability));
    }

    public Fila getDestination() {
        if (this.destinations.isEmpty()) {
            return null;
        }

        double prob = new Random().nextDouble();
        double sum = 0;
        for (FilaDestination destination : this.destinations) {
            sum += destination.probability;
            if (prob < sum) {
                return destination.destination;
            }
        }
        return null;
    }

    public int getPopulation() {
        return this.population;
    }

    public double getMinArrival() {
        return this.minArrival;
    }

    public double getMaxArrival() {
        return this.maxArrival;
    }

    public double getMinDeparture() {
        return this.minDeparture;
    }

    public double getMaxDeparture() {
        return this.maxDeparture;
    }

    public Map<Integer, Double> getStatistics() {
        return this.statistics;
    }

    public int getServers() {
        return this.servers;
    }

    public int getLost() {
        return this.lost;
    }
}

class FilaDestination {
    double probability;
    Fila destination;

    public FilaDestination(Fila destination, double probability) {
        this.destination = destination;
        this.probability = probability;
    }
}

