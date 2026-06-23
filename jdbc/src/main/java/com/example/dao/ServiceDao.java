package com.example.dao;

import com.example.model.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDao {
    private final Connection conn;

    public ServiceDao(Connection conn) {
        this.conn = conn;
    }

    public void create(Service service) {
        String sql = "INSERT INTO service (name, description, website_url, logo_url) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, service.getName());
            preparedStatement.setString(2, service.getDescription());
            preparedStatement.setString(3, service.getWebsiteUrl());
            preparedStatement.setString(4, service.getLogoUrl());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating service: " + e.getMessage());
        }
    }

    public Service findById(Integer id) {
        String sql = "SELECT * FROM service WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Service(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("website_url"),
                            resultSet.getString("logo_url")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding service: " + e.getMessage());
        }
        return null;
    }

    public List<Service> findAll() {
        String sql = "SELECT * FROM service";
        List<Service> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new Service(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("website_url"),
                        resultSet.getString("logo_url")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding services: " + e.getMessage());
        }
        return list;
    }

    public void update(Service service) {
        String sql = "UPDATE service SET name = ?, description = ?, website_url = ?, logo_url = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, service.getName());
            preparedStatement.setString(2, service.getDescription());
            preparedStatement.setString(3, service.getWebsiteUrl());
            preparedStatement.setString(4, service.getLogoUrl());
            preparedStatement.setInt(5, service.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating service: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM service WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting service: " + e.getMessage());
        }
    }
}
