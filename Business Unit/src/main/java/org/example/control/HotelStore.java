package org.example.control;
import org.example.model.Hotel;
import org.example.model.Location;

public interface HotelStore {
    void save(Hotel hotel, Location location);
}