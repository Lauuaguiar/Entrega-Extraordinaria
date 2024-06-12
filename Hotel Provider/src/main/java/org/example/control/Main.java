package org.example.control;

public class Main {
    public static void main(String[] args) {
        HotelController hotelControl = new HotelController(args[0], args[1]);
        hotelControl.processHotelFile("Hotel Provider\\src\\main\\resources\\locations.csv");
    }
}
