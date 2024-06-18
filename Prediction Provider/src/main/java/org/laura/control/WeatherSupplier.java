package org.laura.control;

import org.laura.model.Location;
import org.laura.model.Weather;

import java.util.List;

public interface WeatherSupplier {
    List<Weather> getWeather(Location location);
}
