package com.steamdb;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SteamApp {

    Connection connection = null;
    MainWindow mainWin = null;
    QueryWindow queryWin = null;

    public static String title = "Steam Database Application";

    SteamApp(String[] args) {

    }

    public void showMainWindow(){
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
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    if (connection != null){
                        connection.close();
                        mainWin.queryBtn.setEnabled(false);
                    }
                    connection = createConnection(mainWin.servAddrText.getText(), mainWin.servPortText.getText(),
                            mainWin.dbNameText.getText(), mainWin.userText.getText(),
                            String.valueOf(mainWin.passwdText.getPassword()));
                    mainWin.connResponse.setText("Connessione stabilita. E' possibile interrogare il db.");
                    mainWin.queryBtn.setEnabled(true);
                } catch (SQLException e) {
                    mainWin.connResponse.setText("<html><p style=\"text-align:center\">Qualcosa è andato storto.<br>"
                            + e.getMessage() + "</p></html>");
                }
            }
        });

        mainWin.queryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQueryWindow();
            }
        });

         /*
        int no_rows = 6;

        // Show the UI.
        JFrame frame = new JFrame("Steam Database");
        JLabel addLab = new JLabel("IP Address: ");
        JLabel portLab = new JLabel("Port: ");
        JLabel userLab = new JLabel("Username: ");
        JLabel pwLab = new JLabel("Password: ");
        JLabel dbLab = new JLabel("Database name: ");
        JTextField addText = new JTextField(12);
        JTextField portText = new JTextField(6);
        JTextField dbText = new JTextField(20);
        JTextField userText = new JTextField(20);
        JLabel connectionResponse = new JLabel();
        JPasswordField pwText = new JPasswordField(10);
        JPanel[] rows = new JPanel[no_rows];

        for (int i = 0; i < no_rows; i++) {
            rows[i] = new JPanel();
            rows[i].setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        }

        //1st ROW: SERVER ADDRESS
        rows[0].add(addLab);
        rows[0].add(addText);

        //2nd ROW: PORT
        rows[1].add(portLab);
        rows[1].add(portText);

        //3rd ROW: DB NAME
        rows[2].add(dbLab);
        rows[2].add(dbText);

        //4th ROW: USER NAME
        rows[3].add(userLab);
        rows[3].add(userText);

        //5th ROW: PASSWORD
        rows[4].add(pwLab);
        rows[4].add(pwText);


        //ADD A BUTTON TO ESTABLISH CONNECTION
        JButton connectionBtn = new JButton("Connect");
        connectionBtn.setHorizontalAlignment(JButton.CENTER);
        connectionBtn.setMargin(new Insets(5, 5, 5, 5));
        connectionBtn.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        //SHOW THIS ONLY IF THE CONNECTION HAS BEEN SUCCESSFULLY ESTABLISHED
        JButton queryBtn = new JButton("Start a query");
        queryBtn.setHorizontalAlignment(JButton.CENTER);
        queryBtn.setMargin(new Insets(5, 5, 5, 5));
        queryBtn.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        queryBtn.setVisible(false);*/
    }

    public void showQueryWindow() {

        JDialog dialog = new JDialog((JFrame)null, "interroga il db", true);
        queryWin = new QueryWindow();
        dialog.setContentPane(queryWin.mainPanel);
        dialog.setContentPane(queryWin.mainPanel);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static void main(final String[] args) {
        SteamApp steamApp = new SteamApp(args);

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                steamApp.showMainWindow();
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
