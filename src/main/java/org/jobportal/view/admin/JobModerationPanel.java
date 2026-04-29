package org.jobportal.view.admin;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class JobModerationPanel extends JPanel {

    public JobModerationPanel() {
        // thiet lap layout chinh
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. tieu de trang va huy hieu (badge)
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. luoi danh sach cac the can duyet
        mainContent.add(createCardsGrid());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // 3. phan trang
        mainContent.add(createPagination());

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

        JLabel lblTitle = new JLabel("Job Moderation");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));

        JLabel lblSub = new JLabel("Review pending job postings for quality and compliance.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(108, 117, 125));

        leftPanel.add(lblTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(lblSub);

        // the huy hieu ben phai
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(248, 249, 250));

        JLabel lblBadge = new JLabel(" 24 Pending Review ");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBadge.setOpaque(true);
        lblBadge.setBackground(new Color(226, 232, 240));
        lblBadge.setForeground(new Color(71, 85, 105));
        lblBadge.setBorder(new EmptyBorder(5, 10, 5, 10));

        rightPanel.add(lblBadge);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createCardsGrid() {
        // su dung GridLayout de chia 2 cot, khoang cach 20px
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        gridPanel.setBackground(new Color(248, 249, 250));
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        gridPanel.add(createJobCard(
                "Senior Product Designer", "Oct 24, 2023", "Velocity Digital Systems",
                "Seeking an experienced designer to lead our core product ecosystem. Requires 8+ years experience and a strong portfolio..."
        ));

        gridPanel.add(createJobCard(
                "Full-Stack Engineer (Remote)", "Oct 23, 2023", "CloudArch Solutions",
                "Join our infrastructure team to build scalable serverless applications. Expertise in Node.js and AWS required..."
        ));

        gridPanel.add(createJobCard(
                "Marketing Director", "Oct 23, 2023", "GreenCycle Growth",
                "Developing brand strategy and overseeing performance marketing channels for our sustainable consumer goods..."
        ));

        gridPanel.add(createJobCard(
                "Data Scientist, ML", "Oct 22, 2023", "Aether AI",
                "Building large language models for medical diagnostics. PhD in Computer Science or related field preferred..."
        ));

        gridPanel.add(createJobCard(
                "Customer Success Lead", "Oct 22, 2023", "Nexus SaaS",
                "Managing key enterprise accounts and ensuring customer satisfaction through proactive support and training..."
        ));

        gridPanel.add(createJobCard(
                "HR Operations Specialist", "Oct 21, 2023", "Global Logistics Corp",
                "Handling payroll processing and benefits administration for a multi-regional workforce of 500+ employees..."
        ));

        return gridPanel;
    }

    private JPanel createJobCard(String title, String date, String company, String desc) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // dong 1: Tieu de va ngay thang
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(33, 37, 41));

        JLabel lblDate = new JLabel(date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDate.setOpaque(true);
        lblDate.setBackground(new Color(241, 245, 249));
        lblDate.setForeground(new Color(100, 116, 139));
        lblDate.setBorder(new EmptyBorder(3, 8, 3, 8));

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(lblDate, BorderLayout.EAST);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // dong 2: Ten cong ty
        JPanel companyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        companyPanel.setBackground(Color.WHITE);
        companyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcon = new JLabel("B"); // icon toa nha gia lap
        lblIcon.setForeground(Color.GRAY);

        JLabel lblCompany = new JLabel(company);
        lblCompany.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCompany.setForeground(new Color(108, 117, 125));

        companyPanel.add(lblIcon);
        companyPanel.add(lblCompany);

        // dong 3: Mo ta
        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDesc.setForeground(new Color(73, 80, 87));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setFocusable(false);
        txtDesc.setBorder(new EmptyBorder(15, 0, 20, 0));

        // dong 4: Hai nut Approve va Reject
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton btnApprove = new JButton("Approve");
        btnApprove.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnApprove.setBackground(new Color(13, 110, 253));
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setFocusPainted(false);
        btnApprove.setBorderPainted(false);
        btnApprove.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnReject = new JButton("Reject");
        btnReject.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnReject.setBackground(Color.WHITE);
        btnReject.setForeground(Color.DARK_GRAY);
        btnReject.setBorder(new LineBorder(new Color(226, 230, 234), 1));
        btnReject.setFocusPainted(false);
        btnReject.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);

        // ghep vao the chình
        card.add(topPanel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(companyPanel);
        card.add(txtDesc);
        card.add(Box.createVerticalGlue()); // day cac nut xuong day the neu do cao cac the khac nhau
        card.add(btnPanel);

        return card;
    }

    private JPanel createPagination() {
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        paginationPanel.setBackground(new Color(248, 249, 250));

        paginationPanel.add(createPageBtn("1", true));
        paginationPanel.add(createPageBtn("2", false));
        paginationPanel.add(createPageBtn("3", false));
        paginationPanel.add(createPageBtn("...", false));
        paginationPanel.add(createPageBtn("8", false));

        return paginationPanel;
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
                btn.setBackground(new Color(248, 249, 250));
            } else {
                btn.setBorder(new LineBorder(new Color(226, 230, 234), 1));
            }
        }
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Portal - Job Moderation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 850);
            frame.setLayout(new BorderLayout());

            // Header o NORTH
            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            // Sidebar o WEST (quyen ADMIN)
            SidebarPanel sidebar = new SidebarPanel(SidebarPanel.Role.ADMIN);
            frame.add(sidebar, BorderLayout.WEST);

            // Giao dien chinh o CENTER
            JobModerationPanel moderationPanel = new JobModerationPanel();
            frame.add(moderationPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}