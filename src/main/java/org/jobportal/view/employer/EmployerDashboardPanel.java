package org.jobportal.view.employer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

/**
 * Employer dashboard panel matching t_ng_quan_nh_tuy_n_d_ng stitch design.
 * Layout: page header + 3-col stats row + two-column (activity card + quick actions/package).
 */
public class EmployerDashboardPanel extends JPanel {

    public EmployerDashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        // 1. tieu de trang
        mainContent.add(createHeaderSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // separator nhe
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_LIGHT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(sep);
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 2. 3 the thong ke
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 3. luoi chinh: hoat dong gan day (trai) + thao tac nhanh + goi dich vu (phai)
        mainContent.add(createMainGrid());

        add(createScrollPane(mainContent), BorderLayout.CENTER);
    }

    private JPanel createHeaderSection() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_PAGE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Tổng quan");
        title.setFont(heading2());
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Chào mừng quay trở lại. Đây là số liệu thống kê tuyển dụng của bạn.");
        subtitle.setFont(body());
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        header.add(subtitle);
        return header;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 3, SPACE_5, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        panel.add(createStatCard("TOTAL ACTIVE JOBS", "12", "↗  +2 since last month", SUCCESS));
        panel.add(createStatCard("TOTAL APPLICATIONS", "458", "👤  Avg. 38 per job", SUCCESS));
        panel.add(createStatCard("NEW APPLICANTS", "24", "○  Last 24 hours", DANGER));

        return panel;
    }

    private JPanel createStatCard(String label, String value, String subText, Color accentColor) {
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

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(heading2());
        lblValue.setForeground(TEXT_PRIMARY);

        JLabel lblSub = new JLabel(subText);
        lblSub.setFont(bodySmall());
        lblSub.setForeground(accentColor);

        card.add(lblLabel);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        card.add(lblValue);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        card.add(lblSub);
        return card;
    }

    private JPanel createMainGrid() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // cot trai: hoat dong gan day
        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, SPACE_5);
        panel.add(createRecentActivityCard(), gbc);

        // cot phai: thao tac nhanh + goi dich vu
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(createRightColumn(), gbc);

        return panel;
    }

    private JPanel createRecentActivityCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(new LineBorder(BORDER, 1));

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SURFACE);
        header.setBorder(new EmptyBorder(SPACE_4, SPACE_5, SPACE_4, SPACE_5));

        JLabel title = new JLabel("Hoạt động gần đây");
        title.setFont(fontBold(FONT_SIZE_LG));
        title.setForeground(TEXT_PRIMARY);

        JLabel viewAll = new JLabel("Xem tất cả");
        viewAll.setFont(body());
        viewAll.setForeground(PRIMARY);
        viewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));

        header.add(title, BorderLayout.WEST);
        header.add(viewAll, BorderLayout.EAST);
        card.add(header);

        // activity items
        card.add(createActivityItem("👤", "Nguyễn Văn A đã ứng tuyển vào vị trí Senior Frontend Developer", "2 giờ trước", new String[]{"ReactJS", "Tailwind"}));
        card.add(createActivityItem("📝", "Bạn đã cập nhật thông tin cho tin đăng Product Manager", "5 giờ trước", null));
        card.add(createActivityItem("👤", "Lê Thị B đã ứng tuyển vào vị trí UI/UX Designer", "Hôm qua", new String[]{"Figma"}));

        return card;
    }

    private JPanel createActivityItem(String icon, String text, String time, String[] tags) {
        JPanel row = new JPanel(new BorderLayout(SPACE_3, 0));
        row.setBackground(BG_SURFACE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_LIGHT),
                new EmptyBorder(SPACE_4, SPACE_5, SPACE_4, SPACE_5)
        ));

        // icon
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(fontRegular(20));
        lblIcon.setPreferredSize(new Dimension(36, 36));
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        row.add(lblIcon, BorderLayout.WEST);

        // text content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_SURFACE);

        JLabel lblText = new JLabel("<html>" + text + "</html>");
        lblText.setFont(body());
        lblText.setForeground(TEXT_PRIMARY);
        content.add(lblText);

        if (tags != null && tags.length > 0) {
            JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_1, SPACE_1));
            tagPanel.setBackground(BG_SURFACE);
            for (String tag : tags) {
                JLabel chip = new JLabel(tag);
                chip.setFont(caption());
                chip.setOpaque(true);
                chip.setBackground(BG_PAGE);
                chip.setForeground(TEXT_SECONDARY);
                chip.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1),
                        new EmptyBorder(1, SPACE_2, 1, SPACE_2)
                ));
                tagPanel.add(chip);
            }
            content.add(tagPanel);
        }
        row.add(content, BorderLayout.CENTER);

        // time
        JLabel lblTime = new JLabel(time);
        lblTime.setFont(bodySmall());
        lblTime.setForeground(TEXT_MUTED);
        lblTime.setVerticalAlignment(SwingConstants.TOP);
        row.add(lblTime, BorderLayout.EAST);

        return row;
    }

    private JPanel createRightColumn() {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(BG_PAGE);

        column.add(createQuickActionsCard());
        column.add(Box.createRigidArea(new Dimension(0, SPACE_4)));
        column.add(createPackageCard());

        return column;
    }

    private JPanel createQuickActionsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(new LineBorder(BORDER, 1));

        JLabel title = new JLabel("Thao tác nhanh");
        title.setFont(fontBold(FONT_SIZE_LG));
        title.setForeground(TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(SPACE_4, SPACE_5, SPACE_3, SPACE_5));
        card.add(title);

        card.add(createActionRow("ĐĂNG TIN MỚI", "+"));
        card.add(createActionRow("TÌM KIẾM ỨNG VIÊN", "🔍"));
        card.add(createActionRow("CÀI ĐẶT CÔNG TY", "⚙"));

        return card;
    }

    private JPanel createActionRow(String text, String icon) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG_SURFACE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_LIGHT),
                new EmptyBorder(SPACE_3, SPACE_5, SPACE_3, SPACE_5)
        ));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblText = new JLabel(text);
        lblText.setFont(fontBold(FONT_SIZE_SM));
        lblText.setForeground(TEXT_PRIMARY);
        row.add(lblText, BorderLayout.WEST);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(fontRegular(FONT_SIZE_LG));
        lblIcon.setForeground(TEXT_MUTED);
        row.add(lblIcon, BorderLayout.EAST);

        return row;
    }

    private JPanel createPackageCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_5, SPACE_5, SPACE_5, SPACE_5)
        ));

        JLabel title = new JLabel("Gói dịch vụ");
        title.setFont(fontBold(FONT_SIZE_LG));
        title.setForeground(TEXT_PRIMARY);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        // progress row
        JPanel progressRow = new JPanel(new BorderLayout());
        progressRow.setBackground(BG_SURFACE);
        JLabel lblLeft = new JLabel("Số tin đã đăng");
        lblLeft.setFont(bodySmall());
        lblLeft.setForeground(TEXT_SECONDARY);
        JLabel lblRight = new JLabel("12/20");
        lblRight.setFont(fontBold(FONT_SIZE_SM));
        lblRight.setForeground(TEXT_PRIMARY);
        progressRow.add(lblLeft, BorderLayout.WEST);
        progressRow.add(lblRight, BorderLayout.EAST);
        card.add(progressRow);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));

        // progress bar
        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(60);
        progress.setBorderPainted(false);
        progress.setBackground(BORDER_LIGHT);
        progress.setForeground(PRIMARY);
        progress.setPreferredSize(new Dimension(0, 8));
        progress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        card.add(progress);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        JLabel note = new JLabel("Gói Premium của bạn sẽ hết hạn trong 15 ngày.");
        note.setFont(bodySmall());
        note.setForeground(TEXT_SECONDARY);
        card.add(note);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        JButton btn = new JButton("GIA HẠN NGAY");
        btn.setFont(fontBold(FONT_SIZE_SM));
        btn.setBackground(TEXT_PRIMARY);
        btn.setForeground(BG_SURFACE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(btn);

        return card;
    }

    /**
     * Main method for independent testing of this panel.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            frame.add(new org.jobportal.view.common.HeaderPanel(), BorderLayout.NORTH);
            frame.add(new org.jobportal.view.common.SidebarPanel(org.jobportal.view.common.SidebarPanel.Role.EMPLOYER), BorderLayout.WEST);
            frame.add(new EmployerDashboardPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
