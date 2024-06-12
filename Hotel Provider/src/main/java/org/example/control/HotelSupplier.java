package org.example.control;

import org.example.model.Hotel;
import org.example.model.Location;

import java.util.List;

public interface HotelSupplier {
    List<Hotel> getHotels(Location location);
}

