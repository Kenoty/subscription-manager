package com.example.menu;

import com.example.dao.ServiceDao;
import com.example.model.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class ServiceMenu {
    private final Scanner scanner;
    private final ServiceDao dao;

    public ServiceMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new ServiceDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Services ---");
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
        System.out.print("Description: ");
        String description = scanner.next();
        System.out.print("Website URL: ");
        String websiteUrl = scanner.next();
        System.out.print("Logo URL: ");
        String logoUrl = scanner.next();
        dao.create(new Service(name, description, websiteUrl, logoUrl));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Service service = dao.findById(id);
        if (service != null) {
            System.out.println("id | name | description | website_url | logo_url");
            System.out.println(service.getId() + " | " + service.getName() + " | " + service.getDescription() + " | " + service.getWebsiteUrl() + " | " + service.getLogoUrl());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<Service> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | name | description | website_url | logo_url");
            list.forEach(service -> System.out.println(service.getId() + " | " + service.getName() + " | " + service.getDescription() + " | " + service.getWebsiteUrl() + " | " + service.getLogoUrl()));
        }
    }

    private void update() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Service service = dao.findById(id);
        if (service == null) { System.out.println("Not found"); return; }
        System.out.print("New name: ");
        service.setName(scanner.next());
        System.out.print("New description: ");
        service.setDescription(scanner.next());
        System.out.print("New website URL: ");
        service.setWebsiteUrl(scanner.next());
        System.out.print("New logo URL: ");
        service.setLogoUrl(scanner.next());
        dao.update(service);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id: ");
        dao.delete(scanner.nextInt());
        System.out.println("Deleted successfully");
    }
}
