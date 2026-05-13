package org.jobportal.view.common;

import javax.swing.*;
import java.awt.*;

import org.jobportal.enums.Role;

public class SidebarPanel extends JPanel {

    private JPanel menuContainer;

    public interface MenuSelectionListener {
        void onMenuSelected(String menuTitle);
    }
    
    private MenuSelectionListener menuListener;

    public void setMenuListener(MenuSelectionListener listener) {
        this.menuListener = listener;
    }

    public SidebarPanel(Role role) {
        // khoi tao layout chinh cho sidebar
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250)); // mau nen nhe giong tren hinh
        setPreferredSize(new Dimension(260, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        // container chua cac nut menu
        menuContainer = new JPanel();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.setBackground(new Color(248, 249, 250));
        menuContainer.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // tao menu dua tren quyen
        buildMenu(role);

        add(menuContainer, BorderLayout.CENTER);
    }

    // ham xu ly logic hien thi if-else theo role
    public void buildMenu(Role role) {
        menuContainer.removeAll(); // xoa menu cu neu co

        if (role == Role.CANDIDATE) {
            // hien thi menu cua ung vien tim viec
            addMenuItem("Tìm việc");
            addMenuItem("Đã ứng tuyển");
            addMenuItem("Quản lý CV");
            addMenuItem("Thông tin người dùng");

        } else if (role == Role.EMPLOYER) {
            // hien thi menu cua nha tuyen dung
            addMenuItem("Tổng quan");
            addMenuItem("Đăng tin tuyển dụng");
            addMenuItem("Quản lý tin tuyển dụng");
            addMenuItem("Danh sách các ứng viên");
            addMenuItem("Thông tin công ty");
            addMenuItem("Thông tin người dùng");

        } else if (role == Role.ADMIN) {
            // hien thi menu cua he thong quan tri
            addMenuItem("Thống kê hệ thống");
            addMenuItem("Quản lý người dùng");
            addMenuItem("Quản lý danh mục");
            addMenuItem("Kiểm duyệt tin tuyển dụng");
        }

        // Đẩy nút Đăng xuất xuống dưới cùng
        menuContainer.add(Box.createVerticalGlue());
        
        JButton btnLogout = createMenuButton("Đăng xuất");
        // Ghi đè màu chữ đỏ cho nút đăng xuất (tuỳ chọn)
        btnLogout.setForeground(new Color(220, 53, 69));
        // Thêm sự kiện đăng xuất
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(255, 235, 235));
                btnLogout.setForeground(new Color(200, 35, 51));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(248, 249, 250));
                btnLogout.setForeground(new Color(220, 53, 69));
            }
        });
        menuContainer.add(btnLogout);

        // cap nhat lai giao dien sau khi them nut
        menuContainer.revalidate();
        menuContainer.repaint();
    }

    // ham ho tro them menu item voi khoang cach
    private JButton addMenuItem(String title) {
        JButton btn = createMenuButton(title);
        menuContainer.add(btn);
        menuContainer.add(Box.createRigidArea(new Dimension(0, 10))); // khoang cach cac muc
        return btn;
    }

    // ham ho tro tao nut menu voi UI custom
    private JButton createMenuButton(String title) {
        JButton btn = new JButton(title);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // chieu cao nut
        btn.setBackground(new Color(248, 249, 250));
        btn.setForeground(new Color(50, 50, 50));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // bo vien de nhin giong menu web
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // padding text

        // them hieu ung hover (doi mau nen va chu)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(230, 240, 255));
                btn.setForeground(new Color(0, 82, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(248, 249, 250));
                btn.setForeground(new Color(50, 50, 50));
            }
        });

        // xu ly su kien click chuyen tab
        btn.addActionListener(e -> {
            if (menuListener != null) {
                menuListener.onMenuSelected(title);
            }
        });

        return btn;
    }

    // ham main de test giao dien ngay tai cho
    public static void main(String[] args) {
        // dam bao UI chay tren EDT
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Kiem tra Sidebar Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLayout(new BorderLayout());

            // khoi tao sidebar voi quyen Candidate mac dinh
            SidebarPanel sidebar = new SidebarPanel(Role.CANDIDATE);
            frame.add(sidebar, BorderLayout.WEST);

            // panel chua cac nut phan quyen de kiem tra viec ve lai menu
            JPanel mainContentPanel = new JPanel();
            mainContentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));
            mainContentPanel.add(new JLabel("Chon role de kiem tra logic doi menu: "));

            JButton btnCandidate = new JButton("Load Candidate Menu");
            btnCandidate.addActionListener(e -> sidebar.buildMenu(Role.CANDIDATE));

            JButton btnEmployer = new JButton("Load Employer Menu");
            btnEmployer.addActionListener(e -> sidebar.buildMenu(Role.EMPLOYER));

            JButton btnAdmin = new JButton("Load Admin Menu");
            btnAdmin.addActionListener(e -> sidebar.buildMenu(Role.ADMIN));

            mainContentPanel.add(btnCandidate);
            mainContentPanel.add(btnEmployer);
            mainContentPanel.add(btnAdmin);

            frame.add(mainContentPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}