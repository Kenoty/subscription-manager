package com.example.menu;

import java.sql.Connection;
import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner;
    private final Connection conn;

    public MainMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.conn    = conn;
    }

    public void show() {
        while (true) {
            System.out.println("\n=== Subscription Manager ===");
            System.out.println("1.  Billing Periods");
            System.out.println("2.  Device Types");
            System.out.println("3.  Devices");
            System.out.println("4.  Event Types");
            System.out.println("5.  Notifications");
            System.out.println("6.  Payments");
            System.out.println("7.  Plans");
            System.out.println("8.  Roles");
            System.out.println("9.  Services");
            System.out.println("10. Subscriptions");
            System.out.println("11. Subscription Devices");
            System.out.println("12. Subscription Events");
            System.out.println("13. Users");
            System.out.println("0.  Exit");
            System.out.print("Choose: ");

            switch (scanner.nextInt()) {
                case 1  -> new BillingPeriodMenu(scanner, conn).show();
                case 2  -> new DeviceTypeMenu(scanner, conn).show();
                case 3  -> new DeviceMenu(scanner, conn).show();
                case 4  -> new EventTypeMenu(scanner, conn).show();
                case 5  -> new NotificationMenu(scanner, conn).show();
                case 6  -> new PaymentMenu(scanner, conn).show();
                case 7  -> new PlanMenu(scanner, conn).show();
                case 8  -> new RoleMenu(scanner, conn).show();
                case 9  -> new ServiceMenu(scanner, conn).show();
                case 10 -> new SubscriptionMenu(scanner, conn).show();
                case 11 -> new SubscriptionDeviceMenu(scanner, conn).show();
                case 12 -> new SubscriptionEventMenu(scanner, conn).show();
                case 13 -> new UserMenu(scanner, conn).show();
                case 0  -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
