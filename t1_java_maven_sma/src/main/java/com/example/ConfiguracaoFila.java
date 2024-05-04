package com.example;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfiguracaoFila {
    private List<Evento> eventos = new ArrayList<>();
    private double tempo = 0.0;
    private double ultimoTempoDeEvento = 0.0;
    private double tempoAcumulado = 0.0;
    private Map<String, Fila> filas = new HashMap<>();
    GeradorAleatorio random;

    public ConfiguracaoFila(Map<String, BaseFila> Filas, List<Destino> network, Map<String, Double> arrivals, int seed, int qtyOfRandomNumbers) {
        random = new GeradorAleatorio(seed, qtyOfRandomNumbers);

        for (Map.Entry<String, BaseFila> entry : Filas.entrySet()) {
            addFila(new Fila(entry.getKey(), this, entry.getValue()));
        }

        for (Destino dest : network) {
            Fila sourceFila = getFila(dest.source);
            Fila targetFila = getFila(dest.target);
            if (sourceFila != null && targetFila != null) {
                sourceFila.addDestination(targetFila, dest.probability);
            }
        }

        for (Map.Entry<String, Double> entry : arrivals.entrySet()) {
            Fila Fila = getFila(entry.getKey());
            if (Fila != null) {
                scheduleArrival(Fila, entry.getValue());
            }
        }
    }

    public double getTime() {
        return tempo;
    }

    public double getAccumulatedTime() {
        return tempoAcumulado;
    }

    public void recordTime() {
        for (Fila Fila : filas.values()) {
            int population = Fila.getPopulation();
            double subtotal = Fila.getStatistics().getOrDefault(population, 0.0);
            subtotal += this.tempo - this.ultimoTempoDeEvento;
            Fila.getStatistics().put(population, subtotal);
        }
        this.ultimoTempoDeEvento = this.tempo;
    }

    public Evento scheduleArrival(Fila Fila, Double delay) {
        delay = delay != null ? delay : random.nextRandomInRange(Fila.getMinArrival(), Fila.getMaxArrival());
        Evento event = new Evento(this.tempo + delay, TipoEvento.ARRIVAL, Fila);
        eventos.add(event);
        eventos.sort((a, b) -> Double.compare(a.time, b.time));
        return event;
    }

    public void scheduleDeparture(Fila Fila) {
        double delay = random.nextRandomInRange(Fila.getMinDeparture(), Fila.getMaxDeparture());
        Evento event = new Evento(this.tempo + delay, TipoEvento.DEPARTURE, Fila);
        eventos.add(event);
        eventos.sort((a, b) -> Double.compare(a.time, b.time));
    }

    public void schedulePassing(Fila Fila, Fila destination) {
        double delay = random.nextRandomInRange(Fila.getMinDeparture(), Fila.getMaxDeparture());
        Evento event = new Evento(this.tempo + delay, TipoEvento.PASSING, Fila, destination);
        eventos.add(event);
        eventos.sort((a, b) -> Double.compare(a.time, b.time));
    }

    public void step() {
        Evento event = eventos.remove(0);
        this.ultimoTempoDeEvento = this.tempo;
        this.tempo = event.time;
        switch (event.eventType) {
            case ARRIVAL:
                event.Fila.arrival();
                break;
            case DEPARTURE:
                event.Fila.departure();
                break;
            case PASSING:
                event.Fila.passing(event.destination);
                break;
        }
    }

    public List<String> listFilas() {
        return new ArrayList<>(filas.keySet());
    }

    public void addFila(Fila Fila) {
        filas.put(Fila.getId(), Fila);
    }

    public Fila getFila(String id) {
        return filas.get(id);
    }
}

// Classes e enums adicionais necessários
enum TipoEvento {
    ARRIVAL, DEPARTURE, PASSING
}

class Evento {
    double time;
    TipoEvento eventType;
    Fila Fila;
    Fila destination;

    public Evento(double time, TipoEvento eventType, Fila Fila, Fila destination) {
        this.time = time;
        this.eventType = eventType;
        this.Fila = Fila;
        this.destination = destination;
    }

    public Evento(double time, TipoEvento eventType, Fila Fila) {
        this(time, eventType, Fila, null);
    }
}
