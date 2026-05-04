package org.jobportal.view.employer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

/**
 * Application review panel matching duy_t_h_s_ng_vi_n stitch design.
 * Layout: breadcrumb + page header (title + Bộ lọc / Xuất báo cáo buttons)
 *         + 4-col stats row + applicant table with avatar/actions
 *         + pagination + bottom insights (AI suggestions + boost card).
 */
public class ApplicationReviewPanel extends JPanel {

    public ApplicationReviewPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        // 1. breadcrumb
        mainContent.add(createBreadcrumb());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_2)));

        // 2. tieu de trang va nut
        mainContent.add(createPageHeaderSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 3. thong ke nhanh
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 4. bang ung vien
        mainContent.add(createApplicantTable());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 5. goi y + boost
        mainContent.add(createBottomInsights());

        add(createScrollPane(mainContent), BorderLayout.CENTER);
    }

    private JPanel createBreadcrumb() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRecruitment = new JLabel("Recruitment");
        lblRecruitment.setFont(body());
        lblRecruitment.setForeground(TEXT_SECONDARY);

        JLabel lblSep = new JLabel("›");
        lblSep.setFont(body());
        lblSep.setForeground(TEXT_MUTED);

        JLabel lblJob = new JLabel("Senior Frontend Developer");
        lblJob.setFont(fontBold(FONT_SIZE_BASE));
        lblJob.setForeground(PRIMARY);

        panel.add(lblRecruitment);
        panel.add(lblSep);
        panel.add(lblJob);
        return panel;
    }

    private JPanel createPageHeaderSection() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PAGE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(BG_PAGE);

        JLabel lblTitle = createPageTitle("Duyệt hồ sơ ứng viên");
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblSub = createPageSubtitle("Đang xem 12 hồ sơ cho vị trí Senior Frontend Developer");
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(lblTitle);
        left.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        left.add(lblSub);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_3, 0));
        rightActions.setBackground(BG_PAGE);

        JButton btnFilter = createOutlineButton("≡  Bộ lọc");
        btnFilter.setPreferredSize(new Dimension(110, 40));
        rightActions.add(btnFilter);

        JButton btnExport = createPrimaryButton("↓  Xuất báo cáo");
        btnExport.setPreferredSize(new Dimension(160, 40));
        rightActions.add(btnExport);

        header.add(left, BorderLayout.WEST);
        header.add(rightActions, BorderLayout.EAST);
        return header;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 4, SPACE_4, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        panel.add(createStatCard("TỔNG SỐ ỨNG VIÊN", "128", TEXT_PRIMARY, "↗  12% so với tháng trước", SUCCESS));
        panel.add(createStatCard("ĐANG CHỜ DUYỆT", "45", PRIMARY, null, null));
        panel.add(createStatCard("ĐÃ PHỎNG VẤN", "18", PRIMARY, null, null));
        panel.add(createStatCard("TỈ LỆ CHẤP THUẬN", "14.2%", TEXT_PRIMARY, null, null));

        return panel;
    }

    private JPanel createStatCard(String label, String value, Color valueColor, String subText, Color subColor) {
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

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(heading2());
        lblValue.setForeground(valueColor);

        card.add(lblLabel);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        card.add(lblValue);

        if (subText != null) {
            card.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
            JLabel lblSub = new JLabel(subText);
            lblSub.setFont(bodySmall());
            lblSub.setForeground(subColor != null ? subColor : TEXT_MUTED);
            card.add(lblSub);
        }

        return card;
    }

    private JPanel createApplicantTable() {
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(BG_SURFACE);
        tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableContainer.setBorder(new LineBorder(BORDER, 1));

        // header row
        tableContainer.add(createApplicantRow(null, null, null, null, null, true));

        // data rows
        tableContainer.add(createApplicantRow("NH", "Nguyễn Hoàng Nam", "nam.nguyen@example.com", "12/10/2023", "PENDING", false));
        tableContainer.add(createApplicantRow("LT", "Lê Thị Thu Thảo", "thao.le@company.vn", "10/10/2023", "REVIEWING", false));
        tableContainer.add(createApplicantRow("TQ", "Trần Minh Quân", "quan.tm@techflow.io", "08/10/2023", "HIRED", false));
        tableContainer.add(createApplicantRow("PV", "Phạm Văn Duy", "duy.pv@outlook.com", "05/10/2023", "PENDING", false));

        // footer + pagination
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_SURFACE);
        footer.setBorder(new EmptyBorder(SPACE_3, SPACE_5, SPACE_3, SPACE_5));

        JLabel lblCount = new JLabel("Hiển thị 1 - 4 của 12 ứng viên");
        lblCount.setFont(bodySmall());
        lblCount.setForeground(TEXT_MUTED);
        footer.add(lblCount, BorderLayout.WEST);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_1, 0));
        pagination.setBackground(BG_SURFACE);
        pagination.add(createPageButton("<", false));
        pagination.add(createPageButton("1", true));
        pagination.add(createPageButton("2", false));
        pagination.add(createPageButton("3", false));
        pagination.add(createPageButton(">", false));
        footer.add(pagination, BorderLayout.EAST);

        tableContainer.add(footer);

        return tableContainer;
    }

    private JPanel createApplicantRow(String initials, String name, String email, String date, String status, boolean isHeader) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? BG_SURFACE_ALT : BG_SURFACE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setPreferredSize(new Dimension(0, 70));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        Font hFont = tableHeader();
        Color hColor = TEXT_MUTED;

        // col 1: Avatar + name
        gbc.gridx = 0; gbc.weightx = 0.3; gbc.insets = new Insets(SPACE_2, SPACE_5, SPACE_2, SPACE_3);
        if (isHeader) {
            JLabel lbl = new JLabel("ỨNG VIÊN"); lbl.setFont(hFont); lbl.setForeground(hColor);
            row.add(lbl, gbc);
        } else {
            JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_3, 0));
            cell.setOpaque(false);

            // avatar circle
            JLabel avatar = new JLabel(initials, SwingConstants.CENTER);
            avatar.setFont(fontBold(FONT_SIZE_SM));
            avatar.setForeground(BG_SURFACE);
            avatar.setOpaque(true);
            avatar.setBackground(PRIMARY);
            avatar.setPreferredSize(new Dimension(36, 36));

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);
            JLabel lblName = new JLabel(name);
            lblName.setFont(fontBold(FONT_SIZE_BASE));
            lblName.setForeground(TEXT_PRIMARY);
            JLabel lblEmail = new JLabel(email);
            lblEmail.setFont(bodySmall());
            lblEmail.setForeground(TEXT_MUTED);

            textPanel.add(lblName);
            textPanel.add(lblEmail);

            cell.add(avatar);
            cell.add(textPanel);
            row.add(cell, gbc);
        }

        // col 2: Date
        gbc.gridx = 1; gbc.weightx = 0.15; gbc.insets = new Insets(SPACE_2, SPACE_3, SPACE_2, SPACE_3);
        if (isHeader) {
            JLabel lbl = new JLabel("NGÀY NỘP"); lbl.setFont(hFont); lbl.setForeground(hColor);
            row.add(lbl, gbc);
        } else {
            JLabel lbl = new JLabel(date); lbl.setFont(body()); lbl.setForeground(TEXT_SECONDARY);
            row.add(lbl, gbc);
        }

        // col 3: Position
        gbc.gridx = 2; gbc.weightx = 0.2;
        if (isHeader) {
            JLabel lbl = new JLabel("VỊ TRÍ HIỆN TẠI"); lbl.setFont(hFont); lbl.setForeground(hColor);
            row.add(lbl, gbc);
        } else {
            String position = "";
            if ("Nguyễn Hoàng Nam".equals(name)) position = "UI/UX Designer";
            else if ("Lê Thị Thu Thảo".equals(name)) position = "Senior Frontend Engineer";
            else if ("Trần Minh Quân".equals(name)) position = "Fullstack Developer";
            else if ("Phạm Văn Duy".equals(name)) position = "Lead Developer";
            JLabel lbl = new JLabel(position); lbl.setFont(body()); lbl.setForeground(TEXT_PRIMARY);
            row.add(lbl, gbc);
        }

        // col 4: Status badge
        gbc.gridx = 3; gbc.weightx = 0.15;
        if (isHeader) {
            JLabel lbl = new JLabel("TRẠNG THÁI"); lbl.setFont(hFont); lbl.setForeground(hColor);
            row.add(lbl, gbc);
        } else {
            row.add(createApplicantStatusBadge(status), gbc);
        }

        // col 5: Actions
        gbc.gridx = 4; gbc.weightx = 0.2; gbc.insets = new Insets(SPACE_2, SPACE_3, SPACE_2, SPACE_5);
        if (isHeader) {
            JLabel lbl = new JLabel("THAO TÁC"); lbl.setFont(hFont); lbl.setForeground(hColor);
            row.add(lbl, gbc);
        } else {
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
            actions.setOpaque(false);

            JLabel btnView = createActionIcon("👁", TEXT_MUTED);
            JLabel btnApprove = createActionIcon("✓", SUCCESS);
            JLabel btnReject = createActionIcon("✕", DANGER);

            actions.add(btnView);
            actions.add(btnApprove);
            actions.add(btnReject);
            row.add(actions, gbc);
        }

        return row;
    }

    private JLabel createActionIcon(String icon, Color color) {
        JLabel lbl = new JLabel(icon);
        lbl.setFont(fontRegular(18));
        lbl.setForeground(color);
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl.setPreferredSize(new Dimension(32, 32));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private JLabel createApplicantStatusBadge(String status) {
        Color bg, fg;
        switch (status) {
            case "PENDING": bg = BADGE_BG; fg = BADGE_FG; break;
            case "REVIEWING": bg = BADGE_INFO_BG; fg = BADGE_INFO_FG; break;
            case "HIRED": bg = BADGE_SUCCESS_BG; fg = BADGE_SUCCESS_FG; break;
            case "REJECTED": bg = BADGE_DANGER_BG; fg = BADGE_DANGER_FG; break;
            default: bg = BADGE_BG; fg = BADGE_FG; break;
        }
        return createBadge(status, bg, fg);
    }

    private JPanel createBottomInsights() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // cot trai: AI suggestions
        gbc.gridx = 0; gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, 0, SPACE_5);
        panel.add(createAISuggestionsCard(), gbc);

        // cot phai: boost card
        gbc.gridx = 1; gbc.weightx = 0.4;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(createBoostCard(), gbc);

        return panel;
    }

    private JPanel createAISuggestionsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_5, SPACE_5, SPACE_5, SPACE_5)
        ));

        JLabel title = new JLabel("Gợi ý từ hệ thống AI");
        title.setFont(fontBold(FONT_SIZE_LG));
        title.setForeground(TEXT_PRIMARY);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        // suggestion 1
        card.add(createSuggestionItem(WARNING,
                "Ứng viên sáng giá nhất",
                "Lê Thị Thu Thảo có 95% tương thích với yêu cầu công việc dựa trên kỹ năng React và Node.js."));
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        // suggestion 2
        card.add(createSuggestionItem(DANGER,
                "Thời gian phỏng vấn",
                "Bạn còn 3 hồ sơ chưa phản hồi quá 48 giờ. Hãy kiểm tra lại để tránh mất ứng viên tốt."));

        return card;
    }

    private JPanel createSuggestionItem(Color accentColor, String title, String description) {
        JPanel item = new JPanel(new BorderLayout(SPACE_3, 0));
        item.setBackground(BG_SURFACE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, accentColor),
                new EmptyBorder(SPACE_3, SPACE_4, SPACE_3, SPACE_4)
        ));

        JLabel lblIcon = new JLabel(accentColor == WARNING ? "💡" : "📅");
        lblIcon.setFont(fontRegular(18));
        item.add(lblIcon, BorderLayout.WEST);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(BG_SURFACE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(fontBold(FONT_SIZE_BASE));
        lblTitle.setForeground(TEXT_PRIMARY);

        JLabel lblDesc = new JLabel("<html>" + description + "</html>");
        lblDesc.setFont(body());
        lblDesc.setForeground(TEXT_SECONDARY);

        text.add(lblTitle);
        text.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        text.add(lblDesc);

        item.add(text, BorderLayout.CENTER);
        return item;
    }

    private JPanel createBoostCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(59, 130, 246));
        card.setBorder(new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6));

        JLabel title = new JLabel("Nâng cấp tin đăng");
        title.setFont(fontBold(FONT_SIZE_LG));
        title.setForeground(Color.WHITE);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        JLabel desc = new JLabel("<html>Tiếp cận gấp 5 lần ứng viên tiềm năng bằng cách đẩy tin đăng của bạn lên trang đầu.</html>");
        desc.setFont(body());
        desc.setForeground(new Color(219, 234, 254));
        card.add(desc);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_6)));

        JButton btnBoost = new JButton("NÂNG CẤP NGAY");
        btnBoost.setFont(fontBold(FONT_SIZE_SM));
        btnBoost.setBackground(BG_SURFACE);
        btnBoost.setForeground(TEXT_PRIMARY);
        btnBoost.setFocusPainted(false);
        btnBoost.setBorderPainted(false);
        btnBoost.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBoost.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(btnBoost);

        return card;
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