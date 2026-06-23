package com.example.menu;

import com.example.dao.RoleDao;
import com.example.model.Role;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class RoleMenu {
    private final Scanner scanner;
    private final RoleDao dao;

    public RoleMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new RoleDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Roles ---");
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
        dao.create(new Role(name));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Role role = dao.findById(id);
        if (role != null) {
            System.out.println("id | name");
            System.out.println(role.getId() + " | " + role.getName());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<Role> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | name");
            list.forEach(role -> System.out.println(role.getId() + " | " + role.getName()));
        }
    }

    private void update() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        Role role = dao.findById(id);
        if (role == null) { System.out.println("Not found"); return; }
        System.out.print("New name: ");
        role.setName(scanner.next());
        dao.update(role);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id: ");
        dao.delete(scanner.nextInt());
        System.out.println("Deleted successfully");
    }
}
