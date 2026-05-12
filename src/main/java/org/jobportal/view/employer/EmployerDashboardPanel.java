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

        mainContent.add(createPageHeader());
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

    private JPanel createPageHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Tong quan nha tuyen dung");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 37, 41));

        JLabel lblSub = new JLabel("Theo doi hieu suat tuyen dung va luong ung vien theo thoi gian thuc.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(108, 117, 125));

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(lblSub);

        return headerPanel;
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

        panel.add(createStatCard("T", "TONG TIN DANG", lblTotalRecruitments, "Tat ca", new Color(230, 240, 255), new Color(13, 110, 253)));
        panel.add(createStatCard("O", "TIN DANG MO", lblOpenRecruitments, "Dang hoat dong", new Color(230, 250, 240), new Color(40, 167, 69)));
        panel.add(createStatCard("U", "TONG UNG VIEN", lblTotalApplicants, "Tat ca", new Color(255, 244, 230), new Color(255, 140, 0)));
        panel.add(createStatCard("N", "UNG VIEN HOM NAY", lblNewApplicants, "Hom nay", new Color(240, 240, 240), new Color(70, 70, 70)));

        return panel;
    }

    private JPanel createStatCard(String iconTxt, String label, JLabel valueLabel,
                                  String badgeTxt, Color badgeBg, Color badgeFg) {
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

        JLabel lblBadge = new JLabel(" " + badgeTxt + " ");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblBadge.setOpaque(true);
        lblBadge.setBackground(badgeBg);
        lblBadge.setForeground(badgeFg);
        topPanel.add(lblBadge, BorderLayout.EAST);

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

        JLabel title = new JLabel("Trang thai tin dang");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(33, 37, 41));

        JPanel row = new JPanel(new GridLayout(1, 3, 20, 0));
        row.setBackground(Color.WHITE);

        lblPendingCount = new JLabel("0");
        lblApprovedCount = new JLabel("0");
        lblRejectedCount = new JLabel("0");

        row.add(createStatusItem("Cho duyet", lblPendingCount, new Color(255, 193, 7)));
        row.add(createStatusItem("Da duyet", lblApprovedCount, new Color(40, 167, 69)));
        row.add(createStatusItem("Bi tu choi", lblRejectedCount, new Color(220, 53, 69)));

        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 12)));
        container.add(row);

        return container;
    }

    private JPanel createStatusItem(String label, JLabel valueLabel, Color dotColor) {
        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
        item.setBackground(Color.WHITE);

        JLabel dot = new JLabel(" ");
        dot.setOpaque(true);
        dot.setBackground(dotColor);
        dot.setPreferredSize(new Dimension(10, 10));
        dot.setMaximumSize(new Dimension(10, 10));

        JPanel dotWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dotWrapper.setBackground(Color.WHITE);
        dotWrapper.add(dot);

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

        JLabel lblTitle = new JLabel("Tin dang gan day");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblSub = new JLabel("Toi da 5 tin moi nhat cua ban.");
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
        recentContainer.add(createRecentRow("TIEU DE", "NGAY DANG", "TRANG THAI", true, null));

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
        JPanel row = new JPanel(new GridLayout(1, 3));
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

        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        p3.setOpaque(false);
        JLabel l3 = new JLabel(col3); l3.setFont(font); l3.setForeground(textColor);
        p3.add(l3);

        row.add(p1);
        row.add(p2);
        row.add(p3);

        return row;
    }

    private JPanel createEmptyRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.add(new JLabel("Chua co tin dang nao."));
        return row;
    }

    private String buildStatusText(RecruitmentDTO job) {
        if (job == null) return "N/A";
        if (job.getAdminStatus() == AdminStatus.PENDING) return "Cho duyet";
        if (job.getAdminStatus() == AdminStatus.REJECTED) return "Bi tu choi";
        if (job.getAdminStatus() == AdminStatus.APPROVED) {
            RecruitmentStatus status = job.getStatus();
            if (status == RecruitmentStatus.OPEN) return "Dang hoat dong";
            if (status == RecruitmentStatus.CLOSED) return "Da dong";
            if (status == RecruitmentStatus.EXPIRED) return "Het han";
        }
        return "Ban nhap";
    }
}
