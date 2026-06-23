package com.example.menu;

import com.example.dao.PaymentDao;
import com.example.model.Payment;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class PaymentMenu {
    private final Scanner scanner;
    private final PaymentDao dao;

    public PaymentMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new PaymentDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Payments ---");
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
        System.out.print("Event id: ");
        int eventId = scanner.nextInt();
        System.out.print("Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        System.out.print("Currency: ");
        String currency = scanner.next();
        dao.create(new Payment(eventId, amount, currency));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Payment payment = dao.findById(id);
        if (payment != null) {
            System.out.println("id | event_id | amount | currency | paid_at");
            System.out.println(payment.getId() + " | " + payment.getEventId() + " | " + payment.getAmount() + " | " + payment.getCurrency() + " | " + payment.getPaidAt());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<Payment> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | event_id | amount | currency | paid_at");
            list.forEach(payment -> System.out.println(payment.getId() + " | " + payment.getEventId() + " | " + payment.getAmount() + " | " + payment.getCurrency() + " | " + payment.getPaidAt()));
        }
    }

    private void update() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Payment payment = dao.findById(id);
        if (payment == null) { System.out.println("Not found"); return; }
        System.out.print("New event id: ");
        payment.setEventId(scanner.nextInt());
        System.out.print("New amount: ");
        payment.setAmount(scanner.nextBigDecimal());
        System.out.print("New currency: ");
        payment.setCurrency(scanner.next());
        dao.update(payment);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id: ");
        dao.delete(scanner.nextInt());
        System.out.println("Deleted successfully");
    }
}
