package com.example.dao;

import com.example.model.Plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanDao {
    private final Connection conn;

    public PlanDao(Connection conn) {
        this.conn = conn;
    }

    public void create(Plan plan) {
        String sql = "INSERT INTO plan (service_id, name, price, currency, billing_period_id, max_devices) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, plan.getServiceId());
            preparedStatement.setString(2, plan.getName());
            preparedStatement.setBigDecimal(3, plan.getPrice());
            preparedStatement.setString(4, plan.getCurrency());
            preparedStatement.setInt(5, plan.getBillingPeriodId());
            preparedStatement.setInt(6, plan.getMaxDevices());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating plan: " + e.getMessage());
        }
    }

    public Plan findById(Integer id) {
        String sql = "SELECT * FROM plan WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Plan(
                            resultSet.getInt("id"),
                            resultSet.getInt("service_id"),
                            resultSet.getString("name"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getString("currency"),
                            resultSet.getInt("billing_period_id"),
                            resultSet.getInt("max_devices")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding plan: " + e.getMessage());
        }
        return null;
    }

    public List<Plan> findAll() {
        String sql = "SELECT * FROM plan";
        List<Plan> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new Plan(
                        resultSet.getInt("id"),
                        resultSet.getInt("service_id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("currency"),
                        resultSet.getInt("billing_period_id"),
                        resultSet.getInt("max_devices")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding plans: " + e.getMessage());
        }
        return list;
    }

    public void update(Plan plan) {
        String sql = "UPDATE plan SET service_id = ?, name = ?, price = ?, currency = ?, billing_period_id = ?, max_devices = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, plan.getServiceId());
            preparedStatement.setString(2, plan.getName());
            preparedStatement.setBigDecimal(3, plan.getPrice());
            preparedStatement.setString(4, plan.getCurrency());
            preparedStatement.setInt(5, plan.getBillingPeriodId());
            preparedStatement.setInt(6, plan.getMaxDevices());
            preparedStatement.setInt(7, plan.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating plan: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM plan WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting plan: " + e.getMessage());
        }
    }
}
