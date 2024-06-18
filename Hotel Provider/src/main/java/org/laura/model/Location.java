package org.laura.model;

public class Location {
    private final double lat;
    private final double lon;

    private final String island;
    private final String iataCode;

    public Location(double lat, double lon, String island, String iataCode) {
        this.lat = lat;
        this.lon = lon;
        this.island = island;
        this.iataCode = iataCode;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getIataCode() {
        return iataCode;
    }

    public String getIsland() {
        return island;
    }
}
