package com.steamdb;

import javax.swing.*;

public class SteamApp implements Runnable {

    public SteamApp(String[] args) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SteamApp(args));
    }

    @Override
    public void run() {
        
    }

}
