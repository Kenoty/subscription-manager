package com.example.menu;

import com.example.dao.DeviceDao;
import com.example.model.Device;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class DeviceMenu {
    private final Scanner scanner;
    private final DeviceDao dao;

    public DeviceMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new DeviceDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Devices ---");
            System.out.println("1. Create");
            System.out.println("2. Find by id");
            System.out.println("3. Find all");
            System.out.println("4. Update");
            System.out.println("5. Delete");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            switch (scanner.nextInt()) {
                case 1 -> create();
                case 2 -> findById();
                case 3 -> findAll();
                case 4 -> update();
                case 5 -> delete();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void create() {
        System.out.print("Type id: ");
        int typeId = scanner.nextInt();
        System.out.print("Name: ");
        String name = scanner.next();
        System.out.print("Note: ");
        String note = scanner.next();
        dao.create(new Device(typeId, name, note));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id (UUID): ");
        UUID id = UUID.fromString(scanner.next());
        Device device = dao.findById(id);
        if (device != null) {
            System.out.println("id | type_id | name | note");
            System.out.println(device.getId() + " | " + device.getTypeId() + " | " + device.getName() + " | " + device.getNote());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<Device> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | type_id | name | note");
            list.forEach(device -> System.out.println(device.getId() + " | " + device.getTypeId() + " | " + device.getName() + " | " + device.getNote()));
        }
    }

    private void update() {
        System.out.print("Id (UUID): ");
        UUID id = UUID.fromString(scanner.next());
        Device device = dao.findById(id);
        if (device == null) { System.out.println("Not found"); return; }
        System.out.print("New type id: ");
        device.setTypeId(scanner.nextInt());
        System.out.print("New name: ");
        device.setName(scanner.next());
        System.out.print("New note: ");
        device.setNote(scanner.next());
        dao.update(device);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id (UUID): ");
        dao.delete(UUID.fromString(scanner.next()));
        System.out.println("Deleted successfully");
    }
}
