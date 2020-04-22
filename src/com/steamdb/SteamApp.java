package com.steamdb;

import javax.swing.*;
import java.awt.*;

public class SteamApp {

    SteamApp(String[] args) {

    }

    public void show() {
        // Show the UI.
        JFrame frame = new JFrame("steam db");
        JLabel addLab = new JLabel("Indirizzo del server a cui connettersi: ");
        JLabel portLab = new JLabel("Porta: ");
        JLabel userLab = new JLabel("Nome utente: ");
        JLabel pwLab = new JLabel("Password: ");
        JLabel dbLab = new JLabel("Nome del database: ");
        JTextField addText = new JTextField(12);
        JTextField portText = new JTextField(6);
        JTextField dbText = new JTextField(20);
        JTextField userText = new JTextField(20);
        JPasswordField pwText = new JPasswordField(10);
        JPanel[] rows = new JPanel[5];

        for(int i = 0; i < 5; i++){
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

        JButton bun = new JButton("Connettiti al database");
        bun.setHorizontalAlignment(JButton.CENTER);
        bun.setMargin(new Insets(5, 5, 5, 5));
        bun.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        //PUT EVERYTHING TOGETHER
        Box verticalBox = Box.createVerticalBox();
        for(int i = 0; i < 5; i++){
            rows[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, rows[i].getHeight()));
            verticalBox.add(rows[i], JComponent.LEFT_ALIGNMENT);
        }

        Component c = Box.createRigidArea(new Dimension(Integer.MAX_VALUE, 20));
        verticalBox.add(c);
        verticalBox.add(bun);

        verticalBox.add(Box.createVerticalGlue());
        frame.add(verticalBox);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(final String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SteamApp(args).show();
            }
        });
    }
}
