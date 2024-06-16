package org.Laura.control;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        try {
            String locations = "Hotel Provider\\src\\main\\resources\\locations.csv";
            HotelController hotelController = new HotelController(args[0], args[1]);
            Timer timer = new Timer();
            long period = 6 * 60 * 60 * 1000;
            timer.schedule(new TimerTask() {@Override
                public void run() {
                    try {hotelController.processHotelFile(locations);
                    } catch (Exception e) {e.printStackTrace();}
                }}, 0, period);
        } catch (Exception e) {e.printStackTrace();}
    }
}

