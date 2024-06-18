package org.laura.model;


public class Hotel {
    private final String id;
    private final String name;
    private final int stars;

    private final String ts;
    private final Location location;

    public Hotel(String id, String name, int stars, String ts, Location location) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.ts = ts;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return stars;
    }

    public String getTs() {
        return ts;
    }

    public Location getLocation() {
        return location;
    }

}
