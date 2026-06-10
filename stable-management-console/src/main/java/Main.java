import app.Session;
import config.DbConfig;
import config.JpaConfig;
import exception.*;
import service.*;
import ui.AdminMenu;
import ui.AuthMenu;
import ui.UserMenu;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        StableService stableService = new StableService();
        CsvService csvService = new CsvService();
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);
        Session session = new Session();

        try {
            DbConfig.getDbConnection();
        }
        catch(DatabaseException e) {
            System.out.println("Failed to connect to database.");
            JpaConfig.shutdown();
            scanner.close();
            System.exit(1);
        }

        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.engine.jdbc.connections.internal").setLevel(Level.WARNING);

        session.login(AuthMenu.start(scanner, userService));

        if(session.getCurrentUser().isAdmin()) {
            AdminMenu.start(scanner, stableService, csvService);
        }
        else {
            UserMenu.start(scanner, stableService, csvService);
        }

        JpaConfig.shutdown();
        scanner.close();
    }
}
