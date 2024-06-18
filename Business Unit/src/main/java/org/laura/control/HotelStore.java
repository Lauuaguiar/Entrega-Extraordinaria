package org.laura.control;
import org.laura.model.Hotel;
import org.laura.model.Location;

public interface HotelStore {
    void save(Hotel hotel, Location location);
}