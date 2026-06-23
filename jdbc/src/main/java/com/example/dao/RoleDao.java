package com.example.dao;

import com.example.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDao {
    private final Connection conn;

    public RoleDao(Connection conn) {
        this.conn = conn;
    }

    public void create(Role role) {
        String sql = "INSERT INTO role (name) VALUES (?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, role.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating role: " + e.getMessage());
        }
    }

    public Role findById(Integer id) {
        String sql = "SELECT * FROM role WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Role(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding role: " + e.getMessage());
        }
        return null;
    }

    public List<Role> findAll() {
        String sql = "SELECT * FROM role";
        List<Role> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new Role(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding roles: " + e.getMessage());
        }
        return list;
    }

    public void update(Role role) {
        String sql = "UPDATE role SET name = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, role.getName());
            preparedStatement.setInt(2, role.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating role: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM role WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting role: " + e.getMessage());
        }
    }
}
