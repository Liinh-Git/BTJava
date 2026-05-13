package org.jobportal.view.employer;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

public class EmployerDashboardPanel extends JPanel {

    private final RecruitmentService recruitmentService = new RecruitmentService();
    private final ApplicationService applicationService = new ApplicationService();
    private JLabel lblActiveJobs;
    private JLabel lblTotalApps;
    private JLabel lblNewApps;

    public EmployerDashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_6)));

        mainContent.add(createMainGrid());

        add(createScrollPane(mainContent), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        String employerId = org.jobportal.utils.SessionManager.getCurrentUser().getUserId();
        int activeJobs = recruitmentService.countOpenRecruitments();
        int totalApps = applicationService.getTotalApplicationCount(employerId);
        int newApps = applicationService.getNewApplicantsToday(employerId);
        lblActiveJobs.setText(String.valueOf(activeJobs));
        lblTotalApps.setText(String.valueOf(totalApps));
        lblNewApps.setText(String.valueOf(newApps));
    }

    private JPanel createPageHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_PAGE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = createPageTitle("Dashboard Nhà tuyển dụng");
        JLabel lblSub = createPageSubtitle("Tổng quan về hoạt động tuyển dụng của bạn.");

        header.add(lblTitle);
        header.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        header.add(lblSub);

        return header;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 3, SPACE_4, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        lblActiveJobs = new JLabel("0");
        lblTotalApps = new JLabel("0");
        lblNewApps = new JLabel("0");
        panel.add(createStatCard("TOTAL ACTIVE JOBS", lblActiveJobs, TEXT_PRIMARY, PRIMARY));
        panel.add(createStatCard("TOTAL APPLICATIONS", lblTotalApps, TEXT_PRIMARY, new Color(51, 102, 255)));
        panel.add(createStatCard("NEW APPLICANTS", lblNewApps, PRIMARY, new Color(220, 38, 38)));

        return panel;
    }

    private JPanel createStatCard(String label, JLabel valueLabel, Color valueColor, Color trendColor) {
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

    private JPanel createMainGrid() {
        JPanel grid = new JPanel(new GridLayout(1, 2, SPACE_5, 0));
        grid.setBackground(BG_PAGE);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        grid.add(createRecentActivityCard());
        grid.add(createQuickActionsCard());

        return grid;
    }

    private JPanel createRecentActivityCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_5, SPACE_5, SPACE_5, SPACE_5)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SURFACE);
        JLabel lblTitle = new JLabel("Hoạt động gần đây");
        lblTitle.setFont(fontBold(FONT_SIZE_LG));
        lblTitle.setForeground(TEXT_PRIMARY);
        header.add(lblTitle, BorderLayout.WEST);
        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        card.add(createActivityItem("N", "Nguyễn Văn A đã ứng tuyển vào vị trí Senior Frontend Developer (React).",
                "15 phút trước", true));
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));
        card.add(createActivityItem("T", "Trần Thị B đã gửi CV cho vị trí Marketing Executive.",
                "1 giờ trước", true));
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));
        card.add(createActivityItem("L", "Lê Văn C đã được bạn từ chối cho vị trí Data Analyst.",
                "3 giờ trước", false));
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));
        card.add(createActivityItem("P", "Phạm Thị D đã được bạn phê duyệt cho vị trí UI/UX Designer.",
                "5 giờ trước", false));

        return card;
    }

    private JPanel createActivityItem(String icon, String message, String time, boolean isNew) {
        JPanel item = new JPanel(new BorderLayout(SPACE_3, 0));
        item.setBackground(BG_SURFACE);
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(fontBold(FONT_SIZE_BASE));
        lblIcon.setOpaque(true);
        lblIcon.setBackground(BG_PAGE);
        lblIcon.setForeground(TEXT_PRIMARY);
        lblIcon.setPreferredSize(new Dimension(36, 36));
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setVerticalAlignment(SwingConstants.CENTER);
        item.add(lblIcon, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(BG_SURFACE);

        JLabel lblMessage = new JLabel("<html>" + message + "</html>");
        lblMessage.setFont(body());
        lblMessage.setForeground(TEXT_PRIMARY);

        JLabel lblTime = new JLabel(time);
        lblTime.setFont(bodySmall());
        lblTime.setForeground(TEXT_MUTED);

        center.add(lblMessage);
        center.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        center.add(lblTime);

        item.add(center, BorderLayout.CENTER);

        if (isNew) {
            JLabel badge = new JLabel("NEW");
            badge.setFont(fontBold(FONT_SIZE_XS));
            badge.setOpaque(true);
            badge.setBackground(BADGE_INFO_BG);
            badge.setForeground(BADGE_INFO_FG);
            badge.setBorder(new EmptyBorder(2, SPACE_2, 2, SPACE_2));
            badge.setAlignmentY(Component.TOP_ALIGNMENT);
            item.add(badge, BorderLayout.EAST);
        }

        return item;
    }

    private JPanel createQuickActionsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_5, SPACE_5, SPACE_5, SPACE_5)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Thao tác nhanh");
        lblTitle.setFont(fontBold(FONT_SIZE_LG));
        lblTitle.setForeground(TEXT_PRIMARY);
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, SPACE_3, SPACE_3));
        buttonsPanel.setBackground(BG_SURFACE);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        buttonsPanel.add(createActionButton("📋", "Đăng tin mới", PRIMARY));
        buttonsPanel.add(createActionButton("✏️", "Quản lý tin đăng", new Color(51, 102, 255)));
        buttonsPanel.add(createActionButton("👥", "Duyệt hồ sơ", SUCCESS));
        buttonsPanel.add(createActionButton("📊", "Thống kê", new Color(220, 38, 38)));

        card.add(buttonsPanel);
        return card;
    }

    private JPanel createActionButton(String icon, String text, Color color) {
        JPanel btn = new JPanel();
        btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));
        btn.setBackground(color);
        btn.setBorder(new EmptyBorder(SPACE_4, SPACE_4, SPACE_4, SPACE_4));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(fontRegular(20));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIcon.setForeground(Color.WHITE);

        JLabel lblText = new JLabel(text);
        lblText.setFont(fontBold(FONT_SIZE_SM));
        lblText.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblText.setForeground(Color.WHITE);

        btn.add(lblIcon);
        btn.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        btn.add(lblText);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            frame.add(new HeaderPanel(), BorderLayout.NORTH);
            frame.add(new SidebarPanel(SidebarPanel.Role.EMPLOYER), BorderLayout.WEST);
            frame.add(new EmployerDashboardPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
