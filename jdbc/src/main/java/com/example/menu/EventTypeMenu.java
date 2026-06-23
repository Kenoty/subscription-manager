package com.example.menu;

import com.example.dao.EventTypeDao;
import com.example.model.EventType;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class EventTypeMenu {
    private final Scanner scanner;
    private final EventTypeDao dao;

    public EventTypeMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new EventTypeDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Event Types ---");
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
        System.out.print("Name: ");
        String name = scanner.next();
        dao.create(new EventType(name));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        EventType eventType = dao.findById(id);
        if (eventType != null) {
            System.out.println("id | name");
            System.out.println(eventType.getId() + " | " + eventType.getName());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<EventType> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | name");
            list.forEach(eventType -> System.out.println(eventType.getId() + " | " + eventType.getName()));
        }
    }

    private void update() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        EventType eventType = dao.findById(id);
        if (eventType == null) { System.out.println("Not found"); return; }
        System.out.print("New name: ");
        eventType.setName(scanner.next());
        dao.update(eventType);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id: ");
        dao.delete(scanner.nextInt());
        System.out.println("Deleted successfully");
    }
}
