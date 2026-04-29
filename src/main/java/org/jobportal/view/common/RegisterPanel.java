package org.jobportal.view.common;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RegisterPanel extends JPanel {

    public RegisterPanel() {
        // thiet lap layout can giua
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        formPanel.setPreferredSize(new Dimension(450, 600));

        // logo
        JLabel lblLogo = new JLabel("JobPortal");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(new Color(13, 110, 253));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // tieu de
        JLabel lblTitle = new JLabel("Create your account");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubTitle = new JLabel("Join the professional network of tomorrow.");
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubTitle.setForeground(Color.GRAY);
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // form nhap lieu
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.add(createLabel("FULL NAME"));
        JTextField txtFullName = new JTextField();
        txtFullName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(txtFullName);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        inputPanel.add(createLabel("EMAIL ADDRESS"));
        JTextField txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(txtEmail);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // chia cot cho mat khau
        JPanel passPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        passPanel.setBackground(Color.WHITE);
        passPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.setBackground(Color.WHITE);
        p1.add(createLabel("PASSWORD"));
        JPasswordField txtPass = new JPasswordField();
        p1.add(txtPass);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.setBackground(Color.WHITE);
        p2.add(createLabel("CONFIRM"));
        JPasswordField txtConfirm = new JPasswordField();
        p2.add(txtConfirm);

        passPanel.add(p1);
        passPanel.add(p2);
        inputPanel.add(passPanel);

        // nut dang ky
        JButton btnRegister = new JButton("Sign Up");
        btnRegister.setBackground(new Color(13, 110, 253));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // link dang nhap
        JLabel lblLogin = new JLabel("<html>Already have an account? <font color='#0d6efd'>Log in</font></html>");
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // rap cac thanh phan
        formPanel.add(lblLogo);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(lblTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(lblSubTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(inputPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(btnRegister);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(lblLogin);

        add(formPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(Color.DARK_GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ham test giao dien
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Register");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 750);
            frame.add(new RegisterPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}