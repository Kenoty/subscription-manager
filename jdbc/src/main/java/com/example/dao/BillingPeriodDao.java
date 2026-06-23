package com.example.dao;

import com.example.model.BillingPeriod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillingPeriodDao {
    private final Connection conn;

    public BillingPeriodDao(Connection conn) {
        this.conn = conn;
    }

    public void create(BillingPeriod billingPeriod) {
        String sql = "INSERT INTO billing_period (name) VALUES (?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, billingPeriod.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating billing period: " + e.getMessage());
        }
    }

    public BillingPeriod findById(Integer id) {
        String sql = "SELECT * FROM billing_period WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new BillingPeriod(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding billing period: " + e.getMessage());
        }
        return null;
    }

    public List<BillingPeriod> findAll() {
        String sql = "SELECT * FROM billing_period";
        List<BillingPeriod> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                 list.add(new BillingPeriod(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding billing periods: " + e.getMessage());
        }
        return list;
    }

    public void update(BillingPeriod billingPeriod) {
        String sql = "UPDATE billing_period SET name = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, billingPeriod.getName());
            preparedStatement.setInt(2, billingPeriod.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating billing period: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM billing_period WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting billing period: " + e.getMessage());
        }
    }
}
