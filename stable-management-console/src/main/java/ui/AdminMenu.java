package ui;

import exception.CsvServiceException;
import exception.HorseOperationException;
import exception.StableOperationException;
import exception.StableServiceOperationException;
import model.Horse;
import model.Stable;
import service.CsvService;
import service.InputService;
import service.StableService;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    public static void start(Scanner scanner, StableService stableService, CsvService csvService) {
        boolean isRunning = true;
        while(isRunning) {
            System.out.print("""
                    1. List stables (name, occupancy)
                    2. Create stable
                    3. Delete stable
                    4. Add horse
                    5. Remove horse
                    6. List all horses (sorted)
                    7. Add rating to horse
                    8. Export stable to CSV
                    9. Import stable from CSV
                    0. Exit
                    """);
            int choice = InputService.readInt(scanner, "Choice: ");
            while (choice > 9 || choice < 0) {
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
                    System.out.print("Enter name of the stable: ");
                    String name = scanner.nextLine();

                    int capacity = InputService.readInt(scanner, "Enter capacity of the stable: ");
                    try {
                        stableService.createStable(name, capacity);
                    } catch (StableServiceOperationException e) {
                        System.out.println("Failed to add stable (" + e.getMessage() + ")");
                    }
                }
                case 3 -> {
                    System.out.print("Enter stable name: ");
                    String name = scanner.nextLine();
                    try {
                        stableService.deleteStable(name);
                    }
                    catch (StableOperationException e) {
                        System.out.println("Failed to delete stable (" + e.getMessage() + ")\n");
                        System.out.println("Do you wish to delete non-empty stable? (y\\n)");
                        String c = scanner.nextLine();
                        if (c.equalsIgnoreCase("y")) {
                            stableService.forceDeleteStable(name);
                            System.out.println("Successfully deleted stable");
                        }
                    }
                    catch (StableServiceOperationException e) {
                        System.out.println("Failed to delete stable (" + e.getMessage() + ")");
                    }
                }
                case 4 -> {
                    System.out.print("Enter stable name: ");
                    String name = scanner.nextLine();
                    try {
                        InputService.inputHorse(stableService, scanner, name);
                    } catch (StableServiceOperationException e) {
                        System.out.println("Failed to add horse (" + e.getMessage() + ")");
                    }
                }
                case 5 -> {
                    int horseId = InputService.readInt(scanner, "Enter horse id: ");
                    try {
                        stableService.deleteHorse(horseId);
                    } catch (StableServiceOperationException e) {
                        System.out.println("Failed to delete horse (" + e.getMessage() + ")");
                    }
                }
                case 6 -> {
                    List<Stable> stables = stableService.listStables();
                    for (Stable stable : stables) {
                        System.out.println(stable.getName() + ": ");
                        for (Horse horse : stableService.getHorsesSorted(stable.getName())) {
                            System.out.println(horse);
                        }

                    }
                }
                case 7 -> {
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
                case 8 -> {
                    System.out.print("Enter stable name: ");
                    String name = scanner.nextLine();
                    try {
                        csvService.exportStableToCsv(stableService, name);
                    } catch (CsvServiceException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 9 -> {
                    System.out.print("Enter file name: ");
                    String fileName = scanner.nextLine();
                    try {
                        csvService.importStableFromCsv(stableService, fileName);
                    } catch (CsvServiceException | StableServiceOperationException e) {
                        System.out.println(e.getMessage());
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
