package com.example.menu;

import com.example.dao.NotificationDao;
import com.example.model.Notification;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class NotificationMenu {
    private final Scanner scanner;
    private final NotificationDao dao;

    public NotificationMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new NotificationDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Notifications ---");
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
        System.out.print("User id: ");
        int userId = scanner.nextInt();
        System.out.print("Message: ");
        String message = scanner.next();
        System.out.print("Is unread (true/false): ");
        boolean isUnread = scanner.nextBoolean();
        dao.create(new Notification(userId, message, isUnread));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Notification notification = dao.findById(id);
        if (notification != null) {
            System.out.println("id | user_id | message | is_unread | created_at");
            System.out.println(notification.getId() + " | " + notification.getUserId() + " | " + notification.getMessage() + " | " + notification.getIsUnread() + " | " + notification.getCreatedAt());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<Notification> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | user_id | message | is_unread | created_at");
            list.forEach(notification -> System.out.println(notification.getId() + " | " + notification.getUserId() + " | " + notification.getMessage() + " | " + notification.getIsUnread() + " | " + notification.getCreatedAt()));
        }
    }

    private void update() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Notification notification = dao.findById(id);
        if (notification == null) { System.out.println("Not found"); return; }
        System.out.print("New user id: ");
        notification.setUserId(scanner.nextInt());
        System.out.print("New message: ");
        notification.setMessage(scanner.next());
        System.out.print("Is unread (true/false): ");
        notification.setIsUnread(scanner.nextBoolean());
        dao.update(notification);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id: ");
        dao.delete(scanner.nextInt());
        System.out.println("Deleted successfully");
    }
}
