package org.jobportal.view.candidate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

/**
 * Applied jobs panel matching screen.png / code.html reference.
 * Layout:
 *   - Page header: "Applied Jobs" title + subtitle + Filter/Export buttons
 *   - 3-col stats: TOTAL APPLICATIONS / ACTIVE INTERVIEWS / SUCCESS RATE
 *   - Table: JOB TITLE (with tags), COMPANY (with icon), DATE APPLIED, STATUS (badge), ACTIONS
 *   - Pagination: "Showing 4 of 24 applications" + page buttons
 *   - Footer tip: "Pro Tip: Keep your profile updated"
 */
public class AppliedJobsPanel extends JPanel {

    public AppliedJobsPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_SURFACE);

        JPanel mainContent = createContentPanel();

        // 1. page header
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        // 2. stats row
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        // 3. table
        mainContent.add(createJobTable());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        // 4. footer tip
        mainContent.add(createFooterTip());

        add(createScrollPane(mainContent), BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SURFACE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        // left: title + subtitle
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

        // right: Filter + Export buttons
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_4, 0));
        right.setBackground(BG_SURFACE);

        JButton btnFilter = createOutlineButton("Filter");
        btnFilter.setPreferredSize(new Dimension(100, BUTTON_HEIGHT));
        right.add(btnFilter);

        JButton btnExport = createOutlineButton("Export");
        btnExport.setPreferredSize(new Dimension(100, BUTTON_HEIGHT));
        right.add(btnExport);

        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 3, SPACE_6, 0));
        panel.setBackground(BG_SURFACE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // TOTAL APPLICATIONS
        panel.add(createStatCard("TOTAL APPLICATIONS", "24", null));

        // ACTIVE INTERVIEWS with "Coming up" badge
        JPanel interviewCard = new JPanel();
        interviewCard.setLayout(new BoxLayout(interviewCard, BoxLayout.Y_AXIS));
        interviewCard.setBackground(BG_SURFACE);
        interviewCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JLabel lblIntLabel = new JLabel("ACTIVE INTERVIEWS");
        lblIntLabel.setFont(label());
        lblIntLabel.setForeground(TEXT_MUTED);
        interviewCard.add(lblIntLabel);
        interviewCard.add(Box.createRigidArea(new Dimension(0, SPACE_2)));

        JPanel valueRow = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        valueRow.setBackground(BG_SURFACE);
        valueRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIntValue = new JLabel("3");
        lblIntValue.setFont(heading1());
        lblIntValue.setForeground(PRIMARY);
        valueRow.add(lblIntValue);

        JLabel badgeComingUp = createBadge("Coming up", BADGE_INFO_BG, BADGE_INFO_FG);
        valueRow.add(badgeComingUp);
        interviewCard.add(valueRow);

        panel.add(interviewCard);

        // SUCCESS RATE
        panel.add(createStatCard("SUCCESS RATE", "12%", null));

        return panel;
    }

    private JPanel createStatCard(String labelText, String value, String extra) {
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

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(heading1());
        lblValue.setForeground(TEXT_PRIMARY);
        card.add(lblValue);

        return card;
    }

    private JPanel createJobTable() {
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(BG_SURFACE);
        tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableContainer.setBorder(new LineBorder(BORDER, 1));

        // header row
        tableContainer.add(createTableHeader());

        // data rows
        tableContainer.add(createJobRow("Senior Frontend Developer", new String[]{"FULL-TIME", "REMOTE"}, "TechCorp Inc.", "Oct 24, 2023", "In Review", "blue"));
        tableContainer.add(createJobRow("UI/UX Designer", new String[]{"CONTRACT"}, "CreativePulse", "Oct 20, 2023", "Interview Scheduled", "orange"));
        tableContainer.add(createJobRow("Backend Engineer (Go)", new String[]{"FULL-TIME"}, "Streamline Soft", "Oct 18, 2023", "Not Selected", "grey"));
        tableContainer.add(createJobRow("Product Manager", new String[]{"FULL-TIME"}, "Nexus Labs", "Oct 15, 2023", "Applied", "blue"));

        // pagination footer
        tableContainer.add(createPaginationFooter());

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

        // Col 1: Job Title + tags
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

        // Col 2: Company
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

        // Col 3: Date Applied
        gbc.gridx = 2;
        gbc.weightx = 0.18;

        JLabel lblDate = new JLabel(date);
        lblDate.setFont(body());
        lblDate.setForeground(TEXT_SECONDARY);
        row.add(lblDate, gbc);

        // Col 4: Status badge
        gbc.gridx = 3;
        gbc.weightx = 0.18;

        Color badgeBg, badgeFg, dotColor;
        Color borderColor;
        switch (statusColor) {
            case "blue":
                badgeBg = new Color(239, 246, 255);
                badgeFg = new Color(29, 78, 216);
                dotColor = new Color(37, 99, 235);
                borderColor = new Color(191, 219, 254);
                break;
            case "orange":
                badgeBg = new Color(255, 219, 204);
                badgeFg = new Color(124, 46, 0);
                dotColor = new Color(158, 61, 0);
                borderColor = new Color(198, 79, 0);
                break;
            case "grey":
                badgeBg = new Color(237, 238, 239);
                badgeFg = new Color(65, 71, 84);
                dotColor = new Color(113, 119, 134);
                borderColor = BORDER;
                break;
            default:
                badgeBg = BADGE_BG;
                badgeFg = BADGE_FG;
                dotColor = TEXT_MUTED;
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

        // Col 5: Actions (more_horiz icon)
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

    private JPanel createPaginationFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_SURFACE);
        footer.setBorder(new EmptyBorder(SPACE_4, SPACE_6, SPACE_4, SPACE_6));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JLabel lblShowing = new JLabel("Showing 4 of 24 applications");
        lblShowing.setFont(label());
        lblShowing.setForeground(TEXT_MUTED);
        footer.add(lblShowing, BorderLayout.WEST);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_1, 0));
        pagination.setBackground(BG_SURFACE);
        pagination.add(createPageButton("1", true));
        pagination.add(createPageButton("2", false));
        pagination.add(createPageButton("3", false));
        pagination.add(createPageButton(">", false));
        footer.add(pagination, BorderLayout.EAST);

        return footer;
    }

    private JPanel createFooterTip() {
        JPanel tip = new JPanel(new BorderLayout(SPACE_6, 0));
        tip.setBackground(BG_PAGE);
        tip.setAlignmentX(Component.LEFT_ALIGNMENT);
        tip.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JLabel lblIcon = new JLabel("Q");
        lblIcon.setFont(fontBold(FONT_SIZE_XL));
        lblIcon.setForeground(PRIMARY);
        tip.add(lblIcon, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(BG_PAGE);

        JLabel lblTipTitle = new JLabel("Pro Tip: Keep your profile updated");
        lblTipTitle.setFont(fontBold(FONT_SIZE_BASE));
        lblTipTitle.setForeground(TEXT_PRIMARY);

        JLabel lblTipDesc = new JLabel("Companies are 3x more likely to view candidates who have updated their CV in the last 30 days.");
        lblTipDesc.setFont(body());
        lblTipDesc.setForeground(TEXT_SECONDARY);

        textPanel.add(lblTipTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        textPanel.add(lblTipDesc);
        tip.add(textPanel, BorderLayout.CENTER);

        return tip;
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