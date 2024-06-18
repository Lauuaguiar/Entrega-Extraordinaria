package org.laura.control;

import org.laura.model.Weather;

public interface WeatherPublisher {
    void publishWeather(Weather weather);
}
