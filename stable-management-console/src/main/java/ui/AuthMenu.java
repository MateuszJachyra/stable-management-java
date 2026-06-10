package ui;

import exception.AuthException;
import exception.DatabaseException;
import model.User;
import service.InputService;
import service.UserService;

import java.util.Scanner;

public class AuthMenu {
    public static User start(Scanner scanner, UserService userService) {
        while(true) {
            int choice = InputService.readInt(scanner, "1. login\n2. register\nChoice:");
            while (choice > 2 || choice < 1) {
                choice = InputService.readInt(scanner, "Invalid choice\nChoice: ");
            }
            System.out.println();
            switch(choice) {
                case 1 -> {
                    System.out.println("Please enter your username:");
                    String username = scanner.nextLine();
                    System.out.println("Please enter your password:");
                    String password = scanner.nextLine();
                    try {
                        return userService.authenticate(username, password);
                    }
                    catch (AuthException | DatabaseException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.println("Please enter your username:");
                    String username = scanner.nextLine();
                    System.out.println("Please enter your password:");
                    String password = scanner.nextLine();
                    try {
                        return userService.register(username, password);
                    }
                    catch (AuthException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (DatabaseException e) {
                        System.out.println("Failed to register. "+e.getMessage());
                    }
                }
            }
        }
    }
}
