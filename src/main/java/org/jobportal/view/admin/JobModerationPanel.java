package org.jobportal.view.admin;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class JobModerationPanel extends JPanel {

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private JPanel gridPanel;

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
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 1.5. thanh tim kiem tin cho duyet
        mainContent.add(createSearchBar());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. danh sach the tin dang cho duyet
        gridPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        gridPanel.setBackground(new Color(248, 249, 250));
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainContent.add(gridPanel);
        
        loadData();
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

    private JPanel createSearchBar() {
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(12, 15, 12, 15)
        ));
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        JTextField txtSearch = new JTextField("Tìm theo tên tin hoặc công ty...");
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(new Color(108, 117, 125));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSearch.setBackground(new Color(13, 110, 253));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(110, 35));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);

        return searchPanel;
    }

    private void loadData() {
        gridPanel.removeAll();
        List<RecruitmentDTO> pendingJobs = recruitmentService.getPendingRecruitments();
        for (RecruitmentDTO job : pendingJobs) {
            gridPanel.add(createModerationJobCard(job));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createModerationJobCard(RecruitmentDTO job) {
        String company = job.getCompanyName() != null ? job.getCompanyName() : "Unknown Company";
        String desc = job.getDescription().length() > 100 ? job.getDescription().substring(0, 100) + "..." : job.getDescription();
        String[] tags = {job.getCategoryName() != null ? job.getCategoryName() : "Khác"};
        
        org.jobportal.view.common.JobCardPanel card = new org.jobportal.view.common.JobCardPanel(
            job.getTitle(), company, String.valueOf(job.getSalary()), job.getLocation(), desc, tags);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton btnApprove = new JButton("Approve");
        btnApprove.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnApprove.setBackground(new Color(13, 110, 253));
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setFocusPainted(false);
        btnApprove.setBorderPainted(false);
        btnApprove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnApprove.addActionListener(e -> {
            boolean success = recruitmentService.adminModerate(job.getRecruitmentId(), "APPROVED");
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã duyệt tin tuyển dụng!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi duyệt tin!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnReject = new JButton("Reject");
        btnReject.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnReject.setBackground(Color.WHITE);
        btnReject.setForeground(Color.DARK_GRAY);
        btnReject.setBorder(new LineBorder(new Color(226, 230, 234), 1));
        btnReject.setFocusPainted(false);
        btnReject.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReject.addActionListener(e -> {
            boolean success = recruitmentService.adminModerate(job.getRecruitmentId(), "REJECTED");
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã từ chối tin tuyển dụng!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi từ chối tin!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);

        card.setActionComponent(btnPanel);
        return card;
    }

    private JPanel createPagination() {
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        paginationPanel.setBackground(new Color(248, 249, 250));
        paginationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

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
            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.ADMIN);
            frame.add(sidebar, BorderLayout.WEST);

            // Giao dien chinh o CENTER
            JobModerationPanel moderationPanel = new JobModerationPanel();
            frame.add(moderationPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}