package com.steamdb;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class QueryWindow {
    JPanel mainPanel;
    private JTable table4;
    private JTextField userText;
    private JButton startBtn;
    private JLabel queryResponse;

    public QueryWindow(){

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};

        Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", new Integer(5), new Boolean(false)},
                {"John", "Doe",
                        "Rowing", new Integer(3), new Boolean(true)},
                {"Sue", "Black",
                        "Knitting", new Integer(2), new Boolean(false)},
                {"Jane", "White",
                        "Speed reading", new Integer(20), new Boolean(true)},
                {"Joe", "Brown",
                        "Pool", new Integer(10), new Boolean(false)}
        };


        table4 = new JTable(data, columnNames);

    }
}
