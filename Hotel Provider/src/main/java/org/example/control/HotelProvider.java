package org.example.control;

import org.example.model.Hotel;
import org.example.model.Location;

import java.util.List;

public interface HotelProvider {
    List<Hotel> getHotels(Location location) throws HotelExecutionException;
}

