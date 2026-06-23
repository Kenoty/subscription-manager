package com.example.dao;

import com.example.model.Subscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDao {
    private final Connection conn;

    public SubscriptionDao(Connection conn) {
        this.conn = conn;
    }

    public void create(Subscription subscription) {
        String sql = "INSERT INTO subscription (user_id, plan_id, auto_renew) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, subscription.getUserId());
            preparedStatement.setInt(2, subscription.getPlanId());
            preparedStatement.setBoolean(3, subscription.getAutoRenew());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating subscription: " + e.getMessage());
        }
    }

    public Subscription findById(Integer id) {
        String sql = "SELECT * FROM subscription WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Subscription(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getInt("plan_id"),
                            resultSet.getBoolean("auto_renew")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding subscription: " + e.getMessage());
        }
        return null;
    }

    public List<Subscription> findAll() {
        String sql = "SELECT * FROM subscription";
        List<Subscription> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new Subscription(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("plan_id"),
                        resultSet.getBoolean("auto_renew")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding subscriptions: " + e.getMessage());
        }
        return list;
    }

    public void update(Subscription subscription) {
        String sql = "UPDATE subscription SET user_id = ?, plan_id = ?, auto_renew = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, subscription.getUserId());
            preparedStatement.setInt(2, subscription.getPlanId());
            preparedStatement.setBoolean(3, subscription.getAutoRenew());
            preparedStatement.setInt(4, subscription.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating subscription: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM subscription WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting subscription: " + e.getMessage());
        }
    }
}
