package code;

import tables.MainWindow;
import tables.QueryWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    static Connection connection = null;
    static MainWindow mainWin = null;
    static QueryWindow queryWin = null;

    public static String title = "Steam Database Application";
    public static final String errorColor = "red";

    Main(String[] args) {
    }

    /**
     * Display the main window. Set up listener for connect button in order to connect to the database.
     * If this is successful, enable the query button and set the listener for the query window to be displayed.
     */
    public void showMainWindow() {
        JFrame frame = new JFrame(title);
        mainWin = new MainWindow();
        frame.setContentPane(mainWin.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        mainWin.connBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Class.forName("org.postgresql.Driver");
                    if (mainWin.connBtn.getText().equals("Connetti")) {
                        if (connection != null)
                            connection.close();
                        connection = createConnection(mainWin.servAddrText.getText(), mainWin.servPortText.getText(),
                                mainWin.dbNameText.getText(), mainWin.userText.getText(),
                                String.valueOf(mainWin.passwdText.getPassword()));
                        mainWin.connResponse.setText("Connessione stabilita. E' possibile interrogare il db.");
                        mainWin.queryBtn.setEnabled(true);
                        mainWin.connBtn.setText("Disconnetti");
                        return;
                    } else {
                        connection.close();
                        mainWin.connResponse.setText("Riempire i campi per connettersi al database");
                    }
                } catch (Exception e) {
                    mainWin.connResponse.setText(Utils.htmlStyleStr(e.getMessage(),
                            50, Utils.LabelAlignment.center, errorColor));
                }
                mainWin.queryBtn.setEnabled(false);
                mainWin.connBtn.setText("Connetti");
            }
        });

        mainWin.queryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQueryWindow();
            }
        });
    }

    /**
     * display the query window as modal dialog
     */
    public void showQueryWindow() {
        JDialog dialog = new JDialog((JFrame) null, "interroga il db", true);
        queryWin = new QueryWindow();
        dialog.setContentPane(queryWin.mainPanel);
        dialog.setContentPane(queryWin.mainPanel);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static void main(final String[] args) {
        Main main = new Main(args);

        // Schedule a job for the event-dispatching thread: creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                main.showMainWindow();
            }
        });
    }

    /**
     * @param address  ip address of the server hosting the database, can't be null
     * @param port     of the server, can't be null
     * @param dbName   name of the database, can't be null
     * @param username who wants to connect to the database, can't be null
     * @param password of the user, can't be null
     * @return the {@link Connection} object
     * @throws SQLException may be thrown if something goes wrong with the connection
     */
    public static Connection createConnection(String address, String port, String dbName, String username, String password)
            throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://" +
                address + ":" + port + "/" + dbName, username, password);
    }

}
