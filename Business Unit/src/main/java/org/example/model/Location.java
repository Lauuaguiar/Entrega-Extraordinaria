package org.example.model;
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


    public String getIataCode() {
        return iataCode;
    }
}