package ca.corbett.tasktracker;

import ca.corbett.extras.LookAndFeelManager;
import ca.corbett.tasktracker.ui.MainWindow;
import com.formdev.flatlaf.intellijthemes.FlatXcodeDarkIJTheme;

import javax.swing.JFrame;
import java.awt.SplashScreen;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        // Before we do anything else...
        initializeLogging();

        // Set up extra look and feels:
        LookAndFeelManager.installExtraLafs();

        // Get the splash screen if there is one:
        final SplashScreen splashScreen = SplashScreen.getSplashScreen();

        // Load saved application config:
        Logger.getLogger(Main.class.getName())
              .info(Version.APPLICATION_NAME + " " + Version.VERSION + " initializing...");

        // TODO load extensions and app config:
        //TaskTrackerExtensionManager.getInstance().loadAll();
        //AppConfig.getInstance().load();
        //LookAndFeelManager.switchLaf(AppConfig.getInstance().getLookAndFeelClassname());
        LookAndFeelManager.switchLaf(FlatXcodeDarkIJTheme.class.getName());

        // Load and show main window:
        MainWindow window = MainWindow.getInstance();
        if (splashScreen != null) {
            try {
                // Wait a second or so, so it doesn't just flash up and disappear immediately.
                Thread.sleep(744);
            }
            catch (InterruptedException ignored) {
                // ignored
            }
            splashScreen.close();
        }

        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setVisible(true);
            }
        });
    }

    private static void initializeLogging() {
        // log file can be supplied as a system property:
        if (System.getProperties().containsKey("java.util.logging.config.file")) {
            // Do nothing. It will be used automatically.
        }

        // If it is not set, we'll assume it's in APPLICATION_HOME:
        else {
            File logProperties = new File(Version.SETTINGS_DIR, "logging.properties");
            if (logProperties.exists() && logProperties.canRead()) {
                try {
                    LogManager.getLogManager().readConfiguration(new FileInputStream(logProperties));
                }
                catch (IOException ioe) {
                    System.out.println("WARN: Unable to load log configuration from app dir: " + ioe.getMessage());
                }
            }

            // Otherwise, load our built-in default from jar resources:
            else {
                try {
                    LogManager.getLogManager().readConfiguration(
                        Main.class.getResourceAsStream("/ca/corbett/tasktracker/logging.properties"));
                }
                catch (IOException ioe) {
                    System.out.println("WARN: Unable to load log configuration from jar: " + ioe.getMessage());
                }
            }
        }
    }
}