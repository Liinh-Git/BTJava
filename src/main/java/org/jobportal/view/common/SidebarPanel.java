package org.jobportal.view.common;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {

    // gia lap enum Role de test, khi ghep code hay dung org.jobportal.enums.Role
    public enum Role {
        ADMIN, EMPLOYER, CANDIDATE
    }

    private JPanel menuContainer;

    public SidebarPanel(Role role) {
        // khoi tao layout chinh cho sidebar
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250)); // mau nen nhe giong tren hinh
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        // container chua cac nut menu
        menuContainer = new JPanel();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.setBackground(new Color(248, 249, 250));
        menuContainer.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // tao menu dua tren quyen
        buildMenu(role);

        add(menuContainer, BorderLayout.NORTH);
    }

    // ham xu ly logic hien thi if-else theo role
    public void buildMenu(Role role) {
        menuContainer.removeAll(); // xoa menu cu neu co

        if (role == Role.CANDIDATE) {
            // hien thi menu cua ung vien tim viec
            menuContainer.add(createMenuButton("Tìm việc"));
            menuContainer.add(createMenuButton("Đã ứng tuyển"));
            menuContainer.add(createMenuButton("Quản lý CV"));
            menuContainer.add(createMenuButton("Thông tin người dùng"));

        } else if (role == Role.EMPLOYER) {
            // hien thi menu cua nha tuyen dung
            menuContainer.add(createMenuButton("Tổng quan"));
            menuContainer.add(createMenuButton("Đăng tin tuyển dụng"));
            menuContainer.add(createMenuButton("Quản lý tin tuyển dụng"));
            menuContainer.add(createMenuButton("Danh sách các ứng viên"));
            menuContainer.add(createMenuButton("Thông tin công ty"));
            menuContainer.add(createMenuButton("Thông tin người dùng"));

        } else if (role == Role.ADMIN) {
            // hien thi menu cua he thong quan tri
            menuContainer.add(createMenuButton("Thống kê hệ thống"));
            menuContainer.add(createMenuButton("Quản lý người dùng"));
            menuContainer.add(createMenuButton("Quản lý danh mục"));
            menuContainer.add(createMenuButton("Kiểm duyệt tin tuyển dụng"));
        }

        // cap nhat lai giao dien sau khi them nut
        menuContainer.revalidate();
        menuContainer.repaint();
    }

    // ham ho tro tao nut menu voi UI custom
    private JButton createMenuButton(String title) {
        JButton btn = new JButton(title);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // chieu cao nut
        btn.setBackground(new Color(248, 249, 250));
        btn.setForeground(new Color(50, 50, 50));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // bo vien de nhin giong menu web
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // padding text

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