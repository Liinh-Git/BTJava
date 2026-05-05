package org.jobportal.view.employer;

import org.jobportal.bll.impl.UserService;
import org.jobportal.dto.UserDTO;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

public class CompanyInfoPanel extends JPanel {

    private final UserService userService = new UserService();
    private JTextField txtName;
    private JTextField txtAddress;
    private JTextArea txtDesc;

    public CompanyInfoPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_6)));

        mainContent.add(createFormCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createActionButtons());

        add(createScrollPane(mainContent), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        String userId = org.jobportal.utils.SessionManager.getCurrentUser().getUserId();
        UserDTO dto = userService.getEmployerInfo(userId);
        if (dto == null) return;
        txtName.setText(dto.getCompanyName() != null ? dto.getCompanyName() : "");
        txtAddress.setText(dto.getCompanyAddress() != null ? dto.getCompanyAddress() : "");
        txtDesc.setText(dto.getCompanyDescription() != null ? dto.getCompanyDescription() : "");
    }

    private JPanel createPageHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_PAGE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = createPageTitle("Thông tin công ty");
        JLabel lblSub = createPageSubtitle("Cập nhật thông tin công ty để ứng viên dễ dàng tìm kiếm và liên hệ.");

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
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_3, 0));
        header.setBackground(BG_SURFACE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcon = new JLabel("🖼");
        lblIcon.setFont(fontRegular(20));
        header.add(lblIcon);

        JLabel lblTitle = new JLabel("THÔNG TIN CÔNG TY");
        lblTitle.setFont(fontBold(FONT_SIZE_BASE));
        lblTitle.setForeground(TEXT_PRIMARY);
        header.add(lblTitle);

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_SURFACE);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, SPACE_1, 0);
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        form.add(createFieldLabel("TÊN CÔNG TY"), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        txtName = createInputField("");
        form.add(txtName, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, SPACE_1, 0);
        form.add(createFieldLabel("ĐỊA CHỈ TRỤ SỞ"), gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        txtAddress = createInputField("");
        form.add(txtAddress, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, SPACE_1, 0);
        form.add(createFieldLabel("MÔ TẢ CÔNG TY"), gbc);

        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, 0);
        txtDesc = new JTextArea("");
        txtDesc.setFont(body());
        txtDesc.setForeground(TEXT_PRIMARY);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setBackground(BG_PAGE);
        txtDesc.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_4, SPACE_4, SPACE_4, SPACE_4)
        ));
        txtDesc.setRows(6);
        form.add(new JScrollPane(txtDesc), gbc);

        card.add(form);
        return card;
    }

    private JPanel createActionButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_3, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(body());
        btnCancel.setBackground(BG_SURFACE);
        btnCancel.setForeground(TEXT_PRIMARY);
        btnCancel.setPreferredSize(new Dimension(100, 40));
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnCancel);

        JButton btnSave = createPrimaryButton("Lưu thay đổi");
        btnSave.setPreferredSize(new Dimension(160, 40));
        btnSave.addActionListener(e -> {
            boolean ok = userService.updateCompanyInfo(txtName.getText().trim(), txtAddress.getText().trim(), txtDesc.getText().trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
            }
        });
        panel.add(btnSave);

        return panel;
    }

    private JTextField createInputField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(body());
        field.setForeground(TEXT_PRIMARY);
        field.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_2, SPACE_3, SPACE_2, SPACE_3)
        ));
        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Portal - Thông tin công ty");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 900);
            frame.setLayout(new BorderLayout());

            frame.add(new HeaderPanel(), BorderLayout.NORTH);
            frame.add(new SidebarPanel(SidebarPanel.Role.EMPLOYER), BorderLayout.WEST);
            frame.add(new CompanyInfoPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}