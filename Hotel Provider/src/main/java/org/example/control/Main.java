package org.example.control;

public class Main {
    public static void main(String[] args) {
        HotelControl hotelControl = new HotelControl(args[0], args[1]);
        hotelControl.processWeatherFile("Hotel Provider\\src\\main\\resources\\locations.csv");
    }
}
