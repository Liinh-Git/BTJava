package org.jobportal.view.admin;

import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.enums.JobType;
import org.jobportal.enums.Role;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.JobCardPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class JobModerationPanel extends JPanel {

    private static final int CARD_HEIGHT = 230;
    private static final int GRID_GAP = 20;

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private JPanel gridPanel;
    private JLabel lblBadge;
    private JTextField txtSearch;

    public JobModerationPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(createSearchBar());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        gridPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        gridPanel.setBackground(new Color(248, 249, 250));
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(gridPanel);

        loadData();

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

        JLabel lblTitle = new JLabel("Kiểm duyệt tin tuyển dụng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));

        JLabel lblSub = new JLabel("Duyệt các tin đang chờ kiểm duyệt trước khi hiển thị cho ứng viên.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(108, 117, 125));

        leftPanel.add(lblTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(lblSub);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(248, 249, 250));

        lblBadge = new JLabel(" 0 tin đang duyệt ");
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

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        btnSearch.addActionListener(e -> loadData());

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        return searchPanel;
    }

    private void loadData() {
        gridPanel.removeAll();
        List<RecruitmentDTO> pendingJobs = recruitmentService.getPendingRecruitments();
        String keyword = txtSearch != null ? txtSearch.getText().trim().toLowerCase() : "";

        int visibleCount = 0;
        for (RecruitmentDTO job : pendingJobs) {
            if (!matchesKeyword(job, keyword)) {
                continue;
            }
            gridPanel.add(createModerationJobCard(job));
            visibleCount++;
        }

        lblBadge.setText(" " + pendingJobs.size() + " tin đang duyệt ");
        if (visibleCount == 0) {
            gridPanel.add(createEmptyPanel());
        }
        int rows = Math.max(1, (int) Math.ceil(Math.max(visibleCount, 1) / 2.0));
        int gridHeight = rows * CARD_HEIGHT + Math.max(0, rows - 1) * GRID_GAP;
        gridPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, gridHeight));
        gridPanel.setPreferredSize(new Dimension(0, gridHeight));
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private boolean matchesKeyword(RecruitmentDTO job, String keyword) {
        if (keyword == null || keyword.isEmpty()) return true;
        String title = job.getTitle() != null ? job.getTitle().toLowerCase() : "";
        String company = job.getCompanyName() != null ? job.getCompanyName().toLowerCase() : "";
        return title.contains(keyword) || company.contains(keyword);
    }

    private JPanel createModerationJobCard(RecruitmentDTO job) {
        String company = job.getCompanyName() != null ? job.getCompanyName() : "Chưa rõ công ty";
        String description = job.getDescription() != null ? job.getDescription() : "";
        String desc = description.length() > 100 ? description.substring(0, 100) + "..." : description;
        String salary = job.getSalary() != null ? String.format("%,.0f VND", job.getSalary()) : "Thỏa thuận";
        String location = job.getLocation() != null ? job.getLocation() : "Chưa cập nhật địa điểm";
        String[] tags = {toEnglishJobTypeLabel(job.getJobType())};

        JobCardPanel card = new JobCardPanel(job.getTitle(), company, salary, location, desc, tags);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        Dimension actionSize = new Dimension(180, 38);
        btnPanel.setPreferredSize(actionSize);
        btnPanel.setMinimumSize(actionSize);
        btnPanel.setMaximumSize(actionSize);

        JButton btnApprove = new JButton("Duyệt");
        btnApprove.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnApprove.setBackground(new Color(13, 110, 253));
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setPreferredSize(new Dimension(85, 38));
        btnApprove.setFocusPainted(false);
        btnApprove.setBorderPainted(false);
        btnApprove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnApprove.addActionListener(e -> moderate(job, "APPROVED"));

        JButton btnReject = new JButton("Từ chối");
        btnReject.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnReject.setBackground(Color.WHITE);
        btnReject.setForeground(Color.DARK_GRAY);
        btnReject.setBorder(new LineBorder(new Color(226, 230, 234), 1));
        btnReject.setPreferredSize(new Dimension(85, 38));
        btnReject.setFocusPainted(false);
        btnReject.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReject.addActionListener(e -> moderate(job, "REJECTED"));

        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);
        card.setActionComponent(btnPanel);
        return card;
    }

    private void moderate(RecruitmentDTO job, String decision) {
        boolean success = recruitmentService.adminModerate(job.getRecruitmentId(), decision);
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "APPROVED".equals(decision) ? "Đã duyệt tin tuyển dụng!" : "Đã từ chối tin tuyển dụng!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật trạng thái tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(new Color(226, 230, 234), 1));
        panel.add(new JLabel("Không có tin nào phù hợp."));
        return panel;
    }

    private String toJobTypeLabel(JobType jobType) {
        if (jobType == JobType.PARTTIME) return "Bán thời gian";
        if (jobType == JobType.INTERNSHIP) return "Thực tập";
        return "Toàn thời gian";
    }

    private String toEnglishJobTypeLabel(JobType jobType) {
        if (jobType == JobType.PARTTIME) return "Part-time";
        if (jobType == JobType.INTERNSHIP) return "Internship";
        return "Full-time";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản trị - Kiểm duyệt tin tuyển dụng");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 850);
            frame.setLayout(new BorderLayout());

            frame.add(new HeaderPanel(), BorderLayout.NORTH);
            frame.add(new SidebarPanel(Role.ADMIN), BorderLayout.WEST);
            frame.add(new JobModerationPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
