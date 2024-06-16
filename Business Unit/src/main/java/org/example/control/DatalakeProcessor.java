package org.example.control;

import com.google.gson.Gson;
import org.example.model.Hotel;
import org.example.model.Weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class DatalakeProcessor {
    public static List<Hotel> readFilesInFolderForHotels(String folderPath) {
        List<Hotel> poiList = new ArrayList<>();
        Gson gson = new Gson();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {for (File file : files) {
                if (file.isFile()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            Hotel hotel = gson.fromJson(line, Hotel.class);
                            poiList.add(hotel);}
                    } catch (IOException e) {e.printStackTrace();}}}
        } else {System.out.println("The hotel data folder is empty or does not exist.");}
        return poiList;
    }
    public static List<Weather> readFilesInFolderForWeather(String folderPath) {
        List<Weather> weatherList = new ArrayList<>();
        Gson gson = new Gson();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {for (File file : files) {
                if (file.isFile()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            Weather weather = gson.fromJson(line, Weather.class);
                            weatherList.add(weather);}
                        } catch (IOException e) {e.printStackTrace();}}}
        } else {System.out.println("The weather data folder is empty or does not exist.");}
        return weatherList;
    }
}