package com.steamdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SteamApp {

    SteamApp(String[] args) {

    }

    public void show() {
        int row = 6;

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
        JLabel response = new JLabel();
        JPasswordField pwText = new JPasswordField(10);
        JPanel[] rows = new JPanel[row];

        for (int i = 0; i < row; i++) {
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
        JButton connection_btn = new JButton("Connect");
        connection_btn.setHorizontalAlignment(JButton.CENTER);
        connection_btn.setMargin(new Insets(5, 5, 5, 5));
        connection_btn.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        response.setHorizontalAlignment(JLabel.CENTER);
        response.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        connection_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    Connection connection = createConnection(addText.getText(), portText.getText(), dbText.getText(), userText.getText(), String.valueOf(pwText.getPassword()));
                    response.setText("Connection OK");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    response.setText("Something went wrong");
                }
            }
        });

        //PUT EVERYTHING TOGETHER
        Box verticalBox = Box.createVerticalBox();
        for (int i = 0; i < row; i++) {
            rows[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, rows[i].getHeight()));
            verticalBox.add(rows[i], JComponent.LEFT_ALIGNMENT);
        }

        Component c = Box.createRigidArea(new Dimension(Integer.MAX_VALUE, 20));
        verticalBox.add(c);
        verticalBox.add(connection_btn);
        verticalBox.add(response);
        verticalBox.add(Box.createVerticalGlue());

        frame.add(verticalBox);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(final String[] args) {
        SteamApp steamApp = new SteamApp(args);

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                steamApp.show();
            }
        });

    }

    /**
     * @param address  of the database, can't be null
     * @param port     of the databse, can't be null
     * @param dbName   name of the database, can't be null
     * @param username of user who is connection to the database, can't be null
     * @param password of the user who is connection to the database, can't be null
     * @return the {@link Connection} object
     * @throws SQLException may be thrown if something wrong with the database happens
     */
    public static Connection createConnection(String address, String port, String dbName, String username, String password) throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://" + address + ":" + port + "/" + dbName, username, password);
    }
}
