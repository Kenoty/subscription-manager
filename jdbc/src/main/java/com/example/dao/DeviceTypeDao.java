package com.example.dao;

import com.example.model.DeviceType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceTypeDao {
    private final Connection conn;

    public DeviceTypeDao(Connection conn) {
        this.conn = conn;
    }

    public void create(DeviceType deviceType) {
        String sql = "INSERT INTO device_type (name) VALUES (?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, deviceType.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating device type: " + e.getMessage());
        }
    }

    public DeviceType findById(Integer id) {
        String sql = "SELECT * FROM device_type WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new DeviceType(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding device type: " + e.getMessage());
        }
        return null;
    }

    public List<DeviceType> findAll() {
        String sql = "SELECT * FROM device_type";
        List<DeviceType> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new DeviceType(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding device types: " + e.getMessage());
        }
        return list;
    }

    public void update(DeviceType deviceType) {
        String sql = "UPDATE device_type SET name = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, deviceType.getName());
            preparedStatement.setInt(2, deviceType.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating device type: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM device_type WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting device type: " + e.getMessage());
        }
    }
}
