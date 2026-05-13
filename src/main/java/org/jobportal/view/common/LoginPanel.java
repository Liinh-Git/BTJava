package org.jobportal.view.common;

import org.jobportal.bll.impl.AuthService;
import org.jobportal.bll.interfaces.IAuthService;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.Role;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {

    private MainFrame mainFrame;
    private final IAuthService authService = new AuthService();


    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
        rolePanel.setPreferredSize(new Dimension(370, 45));
        rolePanel.setMaximumSize(new Dimension(370, 45));
        rolePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel userHeaderPanel = new JPanel(new BorderLayout());
        userHeaderPanel.setBackground(Color.WHITE);
        userHeaderPanel.add(createLabel("USERNAME"), BorderLayout.WEST);
        userHeaderPanel.setPreferredSize(new Dimension(370, 20));
        userHeaderPanel.setMaximumSize(new Dimension(370, 20));
        userHeaderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(userHeaderPanel);

        JTextField txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(370, 35));
        txtUsername.setMaximumSize(new Dimension(370, 35));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(txtUsername);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel passHeaderPanel = new JPanel(new BorderLayout());
        passHeaderPanel.setBackground(Color.WHITE);
        passHeaderPanel.add(createLabel("PASSWORD"), BorderLayout.WEST);

        passHeaderPanel.setPreferredSize(new Dimension(370, 20));
        passHeaderPanel.setMaximumSize(new Dimension(370, 20));
        passHeaderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(passHeaderPanel);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(370, 35));
        txtPassword.setMaximumSize(new Dimension(370, 35));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(txtPassword);


        // nut submit
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(13, 110, 253));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(370, 40));
        btnLogin.setMaximumSize(new Dimension(370, 40));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(btnLogin);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(lblRegister);

        add(formPanel);

        // Event listeners
        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());

            Role role = Role.CANDIDATE;
            if (btnEmployer.isSelected()) {
                role = Role.EMPLOYER;
            } else if (btnAdmin.isSelected()) {
                role = Role.ADMIN;
            }

            UserDTO loggedInUser = authService.login(user, pass, role);

            if (loggedInUser != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);
                if (mainFrame != null) {
                    mainFrame.onLoginSuccess();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản, mật khẩu hoặc vai trò!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainFrame != null) {
                    mainFrame.showRegister();
                }
            }
        });
    }

    public LoginPanel() {
        this(null);
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
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
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
