package org.Laura.control;
import org.Laura.model.Location;
import org.Laura.model.Weather;
public interface WeatherStore {
    void save(Weather weather, Location location, String instant);
}