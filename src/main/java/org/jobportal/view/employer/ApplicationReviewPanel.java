package org.jobportal.view.employer;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.dto.ApplicationDTO;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

import static org.jobportal.view.util.DesignSystem.*;

public class ApplicationReviewPanel extends JPanel {

    private final ApplicationService applicationService = new ApplicationService();
    private String currentRecruitmentId = null;
    private JPanel tableContainer;
    private JLabel lblTotal;
    private JLabel lblPending;
    private JLabel lblApproved;
    private JLabel lblRejected;

    public ApplicationReviewPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        mainContent.add(createPageHeaderSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        tableContainer = createApplicantTable();
        mainContent.add(tableContainer);

        add(createScrollPane(mainContent), BorderLayout.CENTER);
    }

    public void loadApplications(String recruitmentId) {
        this.currentRecruitmentId = recruitmentId;
        List<ApplicationDTO> apps = applicationService.getApplicationsByRecruitmentId(recruitmentId);

        int total = apps.size(), pending = 0, approved = 0, rejected = 0;
        for (ApplicationDTO a : apps) {
            switch (a.getStatus()) {
                case PENDING: pending++; break;
                case APPROVED: approved++; break;
                case REJECTED: rejected++; break;
            }
        }
        lblTotal.setText(String.valueOf(total));
        lblPending.setText(String.valueOf(pending));
        lblApproved.setText(String.valueOf(approved));
        lblRejected.setText(String.valueOf(rejected));

        tableContainer.removeAll();
        tableContainer.add(createApplicantHeader());
        for (ApplicationDTO a : apps) {
            tableContainer.add(createApplicantRow(a));
        }
        tableContainer.add(createPaginationFooter(apps.size()));
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private JPanel createPageHeaderSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BG_PAGE);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBreadcrumb = new JLabel("Employer Portal  /  Quản lý tin đăng  /  Ứng tuyển");
        lblBreadcrumb.setFont(bodySmall());
        lblBreadcrumb.setForeground(TEXT_MUTED);
        lblBreadcrumb.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(lblBreadcrumb);
        section.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PAGE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(BG_PAGE);

        JLabel lblTitle = createPageTitle("Danh sách ứng tuyển");
        JLabel lblSub = createPageSubtitle("Xem xét và quản lý hồ sơ ứng viên cho vị trí này.");

        leftPanel.add(lblTitle);
        leftPanel.add(lblSub);

        header.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_3, 0));
        rightPanel.setBackground(BG_PAGE);

        JComboBox<String> cbFilter = new JComboBox<>(new String[]{"Trạng thái: Tất cả", "PENDING", "APPROVED", "REJECTED"});
        cbFilter.setPreferredSize(new Dimension(180, 36));
        cbFilter.setBackground(BG_SURFACE);
        cbFilter.setFont(body());
        rightPanel.add(cbFilter);

        header.add(rightPanel, BorderLayout.EAST);
        section.add(header);

        return section;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, SPACE_4, 0));
        row.setBackground(BG_PAGE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        lblTotal = new JLabel("0");
        lblPending = new JLabel("0");
        lblApproved = new JLabel("0");
        lblRejected = new JLabel("0");
        row.add(createStatCard("TỔNG SỐ", lblTotal, TEXT_PRIMARY));
        row.add(createStatCard("PENDING", lblPending, new Color(51, 102, 255)));
        row.add(createStatCard("APPROVED", lblApproved, PRIMARY));
        row.add(createStatCard("REJECTED", lblRejected, DANGER));

        return row;
    }

    private JPanel createStatCard(String label, JLabel valueLabel, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_5, SPACE_5, SPACE_5, SPACE_5)
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

    private JPanel createApplicantTable() {
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(BG_SURFACE);
        tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableContainer.setBorder(new LineBorder(BORDER, 1));

        tableContainer.add(createApplicantHeader());
        tableContainer.add(createPaginationFooter(0));

        return tableContainer;
    }

    private JPanel createApplicantHeader() {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(BG_SURFACE_ALT);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        String[] headers = {"ỨNG VIÊN", "EMAIL", "TRẠNG THÁI", "THAO TÁC"};
        double[] weights = {0.25, 0.25, 0.2, 0.3};

        for (int i = 0; i < headers.length; i++) {
            gbc.gridx = i;
            gbc.weightx = weights[i];
            gbc.insets = new Insets(0, SPACE_4, 0, SPACE_3);
            JLabel lbl = new JLabel(headers[i]);
            lbl.setFont(tableHeader());
            lbl.setForeground(TEXT_MUTED);
            row.add(lbl, gbc);
        }
        return row;
    }

    private JPanel createApplicantRow(ApplicationDTO dto) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(BG_SURFACE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0; gbc.weightx = 0.25; gbc.insets = new Insets(SPACE_2, SPACE_4, SPACE_2, SPACE_3);
        JPanel infoCell = new JPanel();
        infoCell.setLayout(new BoxLayout(infoCell, BoxLayout.Y_AXIS));
        infoCell.setOpaque(false);
        JLabel lblName = new JLabel(dto.getCandidateName());
        lblName.setFont(fontBold(FONT_SIZE_BASE));
        lblName.setForeground(TEXT_PRIMARY);
        infoCell.add(lblName);
        row.add(infoCell, gbc);

        gbc.gridx = 1; gbc.weightx = 0.25;
        JLabel lblEmail = new JLabel(dto.getEmail());
        lblEmail.setFont(body());
        lblEmail.setForeground(TEXT_SECONDARY);
        row.add(lblEmail, gbc);

        gbc.gridx = 2; gbc.weightx = 0.2;
        row.add(createApplicantStatusBadge(dto.getStatus()), gbc);

        gbc.gridx = 3; gbc.weightx = 0.3; gbc.insets = new Insets(SPACE_2, SPACE_3, SPACE_2, SPACE_4);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_2, 0));
        actionPanel.setOpaque(false);

        if (dto.getStatus() == ApplicationStatus.PENDING) {
            JButton btnApprove = createPrimaryButton("Duyệt");
            btnApprove.setBackground(SUCCESS);
            btnApprove.setPreferredSize(new Dimension(80, 32));
            btnApprove.setFont(fontBold(FONT_SIZE_SM));
            btnApprove.addActionListener(e -> {
                String userId = org.jobportal.utils.SessionManager.getCurrentUser().getUserId();
                applicationService.approveApplication(userId, dto.getApplicationId());
                if (currentRecruitmentId != null) loadApplications(currentRecruitmentId);
            });
            actionPanel.add(btnApprove);

            JButton btnReject = createOutlineButton("Từ chối");
            btnReject.setPreferredSize(new Dimension(90, 32));
            btnReject.setFont(fontBold(FONT_SIZE_SM));
            btnReject.addActionListener(e -> {
                String userId = org.jobportal.utils.SessionManager.getCurrentUser().getUserId();
                applicationService.rejectApplication(userId, dto.getApplicationId());
                if (currentRecruitmentId != null) loadApplications(currentRecruitmentId);
            });
            actionPanel.add(btnReject);
        }

        row.add(actionPanel, gbc);

        return row;
    }

    private JLabel createApplicantStatusBadge(ApplicationStatus status) {
        Color bg, fg;
        switch (status) {
            case PENDING:
                bg = BADGE_INFO_BG; fg = BADGE_INFO_FG; break;
            case APPROVED:
                bg = BADGE_SUCCESS_BG; fg = BADGE_SUCCESS_FG; break;
            case REJECTED:
                bg = BADGE_BG; fg = BADGE_FG; break;
            default:
                bg = BADGE_BG; fg = BADGE_FG; break;
        }
        return createBadge(status.name(), bg, fg);
    }

    private JPanel createPaginationFooter(int total) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_SURFACE);
        footer.setBorder(new EmptyBorder(SPACE_4, SPACE_5, SPACE_4, SPACE_5));

        JLabel lblCount = new JLabel("Hiển thị " + total + " ứng viên");
        lblCount.setFont(bodySmall());
        lblCount.setForeground(TEXT_MUTED);
        footer.add(lblCount, BorderLayout.WEST);

        return footer;
    }

    // ham test giao dien doc lap
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Portal - Duyệt hồ sơ ứng viên");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 900);
            frame.setLayout(new BorderLayout());

            frame.add(new org.jobportal.view.common.HeaderPanel(), BorderLayout.NORTH);
            frame.add(new org.jobportal.view.common.SidebarPanel(org.jobportal.view.common.SidebarPanel.Role.EMPLOYER), BorderLayout.WEST);
            frame.add(new ApplicationReviewPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}