package tables;

import javax.swing.*;

public class MainWindow {

    static String FILL_FIELDS = "Riempire i campi per connettersi al database";
    static String DEFAULT_DB = "Steam";
    static String DEFAULT_PORT = "5432";

    public MainWindow() {
        servPortText.setText(DEFAULT_PORT);
        connResponse.setText(FILL_FIELDS);
        dbNameText.setText(DEFAULT_DB);
        connBtn.setText("Connetti");
        queryBtn.setEnabled(false);
    }

    public JPanel mainPanel;
    public JTextField servAddrText;
    public JTextField servPortText;
    public JTextField dbNameText;
    public JPasswordField passwdText;
    public JTextField userText;
    public JButton connBtn;
    public JButton queryBtn;
    public JLabel connResponse;

    private JLabel servAddr;
    private JLabel servPort;
    private JLabel dbName;
    private JLabel user;
    private JLabel passwd;

}
