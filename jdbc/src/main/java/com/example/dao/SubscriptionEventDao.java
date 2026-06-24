package com.example.dao;

import com.example.model.SubscriptionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionEventDao {
    private final Connection conn;

    public SubscriptionEventDao(Connection conn) {
        this.conn = conn;
    }

    public void create(SubscriptionEvent subscriptionEvent) {
        String sql = "INSERT INTO subscription_event (subscription_id, event_id, event_date, days) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, subscriptionEvent.getSubscriptionId());
            preparedStatement.setInt(2, subscriptionEvent.getEventId());
            preparedStatement.setObject(3, subscriptionEvent.getEventDate());
            preparedStatement.setInt(4, subscriptionEvent.getDays());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating subscription event: " + e.getMessage());
        }
    }

    public SubscriptionEvent findById(Integer id) {
        String sql = "SELECT * FROM subscription_event WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new SubscriptionEvent(
                            resultSet.getInt("id"),
                            resultSet.getInt("subscription_id"),
                            resultSet.getInt("event_id"),
                            resultSet.getObject("event_date", LocalDate.class),
                            resultSet.getInt("days")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding subscription event: " + e.getMessage());
        }
        return null;
    }

    public List<SubscriptionEvent> findAll() {
        String sql = "SELECT * FROM subscription_event";
        List<SubscriptionEvent> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new SubscriptionEvent(
                        resultSet.getInt("id"),
                        resultSet.getInt("subscription_id"),
                        resultSet.getInt("event_id"),
                        resultSet.getObject("event_date", LocalDate.class),
                        resultSet.getInt("days")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding subscription events: " + e.getMessage());
        }
        return list;
    }

    public void update(SubscriptionEvent subscriptionEvent) {
        String sql = "UPDATE subscription_event SET subscription_id = ?, event_id = ?, event_date = ?, days = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, subscriptionEvent.getSubscriptionId());
            preparedStatement.setInt(2, subscriptionEvent.getEventId());
            preparedStatement.setObject(3, subscriptionEvent.getEventDate());
            preparedStatement.setInt(4, subscriptionEvent.getDays());
            preparedStatement.setInt(5, subscriptionEvent.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating subscription event: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM subscription_event WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting subscription event: " + e.getMessage());
        }
    }
}
