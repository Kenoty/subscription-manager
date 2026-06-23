package com.example.menu;

import com.example.dao.PlanDao;
import com.example.model.Plan;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class PlanMenu {
    private final Scanner scanner;
    private final PlanDao dao;

    public PlanMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new PlanDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Plans ---");
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
        System.out.print("Service id: ");
        int serviceId = scanner.nextInt();
        System.out.print("Name: ");
        String name = scanner.next();
        System.out.print("Price: ");
        BigDecimal price = scanner.nextBigDecimal();
        System.out.print("Currency: ");
        String currency = scanner.next();
        System.out.print("Billing period id: ");
        int billingPeriodId = scanner.nextInt();
        System.out.print("Max devices: ");
        int maxDevices = scanner.nextInt();
        dao.create(new Plan(serviceId, name, price, currency, billingPeriodId, maxDevices));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Plan plan = dao.findById(id);
        if (plan != null) {
            System.out.println("id | service_id | name | price | currency | billing_period_id | max_devices");
            System.out.println(plan.getId() + " | " + plan.getServiceId() + " | " + plan.getName() + " | " + plan.getPrice() + " | " + plan.getCurrency() + " | " + plan.getBillingPeriodId() + " | " + plan.getMaxDevices());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<Plan> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | service_id | name | price | currency | billing_period_id | max_devices");
            list.forEach(plan -> System.out.println(plan.getId() + " | " + plan.getServiceId() + " | " + plan.getName() + " | " + plan.getPrice() + " | " + plan.getCurrency() + " | " + plan.getBillingPeriodId() + " | " + plan.getMaxDevices()));
        }
    }

    private void update() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Plan plan = dao.findById(id);
        if (plan == null) { System.out.println("Not found"); return; }
        System.out.print("New service id: ");
        plan.setServiceId(scanner.nextInt());
        System.out.print("New name: ");
        plan.setName(scanner.next());
        System.out.print("New price: ");
        plan.setPrice(scanner.nextBigDecimal());
        System.out.print("New currency: ");
        plan.setCurrency(scanner.next());
        System.out.print("New billing period id: ");
        plan.setBillingPeriodId(scanner.nextInt());
        System.out.print("New max devices: ");
        plan.setMaxDevices(scanner.nextInt());
        dao.update(plan);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id: ");
        dao.delete(scanner.nextInt());
        System.out.println("Deleted successfully");
    }
}
