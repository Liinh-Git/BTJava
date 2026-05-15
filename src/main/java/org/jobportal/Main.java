package org.jobportal;

import org.jobportal.view.common.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Khởi chạy giao diện trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}