package org.jobportal.view.candidate;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AppliedJobsPanel extends JPanel {

    public AppliedJobsPanel() {
        // thiet lap mau nen va layout chinh
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        // panel noi dung chinh co the cuon
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. tieu de trang va cac nut thao tac (Filter, Export)
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. phan thong ke (3 the: Total, Active, Success Rate)
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // 3. danh sach don ung tuyen (Gia lap bang)
        mainContent.add(createApplicationsTable());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 4. banner Pro Tip
        mainContent.add(createProTipBanner());

        // boc vao scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ham tao tieu de trang
    private JPanel createPageHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // ben trai: Tieu de va mo ta
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(248, 249, 250));

        JLabel lblTitle = new JLabel("Applied Jobs");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel lblSub = new JLabel("Track the status of your current job applications in real-time.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);

        leftPanel.add(lblTitle);
        leftPanel.add(lblSub);

        // ben phai: Filter va Export
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(248, 249, 250));

        JButton btnFilter = createOutlineButton("Filter", "Y"); // gia lap icon
        JButton btnExport = createOutlineButton("Export", "v");
        rightPanel.add(btnFilter);
        rightPanel.add(btnExport);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    // ham tao 3 the thong ke
    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(new Color(248, 249, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        panel.add(createStatCard("TOTAL APPLICATIONS", "24", null));
        panel.add(createStatCard("ACTIVE INTERVIEWS", "3", "Coming up"));
        panel.add(createStatCard("SUCCESS RATE", "12%", null));

        return panel;
    }

    private JPanel createStatCard(String label, String value, String badgeText) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblLabel.setForeground(Color.GRAY);

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        valuePanel.setBackground(Color.WHITE);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valuePanel.add(lblValue);

        if (badgeText != null) {
            JLabel badge = new JLabel(" " + badgeText + " ");
            badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            badge.setOpaque(true);
            badge.setBackground(new Color(230, 240, 255));
            badge.setForeground(new Color(0, 100, 250));
            valuePanel.add(badge);
        }

        card.add(lblLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(valuePanel);
        return card;
    }

    // ham tao bang danh sach ung tuyen
    private JPanel createApplicationsTable() {
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new LineBorder(new Color(230, 230, 230), 1));

        // header cua bang
        tableContainer.add(createRow("JOB TITLE", "COMPANY", "DATE APPLIED", "STATUS", "ACTIONS", true));

        // du lieu cac dong
        tableContainer.add(createRow("Senior Frontend Developer", "TechCorp Inc.", "Oct 24, 2023", "In Review", "...", false));
        tableContainer.add(createRow("UI/UX Designer", "CreativePulse", "Oct 20, 2023", "Interview Scheduled", "...", false));
        tableContainer.add(createRow("Backend Engineer (Go)", "Streamline Soft", "Oct 18, 2023", "Not Selected", "...", false));
        tableContainer.add(createRow("Product Manager", "Nexus Labs", "Oct 15, 2023", "Applied", "...", false));

        // phan footer cua bang co phan trang
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblCount = new JLabel("Showing 4 of 24 applications");
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCount.setForeground(Color.GRAY);
        footer.add(lblCount, BorderLayout.WEST);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pagination.setBackground(Color.WHITE);
        pagination.add(createPageBtn("1", true));
        pagination.add(createPageBtn("2", false));
        pagination.add(createPageBtn("3", false));
        pagination.add(createPageBtn(">", false));
        footer.add(pagination, BorderLayout.EAST);

        tableContainer.add(footer);

        return tableContainer;
    }

    private JPanel createRow(String col1, String col2, String col3, String status, String action, boolean isHeader) {
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(isHeader ? new Color(250, 250, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        row.setPreferredSize(new Dimension(0, 70));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.BLACK;

        // cot 1: Job Title
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        p1.setOpaque(false);
        JLabel l1 = new JLabel(col1);
        l1.setFont(font); l1.setForeground(textColor);
        p1.add(l1);
        row.add(p1);

        // cot 2: Company
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2);
        l2.setFont(font); l2.setForeground(textColor);
        p2.add(l2);
        row.add(p2);

        // cot 3: Date
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        p3.setOpaque(false);
        JLabel l3 = new JLabel(col3);
        l3.setFont(font); l3.setForeground(textColor);
        p3.add(l3);
        row.add(p3);

        // cot 4: Status
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        p4.setOpaque(false);
        if (isHeader) {
            JLabel l4 = new JLabel(status);
            l4.setFont(font); l4.setForeground(textColor);
            p4.add(l4);
        } else {
            p4.add(createStatusBadge(status));
        }
        row.add(p4);

        // cot 5: Actions
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        p5.setOpaque(false);
        JLabel l5 = new JLabel(action);
        l5.setFont(new Font("Arial", Font.BOLD, 18));
        l5.setForeground(Color.GRAY);
        p5.add(l5);
        row.add(p5);

        return row;
    }

    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel("  " + status + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setOpaque(true);

        switch (status) {
            case "In Review":
                badge.setBackground(new Color(230, 240, 255));
                badge.setForeground(new Color(13, 110, 253));
                break;
            case "Interview Scheduled":
                badge.setBackground(new Color(255, 243, 230));
                badge.setForeground(new Color(253, 126, 20));
                break;
            case "Not Selected":
                badge.setBackground(new Color(240, 240, 240));
                badge.setForeground(Color.GRAY);
                break;
            case "Applied":
                badge.setBackground(new Color(230, 250, 240));
                badge.setForeground(new Color(40, 167, 69));
                break;
        }

        badge.setBorder(new LineBorder(badge.getForeground(), 1, true));
        return badge;
    }

    private JPanel createProTipBanner() {
        JPanel banner = new JPanel(new BorderLayout(15, 0));
        banner.setBackground(new Color(240, 245, 255));
        banner.setBorder(new EmptyBorder(15, 25, 15, 25));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel icon = new JLabel("L"); // gia lap icon bong den
        icon.setFont(new Font("Segoe UI", Font.BOLD, 20));
        icon.setForeground(new Color(0, 100, 250));
        banner.add(icon, BorderLayout.WEST);

        String text = "<html><b>Pro Tip: Keep your profile updated</b><br/>"
                + "Companies are 3x more likely to view candidates who have updated their CV in the last 30 days.</html>";
        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        banner.add(lblText, BorderLayout.CENTER);

        return banner;
    }

    private JButton createOutlineButton(String text, String icon) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
    }

    private JButton createPageBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        if (active) {
            btn.setBackground(new Color(13, 110, 253));
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        }
        return btn;
    }

    // ham main de kiem tra giao dien
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Applied Jobs Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 850);
            frame.setLayout(new BorderLayout());

            // dung lai HeaderPanel
            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            // dung lai SidebarPanel (Role Candidate)
            SidebarPanel sidebar = new SidebarPanel(SidebarPanel.Role.CANDIDATE);
            frame.add(sidebar, BorderLayout.WEST);

            JPanel rightPanel = new JPanel(new BorderLayout());
            // them panel AppliedJobs vua code
            AppliedJobsPanel appliedPanel = new AppliedJobsPanel();
            rightPanel.add(appliedPanel, BorderLayout.CENTER);

            frame.add(rightPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}