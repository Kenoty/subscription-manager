package com.example.dao;

import com.example.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao {
    private final Connection conn;

    public PaymentDao(Connection conn) {
        this.conn = conn;
    }

    public void create(Payment payment) {
        String sql = "INSERT INTO payment (event_id, amount, currency) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, payment.getEventId());
            preparedStatement.setBigDecimal(2, payment.getAmount());
            preparedStatement.setString(3, payment.getCurrency());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating payment: " + e.getMessage());
        }
    }

    public Payment findById(Integer id) {
        String sql = "SELECT * FROM payment WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Payment(
                            resultSet.getInt("id"),
                            resultSet.getInt("event_id"),
                            resultSet.getBigDecimal("amount"),
                            resultSet.getString("currency"),
                            resultSet.getObject("paid_at", OffsetDateTime.class)
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding payment: " + e.getMessage());
        }
        return null;
    }

    public List<Payment> findAll() {
        String sql = "SELECT * FROM payment";
        List<Payment> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("event_id"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getString("currency"),
                        resultSet.getObject("paid_at", OffsetDateTime.class)                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding payments: " + e.getMessage());
        }
        return list;
    }

    public void update(Payment payment) {
        String sql = "UPDATE payment SET event_id = ?, amount = ?, currency = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, payment.getEventId());
            preparedStatement.setBigDecimal(2, payment.getAmount());
            preparedStatement.setString(3, payment.getCurrency());
            preparedStatement.setInt(4, payment.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating payment: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM payment WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting payment: " + e.getMessage());
        }
    }
}
