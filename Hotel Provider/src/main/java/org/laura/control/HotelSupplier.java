package org.laura.control;

import org.laura.model.Hotel;
import org.laura.model.Location;

import java.util.List;

public interface HotelSupplier {
    List<Hotel> getHotels(Location location);
}

