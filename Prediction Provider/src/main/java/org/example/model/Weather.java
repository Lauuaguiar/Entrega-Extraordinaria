package org.example.model;

import java.time.Instant;



public class Weather {

    private final Instant ts;
    private final String ss;
    private final Instant predictionTime;

    private final double temperature;
    private final double precipitation;
    private final double wind;
    private final int humidity;
    private final int clouds;

    private final Location location;

    public Weather(String ss, Instant predictionTime, double temperature, double precipitation, double wind, int humidity, int clouds, Location location) {
        this.ts = Instant.now();
        this.ss = ss;
        this.predictionTime = predictionTime;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.wind = wind;
        this.humidity = humidity;
        this.clouds = clouds;
        this.location = location;
    }
}
