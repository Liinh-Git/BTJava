package org.jobportal.view.employer;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

import static org.jobportal.view.util.DesignSystem.*;

public class RecruitmentListPanel extends JPanel {

    private final RecruitmentService recruitmentService = new RecruitmentService();
    private final ApplicationService applicationService = new ApplicationService();
    private JPanel tableContainer;
    private JLabel lblActive;
    private JLabel lblExpired;
    private JLabel lblNewApplicants;

    public RecruitmentListPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createFilterSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        tableContainer = createJobListTable();
        mainContent.add(tableContainer);
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        add(createScrollPane(mainContent), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        String employerId = org.jobportal.utils.SessionManager.getCurrentUser().getUserId();
        List<RecruitmentDTO> list = recruitmentService.getRecruitmentsByEmployer(employerId);

        int active = 0, expired = 0, totalApps = applicationService.getTotalApplicationCount(employerId);
        int newApps = applicationService.getNewApplicantsToday(employerId);
        for (RecruitmentDTO r : list) {
            if (r.getStatus() == RecruitmentStatus.OPEN) active++;
            else expired++;
        }
        lblActive.setText(String.valueOf(active));
        lblExpired.setText(String.valueOf(expired));
        lblNewApplicants.setText(String.valueOf(newApps));

        tableContainer.removeAll();
        tableContainer.add(createRow("TIÊU ĐỀ TIN", null, "TRẠNG THÁI", "NGÀY ĐĂNG", "ỨNG TUYỂN", true));
        for (RecruitmentDTO r : list) {
            String statusText = r.getStatus() == RecruitmentStatus.OPEN ? "Đang hiển thị" : "Đã đóng";
            tableContainer.add(createRow(r.getTitle(), r.getRecruitmentId(), statusText,
                    r.getCreatedDate() != null ? r.getCreatedDate().toLocalDate().toString() : "",
                    r.getApplicationCount() + " hồ sơ", false));
        }
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private JPanel createPageHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PAGE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(BG_PAGE);

        JLabel lblTitle = createPageTitle("Quản lý tin đăng");
        JLabel lblSub = createPageSubtitle("Xem và quản lý tất cả các vị trí đang tuyển dụng của bạn.");

        leftPanel.add(lblTitle);
        leftPanel.add(lblSub);

        JButton btnCreate = createPrimaryButton("＋  Tạo tin mới");
        btnCreate.setPreferredSize(new Dimension(150, BUTTON_HEIGHT));
        btnCreate.setMaximumSize(new Dimension(150, BUTTON_HEIGHT));

        btnCreate.addActionListener(e -> {
            Window ancestor = SwingUtilities.getWindowAncestor(this);
            if (ancestor instanceof Frame) {
                JDialog dialog = new JDialog((Frame) ancestor, "Đăng tin tuyển dụng", true);
                dialog.setSize(950, 750);
                dialog.setLocationRelativeTo(ancestor);
                dialog.add(new RecruitmentFormPanel());
                dialog.setVisible(true);
            }
        });

        header.add(leftPanel, BorderLayout.WEST);
        header.add(btnCreate, BorderLayout.EAST);

        return header;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 3, SPACE_4, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        lblActive = new JLabel("0");
        lblNewApplicants = new JLabel("0");
        lblExpired = new JLabel("0");
        panel.add(createStatCard("ĐANG HIỂN THỊ", lblActive, TEXT_PRIMARY));
        panel.add(createStatCard("ỨNG VIÊN MỚI", lblNewApplicants, PRIMARY));
        panel.add(createStatCard("HẾT HẠN", lblExpired, TEXT_PRIMARY));

        return panel;
    }

    private JPanel createStatCard(String label, JLabel valueLabel, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_4, SPACE_5, SPACE_4, SPACE_5)
        ));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(tableHeader());
        lblLabel.setForeground(TEXT_MUTED);

        valueLabel.setFont(heading2());
        valueLabel.setForeground(valueColor);

        card.add(lblLabel);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        card.add(valueLabel);
        return card;
    }

    private JPanel createFilterSection() {
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBackground(BG_PAGE);
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_3, 0));
        left.setBackground(BG_PAGE);

        JTextField txtSearch = new JTextField("Tìm kiếm tin đăng...");
        txtSearch.setPreferredSize(new Dimension(260, 36));
        txtSearch.setFont(body());
        txtSearch.setForeground(TEXT_MUTED);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_2, SPACE_3, SPACE_2, SPACE_3)
        ));

        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang hiển thị", "Đã đóng"});
        cbStatus.setPreferredSize(new Dimension(160, 36));
        cbStatus.setBackground(BG_SURFACE);
        cbStatus.setFont(body());

        left.add(txtSearch);
        left.add(cbStatus);

        JLabel right = new JLabel("");
        right.setFont(bodySmall());
        right.setForeground(TEXT_MUTED);

        filterPanel.add(left, BorderLayout.WEST);
        filterPanel.add(right, BorderLayout.EAST);
        return filterPanel;
    }

    private JPanel createJobListTable() {
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(BG_SURFACE);
        tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableContainer.setBorder(new LineBorder(BORDER, 1));

        tableContainer.add(createRow("TIÊU ĐỀ TIN", null, "TRẠNG THÁI", "NGÀY ĐĂNG", "ỨNG TUYỂN", true));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_SURFACE);
        footer.setBorder(new EmptyBorder(SPACE_4, SPACE_5, SPACE_4, SPACE_5));

        JLabel lblCount = new JLabel("");
        lblCount.setFont(bodySmall());
        lblCount.setForeground(TEXT_MUTED);
        footer.add(lblCount, BorderLayout.WEST);

        tableContainer.add(footer);

        return tableContainer;
    }

    private JPanel createRow(String title, String code, String status, String date, String applicants, boolean isHeader) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? BG_SURFACE_ALT : BG_SURFACE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        Font hFont = tableHeader();
        Font bFont = body();
        Color hColor = TEXT_MUTED;

        gbc.gridx = 0; gbc.weightx = 0.35; gbc.insets = new Insets(SPACE_3, SPACE_5, SPACE_3, SPACE_3);
        if (isHeader) {
            JLabel lbl = new JLabel(title); lbl.setFont(hFont); lbl.setForeground(hColor);
            row.add(lbl, gbc);
        } else {
            JPanel cell = new JPanel();
            cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
            cell.setOpaque(false);
            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(fontBold(FONT_SIZE_BASE));
            lblTitle.setForeground(PRIMARY);
            lblTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
            JLabel lblCode = new JLabel("Mã tin: " + code);
            lblCode.setFont(bodySmall());
            lblCode.setForeground(TEXT_MUTED);
            cell.add(lblTitle);
            cell.add(lblCode);
            row.add(cell, gbc);
        }

        gbc.gridx = 1; gbc.weightx = 0.15; gbc.insets = new Insets(SPACE_3, SPACE_3, SPACE_3, SPACE_3);
        if (isHeader) {
            JLabel lbl = new JLabel(status); lbl.setFont(hFont); lbl.setForeground(hColor);
            row.add(lbl, gbc);
        } else {
            row.add(createStatusBadge(status), gbc);
        }

        gbc.gridx = 2; gbc.weightx = 0.15;
        JLabel lblDate = new JLabel(date);
        lblDate.setFont(isHeader ? hFont : bFont);
        lblDate.setForeground(isHeader ? hColor : TEXT_SECONDARY);
        row.add(lblDate, gbc);

        gbc.gridx = 3; gbc.weightx = 0.15;
        JLabel lblApplicants = new JLabel(applicants);
        lblApplicants.setFont(isHeader ? hFont : bFont);
        lblApplicants.setForeground(isHeader ? hColor : TEXT_PRIMARY);
        row.add(lblApplicants, gbc);

        gbc.gridx = 4; gbc.weightx = 0.2; gbc.insets = new Insets(SPACE_3, SPACE_3, SPACE_3, SPACE_5);
        if (isHeader) {
            JLabel lbl = new JLabel("HÀNH ĐỘNG", SwingConstants.RIGHT);
            lbl.setFont(hFont); lbl.setForeground(hColor);
            row.add(lbl, gbc);
        } else {
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_3, 0));
            actionPanel.setOpaque(false);

            JLabel btnEdit = new JLabel("✎");
            btnEdit.setFont(fontRegular(18));
            btnEdit.setForeground(TEXT_MUTED);
            btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel btnClose = new JLabel("⏹");
            btnClose.setFont(fontRegular(18));
            btnClose.setForeground(WARNING);
            btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int confirm = JOptionPane.showConfirmDialog(RecruitmentListPanel.this,
                            "Đóng tin này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        recruitmentService.closeRecruitment(code);
                        loadData();
                    }
                }
            });

            JLabel btnDelete = new JLabel("🗑");
            btnDelete.setFont(fontRegular(18));
            btnDelete.setForeground(DANGER);
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int confirm = JOptionPane.showConfirmDialog(RecruitmentListPanel.this,
                            "Xóa tin này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        recruitmentService.deleteRecruitment(code);
                        loadData();
                    }
                }
            });

            actionPanel.add(btnEdit);
            actionPanel.add(btnClose);
            actionPanel.add(btnDelete);
            row.add(actionPanel, gbc);
        }

        return row;
    }

    private JLabel createStatusBadge(String status) {
        Color bg, fg;
        switch (status) {
            case "Đang hiển thị":
                bg = BADGE_SUCCESS_BG; fg = BADGE_SUCCESS_FG; break;
            case "Đã đóng":
                bg = BADGE_BG; fg = BADGE_FG; break;
            default:
                bg = BADGE_BG; fg = BADGE_FG; break;
        }
        return createBadge(status, bg, fg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Portal - Quản lý tin đăng");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            SidebarPanel sidebar = new SidebarPanel(SidebarPanel.Role.EMPLOYER);
            frame.add(sidebar, BorderLayout.WEST);

            RecruitmentListPanel listPanel = new RecruitmentListPanel();
            frame.add(listPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}