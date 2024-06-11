package org.example.view;
import org.example.control.SqliteHotelStore;
import org.example.control.SqliteWeatherStore;
import org.example.model.Location;
import org.example.model.Hotel;
import org.example.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        boolean wantsToContinue = true;
        while (wantsToContinue) {
            System.out.println("Which Canary Island are you interested in? (Tenerife, Gran Canaria, Lanzarote, Fuerteventura, La Palma, La Gomera, El Hierro)");
            String island = scanner.nextLine().trim();
            Location location = getLocationByIsland(locations, island);
            if (location != null) {
                System.out.println("Enter your arrival date (yyyy-MM-dd):");
                String arrivalDateStr = scanner.nextLine().trim();
                LocalDate arrivalDate = LocalDate.parse(arrivalDateStr, DATE_FORMATTER);

                System.out.println("Enter your departure date (yyyy-MM-dd):");
                String departureDateStr = scanner.nextLine().trim();
                LocalDate departureDate = LocalDate.parse(departureDateStr, DATE_FORMATTER);

                List<Hotel> hotels = hotelStore.getHotelsByIsland(island, "location.Hotel", location);
                if (!hotels.isEmpty()) {
                    System.out.println("Hotels in " + island + ":");
                    for (Hotel hotel : hotels) {
                        System.out.println("- " + hotel.getName() + " (" + hotel.getStars() + " stars)");
                    }
                } else {
                    System.out.println("Sorry, no hotels found for " + island);
                }

                System.out.println("Do you want to check the weather for " + island + "? (Yes/No)");
                String weatherOption = scanner.nextLine().trim().toLowerCase();
                if (weatherOption.equals("yes") || weatherOption.equals("y")) {
                    System.out.println("Do you want the weather for your arrival date or a summary for your stay? (Arrival/Summary)");
                    String weatherDetailOption = scanner.nextLine().trim().toLowerCase();
                    Instant instant = Instant.now();
                    List<Weather> weatherData = weatherStore.getWeatherByIsland(island, "prediction.Weather", instant.toString(), location);
                    if (!weatherData.isEmpty()) {
                        if (weatherDetailOption.equals("arrival")) {
                            Weather arrivalWeather = getWeatherForDate(weatherData, arrivalDate);
                            if (arrivalWeather != null) {
                                System.out.println("Weather information in " + island + " for " + arrivalDate + ":");
                                System.out.println("- Precipitation: " + arrivalWeather.getPrecipitation());
                                System.out.println("- Humidity: " + arrivalWeather.getHumidity());
                                System.out.println("- Clouds: " + arrivalWeather.getClouds());
                                System.out.println("- Temperature: " + arrivalWeather.getTemperature());
                            } else {
                                System.out.println("No weather data available for your arrival date.");
                            }
                        } else if (weatherDetailOption.equals("summary")) {
                            System.out.println("Weather summary for your stay in " + island + ":");
                            weatherData.forEach(weather -> {
                                Instant instantWeather = Instant.parse(weather.getPredictionTime());
                                LocalDate weatherDate = instantWeather.atZone(ZoneId.of("Atlantic/Canary")).toLocalDate();
                                if (!weatherDate.isBefore(arrivalDate) && !weatherDate.isAfter(departureDate)) {
                                    System.out.println("- " + weatherDate + ":");
                                    System.out.println("  - Precipitation: " + weather.getPrecipitation());
                                    System.out.println("  - Humidity: " + weather.getHumidity());
                                    System.out.println("  - Clouds: " + weather.getClouds());
                                    System.out.println("  - Temperature: " + weather.getTemperature());
                                }
                            });
                        }
                    } else {
                        System.out.println("No weather data available for " + island);
                    }
                }

                System.out.println("Do you want to check another island? (Yes/No)");
                String continueOption = scanner.nextLine().trim().toLowerCase();

                if (continueOption.equals("no")) {
                    wantsToContinue = false;
                }
            } else {
                System.out.println("Sorry, this application only works with Canary Islands.");
            }
        }
        System.out.println("Thank you for using our application. Goodbye!");
    }

    private static List<Location> readCSV() {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(UserInteractive.CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    double latitude = Double.parseDouble(data[0].trim());
                    double longitude = Double.parseDouble(data[1].trim());
                    String island = data[2].trim();
                    String iataCode = data[3].trim();
                    Location location = new Location(latitude, longitude, island, iataCode);
                    locations.add(location);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        return locations;
    }

    private static Location getLocationByIsland(List<Location> locations, String island) {
        for (Location location : locations) {
            if (location.getIsland().equalsIgnoreCase(island)) {
                return location;
            }
        }
        return null;
    }

    private static Weather getWeatherForDate(List<Weather> weatherData, LocalDate date) {
        for (Weather weather : weatherData) {
            Instant instant = Instant.parse(weather.getPredictionTime());
            LocalDate weatherDate = instant.atZone(ZoneId.of("Atlantic/Canary")).toLocalDate();
            if (weatherDate.equals(date)) {
                return weather;
            }
        }
        return null;
    }
}
