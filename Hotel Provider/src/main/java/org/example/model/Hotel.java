package org.example.model;

public class Hotel {
    private String id;
    private String name;
    private int stars;
    private String ss, ts;
    private Location location;

    public Hotel(String id, String name, int stars, String ss, String ts, Location location) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.ss = ss;
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

    public String getSs() {
        return ss;
    }

    public String getTs() {
        return ts;
    }

    public Location getLocation() {
        return location;
    }
}
