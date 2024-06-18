package org.laura.model;
public class Location {
    private final double lat;
    private final double lon;
    private final String island;

    public Location(double lat, double lon, String island) {
        this.lat = lat;
        this.lon = lon;
        this.island = island;
    }

    public String getIsland() {
        return island;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", island='" + island + '\'' +
                '}';
    }
}