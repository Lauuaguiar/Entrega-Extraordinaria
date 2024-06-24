package org.laura.control;
import org.laura.model.Hotel;
import org.laura.model.Location;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteHotelStore implements HotelStore {
    static {
        try {Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {e.printStackTrace();}
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:Hotels.db");
    }

    private boolean tableExist(Connection connection, String tableName) throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();}}
    }

    private void createTable(Connection connection, String tableName) {
        try {
            if (!tableExist(connection, tableName)) {
                String createTableQuery = "CREATE TABLE \"" + tableName + "\" (" + "\"TS\" TEXT, " + "\"ID\" TEXT PRIMARY KEY, " + "\"Name\" TEXT, " + "\"Stars\" INTEGER);";
                executeStatement(connection, createTableQuery);}
        } catch (SQLException e) {System.out.println("Error creating the table: " + e.getMessage());}
    }

    public void insertHotel(Connection connection, Hotel hotel) {
        if (hotel == null) {
            return;
        } try {
            createTable(connection, hotel.getLocation().getIsland());
            String tableName = hotel.getLocation().getIsland();
            String selectQuery = "SELECT * FROM \"" + tableName + "\" WHERE id=?";
            String insertQuery = "INSERT INTO \"" + tableName + "\" ( \"TS\", \"ID\", \"Name\", \"Stars\") VALUES (?, ?, ?, ?)";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, hotel.getId());
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {return;}}}
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, hotel.getTs());
                insertStatement.setString(2, hotel.getId());
                insertStatement.setString(3, hotel.getName());
                insertStatement.setInt(4, hotel.getStars());
                insertStatement.executeUpdate();}
        } catch (SQLException e) {System.out.println("Error interacting with the database: " + e.getMessage());}
    }

    public void save(Hotel hotel, Location location) {
        try {Connection connection = connect();
            insertHotel(connection, hotel);
            connection.close();
        } catch (SQLException e) {System.out.println("Error interacting with the database: " + e.getMessage());}
    }

    private void executeStatement(Connection connection, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException e) {System.out.println("Error executing: " + e.getMessage());}
    }

    public List<Hotel> getHotelsByIslandAndStars(String island, Location location, int stars) {
        List<Hotel> hotels = new ArrayList<>();
        String selectQuery = "SELECT * FROM \"" + island + "\" WHERE stars = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, stars);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String ts = resultSet.getString("ts");
                    hotels.add(new Hotel(id, name, stars, ts, location));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting hotels: " + e.getMessage());
        }
        return hotels;
    }

}
