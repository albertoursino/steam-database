package com.steamdb;

import javax.swing.*;

public class MainWindow {

    static String FILL_FIELDS = "Riempire i campi per connettersi al database";
    static String DEFAULT_DB = "Steam";
    static String DEFAULT_PORT = "5432";

    public MainWindow(){
        servPortText.setText(DEFAULT_PORT);
        connResponse.setText(FILL_FIELDS);
        dbNameText.setText(DEFAULT_DB);
        connBtn.setText("Connetti");
    }

    JPanel mainPanel;
    JTextField servAddrText;
    JTextField servPortText;
    JTextField dbNameText;
    JPasswordField passwdText;
    JTextField userText;
    JButton connBtn;
    JButton queryBtn;
    JLabel connResponse;

    private JLabel servAddr;
    private JLabel servPort;
    private JLabel dbName;
    private JLabel user;
    private JLabel passwd;
}
