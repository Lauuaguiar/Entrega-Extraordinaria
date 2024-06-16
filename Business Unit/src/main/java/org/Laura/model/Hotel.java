package org.Laura.model;


public class Hotel {
    private final String id;
    private final String name;
    private final int stars;
    private final String ss;

    private final String ts;
    private final Location location;

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
