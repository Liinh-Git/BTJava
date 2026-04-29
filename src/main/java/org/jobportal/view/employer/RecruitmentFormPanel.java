package org.jobportal.view.employer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RecruitmentFormPanel extends JPanel {

    public RecruitmentFormPanel() {
        // thiet lap layout chinh
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. tieu de trang
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. the form nhap lieu
        mainContent.add(createFormCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. banner loi khuyen
        mainContent.add(createAdviceBanner());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(248, 249, 250));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Đăng tin tuyển dụng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));

        JLabel lblSub = new JLabel("Tạo tin tuyển dụng mới để tiếp cận hàng nghìn ứng viên tiềm năng.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(108, 117, 125));

        header.add(lblTitle);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(lblSub);

        return header;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // su dung gridbaglayout de chia cot chinh xac
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(Color.WHITE);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 8, 20); // bottom va right padding
        gbc.weightx = 0.5;

        // dong 1: Tieu de cong viec (chiem 2 cot)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formGrid.add(createLabel("TIÊU ĐỀ CÔNG VIỆC"), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 20, 0);
        formGrid.add(createTextField("VD: Senior Frontend Developer (Tailwind CSS)"), gbc);

        // dong 2: Nganh nghe & Loai hinh
        gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, 8, 20);
        gbc.gridx = 0; gbc.gridy = 2;
        formGrid.add(createLabel("NGÀNH NGHỀ"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 8, 0);
        formGrid.add(createLabel("LOẠI HÌNH"), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(0, 0, 20, 20);
        formGrid.add(createComboBox(new String[]{"Công nghệ thông tin", "Marketing", "Kế toán"}), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 20, 0);
        formGrid.add(createComboBox(new String[]{"Toàn thời gian", "Bán thời gian", "Thực tập"}), gbc);

        // dong 3: Muc luong & Han nop ho so
        gbc.gridx = 0; gbc.gridy = 4; gbc.insets = new Insets(0, 0, 8, 20);
        formGrid.add(createLabel("MỨC LƯƠNG"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 8, 0);
        formGrid.add(createLabel("HẠN NỘP HỒ SƠ"), gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.insets = new Insets(0, 0, 20, 20);
        formGrid.add(createTextField("VD: 20 - 30 triệu"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 20, 0);
        formGrid.add(createTextField("mm/dd/yyyy"), gbc);

        // dong 4: Mo ta cong viec (chiem 2 cot)
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 8, 0);
        formGrid.add(createLabel("MÔ TẢ CÔNG VIỆC"), gbc);

        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 20, 0);
        formGrid.add(createEditorField("Nhập chi tiết công việc, yêu cầu và quyền lợi..."), gbc);

        card.add(formGrid);

        // duong ke ngang
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(233, 236, 239));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(separator);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // cac nut thao tac
        card.add(createActionButtons());

        return card;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(73, 80, 87));
        return lbl;
    }

    private JTextField createTextField(String placeholder) {
        JTextField txt = new JTextField(placeholder);
        txt.setPreferredSize(new Dimension(0, 42));
        txt.setForeground(new Color(108, 117, 125));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setPreferredSize(new Dimension(0, 42));
        cb.setBackground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(5, 5, 5, 5)
        ));
        return cb;
    }

    private JPanel createEditorField(String placeholder) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(new Color(206, 212, 218), 1));

        // thanh toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        toolbar.setBackground(new Color(248, 249, 250));
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(206, 212, 218)));

        JLabel btnB = new JLabel("B"); btnB.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel btnI = new JLabel("I"); btnI.setFont(new Font("Serif", Font.ITALIC, 14));
        JLabel btnList = new JLabel("≡");
        JLabel btnLink = new JLabel("🔗");

        toolbar.add(btnB);
        toolbar.add(btnI);
        toolbar.add(btnList);
        toolbar.add(btnLink);
        panel.add(toolbar, BorderLayout.NORTH);

        // phan text
        JTextArea textArea = new JTextArea(placeholder);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setForeground(new Color(108, 117, 125));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(0, 180));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionButtons() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnDraft = new JButton("Lưu nháp");
        btnDraft.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDraft.setBackground(Color.WHITE);
        btnDraft.setForeground(new Color(33, 37, 41));
        btnDraft.setBorder(new LineBorder(new Color(206, 212, 218), 1));
        btnDraft.setPreferredSize(new Dimension(120, 45));
        btnDraft.setFocusPainted(false);
        btnDraft.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnPublish = new JButton("Lưu & Đăng tin");
        btnPublish.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPublish.setBackground(new Color(13, 110, 253));
        btnPublish.setForeground(Color.WHITE);
        btnPublish.setBorderPainted(false);
        btnPublish.setPreferredSize(new Dimension(160, 45));
        btnPublish.setFocusPainted(false);
        btnPublish.setCursor(new Cursor(Cursor.HAND_CURSOR));

        actionPanel.add(btnDraft);
        actionPanel.add(btnPublish);
        return actionPanel;
    }

    private JPanel createAdviceBanner() {
        JPanel banner = new JPanel(new BorderLayout(15, 0));
        banner.setBackground(new Color(244, 248, 253));
        banner.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(205, 220, 240), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcon = new JLabel("ⓘ");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblIcon.setForeground(new Color(13, 110, 253));
        banner.add(lblIcon, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(244, 248, 253));

        JLabel lblTitle = new JLabel("LỜI KHUYÊN CHO NHÀ TUYỂN DỤNG");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(73, 80, 87));

        JLabel lblDesc = new JLabel("Mô tả công việc càng chi tiết và rõ ràng về mức lương sẽ giúp tăng tỉ lệ ứng tuyển chất lượng lên đến 40%.");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(new Color(73, 80, 87));

        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(lblDesc);

        banner.add(textPanel, BorderLayout.CENTER);

        return banner;
    }
}