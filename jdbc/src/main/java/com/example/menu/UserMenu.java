package com.example.menu;

import com.example.dao.UserDao;
import com.example.model.User;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class UserMenu {
    private final Scanner scanner;
    private final UserDao dao;

    public UserMenu(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.dao     = new UserDao(conn);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Users ---");
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
        System.out.print("Role id: ");
        int roleId = scanner.nextInt();
        System.out.print("First name: ");
        String firstName = scanner.next();
        System.out.print("Last name: ");
        String lastName = scanner.next();
        System.out.print("Email: ");
        String email = scanner.next();
        System.out.print("Password hash: ");
        String passwordHash = scanner.next();
        dao.create(new User(roleId, firstName, lastName, email, passwordHash));
        System.out.println("Created successfully");
    }

    private void findById() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        User user = dao.findById(id);
        if (user != null) {
            System.out.println("id | role_id | first_name | last_name | email | created_at");
            System.out.println(user.getId() + " | " + user.getRoleId() + " | " + user.getFirstName() + " | " + user.getLastName() + " | " + user.getEmail() + " | " + user.getCreatedAt());
        } else {
            System.out.println("Not found");
        }
    }

    private void findAll() {
        List<User> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("No records found");
        } else {
            System.out.println("id | role_id | first_name | last_name | email | created_at");
            list.forEach(user -> System.out.println(user.getId() + " | " + user.getRoleId() + " | " + user.getFirstName() + " | " + user.getLastName() + " | " + user.getEmail() + " | " + user.getCreatedAt()));
        }
    }

    private void update() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        User user = dao.findById(id);
        if (user == null) { System.out.println("Not found"); return; }
        System.out.print("New role id: ");
        user.setRoleId(scanner.nextInt());
        System.out.print("New first name: ");
        user.setFirstName(scanner.next());
        System.out.print("New last name: ");
        user.setLastName(scanner.next());
        System.out.print("New email: ");
        user.setEmail(scanner.next());
        System.out.print("New password hash: ");
        user.setPasswordHash(scanner.next());
        dao.update(user);
        System.out.println("Updated successfully");
    }

    private void delete() {
        System.out.print("Id: ");
        dao.delete(scanner.nextInt());
        System.out.println("Deleted successfully");
    }
}
