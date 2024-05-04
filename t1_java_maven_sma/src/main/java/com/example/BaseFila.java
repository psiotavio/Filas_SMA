package com.example;

public class BaseFila {
    private Integer capacity;
    private Integer servers;
    private Double minArrival;
    private Double maxArrival;
    private Double minService;
    private Double maxService;

    // Getters and setters for each field
    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getServers() {
        return servers;
    }

    public void setServers(Integer servers) {
        this.servers = servers;
    }

    public Double getMinArrival() {
        return minArrival;
    }

    public void setMinArrival(Double minArrival) {
        this.minArrival = minArrival;
    }

    public Double getMaxArrival() {
        return maxArrival;
    }

    public void setMaxArrival(Double maxArrival) {
        this.maxArrival = maxArrival;
    }

    public Double getMinService() {
        return minService;
    }

    public void setMinService(Double minService) {
        this.minService = minService;
    }

    public Double getMaxService() {
        return maxService;
    }

    public void setMaxService(Double maxService) {
        this.maxService = maxService;
    }
}
