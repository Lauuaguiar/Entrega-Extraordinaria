package org.Laura.control;
import org.Laura.model.Hotel;
import org.Laura.model.Location;

public interface HotelStore {
    void save(Hotel hotel, Location location);
}