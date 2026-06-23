package com.example.dao;

import com.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    public void create(User user) {
        String sql = "INSERT INTO \"user\" (role_id, first_name, last_name, email, password_hash) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getRoleId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPasswordHash());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    public User findById(Integer id) {
        String sql = "SELECT * FROM \"user\" WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getInt("role_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password_hash"),
                            resultSet.getObject("created_at", OffsetDateTime.class)
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM \"user\"";
        List<User> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getInt("role_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password_hash"),
                        resultSet.getObject("created_at", OffsetDateTime.class)
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding users: " + e.getMessage());
        }
        return list;
    }

    public void update(User user) {
        String sql = "UPDATE \"user\" SET role_id = ?, first_name = ?, last_name = ?, email = ?, password_hash = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getRoleId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPasswordHash());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM \"user\" WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
}
