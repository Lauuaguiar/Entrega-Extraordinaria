package org.laura.control;
import org.laura.model.Location;
import org.laura.model.Weather;
public interface WeatherStore {
    void save(Weather weather, Location location, String instant);
}