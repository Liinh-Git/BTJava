package org.jobportal.view.candidate;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.interfaces.IApplicationService;
import org.jobportal.dto.ApplicationDTO;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppliedJobsPanel extends JPanel {

    private final IApplicationService applicationService = new ApplicationService();
    private JPanel statsPanel;
    private JPanel tableContainer;
    private JLabel lblCount;
    private List<ApplicationDTO> currentApps = null; // cache danh sach hien tai

    public AppliedJobsPanel() {
        // thiet lap mau nen va layout chinh
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        // panel noi dung chinh co the cuon
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 2. phan thong ke (3 the: Total, Active, Success Rate)
        statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(248, 249, 250));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainContent.add(statsPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // 3. danh sach don ung tuyen (Gia lap bang)
        tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        mainContent.add(tableContainer);
        
        loadData();
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // boc vao scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        loadData();
    }

    // ham tao 3 the thong ke
    private void updateStats(String candidateId) {
        statsPanel.removeAll();
        if (candidateId != null) {
            int total = applicationService.getTotalApplyCountByUser(candidateId);
            int approved = applicationService.getApprovedApplicationByUser(candidateId);
            List<ApplicationDTO> apps = applicationService.getListOfApplicationByUser(candidateId);
            int pending = 0;
            for (ApplicationDTO app : apps) {
                if (app.getStatus() == ApplicationStatus.PENDING) {
                    pending++;
                }
            }
            double rate = total > 0 ? (approved * 100.0 / total) : 0;
            
            statsPanel.add(createStatCard("TỔNG ĐƠN", String.valueOf(total), null));
            statsPanel.add(createStatCard("ĐƠN CHỜ DUYỆT", String.valueOf(pending), null));
            statsPanel.add(createStatCard("TỶ LỆ ĐƯỢC DUYỆT", String.format("%.1f%%", rate), null));
        } else {
            statsPanel.add(createStatCard("TỔNG ĐƠN", "0", null));
            statsPanel.add(createStatCard("ĐƠN CHỜ DUYỆT", "0", null));
            statsPanel.add(createStatCard("TỶ LỆ ĐƯỢC DUYỆT", "0%", null));
        }
        statsPanel.revalidate();
        statsPanel.repaint();
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
        lblLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        valuePanel.setBackground(Color.WHITE);
        valuePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
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

    private void loadData() {
        String candidateId = SessionManager.getInstance().getCandidateId();
        updateStats(candidateId);
        
        tableContainer.removeAll();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.add(createRow("TIN TUYỂN DỤNG", "CÔNG TY", "NGÀY ỨNG TUYỂN", "TRẠNG THÁI", "THAO TÁC", true, null));

        if (candidateId != null) {
            currentApps = applicationService.getListOfApplicationByUser(candidateId);
        } else {
            currentApps = null;
        }

        if (currentApps != null && !currentApps.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            for (ApplicationDTO app : currentApps) {
                String title = app.getJobTitle();
                String company = app.getCompanyName(); 
                String dateStr = app.getAppliedDate() != null ? app.getAppliedDate().format(formatter) : "N/A";
                String statusStr = toStatusLabel(app.getStatus());
                
                tableContainer.add(createRow(title, company, dateStr, statusStr, "...", false, app));
            }
        }

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));

        if (lblCount == null) {
            lblCount = new JLabel();
            lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblCount.setForeground(Color.GRAY);
        }
        lblCount.setText("Hiển thị " + (currentApps != null ? currentApps.size() : 0) + " đơn ứng tuyển");
        footer.add(lblCount, BorderLayout.WEST);

        if (currentApps != null && !currentApps.isEmpty()) {
            JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            pagination.setBackground(Color.WHITE);
            pagination.add(createPageBtn("1", true));
            footer.add(pagination, BorderLayout.EAST);
        }

        tableContainer.add(footer);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private JPanel createRow(String col1, String col2, String col3, String status, String action, boolean isHeader, ApplicationDTO app) {
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(isHeader ? new Color(250, 250, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        int rowHeight = isHeader ? 56 : 70;
        row.setPreferredSize(new Dimension(0, rowHeight));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.BLACK;

        // cot 1: Job Title
        JPanel p1 = new JPanel(new FlowLayout(isHeader ? FlowLayout.CENTER : FlowLayout.LEFT, 20, 15));
        p1.setOpaque(false);
        JLabel l1 = new JLabel(col1);
        l1.setFont(font); l1.setForeground(textColor);
        p1.add(l1);
        row.add(p1);

        // cot 2: Company
        JPanel p2 = new JPanel(new FlowLayout(isHeader ? FlowLayout.CENTER : FlowLayout.LEFT, 20, 15));
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2);
        l2.setFont(font); l2.setForeground(textColor);
        p2.add(l2);
        row.add(p2);

        // cot 3: Date
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        p3.setOpaque(false);
        JLabel l3 = new JLabel(col3);
        l3.setFont(font); l3.setForeground(textColor);
        p3.add(l3);
        row.add(p3);

        // cot 4: Status
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
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
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        p5.setOpaque(false);
        if (isHeader) {
            JLabel l5 = new JLabel(action);
            l5.setFont(font);
            l5.setForeground(textColor);
            p5.add(l5);
        } else {
            JButton btnCancel = new JButton("Hủy");
            btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnCancel.setBackground(Color.WHITE);
            btnCancel.setForeground(Color.RED);
            btnCancel.addActionListener(e -> {
                if (app != null) {
                    boolean confirmed = org.jobportal.view.common.ModernDialogUtils.showConfirm(this, "Xác nhận", "Bạn có chắc muốn hủy đơn ứng tuyển này?");
                    if (confirmed) {
                        boolean success = applicationService.cancelApplication(app.getApplicationId());
                        if (success) {
                            org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Đã hủy đơn ứng tuyển!");
                            loadData();
                        } else {
                            org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Lỗi khi hủy đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            p5.add(btnCancel);
        }
        row.add(p5);

        return row;
    }

    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel("  " + status + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setOpaque(true);

        switch (status) {
            case "Đang duyệt":
            case "Đang chờ duyệt":
                badge.setBackground(new Color(230, 240, 255));
                badge.setForeground(new Color(13, 110, 253));
                break;
            case "Bị từ chối":
                badge.setBackground(new Color(255, 243, 230));
                badge.setForeground(new Color(253, 126, 20));
                break;
            case "Đã duyệt":
                badge.setBackground(new Color(230, 250, 240));
                badge.setForeground(new Color(40, 167, 69));
                break;
        }

        badge.setBorder(new LineBorder(badge.getForeground(), 1, true));
        return badge;
    }

    private String toStatusLabel(ApplicationStatus status) {
        if (status == ApplicationStatus.APPROVED) return "Đã duyệt";
        if (status == ApplicationStatus.REJECTED) return "Bị từ chối";
        return "Đang chờ duyệt";
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

}

