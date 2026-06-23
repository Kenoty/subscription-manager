package com.example.menu;

import com.example.dao.SubscriptionEventDao;
import com.example.model.SubscriptionEvent;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class SubscriptionEventMenu {
    private final Scanner scanner;
    private final SubscriptionEventDao dao;

    public SubscriptionEventMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new SubscriptionEventDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Subscription Events ---");
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
        System.out.print("Subscription id: ");
        int subscriptionId = scanner.nextInt();
        System.out.print("Event id: ");
        int eventId = scanner.nextInt();
        System.out.print("Event date (yyyy-MM-dd): ");
        LocalDate eventDate = LocalDate.parse(scanner.next());
        System.out.print("Days: ");
        int days = scanner.nextInt();
        dao.create(new SubscriptionEvent(subscriptionId, eventId, eventDate, days));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        SubscriptionEvent subscriptionEvent = dao.findById(id);
        if (subscriptionEvent != null) {
            System.out.println("id | subscription_id | event_id | event_date | days");
            System.out.println(subscriptionEvent.getId() + " | " + subscriptionEvent.getSubscriptionId() + " | " + subscriptionEvent.getEventId() + " | " + subscriptionEvent.getEventDate() + " | " + subscriptionEvent.getDays());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<SubscriptionEvent> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | subscription_id | event_id | event_date | days");
            list.forEach(subscriptionEvent -> System.out.println(subscriptionEvent.getId() + " | " + subscriptionEvent.getSubscriptionId() + " | " + subscriptionEvent.getEventId() + " | " + subscriptionEvent.getEventDate() + " | " + subscriptionEvent.getDays()));
        }
    }

    private void update() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        SubscriptionEvent subscriptionEvent = dao.findById(id);
        if (subscriptionEvent == null) { System.out.println("Not found"); return; }
        System.out.print("New subscription id: ");
        subscriptionEvent.setSubscriptionId(scanner.nextInt());
        System.out.print("New event id: ");
        subscriptionEvent.setEventId(scanner.nextInt());
        System.out.print("New event date (yyyy-MM-dd): ");
        subscriptionEvent.setEventDate(LocalDate.parse(scanner.next()));
        System.out.print("New days: ");
        subscriptionEvent.setDays(scanner.nextInt());
        dao.update(subscriptionEvent);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id: ");
        dao.delete(scanner.nextInt());
        System.out.println("Deleted successfully");
    }
}
