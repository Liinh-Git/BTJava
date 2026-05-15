package org.jobportal.view.admin;

import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.impl.UserService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.bll.interfaces.IUserService;
import org.jobportal.enums.Role;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminDashboardPanel extends JPanel {

    private final IUserService userService = new UserService();
    private final IRecruitmentService recruitmentService = new RecruitmentService();

    public AdminDashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContent.add(createTopEmployersTable());

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

        JLabel lblSub = new JLabel("Theo dõi các số liệu hiện có trong hệ thống.");
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

        panel.add(createStatCard("UV", "TỔNG ỨNG VIÊN", String.valueOf(userService.countByRole(Role.CANDIDATE))));
        panel.add(createStatCard("NTD", "NHÀ TUYỂN DỤNG", String.valueOf(userService.countByRole(Role.EMPLOYER))));
        panel.add(createStatCard("TD", "TIN ĐANG MỞ", String.valueOf(recruitmentService.countOpenRecruitments())));
        return panel;
    }

    private JPanel createStatCard(String iconTxt, String label, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel lblIcon = new JLabel(" " + iconTxt + " ");
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblIcon.setOpaque(true);
        lblIcon.setBackground(new Color(240, 245, 255));
        lblIcon.setForeground(new Color(13, 110, 253));
        topPanel.add(lblIcon, BorderLayout.WEST);

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

    private JPanel createTopEmployersTable() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(248, 249, 250));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Nhà tuyển dụng có nhiều tin nhất");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblSub = new JLabel("Dựa trên số tin tuyển dụng trong database.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);

        container.add(lblTitle);
        container.add(lblSub);
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new LineBorder(new Color(226, 230, 234), 1));

        tablePanel.add(createTableRow("CÔNG TY", "SỐ TIN", true));
        List<Map<String, Object>> topEmployers = recruitmentService.getTopEmployers();
        if (topEmployers.isEmpty()) {
            tablePanel.add(createTableRow("Chưa có dữ liệu", "0", false));
        } else {
            for (Map<String, Object> employer : topEmployers) {
                tablePanel.add(createTableRow(
                        String.valueOf(employer.getOrDefault("company_name", "Chưa rõ công ty")),
                        String.valueOf(employer.getOrDefault("recruitment_count", 0)),
                        false
                ));
            }
        }

        container.add(tablePanel);
        return container;
    }

    private JPanel createTableRow(String col1, String col2, boolean isHeader) {
        JPanel row = new JPanel(new GridLayout(1, 2));
        row.setBackground(isHeader ? new Color(248, 249, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setPreferredSize(new Dimension(0, 50));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.DARK_GRAY;

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        p1.setOpaque(false);
        JLabel l1 = new JLabel(col1);
        l1.setFont(font);
        l1.setForeground(isHeader ? textColor : Color.BLACK);
        p1.add(l1);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2);
        l2.setFont(font);
        l2.setForeground(textColor);
        p2.add(l2);

        row.add(p1);
        row.add(p2);
        return row;
    }
}
