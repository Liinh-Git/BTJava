package org.jobportal.view.common;

import org.jobportal.enums.Role;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class SidebarPanel extends JPanel {

    private static final Color BG_DEFAULT = new Color(248, 249, 250);
    private static final Color BG_HOVER = new Color(230, 240, 255);
    private static final Color BG_ACTIVE = new Color(222, 235, 255);
    private static final Color FG_DEFAULT = new Color(50, 50, 50);
    private static final Color FG_ACTIVE = new Color(13, 110, 253);

    private final JPanel menuContainer;
    private final Map<String, JButton> menuButtons = new LinkedHashMap<>();
    private String activeMenuTitle;

    public interface MenuSelectionListener {
        void onMenuSelected(String menuTitle);
    }

    private MenuSelectionListener menuListener;

    public void setMenuListener(MenuSelectionListener listener) {
        this.menuListener = listener;
    }

    public SidebarPanel(Role role) {
        setLayout(new BorderLayout());
        setBackground(BG_DEFAULT);
        setPreferredSize(new Dimension(220, 0)); // giam be ngang sidebar
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        menuContainer = new JPanel();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.setBackground(BG_DEFAULT);
        menuContainer.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        buildMenu(role);
        add(menuContainer, BorderLayout.CENTER);
    }

    public void buildMenu(Role role) {
        menuContainer.removeAll();
        menuButtons.clear();
        activeMenuTitle = null;

        if (role == Role.CANDIDATE) {
            addMenuItem("Tìm việc");
            addMenuItem("Đã ứng tuyển");
            addMenuItem("Quản lý CV");
            addMenuItem("Thông tin người dùng");
        } else if (role == Role.EMPLOYER) {
            addMenuItem("Tổng quan");
            addMenuItem("Đăng tin tuyển dụng");
            addMenuItem("Quản lý tin tuyển dụng");
            addMenuItem("Danh sách các ứng viên");
            addMenuItem("Thông tin công ty");
            addMenuItem("Thông tin người dùng");
        } else if (role == Role.ADMIN) {
            addMenuItem("Thống kê hệ thống");
            addMenuItem("Quản lý người dùng");
            addMenuItem("Quản lý danh mục");
            addMenuItem("Kiểm duyệt tin tuyển dụng");
        }

        menuContainer.add(Box.createVerticalGlue());
        menuContainer.add(createLogoutButton("Đăng xuất"));

        menuContainer.revalidate();
        menuContainer.repaint();
    }

    public void setActiveMenu(String menuTitle) {
        activeMenuTitle = menuTitle;
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            applyMenuButtonStyle(entry.getValue(), entry.getKey().equals(menuTitle));
        }
    }

    private JButton addMenuItem(String title) {
        JButton btn = createMenuButton(title);
        menuButtons.put(title, btn);
        menuContainer.add(btn);
        menuContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        return btn;
    }

    private JButton createMenuButton(String title) {
        JButton btn = new JButton(title);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        btn.setPreferredSize(new Dimension(0, 56)); // tang chieu cao item
        btn.setBackground(BG_DEFAULT);
        btn.setForeground(FG_DEFAULT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 14)); // tang padding

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!title.equals(activeMenuTitle)) {
                    btn.setBackground(BG_HOVER);
                    btn.setForeground(FG_ACTIVE);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                applyMenuButtonStyle(btn, title.equals(activeMenuTitle));
            }
        });

        btn.addActionListener(e -> {
            setActiveMenu(title);
            if (menuListener != null) {
                menuListener.onMenuSelected(title);
            }
        });

        return btn;
    }

    private JButton createLogoutButton(String title) {
        JButton btn = new JButton(title);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        btn.setPreferredSize(new Dimension(0, 56));
        btn.setBackground(BG_DEFAULT);
        btn.setForeground(new Color(220, 53, 69));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 14));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(255, 235, 235));
                btn.setForeground(new Color(200, 35, 51));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_DEFAULT);
                btn.setForeground(new Color(220, 53, 69));
            }
        });

        btn.addActionListener(e -> {
            if (menuListener != null) {
                menuListener.onMenuSelected(title);
            }
        });

        return btn;
    }

    private void applyMenuButtonStyle(JButton btn, boolean isActive) {
        if (isActive) {
            btn.setBackground(BG_ACTIVE);
            btn.setForeground(FG_ACTIVE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        } else {
            btn.setBackground(BG_DEFAULT);
            btn.setForeground(FG_DEFAULT);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
    }
}
