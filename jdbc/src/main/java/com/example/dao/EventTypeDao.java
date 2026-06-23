package com.example.dao;

import com.example.model.EventType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventTypeDao {
    private final Connection conn;

    public EventTypeDao(Connection conn) {
        this.conn = conn;
    }

    public void create(EventType eventType) {
        String sql = "INSERT INTO event_type (name) VALUES (?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, eventType.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating event type: " + e.getMessage());
        }
    }

    public EventType findById(Integer id) {
        String sql = "SELECT * FROM event_type WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new EventType(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding event type: " + e.getMessage());
        }
        return null;
    }

    public List<EventType> findAll() {
        String sql = "SELECT * FROM event_type";
        List<EventType> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new EventType(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding event types: " + e.getMessage());
        }
        return list;
    }

    public void update(EventType eventType) {
        String sql = "UPDATE event_type SET name = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, eventType.getName());
            preparedStatement.setInt(2, eventType.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating event type: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM event_type WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting event type: " + e.getMessage());
        }
    }
}
