package org.Laura.view;

import org.Laura.control.SqliteHotelStore;
import org.Laura.control.SqliteWeatherStore;
import org.Laura.model.Location;
import org.Laura.model.Hotel;
import org.Laura.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInteractive {
    private final SqliteHotelStore hotelStore;
    private final SqliteWeatherStore weatherStore;
    private static final String CSV_FILE_PATH = "Business Unit\\src\\main\\resources\\locations.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final List<Location> locations;
    public UserInteractive(SqliteHotelStore hotelStore, SqliteWeatherStore weatherStore) {
        this.hotelStore = hotelStore;
        this.weatherStore = weatherStore;
        this.locations = readCSV();
    }
    public void startInteraction() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String island = promptIsland(scanner);
            Location location = getLocationByIsland(island);
            if (location == null) {
                System.out.println("Sorry, this application only works with Canary Islands.");
                continue;}
            LocalDate[] dates = promptDates(scanner);
            int stars = promptStars(scanner);
            showHotels(island, location, stars);
            if (promptWeather(scanner, island)) {showWeather(scanner, island, dates[0], dates[1], location);}
            if (!promptContinue(scanner)) {break;}
        }System.out.println("Thank you for using our application. Goodbye!");
    }
    private String promptIsland(Scanner scanner) {
        System.out.println("Which Canary Island are you interested in? (Tenerife, Gran Canaria, Lanzarote, Fuerteventura, La Palma, La Gomera, El Hierro)");
        return scanner.nextLine().trim();
    }
    private LocalDate[] promptDates(Scanner scanner) {
        System.out.println("Enter your arrival date (yyyy-MM-dd):");
        LocalDate arrivalDate = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
        System.out.println("Enter your departure date (yyyy-MM-dd):");
        LocalDate departureDate = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
        return new LocalDate[]{arrivalDate, departureDate};
    }
    private int promptStars(Scanner scanner) {
        System.out.println("How many stars do you want for the hotel?");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private void showHotels(String island, Location location, int stars) {
        List<Hotel> hotels = hotelStore.getHotelsByIslandAndStars(island, location, stars);
        if (hotels.isEmpty()) {
            System.out.println("Sorry, no hotels found for " + island);
        } else {
            System.out.println("Hotels in " + island + " with " + stars + " stars:");
            hotels.stream().filter(hotel -> hotel.getStars() == stars).forEach(hotel -> System.out.println("- " + hotel.getName() + " (" + hotel.getStars() + " stars)"));
        }
    }

    private boolean promptWeather(Scanner scanner, String island) {
        System.out.println("Do you want to check the weather for " + island + "? (Yes/No)");
        return scanner.nextLine().trim().equalsIgnoreCase("yes");
    }

    private void showWeather(Scanner scanner, String island, LocalDate arrivalDate, LocalDate departureDate, Location location) {
        System.out.println("Do you want the weather for your arrival date or a summary for your stay? (Arrival/Summary)");
        String option = scanner.nextLine().trim().toLowerCase();
        Instant instant = Instant.now();
        List<Weather> weatherData = weatherStore.getWeatherByIsland(island, String.valueOf(instant), location);
        if (weatherData.isEmpty()) {
            System.out.println("No weather data available for " + island);
            return;}
        if (option.equals("arrival")) {
            Weather arrivalWeather = getWeatherForDate(weatherData, arrivalDate);
            if (arrivalWeather != null) {
                printWeatherInfo("arrival", island, arrivalDate, arrivalWeather);
            } else {System.out.println("No weather data available for your arrival date.");}
        } else if (option.equals("summary")) {
            System.out.println("Weather summary for your stay in " + island + ":");
            weatherData.forEach(weather -> printWeatherSummary(island, arrivalDate, departureDate, weather));}
    }

    private void printWeatherInfo(String type, String island, LocalDate date, Weather weather) {
        System.out.println("Weather information in " + island + " for " + date + ":");
        System.out.println("- Precipitation: " + weather.getPrecipitation());
        System.out.println("- Humidity: " + weather.getHumidity());
        System.out.println("- Clouds: " + weather.getClouds());
        System.out.println("- Temperature: " + weather.getTemperature());
    }

    private void printWeatherSummary(String island, LocalDate arrivalDate, LocalDate departureDate, Weather weather) {
        Instant instant = Instant.parse(weather.getPredictionTime());
        LocalDate weatherDate = instant.atZone(ZoneId.of("Atlantic/Canary")).toLocalDate();
        if (!weatherDate.isBefore(arrivalDate) && !weatherDate.isAfter(departureDate)) {
            System.out.println("- " + weatherDate + ":");
            System.out.println("  - Precipitation: " + weather.getPrecipitation());
            System.out.println("  - Humidity: " + weather.getHumidity());
            System.out.println("  - Clouds: " + weather.getClouds());
            System.out.println("  - Temperature: " + weather.getTemperature());}
    }

    private boolean promptContinue(Scanner scanner) {
        System.out.println("Do you want to check another island? (Yes/No)");
        return scanner.nextLine().trim().equalsIgnoreCase("yes");
    }

    private static List<Location> readCSV() {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    double latitude = Double.parseDouble(data[0].trim());
                    double longitude = Double.parseDouble(data[1].trim());
                    String island = data[2].trim();
                    locations.add(new Location(latitude, longitude, island));
                }}} catch (IOException | NumberFormatException e) {e.printStackTrace();}
        return locations;
    }

    private Location getLocationByIsland(String island) {
        return locations.stream()
                .filter(location -> location.getIsland().equalsIgnoreCase(island))
                .findFirst()
                .orElse(null);
    }

    private Weather getWeatherForDate(List<Weather> weatherData, LocalDate date) {
        return weatherData.stream().filter(weather -> {
                    Instant instant = Instant.parse(weather.getPredictionTime());
                    LocalDate weatherDate = instant.atZone(ZoneId.of("Atlantic/Canary")).toLocalDate();
                    return weatherDate.equals(date);}).findFirst().orElse(null);
    }
}
