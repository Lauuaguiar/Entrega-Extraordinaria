package org.Laura.view;

import jakarta.jms.JMSException;
import org.Laura.control.Controller;
import org.Laura.control.SqliteHotelStore;
import org.Laura.control.SqliteWeatherStore;

import java.util.Arrays;
import java.util.List;
public class Main {
    public static void main(String[] args) throws JMSException {
        String brokerURL = "tcp://localhost:61616";
        List<String> topics = Arrays.asList("prediction.Weather", "points.Of.Interest");
        String directoryPath = args[0] + "\\datalake\\eventstore";
                Controller controller = new Controller();
                controller.setTopics(topics);
                controller.setBrokerURL(brokerURL);
                controller.setDirectoryPath(directoryPath);
                controller.processMessages();
        SqliteHotelStore hotelStore = new SqliteHotelStore();
        SqliteWeatherStore weatherStore = new SqliteWeatherStore();
        UserInteractive userInteractive = new UserInteractive(hotelStore, weatherStore);
        userInteractive.startInteraction();
    }
}