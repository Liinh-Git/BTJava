package org.jobportal.view.admin;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.UserService;
import org.jobportal.bll.interfaces.IUserService;
import org.jobportal.dto.UserDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserManagementPanel extends JPanel {

    private final IUserService userService = new UserService();
    private JPanel tableContainer;
    private JLabel lblCount;

    public UserManagementPanel() {
        // thiet lap layout chinh
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. tieu de trang va nut them
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. khu vuc loc (filter)
        mainContent.add(createFilterSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. bang du lieu
        tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableContainer.setBorder(new LineBorder(new Color(226, 230, 234), 1));
        
        mainContent.add(tableContainer);
        
        loadData();

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(248, 249, 250));

        JLabel lblTitle = new JLabel("Quản lý người dùng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 37, 41));

        JLabel lblSub = new JLabel("Manage administrative access and user permissions across the system.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(108, 117, 125));

        leftPanel.add(lblTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(lblSub);

        headerPanel.add(leftPanel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createFilterSection() {
        // su dung gridbaglayout hoac don gian la boxlayout/flowlayout
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filterPanel.setBackground(new Color(248, 249, 250));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // thanh tim kiem
        JTextField txtSearch = new JTextField("Filter by name or email...");
        txtSearch.setPreferredSize(new Dimension(350, 42));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(5, 15, 5, 15)
        ));

        // combobox role
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"Role: All", "ADMIN", "EMPLOYER", "CANDIDATE"});
        cbRole.setPreferredSize(new Dimension(200, 42));
        cbRole.setBackground(Color.WHITE);

        // combobox status
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Status: All", "Active", "Inactive"});
        cbStatus.setPreferredSize(new Dimension(200, 42));
        cbStatus.setBackground(Color.WHITE);

        filterPanel.add(txtSearch);
        filterPanel.add(cbRole);
        filterPanel.add(cbStatus);

        return filterPanel;
    }

    private void loadData() {
        tableContainer.removeAll();
        // header
        tableContainer.add(createTableRow("TÊN", "EMAIL", "VAI TRÒ", "TRẠNG THÁI", "THAO TÁC", true, "", null, null));

        List<UserDTO> users = userService.getAllUsers(null, null);
        
        Color[] bgColors = {
            new Color(220, 230, 255), new Color(240, 240, 245), 
            new Color(255, 235, 235), new Color(226, 232, 240)
        };
        
        int colorIdx = 0;
        for (UserDTO user : users) {
            String initials = user.getUsername().length() >= 2 ? user.getUsername().substring(0, 2).toUpperCase() : "U";
            tableContainer.add(createTableRow(user.getUsername(), user.getEmail(), 
                                                user.getRole().name(), 
                                                user.isActive() ? "Active" : "Inactive", 
                                                "", false, initials, bgColors[colorIdx % 4], user));
            colorIdx++;
        }

        // phan trang
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));

        if (lblCount == null) {
            lblCount = new JLabel();
            lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblCount.setForeground(Color.GRAY);
        }
        lblCount.setText("Showing " + users.size() + " users");
        footer.add(lblCount, BorderLayout.WEST);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pagination.setBackground(Color.WHITE);
        pagination.add(createPageBtn("1", true));
        footer.add(pagination, BorderLayout.EAST);

        tableContainer.add(footer);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private JPanel createTableRow(String col1, String col2, String role, String status, String action, boolean isHeader, String initials, Color avatarBg, UserDTO user) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? new Color(248, 249, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setPreferredSize(new Dimension(0, 70));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 14);
        Color textColor = isHeader ? new Color(108, 117, 125) : new Color(33, 37, 41);

        // Name
        gbc.gridx = 0; gbc.weightx = 0.20; // 20%
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        p1.setOpaque(false);
        p1.setMinimumSize(new Dimension(10, 10)); // Force alignment
        if (isHeader) {
            JLabel l1 = new JLabel(col1);
            l1.setFont(font); l1.setForeground(textColor);
            p1.add(l1);
        } else {
            p1.add(createAvatar(initials, avatarBg));
            JLabel l1 = new JLabel(col1);
            l1.setFont(new Font("Segoe UI", Font.BOLD, 14));
            p1.add(l1);
        }
        row.add(p1, gbc);

        // Email
        gbc.gridx = 1; gbc.weightx = 0.30; // 30%
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 25));
        p2.setOpaque(false);
        p2.setMinimumSize(new Dimension(10, 10)); // Force alignment
        JLabel l2 = new JLabel(col2);
        l2.setFont(isHeader ? font : new Font("Segoe UI", Font.PLAIN, 14));
        l2.setForeground(isHeader ? textColor : Color.DARK_GRAY);
        p2.add(l2);
        row.add(p2, gbc);

        // Role
        gbc.gridx = 2; gbc.weightx = 0.15;
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 25));
        p3.setOpaque(false);
        p3.setMinimumSize(new Dimension(10, 10)); // Force alignment
        if (isHeader) {
            JLabel l3 = new JLabel(role);
            l3.setFont(font); l3.setForeground(textColor);
            p3.add(l3);
        } else {
            p3.add(createRoleBadge(role));
        }
        row.add(p3, gbc);

        // Status
        gbc.gridx = 3; gbc.weightx = 0.10; // 10%
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 25));
        p4.setOpaque(false);
        p4.setMinimumSize(new Dimension(10, 10)); // Force alignment
        if (isHeader) {
            JLabel l4 = new JLabel(status);
            l4.setFont(font); l4.setForeground(textColor);
            p4.add(l4);
        } else {
            p4.add(createStatusLabel(status));
        }
        row.add(p4, gbc);

        // Actions
        gbc.gridx = 4; gbc.weightx = 0.25; // 25% (Kéo dài cột này)
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20)); // Adjusted spacing
        p5.setOpaque(false);
        p5.setMinimumSize(new Dimension(10, 10)); // Force alignment
        if (isHeader) {
            JLabel l5 = new JLabel(action);
            l5.setFont(font); l5.setForeground(textColor);
            p5.add(l5);
        } else {
            JButton btnEdit = new JButton(status.equals("Active") ? "Khóa" : "Mở khóa");
            btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 11)); // Nho font chu xuong 1 ti
            btnEdit.setMargin(new Insets(4, 10, 4, 10)); // Thu nho padding cua nut
            btnEdit.setBackground(status.equals("Active") ? new Color(255, 193, 7) : new Color(40, 167, 69)); // Warning or Success
            btnEdit.setForeground(Color.WHITE);
            btnEdit.setFocusPainted(false);
            btnEdit.setBorderPainted(false);
            btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEdit.addActionListener(e -> {
                if (user != null) {
                    int choice = JOptionPane.showConfirmDialog(UserManagementPanel.this, 
                        (user.isActive() ? "Khóa" : "Mở khóa") + " tài khoản này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        boolean success = userService.updateUserStatus(user.getUserId(), !user.isActive());
                        if (success) {
                            JOptionPane.showMessageDialog(UserManagementPanel.this, "Đã cập nhật trạng thái!");
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(UserManagementPanel.this, "Không thể cập nhật trạng thái!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            JButton btnDelete = new JButton("Xóa");
            btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 11)); // Nho font chu xuong 1 ti
            btnDelete.setMargin(new Insets(4, 10, 4, 10)); // Thu nho padding cua nut
            btnDelete.setBackground(new Color(220, 53, 69)); // Danger red
            btnDelete.setForeground(Color.WHITE);
            btnDelete.setFocusPainted(false);
            btnDelete.setBorderPainted(false);
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDelete.addActionListener(e -> {
                if (user != null) {
                    int choice = JOptionPane.showConfirmDialog(UserManagementPanel.this, "Xóa tài khoản này vĩnh viễn?", "Cảnh báo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (choice == JOptionPane.YES_OPTION) {
                        boolean success = userService.deleteUser(user.getUserId());
                        if (success) {
                            JOptionPane.showMessageDialog(UserManagementPanel.this, "Đã xóa tài khoản!");
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(UserManagementPanel.this, "Không thể xóa tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            p5.add(btnEdit);
            p5.add(btnDelete);
        }
        row.add(p5, gbc);

        return row;
    }

    private JPanel createAvatar(String initials, Color bg) {
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(35, 35));
        avatar.setLayout(new GridBagLayout());
        JLabel lbl = new JLabel(initials);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(50, 60, 80));
        avatar.add(lbl);
        return avatar;
    }

    private JLabel createRoleBadge(String role) {
        JLabel badge = new JLabel(" " + role + " ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setOpaque(true);
        badge.setBackground(new Color(241, 245, 249)); // light gray
        badge.setForeground(new Color(100, 116, 139));
        badge.setBorder(new EmptyBorder(4, 6, 4, 6));
        return badge;
    }

    private JLabel createStatusLabel(String status) {
        String colorHex;
        if (status.equals("Active")) {
            colorHex = "#28a745"; // green
        } else if (status.equals("Suspended")) {
            colorHex = "#dc3545"; // red
        } else {
            colorHex = "#6c757d"; // grey
        }

        String html = "<html><font color='" + colorHex + "'>●</font> " + status + "</html>";
        JLabel lbl = new JLabel(html);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }

    private JButton createPageBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (active) {
            btn.setBackground(new Color(240, 245, 255));
            btn.setForeground(new Color(13, 110, 253));
            btn.setBorder(new LineBorder(new Color(13, 110, 253), 1));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.DARK_GRAY);
            if (text.equals("...")) {
                btn.setBorderPainted(false);
            } else {
                btn.setBorder(new LineBorder(new Color(226, 230, 234), 1));
            }
        }
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Portal - User Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 850);
            frame.setLayout(new BorderLayout());

            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.ADMIN);
            frame.add(sidebar, BorderLayout.WEST);

            UserManagementPanel userPanel = new UserManagementPanel();
            frame.add(userPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}