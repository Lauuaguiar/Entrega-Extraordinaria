package org.example.control;
import org.example.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HotelControl {

    private HotelSearcher hotelSearcher;
    private HotelPublisher hotelPublisher;

    public HotelControl(String apikey, String apiSecret) {
        this.hotelSearcher = new HotelSearcher(apikey, apiSecret);
        this.hotelPublisher = new HotelPublisher();
    }

    public void execute(List<Location> locationList){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable updateTask = () -> {
            for (Location location : locationList) {
                try {
                    List<Hotel> hotels = getHotelSearcher().getHotels(location);
                    for (Hotel hotel : hotels){
                        getHotelPublisher().publishHotel(hotel);
                    }
                } catch (HotelExecutionException e) {
                    System.out.println("Execution Error");
                }
            }

        };
        scheduler.scheduleAtFixedRate(updateTask, 0, 3, TimeUnit.HOURS);
    }

    private List<Location> readCSV(String csvFilePath) {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            reader.lines().forEach(line -> parseCSVLine(line, locations));
        } catch (IOException e) {
            throw new RuntimeException("Error reading the CSV file: " + e.getMessage(), e);
        }
        return locations;
    }

    private void parseCSVLine(String line, List<Location> locations) {
        String[] parts = line.split(",");
        try {
            double lat = Double.parseDouble(parts[0]);
            double lon = Double.parseDouble(parts[1]);
            locations.add(new Location(lat, lon, parts[2].trim(), parts[3].trim()));
        } catch (NumberFormatException e) {
            System.err.println("Error converting values in the CSV file: " + e.getMessage());
        }
    }

    public void processWeatherFile(String csvFile) {
        try {
            List<Location> locationList = readCSV(csvFile);
            execute(locationList);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error processing weather data: " + e.getMessage(), e);
        }
    }

    private HotelSearcher getHotelSearcher() {
        return hotelSearcher;
    }

    private HotelPublisher getHotelPublisher() {
        return hotelPublisher;
    }
}
