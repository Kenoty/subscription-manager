package com.example.menu;

import com.example.dao.SubscriptionDao;
import com.example.model.Subscription;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class SubscriptionMenu {
    private final Scanner scanner;
    private final SubscriptionDao dao;

    public SubscriptionMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new SubscriptionDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Subscriptions ---");
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
        System.out.print("Plan id: ");
        int planId = scanner.nextInt();
        System.out.print("Auto renew (true/false): ");
        boolean autoRenew = scanner.nextBoolean();
        dao.create(new Subscription(userId, planId, autoRenew));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Subscription subscription = dao.findById(id);
        if (subscription != null) {
            System.out.println("id | user_id | plan_id | auto_renew");
            System.out.println(subscription.getId() + " | " + subscription.getUserId() + " | " + subscription.getPlanId() + " | " + subscription.getAutoRenew());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<Subscription> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | user_id | plan_id | auto_renew");
            list.forEach(subscription -> System.out.println(subscription.getId() + " | " + subscription.getUserId() + " | " + subscription.getPlanId() + " | " + subscription.getAutoRenew()));
        }
    }

    private void update() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Subscription subscription = dao.findById(id);
        if (subscription == null) { System.out.println("Not found"); return; }
        System.out.print("New user id: ");
        subscription.setUserId(scanner.nextInt());
        System.out.print("New plan id: ");
        subscription.setPlanId(scanner.nextInt());
        System.out.print("Auto renew (true/false): ");
        subscription.setAutoRenew(scanner.nextBoolean());
        dao.update(subscription);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id: ");
        dao.delete(scanner.nextInt());
        System.out.println("Deleted successfully");
    }
}
