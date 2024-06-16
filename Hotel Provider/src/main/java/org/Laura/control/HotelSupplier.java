package org.Laura.control;

import org.Laura.model.Hotel;
import org.Laura.model.Location;

import java.util.List;

public interface HotelSupplier {
    List<Hotel> getHotels(Location location);
}

