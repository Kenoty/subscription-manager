package com.example.dao;

import com.example.model.Device;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeviceDao {
    private final Connection conn;

    public DeviceDao(Connection conn) {
        this.conn = conn;
    }

    public void create(Device device) {
        String sql = "INSERT INTO device (type_id, name, note) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, device.getTypeId());
            preparedStatement.setString(2, device.getName());
            preparedStatement.setString(3, device.getNote());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error creating device: " + e.getMessage());
        }
    }

    public Device findById(UUID id) {
        String sql = "SELECT * FROM device WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Device(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getInt("type_id"),
                            resultSet.getString("name"),
                            resultSet.getString("note")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding device: " + e.getMessage());
        }
        return null;
    }

    public List<Device> findAll() {
        String sql = "SELECT * FROM device";
        List<Device> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new Device(
                        resultSet.getObject("id", UUID.class),
                        resultSet.getInt("type_id"),
                        resultSet.getString("name"),
                        resultSet.getString("note")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error finding devices: " + e.getMessage());
        }
        return list;
    }

    public void update(Device device) {
        String sql = "UPDATE device SET type_id = ?, name = ?, note = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, device.getTypeId());
            preparedStatement.setString(2, device.getName());
            preparedStatement.setString(3, device.getNote());
            preparedStatement.setObject(4, device.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating device: " + e.getMessage());
        }
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM device WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting device: " + e.getMessage());
        }
    }
}
