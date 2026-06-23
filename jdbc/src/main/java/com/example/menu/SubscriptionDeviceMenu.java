package com.example.menu;

import com.example.dao.SubscriptionDeviceDao;
import com.example.model.SubscriptionDevice;

import java.sql.Connection;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

public class SubscriptionDeviceMenu {
    private final Scanner scanner;
    private final SubscriptionDeviceDao dao;

    public SubscriptionDeviceMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new SubscriptionDeviceDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Subscription Devices ---");
            System.out.println("1. Create");
            System.out.println("2. Find by device id and subscription id");
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
        System.out.print("Device id: ");
        int deviceId = scanner.nextInt();
        System.out.print("Subscription id: ");
        int subscriptionId = scanner.nextInt();
        dao.create(new SubscriptionDevice(deviceId, subscriptionId, OffsetDateTime.now(ZoneId.systemDefault()), null));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Device id: ");
        int deviceId = scanner.nextInt();
        System.out.print("Subscription id: ");
        int subscriptionId = scanner.nextInt();
        SubscriptionDevice subscriptionDevice = dao.findById(deviceId, subscriptionId);
        if (subscriptionDevice != null) {
            System.out.println("device_id | subscription_id | added_at | removed_at");
            System.out.println(subscriptionDevice.getDeviceId() + " | " + subscriptionDevice.getSubscriptionId() + " | " + subscriptionDevice.getAddedAt() + " | " + subscriptionDevice.getRemovedAt());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<SubscriptionDevice> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("device_id | subscription_id | added_at | removed_at");
            list.forEach(subscriptionDevice -> System.out.println(subscriptionDevice.getDeviceId() + " | " + subscriptionDevice.getSubscriptionId() + " | " + subscriptionDevice.getAddedAt() + " | " + subscriptionDevice.getRemovedAt()));
        }
    }

    private void update() {
        System.out.print("Device id: ");
        int deviceId = scanner.nextInt();
        System.out.print("Subscription id: ");
        int subscriptionId = scanner.nextInt();
        SubscriptionDevice subscriptionDevice = dao.findById(deviceId, subscriptionId);
        if (subscriptionDevice == null) { System.out.println("Not found"); return; }
        System.out.print("Set removed at now? (true/false): ");
        if (scanner.nextBoolean()) {
            subscriptionDevice.setRemovedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        }
        dao.update(subscriptionDevice);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Device id: ");
        int deviceId = scanner.nextInt();
        System.out.print("Subscription id: ");
        int subscriptionId = scanner.nextInt();
        dao.delete(deviceId, subscriptionId);
        System.out.println("Deleted successfully");
    }
}
