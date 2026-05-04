package org.jobportal.view.employer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

/**
 * Recruitment form panel matching ng_tin_tuy_n_d_ng stitch design.
 * Layout: page header + form card (TIÊU ĐỀ, NGÀNH NGHỀ/LOẠI HÌNH,
 *         MỨC LƯƠNG/HẠN NỘP, MÔ TẢ CÔNG VIỆC with toolbar)
 *         + action buttons (Lưu nháp / Lưu & Đăng tin)
 *         + advice banner.
 */
public class RecruitmentFormPanel extends JPanel {

    public RecruitmentFormPanel() {
        // thiet lap layout chinh
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        // 1. tieu de trang
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_6)));

        // 2. the form nhap lieu
        mainContent.add(createFormCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 3. banner loi khuyen
        mainContent.add(createAdviceBanner());

        add(createScrollPane(mainContent), BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_PAGE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = createPageTitle("Đăng tin tuyển dụng");
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = createPageSubtitle("Tạo tin tuyển dụng mới để tiếp cận hàng nghìn ứng viên tiềm năng.");
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(lblTitle);
        header.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        header.add(lblSub);

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

        // su dung gridbaglayout de chia cot chinh xac
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(BG_SURFACE);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, SPACE_2, SPACE_5);
        gbc.weightx = 0.5;

        // dong 1: Tieu de cong viec (chiem 2 cot)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, SPACE_2, 0);
        formGrid.add(createFieldLabel("TIÊU ĐỀ CÔNG VIỆC"), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        formGrid.add(createFormTextField("VD: Senior Frontend Developer (Tailwind CSS)"), gbc);

        // dong 2: Nganh nghe & Loai hinh
        gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, SPACE_2, SPACE_5);
        gbc.gridx = 0; gbc.gridy = 2;
        formGrid.add(createFieldLabel("NGÀNH NGHỀ"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_2, 0);
        formGrid.add(createFieldLabel("LOẠI HÌNH"), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(0, 0, SPACE_5, SPACE_5);
        formGrid.add(createFormComboBox(new String[]{"Công nghệ thông tin", "Marketing", "Kế toán"}), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        formGrid.add(createFormComboBox(new String[]{"Toàn thời gian", "Bán thời gian", "Thực tập"}), gbc);

        // dong 3: Muc luong & Han nop ho so
        gbc.gridx = 0; gbc.gridy = 4; gbc.insets = new Insets(0, 0, SPACE_2, SPACE_5);
        formGrid.add(createFieldLabel("MỨC LƯƠNG"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_2, 0);
        formGrid.add(createFieldLabel("HẠN NỘP HỒ SƠ"), gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.insets = new Insets(0, 0, SPACE_5, SPACE_5);
        formGrid.add(createFormTextField("VD: 20 - 30 triệu"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        formGrid.add(createFormTextField("mm/dd/yyyy"), gbc);

        // dong 4: Mo ta cong viec (chiem 2 cot)
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, SPACE_2, 0);
        formGrid.add(createFieldLabel("MÔ TẢ CÔNG VIỆC"), gbc);

        gbc.gridy = 7; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        formGrid.add(createEditorField("Nhập chi tiết công việc, yêu cầu và quyền lợi..."), gbc);

        card.add(formGrid);

        // duong ke ngang
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_LIGHT);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(separator);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // cac nut thao tac
        card.add(createActionButtons());

        return card;
    }

    private JTextField createFormTextField(String placeholder) {
        JTextField txt = new JTextField(placeholder);
        txt.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        txt.setForeground(TEXT_MUTED);
        txt.setFont(body());
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_INPUT, 1),
            new EmptyBorder(SPACE_2, SPACE_3, SPACE_2, SPACE_3)
        ));
        return txt;
    }

    private JComboBox<String> createFormComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        cb.setBackground(BG_SURFACE);
        cb.setFont(body());
        cb.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_1, SPACE_1, SPACE_1, SPACE_1)
        ));
        return cb;
    }

    private JPanel createEditorField(String placeholder) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(BORDER_INPUT, 1));

        // thanh toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_3, SPACE_2));
        toolbar.setBackground(BG_PAGE);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_INPUT));

        JLabel btnB = new JLabel("B"); btnB.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel btnI = new JLabel("I"); btnI.setFont(new Font("Serif", Font.ITALIC, 14));
        JLabel btnList = new JLabel("≡"); btnList.setFont(fontRegular(14));
        JLabel btnLink = new JLabel("🔗"); btnLink.setFont(fontRegular(14));

        toolbar.add(btnB);
        toolbar.add(btnI);
        toolbar.add(btnList);
        toolbar.add(btnLink);
        panel.add(toolbar, BorderLayout.NORTH);

        // phan text
        JTextArea textArea = new JTextArea(placeholder);
        textArea.setFont(body());
        textArea.setForeground(TEXT_MUTED);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(SPACE_4, SPACE_4, SPACE_4, SPACE_4));

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(0, 180));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionButtons() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_4, 0));
        actionPanel.setBackground(BG_SURFACE);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnDraft = createOutlineButton("Lưu nháp");
        btnDraft.setPreferredSize(new Dimension(130, BUTTON_HEIGHT));
        actionPanel.add(btnDraft);

        JButton btnPublish = createPrimaryButton("▶  Lưu & Đăng tin");
        btnPublish.setPreferredSize(new Dimension(180, BUTTON_HEIGHT));
        actionPanel.add(btnPublish);

        return actionPanel;
    }

    private JPanel createAdviceBanner() {
        JPanel banner = new JPanel(new BorderLayout(SPACE_4, 0));
        banner.setBackground(new Color(244, 248, 253));
        banner.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(205, 220, 240), 1),
            new EmptyBorder(SPACE_5, SPACE_5, SPACE_5, SPACE_5)
        ));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcon = new JLabel("ⓘ");
        lblIcon.setFont(fontRegular(24));
        lblIcon.setForeground(PRIMARY);
        banner.add(lblIcon, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(244, 248, 253));

        JLabel lblTitle = new JLabel("LỜI KHUYÊN CHO NHÀ TUYỂN DỤNG");
        lblTitle.setFont(fontBold(FONT_SIZE_SM));
        lblTitle.setForeground(TEXT_PRIMARY);

        JLabel lblDesc = new JLabel("Mô tả công việc càng chi tiết và rõ ràng về mức lương sẽ giúp tăng tỉ lệ ứng tuyển chất lượng lên đến 40%.");
        lblDesc.setFont(body());
        lblDesc.setForeground(TEXT_SECONDARY);

        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        textPanel.add(lblDesc);

        banner.add(textPanel, BorderLayout.CENTER);

        return banner;
    }

    /**
     * Main method for independent testing of this panel.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Recruitment Form");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            frame.add(new org.jobportal.view.common.HeaderPanel(), BorderLayout.NORTH);
            frame.add(new org.jobportal.view.common.SidebarPanel(org.jobportal.view.common.SidebarPanel.Role.EMPLOYER), BorderLayout.WEST);
            frame.add(new RecruitmentFormPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
