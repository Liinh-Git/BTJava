package org.jobportal.view.common;

import org.jobportal.bll.impl.AuthService;
import org.jobportal.bll.impl.UserService;
import org.jobportal.bll.interfaces.IAuthService;
import org.jobportal.bll.interfaces.IUserService;
import org.jobportal.dto.UserDTO;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UserProfilePanel extends JPanel {

    private final IUserService userService = new UserService();
    private final IAuthService authService = new AuthService();

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JTextField txtFullName;
    private JTextField txtPhone;

    private JPasswordField txtOldPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;

    public UserProfilePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        mainContent.add(createProfileCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        mainContent.add(createProfileActions());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        mainContent.add(createPasswordCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));

        mainContent.add(createPasswordActions());

        loadData();

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        UserDTO user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        txtUsername.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        txtFullName.setText(user.getFullName());
        txtPhone.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
    }

    private JPanel createPageHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Thong tin nguoi dung");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 37, 41));

        JPanel underline = new JPanel();
        underline.setBackground(new Color(13, 110, 253));
        underline.setPreferredSize(new Dimension(50, 4));
        underline.setMaximumSize(new Dimension(50, 4));
        underline.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        headerPanel.add(underline);

        return headerPanel;
    }

    private JPanel createProfileCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel sectionTitle = new JLabel("Thong tin ca nhan");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sectionTitle);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel usernamePanel = createInputGroup("USERNAME", false);
        txtUsername = (JTextField) usernamePanel.getComponent(2);
        txtUsername.setEditable(false);
        txtUsername.setBackground(new Color(245, 247, 250));
        card.add(usernamePanel);
        card.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel emailPanel = createInputGroup("EMAIL", false);
        txtEmail = (JTextField) emailPanel.getComponent(2);
        txtEmail.setEditable(false);
        txtEmail.setBackground(new Color(245, 247, 250));
        card.add(emailPanel);
        card.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel namePanel = createInputGroup("TEN HIEN THI", false);
        txtFullName = (JTextField) namePanel.getComponent(2);
        card.add(namePanel);
        card.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel phonePanel = createInputGroup("SO DIEN THOAI", false);
        txtPhone = (JTextField) phonePanel.getComponent(2);
        card.add(phonePanel);

        return card;
    }

    private JPanel createProfileActions() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(new Color(248, 249, 250));
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancel = new JButton("Huy");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(new Color(33, 37, 41));
        btnCancel.setBorder(new LineBorder(new Color(226, 230, 234), 1));
        btnCancel.setPreferredSize(new Dimension(100, 45));
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> loadData());

        JButton btnSave = new JButton("Luu thong tin");
        btnSave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSave.setBackground(new Color(13, 110, 253));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorderPainted(false);
        btnSave.setPreferredSize(new Dimension(140, 45));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> {
            boolean success = userService.updateUserProfile(
                    txtFullName.getText().trim(),
                    txtPhone.getText().trim()
            );
            if (success) {
                JOptionPane.showMessageDialog(this, "Cap nhat thong tin thanh cong!", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Khong the cap nhat thong tin!", "Loi", JOptionPane.ERROR_MESSAGE);
            }
        });

        actionPanel.add(btnCancel);
        actionPanel.add(btnSave);
        return actionPanel;
    }

    private JPanel createPasswordCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel sectionTitle = new JLabel("Doi mat khau");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sectionTitle);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel oldPassPanel = createInputGroup("MAT KHAU CU", true);
        txtOldPassword = (JPasswordField) oldPassPanel.getComponent(2);
        card.add(oldPassPanel);
        card.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel newPassPanel = createInputGroup("MAT KHAU MOI", true);
        txtNewPassword = (JPasswordField) newPassPanel.getComponent(2);
        card.add(newPassPanel);
        card.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel confirmPanel = createInputGroup("XAC NHAN MAT KHAU", true);
        txtConfirmPassword = (JPasswordField) confirmPanel.getComponent(2);
        card.add(confirmPanel);

        return card;
    }

    private JPanel createPasswordActions() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(new Color(248, 249, 250));
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnChangePassword = new JButton("Doi mat khau");
        btnChangePassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnChangePassword.setBackground(new Color(13, 110, 253));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setBorderPainted(false);
        btnChangePassword.setPreferredSize(new Dimension(140, 45));
        btnChangePassword.setFocusPainted(false);
        btnChangePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChangePassword.addActionListener(e -> handleChangePassword());

        actionPanel.add(btnChangePassword);
        return actionPanel;
    }

    private void handleChangePassword() {
        String oldPass = new String(txtOldPassword.getPassword());
        String newPass = new String(txtNewPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long nhap day du thong tin mat khau!", "Canh bao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = authService.changePassword(oldPass, newPass, confirmPass);
        if (success) {
            JOptionPane.showMessageDialog(this, "Doi mat khau thanh cong!", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
            txtOldPassword.setText("");
            txtNewPassword.setText("");
            txtConfirmPassword.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Khong the doi mat khau!", "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createInputGroup(String label, boolean isPassword) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(108, 117, 125));

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        if (isPassword) {
            JPasswordField field = new JPasswordField();
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            field.setPreferredSize(new Dimension(0, 45));
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(226, 230, 234), 1),
                    new EmptyBorder(5, 15, 5, 15)
            ));
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(field);
        } else {
            JTextField field = new JTextField();
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            field.setPreferredSize(new Dimension(0, 45));
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(226, 230, 234), 1),
                    new EmptyBorder(5, 15, 5, 15)
            ));
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(field);
        }

        return panel;
    }
}
