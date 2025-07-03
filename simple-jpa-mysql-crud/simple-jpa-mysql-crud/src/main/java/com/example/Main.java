package com.example;

import com.example.dao.UserDAO;
import com.example.entity.User;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== User Management System ===");
            System.out.println("1. Create User");
            System.out.println("2. Read User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. List All Users");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    userDAO.createUser(new User(name, email));
                    System.out.println("User created successfully!");
                    break;

                case 2:
                    System.out.print("Enter user ID: ");
                    Long id = scanner.nextLong();
                    User user = userDAO.getUser(id);
                    System.out.println(user != null ? user : "User not found!");
                    break;

                case 3:
                    System.out.print("Enter user ID to update: ");
                    Long updateId = scanner.nextLong();
                    scanner.nextLine();
                    User updateUser = userDAO.getUser(updateId);
                    if (updateUser != null) {
                        System.out.print("Enter new name: ");
                        updateUser.setName(scanner.nextLine());
                        System.out.print("Enter new email: ");
                        updateUser.setEmail(scanner.nextLine());
                        userDAO.updateUser(updateUser);
                        System.out.println("User updated successfully!");
                    } else {
                        System.out.println("User not found!");
                    }
                    break;

                case 4:
                    System.out.print("Enter user ID to delete: ");
                    Long deleteId = scanner.nextLong();
                    userDAO.deleteUser(deleteId);
                    System.out.println("User deleted successfully!");
                    break;

                case 5:
                    System.out.println("All Users:");
                    userDAO.getAllUsers().forEach(System.out::println);
                    break;

                case 6:
                    userDAO.close();
                    scanner.close();
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}