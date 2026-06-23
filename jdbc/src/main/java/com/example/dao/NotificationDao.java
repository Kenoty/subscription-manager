package com.example.dao;

import com.example.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {
    private final Connection conn;

    public NotificationDao(Connection conn) {
        this.conn = conn;
    }

    public void create(Notification notification) {
        String sql = "INSERT INTO notification (user_id, message, is_unread) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, notification.getUserId());
            preparedStatement.setString(2, notification.getMessage());
            preparedStatement.setBoolean(3, notification.getIsUnread());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating notification: " + e.getMessage());
        }
    }

    public Notification findById(Integer id) {
        String sql = "SELECT * FROM notification WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Notification(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("message"),
                            resultSet.getBoolean("is_unread"),
                            resultSet.getObject("created_at", OffsetDateTime.class)
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding notification: " + e.getMessage());
        }
        return null;
    }

    public List<Notification> findAll() {
        String sql = "SELECT * FROM notification";
        List<Notification> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new Notification(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("message"),
                        resultSet.getBoolean("is_unread"),
                        resultSet.getObject("created_at", OffsetDateTime.class)
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding notifications: " + e.getMessage());
        }
        return list;
    }

    public void update(Notification notification) {
        String sql = "UPDATE notification SET user_id = ?, message = ?, is_unread = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, notification.getUserId());
            preparedStatement.setString(2, notification.getMessage());
            preparedStatement.setBoolean(3, notification.getIsUnread());
            preparedStatement.setInt(4, notification.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating notification: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM notification WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting notification: " + e.getMessage());
        }
    }
}
