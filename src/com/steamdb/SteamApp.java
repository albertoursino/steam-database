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
                    if(mainWin.connBtn.getText().equals("Connetti")){
                        if (connection != null)
                            connection.close();
                    connection = createConnection(mainWin.servAddrText.getText(), mainWin.servPortText.getText(),
                            mainWin.dbNameText.getText(), mainWin.userText.getText(),
                            String.valueOf(mainWin.passwdText.getPassword()));
                    mainWin.connResponse.setText("Connessione stabilita. E' possibile interrogare il db.");
                    mainWin.queryBtn.setEnabled(true);
                    mainWin.connBtn.setText("Disconnetti");
                    return;
                    }
                    else {
                        connection.close();
                        mainWin.connResponse.setText("Riempire i campi per connettersi al database");
                    }
                }catch(SQLException e) {
                    mainWin.connResponse.setText(htmlCenteredStr(e.getMessage(), 50));
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

    /**
     * format messages such that JLabels displaying it contain at most breakLen chars for each line.
     * Text is also centered
     * @param msg to be formatted
     * @param breakLen number of chars for each line
     * @return the formatted html message
     *
     * */
    private String htmlCenteredStr(String msg, int breakLen){
        String head = "<html><p style=\"text-align:center\">";
        String end = "</p></html>";
        int msgLen = msg.length();
        int lineBreaks = msgLen / breakLen;
        StringBuilder builder = new StringBuilder(head);
        int i = 0;
        for(; i < lineBreaks; i++){
            builder.append(msg.substring(i * breakLen, (i+1) * breakLen));
            builder.append("<br>");
        }
        builder.append(msg.substring(i*breakLen));
        builder.append(end);
        return builder.toString();
    }
}
