package org.jobportal.view.employer;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IApplicationService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.enums.AdminStatus;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class EmployerDashboardPanel extends JPanel {

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private final IApplicationService applicationService = new ApplicationService();

    private JLabel lblTotalRecruitments;
    private JLabel lblOpenRecruitments;
    private JLabel lblTotalApplicants;
    private JLabel lblNewApplicants;

    private JLabel lblPendingCount;
    private JLabel lblApprovedCount;
    private JLabel lblRejectedCount;

    private JPanel recentContainer;

    public EmployerDashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        mainContent.add(createStatusSummary());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        mainContent.add(createRecentRecruitments());

        loadData();

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBackground(new Color(248, 249, 250));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        lblTotalRecruitments = new JLabel("0");
        lblOpenRecruitments = new JLabel("0");
        lblTotalApplicants = new JLabel("0");
        lblNewApplicants = new JLabel("0");

        panel.add(createStatCard("TỔNG TIN ĐĂNG", lblTotalRecruitments));
        panel.add(createStatCard("TIN ĐĂNG MỞ", lblOpenRecruitments));
        panel.add(createStatCard("TỔNG ỨNG VIÊN", lblTotalApplicants));
        panel.add(createStatCard("ỨNG VIÊN HÔM NAY", lblNewApplicants));

        return panel;
    }

    private JPanel createStatCard(String label, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(10, 20, 15, 20)
        ));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLabel.setForeground(new Color(108, 117, 125));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.BLACK);

        bottomPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        bottomPanel.add(lblLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bottomPanel.add(valueLabel);

        card.add(topPanel);
        card.add(bottomPanel);
        return card;
    }

    private JPanel createStatusSummary() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));

        JLabel title = new JLabel("Trạng thái tin đăng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(33, 37, 41));

        JPanel row = new JPanel(new GridLayout(1, 3, 20, 0));
        row.setBackground(Color.WHITE);

        lblPendingCount = new JLabel("0");
        lblApprovedCount = new JLabel("0");
        lblRejectedCount = new JLabel("0");

        row.add(createStatusItem("Đang duyệt", lblPendingCount));
        row.add(createStatusItem("Đã duyệt", lblApprovedCount));
        row.add(createStatusItem("Bị từ chối", lblRejectedCount));

        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 12)));
        container.add(row);

        return container;
    }

    private JPanel createStatusItem(String label, JLabel valueLabel) {
        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
        item.setBackground(Color.WHITE);

        JPanel dotWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dotWrapper.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(108, 117, 125));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.BLACK);

        item.add(dotWrapper);
        item.add(Box.createRigidArea(new Dimension(0, 6)));
        item.add(lbl);
        item.add(Box.createRigidArea(new Dimension(0, 4)));
        item.add(valueLabel);
        return item;
    }

    private JPanel createRecentRecruitments() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(248, 249, 250));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Tin đăng gần đây");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblSub = new JLabel("Tối đa 5 tin mới nhất của bạn.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);

        container.add(lblTitle);
        container.add(lblSub);
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        recentContainer = new JPanel();
        recentContainer.setLayout(new BoxLayout(recentContainer, BoxLayout.Y_AXIS));
        recentContainer.setBackground(Color.WHITE);
        recentContainer.setBorder(new LineBorder(new Color(226, 230, 234), 1));

        container.add(recentContainer);
        return container;
    }

    private void loadData() {
        String employerId = SessionManager.getInstance().getEmployerId();
        if (employerId == null || employerId.isBlank()) {
            employerId = SessionManager.getInstance().getCurrentUserId();
        }

        int totalRecruitments = recruitmentService.countRecruitmentsByEmployer(employerId);
        int openRecruitments = recruitmentService.countOpenRecruitmentsByEmployer(employerId);
        int pendingRecruitments = recruitmentService.countPendingRecruitmentsByEmployer(employerId);
        int rejectedRecruitments = recruitmentService.countRejectedRecruitmentsByEmployer(employerId);

        int totalApplicants = applicationService.getTotalApplicationCount(employerId);
        int newApplicantsToday = applicationService.getNewApplicantsToday(employerId);

        lblTotalRecruitments.setText(String.valueOf(totalRecruitments));
        lblOpenRecruitments.setText(String.valueOf(openRecruitments));
        lblTotalApplicants.setText(String.valueOf(totalApplicants));
        lblNewApplicants.setText(String.valueOf(newApplicantsToday));

        int approvedRecruitments = Math.max(0, totalRecruitments - pendingRecruitments - rejectedRecruitments);
        lblPendingCount.setText(String.valueOf(pendingRecruitments));
        lblApprovedCount.setText(String.valueOf(approvedRecruitments));
        lblRejectedCount.setText(String.valueOf(rejectedRecruitments));

        loadRecentRecruitments(employerId);
    }

    private void loadRecentRecruitments(String employerId) {
        recentContainer.removeAll();
        recentContainer.add(createRecentRow("TIÊU ĐỀ", "NGÀY ĐĂNG", "TRẠNG THÁI", true, null));

        List<RecruitmentDTO> list = recruitmentService.getRecruitmentsByEmployer(employerId);
        if (list != null && !list.isEmpty()) {
            list.sort(Comparator.comparing(RecruitmentDTO::getCreatedDate,
                    Comparator.nullsLast(Comparator.naturalOrder())).reversed());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            int limit = Math.min(5, list.size());
            for (int i = 0; i < limit; i++) {
                RecruitmentDTO job = list.get(i);
                String date = job.getCreatedDate() != null ? job.getCreatedDate().format(formatter) : "N/A";
                recentContainer.add(createRecentRow(job.getTitle(), date, buildStatusText(job), false, job));
            }
        } else {
            recentContainer.add(createEmptyRow());
        }

        recentContainer.revalidate();
        recentContainer.repaint();
    }

    private JPanel createRecentRow(String col1, String col2, String col3, boolean isHeader, RecruitmentDTO job) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? new Color(248, 249, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        row.setPreferredSize(new Dimension(0, 64));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.DARK_GRAY;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Cột 1: rộng hơn
        gbc.gridx = 0;
        gbc.weightx = 0.56; // tăng/giảm theo ý
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setOpaque(false);
        p1.setBorder(new EmptyBorder(0, 30, 0, 10));
        JLabel l1 = new JLabel(col1);
        l1.setFont(font);
        l1.setForeground(isHeader ? textColor : Color.BLACK);
        p1.add(l1, BorderLayout.WEST);
        row.add(p1, gbc);

        // Cột 2: hẹp hơn
        gbc.gridx = 1;
        gbc.weightx = 0.18;
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setOpaque(false);
        p2.setBorder(new EmptyBorder(0, 20, 0, 10));
        JLabel l2 = new JLabel(col2);
        l2.setFont(font);
        l2.setForeground(textColor);
        p2.add(l2, BorderLayout.WEST);
        row.add(p2, gbc);

        // Cột 3: hẹp hơn
        gbc.gridx = 2;
        gbc.weightx = 0.26;
        JPanel p3 = new JPanel(new BorderLayout());
        p3.setOpaque(false);
        p3.setBorder(new EmptyBorder(0, 20, 0, 12));
        JLabel l3 = new JLabel(col3);
        l3.setFont(font);
        l3.setForeground(textColor);
        p3.add(l3, BorderLayout.WEST);
        row.add(p3, gbc);

        return row;
    }


    private JPanel createEmptyRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        row.setPreferredSize(new Dimension(0, 64));
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.setBorder(new EmptyBorder(0, 30, 0, 10));
        left.add(new JLabel("Chưa có tin đăng nào."), BorderLayout.WEST);
        row.add(left, BorderLayout.CENTER);
        return row;
    }

    private String buildStatusText(RecruitmentDTO job) {
        if (job == null) return "N/A";
        if (job.getAdminStatus() == AdminStatus.PENDING) return "Đang duyệt";
        if (job.getAdminStatus() == AdminStatus.REJECTED) return "Bị từ chối";
        if (job.getAdminStatus() == AdminStatus.APPROVED) {
            RecruitmentStatus status = job.getStatus();
            if (status == RecruitmentStatus.OPEN) return "Đang hoạt động";
            if (status == RecruitmentStatus.CLOSED) return "Đã đóng";
            if (status == RecruitmentStatus.EXPIRED) return "Hết hạn";
        }
        return "Bản nháp";
    }
}
