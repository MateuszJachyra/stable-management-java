package service;

import enumeration.HorseStatus;
import enumeration.HorseType;
import model.Horse;

import java.util.Scanner;

public class InputService {
    public static int readInt(Scanner scanner, String prompt) {
        while(true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer");
            }
        }
    }

    public static double readDouble(Scanner scanner, String prompt) {
        while(true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid floating-point number");
            }
        }
    }

    public static HorseType readHorseType(Scanner scanner, String prompt) {
        while(true) {
            System.out.print(prompt);
            String input = scanner.nextLine().toUpperCase();
            try {
                return HorseType.valueOf(input);
            } catch (RuntimeException e) {
                System.out.println("Please enter a valid horse type");
            }
        }
    }

    public static HorseStatus readHorseStatus(Scanner scanner, String prompt) {
        while(true) {
            System.out.print(prompt);
            String input = scanner.nextLine().toUpperCase();
            try {
                return HorseStatus.valueOf(input);
            } catch (RuntimeException e) {
                System.out.println("Please enter a valid horse status");
            }
        }
    }

    public static void inputHorse(StableService stableService, Scanner scanner, String stableName) {
        System.out.print("Enter horse name: ");
        String horseName =  scanner.nextLine();
        System.out.print("Enter horse breed: ");
        String horseBreed =  scanner.nextLine();
        HorseType horseType =  InputService.readHorseType(scanner, "Enter horse type (WARMBLOODED/COLDBLOODED): ");
        HorseStatus horseStatus = InputService.readHorseStatus(scanner, "Enter horse status (HEALTHY/SICK/TRAINING/SOLD): ");
        int horseAge = InputService.readInt(scanner, "Enter horse age: ");
        double horsePrice = InputService.readDouble(scanner, "Enter horse price: ");
        double horseWeight = InputService.readDouble(scanner, "Enter horse weight: ");
        stableService.addHorse(stableName, new Horse(horseName, horseBreed, horseType, horseStatus, horseAge, horsePrice, horseWeight));
    }
}
