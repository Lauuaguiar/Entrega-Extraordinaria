package org.laura.model;

import java.time.Instant;

public class Hotel {
    private final Instant ts;
    private final String id;
    private final String name;
    private final int stars;
    private final String ss;
    private final Location location;

    public Hotel(String id, String name, int stars, String ss, Location location) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.ss = ss;
        this.ts = Instant.now();
        this.location = location;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "ts=" + ts +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", stars=" + stars +
                ", ss='" + ss + '\'' +
                ", location=" + location +
                '}';
    }
}
