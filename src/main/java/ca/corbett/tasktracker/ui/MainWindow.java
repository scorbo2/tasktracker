package ca.corbett.tasktracker.ui;

import ca.corbett.extras.logging.LogConsole;
import ca.corbett.tasktracker.Version;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.logging.Logger;

/**
 * The main window for the application.
 *
 * @author scorbo2
 */
public class MainWindow extends JFrame {

    private static MainWindow instance;
    private static final Logger logger = Logger.getLogger(MainWindow.class.getName());

    private MainWindow() {
        super(Version.NAME);
        setSize(new Dimension(500,400));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static MainWindow getInstance() {
        if (instance == null)  {
            instance = new MainWindow();
            java.net.URL url = MainWindow.class.getResource("/ca/corbett/tasktracker/images/logo.jpg");
            if (url != null) {
                Image image = Toolkit.getDefaultToolkit().createImage(url);
                instance.setIconImage(image);
                LogConsole.getInstance().setIconImage(image);
            }
            else {
                logger.warning("MainWindow: unable to load logo image from resources.");
            }
            LogConsole.getInstance().setTitle(Version.APPLICATION_NAME + " log console");

        }
        return instance;
    }
}
