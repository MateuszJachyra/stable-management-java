package ui;

import exception.CsvServiceException;
import exception.HorseOperationException;
import exception.StableServiceOperationException;
import model.Horse;
import model.Stable;
import service.CsvService;
import service.InputService;
import service.StableService;

import java.util.List;
import java.util.Scanner;

public class UserMenu {
    public static void start(Scanner scanner, StableService stableService, CsvService csvService) {
        boolean isRunning = true;
        while(isRunning) {
            System.out.print("""
                    1. List stables (name, occupancy)
                    2. List all horses (sorted)
                    3. Add rating to horse
                    4. Export stable to CSV
                    0. Exit
                    """);
            int choice = InputService.readInt(scanner, "Choice: ");
            while (choice > 4 || choice < 0) {
                choice = InputService.readInt(scanner, "Invalid choice\nChoice: ");
            }
            System.out.println();
            switch (choice) {
                case 1 -> {
                    for (Stable stable : stableService.listStables()) {
                        System.out.println(stable);
                    }
                }
                case 2 -> {
                    List<Stable> stables = stableService.listStables();
                    for (Stable stable : stables) {
                        System.out.println(stable.getName() + ": ");
                        for (Horse horse : stableService.getHorsesSorted(stable.getName())) {
                            System.out.println(horse);
                        }

                    }
                }
                case 3 -> {
                    int horseId = InputService.readInt(scanner, "Enter horse id: ");
                    System.out.print("Enter horse rating (1-5): ");
                    int horseRating = Integer.parseInt(scanner.nextLine());
                    try {
                        stableService.addRating(horseId, horseRating);
                    } catch (StableServiceOperationException e) {
                        System.out.println("Failed to find a horse");
                    } catch (HorseOperationException e) {
                        System.out.println("Failed to add a rating (" + e.getMessage() + ")");
                    }
                }
                case 4 -> {
                    System.out.print("Enter stable name: ");
                    String name = scanner.nextLine();
                    try {
                        csvService.exportStableToCsv(stableService, name);
                    } catch (CsvServiceException e) {
                        System.out.println("Failed to export stable (" + e.getMessage() + ")");
                    }
                }
                case 0 -> {
                    isRunning = false;
                }
            }
            System.out.println();
        }
    }
}
