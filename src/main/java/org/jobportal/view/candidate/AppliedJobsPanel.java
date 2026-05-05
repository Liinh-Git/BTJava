package org.jobportal.view.candidate;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.dto.ApplicationDTO;
import org.jobportal.enums.ApplicationStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

import static org.jobportal.view.util.DesignSystem.*;

public class AppliedJobsPanel extends JPanel {

    private final ApplicationService applicationService = new ApplicationService();
    private JPanel tableContainer;
    private JLabel lblTotal;
    private JLabel lblSuccessRate;

    public AppliedJobsPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_SURFACE);

        JPanel mainContent = createContentPanel();

        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        tableContainer = createJobTable();
        mainContent.add(tableContainer);

        add(createScrollPane(mainContent), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        String candidateId = org.jobportal.utils.SessionManager.getCurrentUser().getUserId();
        List<ApplicationDTO> apps = applicationService.getListOfApplicationByUser(candidateId);
        int total = applicationService.getTotalApplyCountByUser(candidateId);
        int approved = applicationService.getApprovedApplicationByUser(candidateId);
        int rate = total == 0 ? 0 : (approved * 100 / total);

        lblTotal.setText(String.valueOf(total));
        lblSuccessRate.setText(rate + "%");

        tableContainer.removeAll();
        tableContainer.add(createTableHeader());
        for (ApplicationDTO app : apps) {
            String statusText = app.getStatus().name();
            String statusColor = getStatusColor(app.getStatus());
            tableContainer.add(createJobRow(
                    app.getJobTitle(),
                    new String[]{app.getJobType().name()},
                    app.getCompanyName(),
                    app.getAppliedDate() != null ? app.getAppliedDate().toLocalDate().toString() : "",
                    statusText,
                    statusColor
            ));
        }
        tableContainer.add(createPaginationFooter(apps.size()));
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private String getStatusColor(ApplicationStatus status) {
        switch (status) {
            case PENDING: return "blue";
            case APPROVED: return "green";
            case REJECTED: return "grey";
            default: return "blue";
        }
    }

    private JPanel createPageHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SURFACE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(BG_SURFACE);

        JLabel lblTitle = new JLabel("Applied Jobs");
        lblTitle.setFont(heading1());
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Track the status of your current job applications in real-time.");
        lblSub.setFont(body());
        lblSub.setForeground(TEXT_SECONDARY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(lblTitle);
        left.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        left.add(lblSub);
        header.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_4, 0));
        right.setBackground(BG_SURFACE);

        JButton btnFilter = createOutlineButton("Filter");
        btnFilter.setPreferredSize(new Dimension(100, BUTTON_HEIGHT));
        right.add(btnFilter);

        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 2, SPACE_6, 0));
        panel.setBackground(BG_SURFACE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        lblTotal = new JLabel("0");
        lblSuccessRate = new JLabel("0%");
        panel.add(createStatCard("TOTAL APPLICATIONS", lblTotal));
        panel.add(createStatCard("SUCCESS RATE", lblSuccessRate));

        return panel;
    }

    private JPanel createStatCard(String labelText, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JLabel lblLabel = new JLabel(labelText);
        lblLabel.setFont(label());
        lblLabel.setForeground(TEXT_MUTED);
        card.add(lblLabel);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));

        valueLabel.setFont(heading1());
        valueLabel.setForeground(TEXT_PRIMARY);
        card.add(valueLabel);

        return card;
    }

    private JPanel createJobTable() {
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(BG_SURFACE);
        tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableContainer.setBorder(new LineBorder(BORDER, 1));

        tableContainer.add(createTableHeader());
        tableContainer.add(createPaginationFooter(0));

        return tableContainer;
    }

    private JPanel createTableHeader() {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(BG_PAGE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        row.setPreferredSize(new Dimension(0, 48));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        String[] headers = {"JOB TITLE", "COMPANY", "DATE APPLIED", "STATUS", "ACTIONS"};
        double[] weights = {0.30, 0.20, 0.18, 0.18, 0.14};
        int[] aligns = {SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.RIGHT};

        for (int i = 0; i < headers.length; i++) {
            gbc.gridx = i;
            gbc.weightx = weights[i];
            gbc.insets = new Insets(0, SPACE_6, 0, SPACE_3);
            JLabel lbl = new JLabel(headers[i]);
            lbl.setFont(label());
            lbl.setForeground(TEXT_MUTED);
            lbl.setHorizontalAlignment(aligns[i]);
            row.add(lbl, gbc);
        }

        return row;
    }

    private JPanel createJobRow(String title, String[] tags, String company, String date, String status, String statusColor) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(BG_SURFACE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row.setPreferredSize(new Dimension(0, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.30;
        gbc.insets = new Insets(SPACE_3, SPACE_6, SPACE_3, SPACE_3);

        JPanel titleCell = new JPanel();
        titleCell.setLayout(new BoxLayout(titleCell, BoxLayout.Y_AXIS));
        titleCell.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(fontBold(FONT_SIZE_LG));
        lblTitle.setForeground(TEXT_PRIMARY);
        titleCell.add(lblTitle);

        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_1, 0));
        tagPanel.setOpaque(false);
        tagPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String tag : tags) {
            JLabel chip = new JLabel(tag);
            chip.setFont(fontBold(FONT_SIZE_XS));
            chip.setOpaque(true);
            chip.setBackground(new Color(225, 227, 228));
            chip.setForeground(TEXT_PRIMARY);
            chip.setBorder(new EmptyBorder(2, SPACE_2, 2, SPACE_2));
            tagPanel.add(chip);
        }
        titleCell.add(tagPanel);
        row.add(titleCell, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.20;
        gbc.insets = new Insets(SPACE_3, SPACE_3, SPACE_3, SPACE_3);

        JPanel companyCell = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        companyCell.setOpaque(false);

        JLabel compIcon = new JLabel("B");
        compIcon.setFont(fontBold(FONT_SIZE_XS));
        compIcon.setOpaque(true);
        compIcon.setBackground(new Color(241, 245, 249));
        compIcon.setForeground(TEXT_MUTED);
        compIcon.setPreferredSize(new Dimension(24, 24));
        compIcon.setHorizontalAlignment(SwingConstants.CENTER);
        companyCell.add(compIcon);

        JLabel lblCompany = new JLabel(company);
        lblCompany.setFont(body());
        lblCompany.setForeground(TEXT_SECONDARY);
        companyCell.add(lblCompany);
        row.add(companyCell, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.18;

        JLabel lblDate = new JLabel(date);
        lblDate.setFont(body());
        lblDate.setForeground(TEXT_SECONDARY);
        row.add(lblDate, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.18;

        Color badgeBg, badgeFg, borderColor;
        switch (statusColor) {
            case "blue":
                badgeBg = new Color(239, 246, 255);
                badgeFg = new Color(29, 78, 216);
                borderColor = new Color(191, 219, 254);
                break;
            case "green":
                badgeBg = new Color(220, 252, 231);
                badgeFg = new Color(22, 101, 52);
                borderColor = new Color(134, 239, 172);
                break;
            case "grey":
                badgeBg = new Color(237, 238, 239);
                badgeFg = new Color(65, 71, 84);
                borderColor = BORDER;
                break;
            default:
                badgeBg = BADGE_BG;
                badgeFg = BADGE_FG;
                borderColor = BORDER;
                break;
        }

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setOpaque(false);

        JLabel statusBadge = new JLabel(status);
        statusBadge.setFont(fontBold(FONT_SIZE_XS));
        statusBadge.setOpaque(true);
        statusBadge.setBackground(badgeBg);
        statusBadge.setForeground(badgeFg);
        statusBadge.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1),
                new EmptyBorder(3, SPACE_2, 3, SPACE_2)
        ));
        statusPanel.add(statusBadge);
        row.add(statusPanel, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0.14;
        gbc.insets = new Insets(SPACE_3, SPACE_3, SPACE_3, SPACE_6);

        JLabel lblActions = new JLabel("...", SwingConstants.RIGHT);
        lblActions.setFont(fontBold(FONT_SIZE_LG));
        lblActions.setForeground(TEXT_MUTED);
        lblActions.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.add(lblActions, gbc);

        return row;
    }

    private JPanel createPaginationFooter(int total) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_SURFACE);
        footer.setBorder(new EmptyBorder(SPACE_4, SPACE_6, SPACE_4, SPACE_6));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JLabel lblShowing = new JLabel("Showing " + total + " applications");
        lblShowing.setFont(label());
        lblShowing.setForeground(TEXT_MUTED);
        footer.add(lblShowing, BorderLayout.WEST);

        return footer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Candidate - Applied Jobs");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            frame.add(new org.jobportal.view.common.HeaderPanel(), BorderLayout.NORTH);
            frame.add(new org.jobportal.view.common.SidebarPanel(org.jobportal.view.common.SidebarPanel.Role.CANDIDATE), BorderLayout.WEST);
            frame.add(new AppliedJobsPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}