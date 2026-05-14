package org.jobportal.view.common;

import org.jobportal.bll.impl.AuthService;
import org.jobportal.bll.interfaces.IAuthService;
import org.jobportal.enums.Role;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPanel extends JPanel {

    private MainFrame mainFrame;
    private final IAuthService authService = new AuthService();

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel nameHeader = new JPanel(new BorderLayout());
        nameHeader.setBackground(Color.WHITE);
        nameHeader.add(createLabel("USERNAME"), BorderLayout.WEST);
        nameHeader.setPreferredSize(new Dimension(370, 20));
        nameHeader.setMaximumSize(new Dimension(370, 20));
        nameHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(nameHeader);

        JTextField txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(370, 35));
        txtUsername.setMaximumSize(new Dimension(370, 35));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(txtUsername);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel emailHeader = new JPanel(new BorderLayout());
        emailHeader.setBackground(Color.WHITE);
        emailHeader.add(createLabel("EMAIL ADDRESS"), BorderLayout.WEST);
        emailHeader.setPreferredSize(new Dimension(370, 20));
        emailHeader.setMaximumSize(new Dimension(370, 20));
        emailHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(emailHeader);

        JTextField txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(370, 35));
        txtEmail.setMaximumSize(new Dimension(370, 35));
        txtEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(txtEmail);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // chia cot cho mat khau
        JPanel passPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        passPanel.setBackground(Color.WHITE);
        passPanel.setPreferredSize(new Dimension(370, 50));
        passPanel.setMaximumSize(new Dimension(370, 50));
        passPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel roleHeader = new JPanel(new BorderLayout());
        roleHeader.setBackground(Color.WHITE);
        roleHeader.add(createLabel("LOẠI TÀI KHOẢN"), BorderLayout.WEST);
        roleHeader.setPreferredSize(new Dimension(370, 20));
        roleHeader.setMaximumSize(new Dimension(370, 20));
        roleHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(roleHeader);

        JComboBox<String> cbRole = new JComboBox<>(new String[]{"Ứng viên", "Nhà tuyển dụng"});
        cbRole.setPreferredSize(new Dimension(370, 35));
        cbRole.setMaximumSize(new Dimension(370, 35));
        cbRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        cbRole.setBackground(Color.WHITE);
        cbRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputPanel.add(cbRole);

        // nut dang ky
        JButton btnRegister = new JButton("Sign Up");
        btnRegister.setBackground(new Color(13, 110, 253));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setPreferredSize(new Dimension(370, 40));
        btnRegister.setMaximumSize(new Dimension(370, 40));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        // Event listeners
        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword());
            String confirm = new String(txtConfirm.getPassword());

            if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!pass.equals(confirm)) {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Role selectedRole = cbRole.getSelectedIndex() == 0 ? Role.CANDIDATE : Role.EMPLOYER;

            boolean registered = authService.register(username, email, pass, confirm, selectedRole);

            if (registered) {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", "Success", JOptionPane.INFORMATION_MESSAGE);
                if (mainFrame != null) {
                    mainFrame.showLogin();
                }
            } else {
                String message = authService.getLastErrorMessage();
                if (message == null || message.isBlank()) {
                    message = "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin.";
                }
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainFrame != null) {
                    mainFrame.showLogin();
                }
            }
        });
    }

    public RegisterPanel() {
        this(null);
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
