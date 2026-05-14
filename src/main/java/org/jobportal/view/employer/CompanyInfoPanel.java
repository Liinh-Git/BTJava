package org.jobportal.view.employer;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.UserService;
import org.jobportal.bll.interfaces.IUserService;
import org.jobportal.dto.UserDTO;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CompanyInfoPanel extends JPanel {

    private final IUserService userService = new UserService();
    private JTextField txtCompanyName;
    private JTextField txtAddress;
    private JTextArea txtDescription;

    public CompanyInfoPanel() {
        // thiet lap layout chinh
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250)); // mau nen xam nhat

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. tieu de trang voi duong gach chan xanh
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. the form nhap lieu thong tin
        mainContent.add(createFormCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. cac nut thao tac (Huy, Luu thay doi)
        mainContent.add(createActionButtons());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // 4. cac the thong ke trang thai ben duoi
        mainContent.add(createStatsRow());

        loadData();

        // boc vao scroll pane de co the cuon
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        UserDTO user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            UserDTO empInfo = userService.getEmployerInfo(user.getUserId());
            if (empInfo != null) {
                if (empInfo.getCompanyName() != null) txtCompanyName.setText(empInfo.getCompanyName());
                if (empInfo.getCompanyAddress() != null) txtAddress.setText(empInfo.getCompanyAddress());
                else txtAddress.setText("");
                if (empInfo.getCompanyDescription() != null) txtDescription.setText(empInfo.getCompanyDescription());
                else txtDescription.setText("");
            }
        }
    }

    private JPanel createPageHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Thông tin công ty");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 37, 41));

        // duong gach chan mau xanh duoi tieu de
        JPanel underline = new JPanel();
        underline.setBackground(new Color(13, 110, 253));
        underline.setPreferredSize(new Dimension(50, 4));
        underline.setMaximumSize(new Dimension(50, 4));
        underline.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        headerPanel.add(underline);

        return headerPanel;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // Ten cong ty
        JPanel namePanel = createInputGroup("TÊN CÔNG TY", "Nhập tên chính thức của công ty", false);
        txtCompanyName = (JTextField) namePanel.getComponent(2);
        
        card.add(namePanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Dia chi
        JPanel addrPanel = createInputGroup("ĐỊA CHỈ TRỤ SỞ", "Số nhà, tên đường, quận/huyện, thành phố", false);
        txtAddress = (JTextField) addrPanel.getComponent(2);
        card.add(addrPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Mo ta cong ty (TextArea)
        JPanel descPanel = createInputGroup("MÔ TẢ CÔNG TY", "Giới thiệu chi tiết về lịch sử, sứ mệnh và định hướng phát triển của công ty...", true);
        txtDescription = (JTextArea) ((JScrollPane) descPanel.getComponent(2)).getViewport().getView();
        card.add(descPanel);

        return card;
    }

    private JPanel createInputGroup(String label, String placeholder, boolean isTextArea) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(108, 117, 125));

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        if (isTextArea) {
            JTextArea txtArea = new JTextArea(placeholder);
            txtArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtArea.setForeground(new Color(108, 117, 125));
            txtArea.setLineWrap(true);
            txtArea.setWrapStyleWord(true);
            txtArea.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(226, 230, 234), 1),
                    new EmptyBorder(15, 15, 15, 15)
            ));

            JScrollPane scroll = new JScrollPane(txtArea);
            scroll.setBorder(null);
            scroll.setPreferredSize(new Dimension(0, 180));
            scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(scroll);
        } else {
            JTextField txtField = new JTextField(placeholder);
            txtField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtField.setForeground(new Color(108, 117, 125));
            txtField.setPreferredSize(new Dimension(0, 45));
            txtField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            txtField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(226, 230, 234), 1),
                    new EmptyBorder(5, 15, 5, 15)
            ));
            txtField.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(txtField);
        }

        return panel;
    }

    private JPanel createActionButtons() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(new Color(248, 249, 250));
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnSave = new JButton("Lưu thay đổi");
        btnSave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSave.setBackground(new Color(13, 110, 253));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorderPainted(false);
        btnSave.setPreferredSize(new Dimension(140, 45));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> {
            if (SessionManager.getInstance().getCurrentUser() != null) {
                boolean success = userService.updateCompanyInfo(
                    txtCompanyName.getText().trim(),
                    txtAddress.getText().trim(),
                    txtDescription.getText().trim()
                );
                if (success) {
                    org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Cập nhật thông tin công ty thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Không thể cập nhật thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        actionPanel.add(btnSave);
        return actionPanel;
    }

    private JPanel createStatsRow() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(248, 249, 250));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        statsPanel.add(createStatCard("TRẠNG THÁI HỒ SƠ", "Hoàn thiện 75%", "v")); // v gia lap icon
        statsPanel.add(createStatCard("LƯỢT XEM THÁNG NÀY", "1,240", "o"));
        statsPanel.add(createStatCard("CẬP NHẬT CUỐI", "2 ngày trước", "c"));

        return statsPanel;
    }

    private JPanel createStatCard(String label, String value, String iconTxt) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcon = new JLabel(iconTxt);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblIcon.setForeground(new Color(13, 110, 253));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLabel.setForeground(new Color(108, 117, 125));

        topPanel.add(lblIcon);
        topPanel.add(lblLabel);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValue.setForeground(Color.BLACK);
        lblValue.setBorder(new EmptyBorder(10, 5, 0, 0));
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(topPanel);
        card.add(lblValue);

        return card;
    }

    // ham main kiem tra giao dien doc lap
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Portal - Thong tin cong ty");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 850);
            frame.setLayout(new BorderLayout());

            // Header o NORTH
            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            // Sidebar o WEST
            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.EMPLOYER);
            frame.add(sidebar, BorderLayout.WEST);

            // Giao dien chinh o CENTER
            CompanyInfoPanel companyPanel = new CompanyInfoPanel();
            frame.add(companyPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}