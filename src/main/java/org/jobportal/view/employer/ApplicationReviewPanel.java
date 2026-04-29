package org.jobportal.view.employer;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ApplicationReviewPanel extends JPanel {

    public ApplicationReviewPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. header va breadcrumb
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. the thong ke
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. bang danh sach ung vien
        mainContent.add(createCandidateTable());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 4. phan goi y AI va quang cao ben duoi
        mainContent.add(createBottomSection());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // phan text ben trai
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(248, 249, 250));

        JLabel lblTitle = new JLabel("Duyệt hồ sơ ứng viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 37, 41));
;
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(lblTitle);

        // phan nut ben phai
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        rightPanel.setBackground(new Color(248, 249, 250));

        JButton btnFilter = new JButton("= Bộ lọc");
        btnFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnFilter.setBackground(Color.WHITE);
        btnFilter.setPreferredSize(new Dimension(100, 38));
        btnFilter.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        btnFilter.setFocusPainted(false);

        JButton btnExport = new JButton("v Xuất báo cáo");
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExport.setBackground(new Color(13, 110, 253));
        btnExport.setForeground(Color.WHITE);
        btnExport.setPreferredSize(new Dimension(140, 38));
        btnExport.setBorderPainted(false);
        btnExport.setFocusPainted(false);

        rightPanel.add(btnFilter);
        rightPanel.add(btnExport);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(new Color(248, 249, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        panel.add(createStatCard("TỔNG SỐ ỨNG VIÊN", "128", "~ 12% so với tháng trước", new Color(13, 110, 253)));
        panel.add(createStatCard("ĐANG CHỜ DUYỆT", "45", null, new Color(200, 80, 20)));
        panel.add(createStatCard("ĐÃ DUYỆT", "18", null, new Color(13, 110, 253)));
        panel.add(createStatCard("TỈ LỆ CHẤP THUẬN", "14.2%", null, Color.BLACK));

        return panel;
    }

    private JPanel createStatCard(String title, String value, String subText, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(valueColor);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblValue);

        if (subText != null) {
            JLabel lblSub = new JLabel(subText);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblSub.setForeground(new Color(13, 110, 253));
            card.add(Box.createRigidArea(new Dimension(0, 5)));
            card.add(lblSub);
        }

        return card;
    }

    private JPanel createCandidateTable() {
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new LineBorder(new Color(230, 230, 230), 1));

        // header
        tableContainer.add(createTableRow("ỨNG VIÊN", "NGÀY NỘP", "VỊ TRÍ HIỆN TẠI", "TRẠNG THÁI", "THAO TÁC", true, null, null));

        // rows
        tableContainer.add(createTableRow("Nguyễn Hoàng Nam", "12/10/2023", "UI/UX Designer", "PENDING", "", false, "nam.nguyen@example.com", "NH"));
        tableContainer.add(createTableRow("Lê Thị Thu Thảo", "10/10/2023", "Senior Frontend Engineer", "REVIEWING", "", false, "thao.le@company.vn", "LT"));
        tableContainer.add(createTableRow("Trần Minh Quân", "08/10/2023", "Fullstack Developer", "HIRED", "", false, "quan.tm@techflow.io", "TM"));
        tableContainer.add(createTableRow("Phạm Văn Duy", "05/10/2023", "Lead Developer", "PENDING", "", false, "duy.pv@outlook.com", "PV"));

        // pagination footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblCount = new JLabel("Hiển thị 1 - 4 của 12 ứng viên");
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCount.setForeground(Color.GRAY);
        footer.add(lblCount, BorderLayout.WEST);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pagination.setBackground(Color.WHITE);
        pagination.add(createPageBtn("<", false));
        pagination.add(createPageBtn("1", true));
        pagination.add(createPageBtn("2", false));
        pagination.add(createPageBtn("3", false));
        pagination.add(createPageBtn(">", false));
        footer.add(pagination, BorderLayout.EAST);

        tableContainer.add(footer);
        return tableContainer;
    }

    private JPanel createTableRow(String col1, String col2, String col3, String status, String action, boolean isHeader, String email, String initials) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? new Color(248, 249, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.DARK_GRAY;

        // cot 1: Ung vien (Avatar + Name + Email)
        gbc.gridx = 0; gbc.weightx = 0.35; gbc.insets = new Insets(10, 20, 10, 10);
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        p1.setOpaque(false);
        if (isHeader) {
            JLabel l1 = new JLabel(col1);
            l1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            l1.setForeground(Color.GRAY);
            p1.add(l1);
        } else {
            p1.add(createAvatar(initials));
            JPanel namePanel = new JPanel();
            namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
            namePanel.setOpaque(false);
            JLabel lName = new JLabel(col1);
            lName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JLabel lEmail = new JLabel(email);
            lEmail.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lEmail.setForeground(Color.GRAY);
            namePanel.add(lName);
            namePanel.add(lEmail);
            p1.add(namePanel);
        }
        row.add(p1, gbc);

        // cot 2: Ngay nop
        gbc.gridx = 1; gbc.weightx = 0.15; gbc.insets = new Insets(10, 10, 10, 10);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, isHeader ? 0 : 15));
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2);
        l2.setFont(isHeader ? new Font("Segoe UI", Font.PLAIN, 11) : font);
        l2.setForeground(textColor);
        p2.add(l2);
        row.add(p2, gbc);

        // cot 3: Vi tri
        gbc.gridx = 2; gbc.weightx = 0.2;
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, isHeader ? 0 : 15));
        p3.setOpaque(false);
        JLabel l3 = new JLabel(col3);
        l3.setFont(isHeader ? new Font("Segoe UI", Font.PLAIN, 11) : font);
        l3.setForeground(textColor);
        p3.add(l3);
        row.add(p3, gbc);

        // cot 4: Trang thai
        gbc.gridx = 3; gbc.weightx = 0.15;
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, isHeader ? 0 : 15));
        p4.setOpaque(false);
        if (isHeader) {
            JLabel l4 = new JLabel(status);
            l4.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            l4.setForeground(textColor);
            p4.add(l4);
        } else {
            p4.add(createStatusBadge(status));
        }
        row.add(p4, gbc);

        // cot 5: Thao tac
        gbc.gridx = 4; gbc.weightx = 0.15;
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, isHeader ? 0 : 10));
        p5.setOpaque(false);
        if (isHeader) {
            JLabel l5 = new JLabel(action);
            l5.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            l5.setForeground(textColor);
            p5.add(l5);
        } else {
            p5.add(createActionBtn("O", new Color(13, 110, 253))); // eye
            p5.add(createActionBtn("V", new Color(160, 50, 50)));  // check
            p5.add(createActionBtn("X", new Color(220, 53, 69)));  // cross
        }
        row.add(p5, gbc);

        return row;
    }

    private JPanel createAvatar(String initials) {
        // tao hinh tron gia lap bang JPanel
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 225, 235));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setLayout(new GridBagLayout());
        JLabel lbl = new JLabel(initials);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(50, 60, 80));
        avatar.add(lbl);
        return avatar;
    }

    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel("  " + status + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setOpaque(true);

        switch (status) {
            case "PENDING":
                badge.setBackground(new Color(230, 230, 230));
                badge.setForeground(Color.DARK_GRAY);
                break;
            case "REVIEWING":
                badge.setBackground(new Color(230, 240, 255));
                badge.setForeground(new Color(13, 110, 253));
                break;
            case "HIRED":
                badge.setBackground(new Color(230, 250, 240));
                badge.setForeground(new Color(40, 167, 69));
                break;
        }
        return badge;
    }

    private JButton createActionBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(color);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createPageBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (active) {
            btn.setBackground(new Color(200, 220, 255));
            btn.setForeground(new Color(13, 110, 253));
            btn.setBorder(new LineBorder(new Color(13, 110, 253), 1));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        }
        return btn;
    }

    private JPanel createBottomSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // cot trai: AI Suggestion (chiem khoang 70%)
        gbc.gridx = 0; gbc.weightx = 0.7; gbc.insets = new Insets(0, 0, 0, 15);

        JPanel aiCard = new JPanel();
        aiCard.setLayout(new BoxLayout(aiCard, BoxLayout.Y_AXIS));
        aiCard.setBackground(Color.WHITE);
        aiCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));

        // cot phai: Promo Banner (chiem khoang 30%)
        gbc.gridx = 1; gbc.weightx = 0.3; gbc.insets = new Insets(0, 0, 0, 0);

        return panel;
    }

    private JPanel createAIHintItem(String title, String desc, Color borderColor) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(new Color(248, 249, 250));
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, borderColor),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lTitle = new JLabel("<html><b>" + title + "</b></html>");
        lTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextArea lDesc = new JTextArea(desc);
        lDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lDesc.setForeground(Color.DARK_GRAY);
        lDesc.setOpaque(false);
        lDesc.setLineWrap(true);
        lDesc.setWrapStyleWord(true);
        lDesc.setEditable(false);

        textPanel.add(lTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(lDesc);

        item.add(textPanel, BorderLayout.CENTER);
        return item;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Portal - Duyet ho so");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 850);
            frame.setLayout(new BorderLayout());

            // Header o NORTH
            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            // Sidebar o WEST
            SidebarPanel sidebar = new SidebarPanel(SidebarPanel.Role.EMPLOYER);
            frame.add(sidebar, BorderLayout.WEST);

            // Panel chinh o CENTER
            ApplicationReviewPanel reviewPanel = new ApplicationReviewPanel();
            frame.add(reviewPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}