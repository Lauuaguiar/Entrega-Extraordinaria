package org.Laura.model;

import java.time.Instant;

public class Hotel {
    private final Instant ts;
    private final String id;
    private final String name;
    private final int stars;
    private final String ss;
    private final Instant predictionTime;

    private final Location location;

    public Hotel(String id, String name, int stars, String ss, Instant predictionTime, Location location) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.ss = ss;
        this.predictionTime = predictionTime;
        this.ts = Instant.now();
        this.location = location;
    }
}
