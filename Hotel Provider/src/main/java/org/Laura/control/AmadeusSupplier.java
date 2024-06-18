package org.Laura.control;

import com.google.gson.JsonElement;
import org.Laura.model.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.time.Instant;

import java.util.ArrayList;
import java.util.List;

public class AmadeusSupplier implements HotelSupplier {
    private final String apikey;
    private final String apiSecret;
    public AmadeusSupplier(String apikey, String apiSecret) {
        this.apikey = apikey;
        this.apiSecret = apiSecret;
    }
    public static String getApi(Location location) {
        return String.format("https://test.api.amadeus.com/v1/reference-data/locations/hotels/by-geocode?latitude=%f&longitude=%f&radius=50&radiusUnit=KM&ratings=1,2,3,4,5&hotelSource=ALL",
                location.getLat(), location.getLon());
    }
    public static JsonObject getJson(String api, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(api)
                .header("Authorization", "Bearer " + token).get().build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return JsonParser.parseString(response.body().string()).getAsJsonObject();
    }

    private Hotel createHotel(JsonObject hotelData, Location location) {
        JsonObject geoCode = hotelData.getAsJsonObject("geoCode");
        String provider = "hotel-provider";
        return new Hotel(hotelData.get("hotelId").getAsString(), hotelData.get("name").getAsString(), hotelData.get("rating").getAsInt(), provider, new Location(geoCode.get("latitude").getAsDouble(), geoCode.get("longitude").getAsDouble(), location.getIsland(), hotelData.get("iataCode").getAsString()));
    }

    @Override
    public List<Hotel> getHotels(Location location) {
        String apiUrl = getApi(location);
        try {String token = tokenGetter();
            JsonObject jsonObject = getJson(apiUrl, token);
            JsonArray hotelDataArray = jsonObject.getAsJsonArray("data");
            return hotelBuilder(hotelDataArray, location);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private String tokenGetter() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://test.api.amadeus.com/v1/security/oauth2/token").post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "grant_type=client_credentials&client_id=" + apikey + "&client_secret=" + apiSecret)).build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            return jsonResponse.get("access_token").getAsString();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private List<Hotel> hotelBuilder(JsonArray hotelDataArray, Location location) {
        List<Hotel> hotels = new ArrayList<>();
        for (JsonElement element : hotelDataArray) {
            JsonObject hotelData = element.getAsJsonObject();
            if (location.getIataCode().equals(hotelData.get("iataCode").getAsString())) {
                hotels.add(createHotel(hotelData, location));}
        }return hotels;
    }
}
