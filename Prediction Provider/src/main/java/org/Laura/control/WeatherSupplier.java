package org.Laura.control;

import org.Laura.model.Location;
import org.Laura.model.Weather;

import java.util.List;

public interface WeatherSupplier {
    List<Weather> getWeather(Location location);
}
