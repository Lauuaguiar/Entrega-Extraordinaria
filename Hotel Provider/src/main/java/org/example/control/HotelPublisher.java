package org.example.control;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.model.Hotel;

import javax.jms.*;

public class HotelPublisher implements HotelPublish{
    @Override
    public void publishHotel(Hotel hotel) throws HotelExecutionException {
        try {
            Gson gson = new Gson();
            String jsonHotel = gson.toJson(hotel);

            String url = ActiveMQConnection.DEFAULT_BROKER_URL;
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("location.Hotels");
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();

            message.setText(jsonHotel);
            producer.send(message);
            connection.close();

        } catch (JMSException e) {
            throw new HotelExecutionException("Execution Error");
        }


    }
}
