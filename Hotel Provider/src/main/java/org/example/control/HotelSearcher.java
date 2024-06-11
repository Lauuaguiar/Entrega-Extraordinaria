package org.example.control;
import org.example.model.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HotelSearcher implements HotelProvider {
    private String apikey;
    private String apiSecret;

    public HotelSearcher(String apikey, String apiSecret) {
        this.apikey = apikey;
        this.apiSecret = apiSecret;
    }

    @Override
    public List<Hotel> getHotels(Location location) throws HotelExecutionException {

        String apiUrl = "https://test.api.amadeus.com/v1/reference-data/locations/hotels/by-geocode?" +
                "latitude=" + location.getLat() + "&longitude=" + location.getLon() + "&radius=50&radiusUnit=KM&" +
                "ratings=1,2,3,4,5&hotelSource=ALL";

        Request request = new Request.Builder().url(apiUrl).get()
                .header("Authorization", "Bearer " + tokenGetter()).build();

        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonArray hotelDataArray = jsonObject.getAsJsonArray("data");

            if (hotelDataArray != null) {
                return hotelBuilder(hotelDataArray, location);
            }
        } catch (IOException e) {
            throw new HotelExecutionException("Error in call");
        }
        return null;
    }

    private String tokenGetter() throws HotelExecutionException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://test.api.amadeus.com/v1/security/oauth2/token")
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                        "grant_type=client_credentials&client_id=" + apikey + "&client_secret=" + apiSecret))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            return jsonResponse.get("access_token").getAsString();
        } catch (IOException e) {
            throw new HotelExecutionException("Error");
        }
    }

    private List<Hotel> hotelBuilder(JsonArray hotelDataArray, Location location) {
        List<Hotel> hotels = new ArrayList<>();

        for (JsonElement element : hotelDataArray) {
            JsonObject hotelData = element.getAsJsonObject();
            JsonObject geoCode = hotelData.getAsJsonObject("geoCode");

            if (location.getIataCode().equals(hotelData.get("iataCode").getAsString())) {

                hotels.add(new Hotel(hotelData.get("hotelId").getAsString(),
                        hotelData.get("name").getAsString(),
                        hotelData.get("rating").getAsInt(),
                        "hotel-provider",
                        DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                        new Location(geoCode.get("latitude").getAsDouble(), geoCode.get("longitude").getAsDouble(), location.getIsland(),
                                hotelData.get("iataCode").getAsString())
                ));
            }
        }
        return hotels;

    }

}
