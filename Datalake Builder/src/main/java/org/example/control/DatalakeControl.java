package org.example.control;

public class DatalakeControl {
    private final WeatherStoreBuilder weatherStoreBuilder;
    private final HotelStoreBuilder hotelStoreBuilder;
    public DatalakeControl(String path) {
        this.weatherStoreBuilder = new WeatherStoreBuilder(path);
        this.hotelStoreBuilder = new HotelStoreBuilder(path);
    }
    public void createDataLake(){
        weatherStoreBuilder.weatherStoreBuild();
        hotelStoreBuilder.hotelStoreBuild();
        System.out.println("The task is finished");
    }
}
