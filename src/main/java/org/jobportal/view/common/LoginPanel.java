package org.jobportal.view.common;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginPanel extends JPanel {

    public LoginPanel() {
        // thiet lap layout chinh de can giua form
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        formPanel.setPreferredSize(new Dimension(450, 550));

        // logo
        JLabel lblLogo = new JLabel("JobPortal");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(new Color(13, 110, 253));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // tieu de
        JLabel lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubTitle = new JLabel("Select your role and enter your credentials");
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubTitle.setForeground(Color.GRAY);
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // chon quyen
        JPanel rolePanel = new JPanel(new GridLayout(1, 3, 10, 0));
        rolePanel.setBackground(Color.WHITE);
        rolePanel.setMaximumSize(new Dimension(400, 50));

        JToggleButton btnCandidate = createRoleButton("Candidate");
        JToggleButton btnEmployer = createRoleButton("Employer");
        JToggleButton btnAdmin = createRoleButton("Admin");

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(btnCandidate);
        roleGroup.add(btnEmployer);
        roleGroup.add(btnAdmin);
        btnCandidate.setSelected(true); // mac dinh chon

        rolePanel.add(btnCandidate);
        rolePanel.add(btnEmployer);
        rolePanel.add(btnAdmin);

        // form nhap lieu
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.add(createLabel("USERNAME"));
        JTextField txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(txtUsername);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel passHeaderPanel = new JPanel(new BorderLayout());
        passHeaderPanel.setBackground(Color.WHITE);
        passHeaderPanel.add(createLabel("PASSWORD"), BorderLayout.WEST);

        JLabel lblForgot = new JLabel("Forgot password?");
        lblForgot.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblForgot.setForeground(new Color(13, 110, 253));
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        passHeaderPanel.add(lblForgot, BorderLayout.EAST);

        passHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        inputPanel.add(passHeaderPanel);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(txtPassword);

        // nho dang nhap
        JCheckBox chkKeep = new JCheckBox("Keep me signed in");
        chkKeep.setBackground(Color.WHITE);
        chkKeep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkKeep.setForeground(Color.DARK_GRAY);

        // nut submit
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(13, 110, 253));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // chuyen den dang ky
        JLabel lblRegister = new JLabel("<html>New to JobPortal? <font color='#0d6efd'>Create an account</font></html>");
        lblRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // them cac thanh phan vao formPanel
        formPanel.add(lblLogo);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(lblTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(lblSubTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(rolePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(inputPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        checkPanel.setBackground(Color.WHITE);
        checkPanel.add(chkKeep);
        formPanel.add(checkPanel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(btnLogin);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(lblRegister);

        add(formPanel);
    }

    // ham ho tro tao nut role
    private JToggleButton createRoleButton(String text) {
        JToggleButton btn = new JToggleButton("<html><center>" + text + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }

    // ham test giao dien
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 700);
            frame.add(new LoginPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}