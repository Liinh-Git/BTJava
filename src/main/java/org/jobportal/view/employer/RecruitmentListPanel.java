package org.jobportal.view.employer;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

/**
 * Recruitment list panel matching qu_n_l_tin_ng stitch design.
 * Layout: page header (title + "Tạo tin mới" button) + 4-col stats row
 *         + filter section + job listing table + pagination
 *         + bottom insights (tip card + payment card).
 */
public class RecruitmentListPanel extends JPanel {

    public RecruitmentListPanel() {
        // thiet lap layout chinh
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        // 1. tieu de trang va nut tao moi
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 2. thong ke nhanh
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 3. thanh tim kiem va loc
        mainContent.add(createFilterSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        // 4. bang danh sach tin dang
        mainContent.add(createJobListTable());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 5. goi y va thanh toan
        mainContent.add(createBottomInsights());

        add(createScrollPane(mainContent), BorderLayout.CENTER);
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
        JPanel panel = new JPanel(new GridLayout(1, 4, SPACE_4, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        panel.add(createStatCard("ĐANG HIỂN THỊ", "12", TEXT_PRIMARY));
        panel.add(createStatCard("TỔNG LƯỢT XEM", "1,482", TEXT_PRIMARY));
        panel.add(createStatCard("ỨNG VIÊN MỚI", "24", PRIMARY));
        panel.add(createStatCard("HẾT HẠN", "3", TEXT_PRIMARY));

        return panel;
    }

    private JPanel createStatCard(String label, String value, Color valueColor) {
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

        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang hiển thị", "Tạm dừng", "Bản nháp"});
        cbStatus.setPreferredSize(new Dimension(160, 36));
        cbStatus.setBackground(BG_SURFACE);
        cbStatus.setFont(body());

        left.add(txtSearch);
        left.add(cbStatus);

        JLabel right = new JLabel("Hiển thị 1-10 của 15 tin");
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

        // header
        tableContainer.add(createRow("TIÊU ĐỀ TIN", null, "TRẠNG THÁI", "NGÀY ĐĂNG", "ỨNG TUYỂN", true));

        // data rows
        tableContainer.add(createRow("Senior Frontend Developer (React)", "JB-2023-001", "Đang hiển thị", "15/10/2023", "12 hồ sơ", false));
        tableContainer.add(createRow("UI/UX Designer", "JB-2023-002", "Đang hiển thị", "12/10/2023", "8 hồ sơ", false));
        tableContainer.add(createRow("Marketing Executive", "JB-2023-003", "Tạm dừng", "05/10/2023", "4 hồ sơ", false));
        tableContainer.add(createRow("Content Writer (Part-time)", "JB-2023-004", "Bản nháp", "---", "N/A", false));
        tableContainer.add(createRow("DevOps Engineer", "JB-2023-005", "Đang hiển thị", "01/10/2023", "18 hồ sơ", false));

        // phan trang
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_SURFACE);
        footer.setBorder(new EmptyBorder(SPACE_4, SPACE_5, SPACE_4, SPACE_5));

        JLabel lblCount = new JLabel("Hiển thị 5 trên tổng số 15 tin đăng");
        lblCount.setFont(bodySmall());
        lblCount.setForeground(TEXT_MUTED);
        footer.add(lblCount, BorderLayout.WEST);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_1, 0));
        pagination.setBackground(BG_SURFACE);
        pagination.add(createPageButton("<", false));
        pagination.add(createPageButton("1", true));
        pagination.add(createPageButton("2", false));
        pagination.add(createPageButton("3", false));
        pagination.add(createPageButton("...", false));
        pagination.add(createPageButton("8", false));
        pagination.add(createPageButton(">", false));
        footer.add(pagination, BorderLayout.EAST);

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

        // col 1: Title
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

        // col 2: Status
        gbc.gridx = 1; gbc.weightx = 0.15; gbc.insets = new Insets(SPACE_3, SPACE_3, SPACE_3, SPACE_3);
        if (isHeader) {
            JLabel lbl = new JLabel(status); lbl.setFont(hFont); lbl.setForeground(hColor);
            row.add(lbl, gbc);
        } else {
            row.add(createStatusBadge(status), gbc);
        }

        // col 3: Date
        gbc.gridx = 2; gbc.weightx = 0.15;
        JLabel lblDate = new JLabel(date);
        lblDate.setFont(isHeader ? hFont : bFont);
        lblDate.setForeground(isHeader ? hColor : TEXT_SECONDARY);
        row.add(lblDate, gbc);

        // col 4: Applicants
        gbc.gridx = 3; gbc.weightx = 0.15;
        JLabel lblApplicants = new JLabel(applicants);
        lblApplicants.setFont(isHeader ? hFont : bFont);
        lblApplicants.setForeground(isHeader ? hColor : TEXT_PRIMARY);
        row.add(lblApplicants, gbc);

        // col 5: Actions
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

            JLabel btnDelete = new JLabel("🗑");
            btnDelete.setFont(fontRegular(18));
            btnDelete.setForeground(DANGER);
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));

            actionPanel.add(btnEdit);
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
            case "Tạm dừng":
                bg = BADGE_BG; fg = BADGE_FG; break;
            case "Bản nháp":
                bg = BADGE_WARNING_BG; fg = BADGE_WARNING_FG; break;
            default:
                bg = BADGE_BG; fg = BADGE_FG; break;
        }
        return createBadge(status, bg, fg);
    }

    private JPanel createBottomInsights() {
        JPanel panel = new JPanel(new GridLayout(1, 2, SPACE_5, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // tip card
        JPanel tipCard = new JPanel();
        tipCard.setLayout(new BoxLayout(tipCard, BoxLayout.Y_AXIS));
        tipCard.setBackground(BG_SURFACE);
        tipCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(SPACE_5, SPACE_5, SPACE_5, SPACE_5)
        ));

        JLabel tipTitle = new JLabel("Gợi ý tuyển dụng");
        tipTitle.setFont(fontBold(FONT_SIZE_LG));
        tipTitle.setForeground(TEXT_PRIMARY);

        JPanel tipContent = new JPanel(new BorderLayout(SPACE_3, 0));
        tipContent.setBackground(BG_SURFACE);

        JLabel tipIcon = new JLabel("💡");
        tipIcon.setFont(fontRegular(20));
        tipContent.add(tipIcon, BorderLayout.WEST);

        JPanel tipTextPanel = new JPanel();
        tipTextPanel.setLayout(new BoxLayout(tipTextPanel, BoxLayout.Y_AXIS));
        tipTextPanel.setBackground(BG_SURFACE);

        JLabel tipBold = new JLabel("Tối ưu hóa mô tả công việc");
        tipBold.setFont(fontBold(FONT_SIZE_BASE));
        tipBold.setForeground(TEXT_PRIMARY);

        JLabel tipText = new JLabel("<html>Tin đăng \"Senior Frontend Developer\" của bạn có lượt xem cao nhưng tỷ lệ ứng tuyển thấp. Hãy cân nhắc điều chỉnh yêu cầu kỹ năng.</html>");
        tipText.setFont(body());
        tipText.setForeground(TEXT_SECONDARY);

        tipTextPanel.add(tipBold);
        tipTextPanel.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        tipTextPanel.add(tipText);
        tipContent.add(tipTextPanel, BorderLayout.CENTER);

        tipCard.add(tipTitle);
        tipCard.add(Box.createRigidArea(new Dimension(0, SPACE_3)));
        tipCard.add(tipContent);

        // payment card
        JPanel paymentCard = new JPanel();
        paymentCard.setLayout(new BoxLayout(paymentCard, BoxLayout.Y_AXIS));
        paymentCard.setBackground(BG_SURFACE);
        paymentCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(SPACE_5, SPACE_5, SPACE_5, SPACE_5)
        ));

        JLabel payTitle = new JLabel("Thanh toán & Gói tin");
        payTitle.setFont(fontBold(FONT_SIZE_LG));
        payTitle.setForeground(TEXT_PRIMARY);

        JPanel payRow = new JPanel(new BorderLayout());
        payRow.setBackground(BG_SURFACE);

        JPanel payLeft = new JPanel();
        payLeft.setLayout(new BoxLayout(payLeft, BoxLayout.Y_AXIS));
        payLeft.setBackground(BG_SURFACE);
        payLeft.add(new JLabel("Gói dịch vụ: Professional Enterprise") {{
            setFont(body()); setForeground(TEXT_SECONDARY);
        }});
        payLeft.add(new JLabel("Hết hạn sau 14 ngày") {{
            setFont(bodySmall()); setForeground(TEXT_MUTED);
        }});
        payRow.add(payLeft, BorderLayout.WEST);

        JLabel lblRenew = new JLabel("Gia hạn ngay");
        lblRenew.setFont(fontBold(FONT_SIZE_BASE));
        lblRenew.setForeground(PRIMARY);
        lblRenew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        payRow.add(lblRenew, BorderLayout.EAST);

        paymentCard.add(payTitle);
        paymentCard.add(Box.createRigidArea(new Dimension(0, SPACE_3)));
        paymentCard.add(payRow);

        panel.add(tipCard);
        panel.add(paymentCard);
        return panel;
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