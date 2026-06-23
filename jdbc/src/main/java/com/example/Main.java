package com.example;

import com.example.menu.MainMenu;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            new MainMenu(scanner, conn).show();

        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}