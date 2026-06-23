package com.example.dao;

import com.example.model.SubscriptionDevice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDeviceDao {
    private final Connection conn;

    public SubscriptionDeviceDao(Connection conn) {
        this.conn = conn;
    }

    public void create(SubscriptionDevice subscriptionDevice) {
        String sql = "INSERT INTO subscription_device (device_id, subscription_id, added_at, removed_at) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, subscriptionDevice.getDeviceId());
            preparedStatement.setInt(2, subscriptionDevice.getSubscriptionId());
            preparedStatement.setObject(3, subscriptionDevice.getAddedAt());
            preparedStatement.setObject(4, subscriptionDevice.getRemovedAt());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating subscription-device relation: " + e.getMessage());
        }
    }

    public SubscriptionDevice findById(Integer deviceId, Integer subscriptionId) {
        String sql = "SELECT * FROM subscription_device WHERE device_id = ? AND subscription_id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, deviceId);
            preparedStatement.setInt(2, subscriptionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new SubscriptionDevice(
                            resultSet.getInt("device_id"),
                            resultSet.getInt("subscription_id"),
                            resultSet.getObject("added_at", OffsetDateTime.class),
                            resultSet.getObject("removed_at", OffsetDateTime.class)
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding subscription-device relation: " + e.getMessage());
        }
        return null;
    }

    public List<SubscriptionDevice> findAll() {
        String sql = "SELECT * FROM subscription_device";
        List<SubscriptionDevice> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new SubscriptionDevice(
                        resultSet.getInt("device_id"),
                        resultSet.getInt("subscription_id"),
                        resultSet.getObject("added_at", OffsetDateTime.class),
                        resultSet.getObject("removed_at", OffsetDateTime.class)
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding subscription-device relations: " + e.getMessage());
        }
        return list;
    }

    public void update(SubscriptionDevice subscriptionDevice) {
        String sql = "UPDATE subscription_device SET added_at = ?, removed_at = ? WHERE device_id = ? AND subscription_id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setObject(1, subscriptionDevice.getAddedAt());
            preparedStatement.setObject(2, subscriptionDevice.getRemovedAt());
            preparedStatement.setInt(3, subscriptionDevice.getDeviceId());
            preparedStatement.setInt(4, subscriptionDevice.getSubscriptionId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating subscription-device relation: " + e.getMessage());
        }
    }

    public void delete(Integer deviceId, Integer subscriptionId) {
        String sql = "DELETE FROM subscription_device WHERE device_id = ? AND subscription_id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, deviceId);
            preparedStatement.setInt(2, subscriptionId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting subscription-device relation: " + e.getMessage());
        }
    }
}
