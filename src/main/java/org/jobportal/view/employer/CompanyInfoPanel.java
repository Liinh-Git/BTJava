package org.jobportal.view.employer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

/**
 * Company info panel matching th_ng_tin_c_ng_ty_minimal_1 stitch design.
 * Layout: page title with blue underline accent + form card (TÊN CÔNG TY,
 *         ĐỊA CHỈ TRỤ SỞ, MÔ TẢ CÔNG TY) + Hủy/Lưu thay đổi buttons
 *         + 3-col stats cards (TRẠNG THÁI HỒ SƠ, LƯỢT XEM, CẬP NHẬT CUỐI).
 */
public class CompanyInfoPanel extends JPanel {

    public CompanyInfoPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        // 1. tieu de trang voi duong gach xanh
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_6)));

        // 2. form card
        mainContent.add(createFormCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 3. cac nut hanh dong
        mainContent.add(createActionButtons());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        // 4. the thong ke
        mainContent.add(createStatsRow());

        add(createScrollPane(mainContent), BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_PAGE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = createPageTitle("Thông tin công ty");
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(lblTitle);
        header.add(Box.createRigidArea(new Dimension(0, SPACE_2)));

        // duong ke xanh accent
        JPanel blueLine = new JPanel();
        blueLine.setBackground(PRIMARY);
        blueLine.setPreferredSize(new Dimension(60, 3));
        blueLine.setMaximumSize(new Dimension(60, 3));
        blueLine.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(blueLine);

        return header;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_8, SPACE_8, SPACE_8, SPACE_8)
        ));

        // TÊN CÔNG TY
        card.add(createFieldLabel("TÊN CÔNG TY"));
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        JTextField txtName = createFormField("Nhập tên chính thức của công ty");
        txtName.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtName);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // ĐỊA CHỈ TRỤ SỞ
        card.add(createFieldLabel("ĐỊA CHỈ TRỤ SỞ"));
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        JTextField txtAddress = createFormField("Số nhà, tên đường, quận/huyện, thành phố");
        txtAddress.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtAddress);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // MÔ TẢ CÔNG TY
        card.add(createFieldLabel("MÔ TẢ CÔNG TY"));
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));

        JTextArea txtDesc = new JTextArea("Giới thiệu chi tiết về lịch sử, sứ mệnh và định hướng phát triển của công ty...");
        txtDesc.setFont(body());
        txtDesc.setForeground(TEXT_MUTED);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setBackground(BG_SURFACE);
        txtDesc.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_3, SPACE_3, SPACE_3, SPACE_3)
        ));
        txtDesc.setRows(8);
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scroll = new JScrollPane(txtDesc);
        scroll.setBorder(null);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(scroll);

        return card;
    }

    private JTextField createFormField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(body());
        field.setForeground(TEXT_MUTED);
        field.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, INPUT_HEIGHT));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_2, SPACE_3, SPACE_2, SPACE_3)
        ));
        return field;
    }

    private JPanel createActionButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_4, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancel = createOutlineButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(100, BUTTON_HEIGHT));
        panel.add(btnCancel);

        JButton btnSave = createPrimaryButton("Lưu thay đổi");
        btnSave.setPreferredSize(new Dimension(160, BUTTON_HEIGHT));
        panel.add(btnSave);

        return panel;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 3, SPACE_5, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        panel.add(createStatCard("⊘", "TRẠNG THÁI HỒ SƠ", "Hoàn thiện 75%"));
        panel.add(createStatCard("◉", "LƯỢT XEM THÁNG NÀY", "1,240"));
        panel.add(createStatCard("↻", "CẬP NHẬT CUỐI", "2 ngày trước"));

        return panel;
    }

    private JPanel createStatCard(String icon, String label, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_4, SPACE_5, SPACE_4, SPACE_5)
        ));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        topRow.setBackground(BG_SURFACE);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(fontRegular(16));
        lblIcon.setForeground(PRIMARY);
        topRow.add(lblIcon);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(tableHeader());
        lblLabel.setForeground(TEXT_MUTED);
        topRow.add(lblLabel);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(fontBold(FONT_SIZE_LG));
        lblValue.setForeground(TEXT_PRIMARY);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(topRow);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        card.add(lblValue);

        return card;
    }

    // ham test giao dien doc lap
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Portal - Thông tin công ty");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            frame.add(new org.jobportal.view.common.HeaderPanel(), BorderLayout.NORTH);
            frame.add(new org.jobportal.view.common.SidebarPanel(org.jobportal.view.common.SidebarPanel.Role.EMPLOYER), BorderLayout.WEST);
            frame.add(new CompanyInfoPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}