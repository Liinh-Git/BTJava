package org.jobportal.view.admin;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AdminDashboardPanel extends JPanel {

    public AdminDashboardPanel() {
        // thiet lap layout chinh cho toan trang
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        // tao panel chua noi dung co the cuon
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. tieu de trang
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. cac the thong ke
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // 3. nhat ky hoat dong he thong (system logs)
        mainContent.add(createSystemLogsSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // 4. bang top nha tuyen dung
        mainContent.add(createTopEmployersTable());

        // dua vao scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Thống kê hệ thống");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 37, 41));

        JLabel lblSub = new JLabel("Giám sát hiệu suất và các chỉ số hoạt động thời gian thực của nền tảng.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(108, 117, 125));

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(lblSub);

        return headerPanel;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(new Color(248, 249, 250));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        panel.add(createStatCard("O", "TỔNG ỨNG VIÊN", "1,250", "+12%", new Color(230, 250, 240), new Color(40, 167, 69)));
        panel.add(createStatCard("B", "NHÀ TUYỂN DỤNG", "45", "+5%", new Color(230, 250, 240), new Color(40, 167, 69)));
        panel.add(createStatCard("V", "TIN ĐĂNG MỞ (OPEN)", "320", "Current", new Color(230, 240, 255), new Color(13, 110, 253)));

        return panel;
    }

    private JPanel createStatCard(String iconTxt, String label, String value, String badgeTxt, Color badgeBg, Color badgeFg) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // dong dau: icon va badge
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel lblIcon = new JLabel(" " + iconTxt + " ");
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblIcon.setOpaque(true);
        lblIcon.setBackground(new Color(240, 245, 255));
        lblIcon.setForeground(new Color(13, 110, 253));
        topPanel.add(lblIcon, BorderLayout.WEST);

        JLabel lblBadge = new JLabel(" " + badgeTxt + " ");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblBadge.setOpaque(true);
        lblBadge.setBackground(badgeBg);
        lblBadge.setForeground(badgeFg);
        topPanel.add(lblBadge, BorderLayout.EAST);

        // dong duoi: ten va gia tri
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLabel.setForeground(new Color(108, 117, 125));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setForeground(Color.BLACK);

        bottomPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        bottomPanel.add(lblLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bottomPanel.add(lblValue);

        card.add(topPanel);
        card.add(bottomPanel);
        return card;
    }

    private JPanel createSystemLogsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setBorder(new LineBorder(new Color(226, 230, 234), 1));

        // header cua section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("Hoạt động gần đây (System Logs)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblViewAll = new JLabel("Xem tất cả");
        lblViewAll.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblViewAll.setForeground(new Color(13, 110, 253));
        lblViewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblViewAll, BorderLayout.EAST);
        section.add(headerPanel);

        // danh sach log
        section.add(createLogItem(new Color(13, 110, 253), "<html><b>FPT Software</b> vừa đăng tin <font color='#0d6efd'>\"Java Backend Developer\"</font></html>", "Vừa xong • Tuyển dụng"));
        section.add(createLogItem(new Color(200, 100, 50), "<html>Ứng viên <b>Trần Văn B</b> vừa tạo tài khoản mới</html>", "10 phút trước • Người dùng mới"));
        section.add(createLogItem(new Color(40, 167, 69), "<html><b>VNG Corp</b> đã cập nhật trạng thái hồ sơ của ứng viên Lê Mỹ Linh</html>", "25 phút trước • Tuyển dụng"));
        section.add(createLogItem(Color.GRAY, "<html>Hệ thống đã tự động đóng tin <i>\"PHP Senior\"</i> do hết hạn</html>", "1 giờ trước • Tự động"));
        section.add(createLogItem(new Color(13, 110, 253), "<html><b>Viettel Group</b> vừa đăng 5 tin tuyển dụng mới</html>", "2 giờ trước • Tuyển dụng"));

        return section;
    }

    private JPanel createLogItem(Color dotColor, String text, String subtext) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // icon cham mau
        JPanel dotPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(dotColor);
                g2.fillOval(5, 5, 8, 8);
            }
        };
        dotPanel.setPreferredSize(new Dimension(20, 20));
        dotPanel.setBackground(Color.WHITE);
        item.add(dotPanel, BorderLayout.WEST);

        // noi dung log
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblText.setForeground(Color.DARK_GRAY);

        JLabel lblSub = new JLabel(subtext);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);

        textPanel.add(lblText);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(lblSub);

        item.add(textPanel, BorderLayout.CENTER);
        return item;
    }

    private JPanel createTopEmployersTable() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(248, 249, 250));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        // title phan bang
        JLabel lblTitle = new JLabel("Top Nhà Tuyển Dụng Nổi Bật");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblSub = new JLabel("DỰA TRÊN SỐ LƯỢNG TIN ĐĂNG ĐANG MỞ");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);

        container.add(lblTitle);
        container.add(lblSub);
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        // bang du lieu
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new LineBorder(new Color(226, 230, 234), 1));

        tablePanel.add(createTableRow("CÔNG TY", "NGÀNH NGHỀ", "SỐ TIN ĐANG MỞ", "TỈ LỆ PHẢN HỒI", true));
        tablePanel.add(createTableRow("FPT Software", "Công nghệ thông tin", "24", "92%", false));
        tablePanel.add(createTableRow("VNG Corp", "Giải trí & Phần mềm", "18", "88%", false));
        tablePanel.add(createTableRow("Techcombank", "Tài chính / Ngân hàng", "15", "95%", false));

        container.add(tablePanel);
        return container;
    }

    private JPanel createTableRow(String col1, String col2, String col3, String col4, boolean isHeader) {
        JPanel row = new JPanel(new GridLayout(1, 4));
        row.setBackground(isHeader ? new Color(248, 249, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setPreferredSize(new Dimension(0, 50));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.DARK_GRAY;

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        p1.setOpaque(false);
        JLabel l1 = new JLabel(col1); l1.setFont(font); l1.setForeground(isHeader ? textColor : Color.BLACK);
        p1.add(l1);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2); l2.setFont(font); l2.setForeground(textColor);
        p2.add(l2);

        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        p3.setOpaque(false);
        JLabel l3 = new JLabel(col3); l3.setFont(font); l3.setForeground(textColor);
        p3.add(l3);

        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        p4.setOpaque(false);
        JLabel l4 = new JLabel(col4); l4.setFont(font);
        l4.setForeground(isHeader ? textColor : new Color(40, 167, 69)); // mau xanh cho ti le phan hoi
        p4.add(l4);

        row.add(p1); row.add(p2); row.add(p3); row.add(p4);
        return row;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Portal - Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 900);
            frame.setLayout(new BorderLayout());

            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            SidebarPanel sidebar = new SidebarPanel(SidebarPanel.Role.ADMIN);
            frame.add(sidebar, BorderLayout.WEST);

            AdminDashboardPanel dashboardPanel = new AdminDashboardPanel();
            frame.add(dashboardPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}