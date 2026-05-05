package org.jobportal.view.employer;

import org.jobportal.bll.impl.CategoryService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.enums.JobType;
import org.jobportal.model.Category;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.jobportal.view.util.DesignSystem.*;

public class RecruitmentFormPanel extends JPanel {

    private final RecruitmentService recruitmentService = new RecruitmentService();
    private final CategoryService categoryService = new CategoryService();
    private JTextField txtTitle;
    private JComboBox<String> cbCategory;
    private JComboBox<String> cbJobType;
    private JTextField txtSalary;
    private JTextField txtDueDate;
    private JTextField txtLocation;
    private JTextArea txtDescription;
    private List<Category> categories;
    private String editingRecruitmentId = null;

    public RecruitmentFormPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_6)));

        mainContent.add(createFormCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createAdviceBanner());

        add(createScrollPane(mainContent), BorderLayout.CENTER);

        loadCategories();
    }

    private void loadCategories() {
        categories = categoryService.getAllCategories();
        cbCategory.removeAllItems();
        for (Category cat : categories) {
            cbCategory.addItem(cat.getCategoryName());
        }
    }

    private String getSelectedCategoryId() {
        int idx = cbCategory.getSelectedIndex();
        if (idx >= 0 && categories != null && idx < categories.size()) {
            return categories.get(idx).getCategoryId();
        }
        return null;
    }

    private JobType getSelectedJobType() {
        String selected = (String) cbJobType.getSelectedItem();
        if (selected == null) return JobType.FULLTIME;
        switch (selected) {
            case "Part-time": return JobType.PARTTIME;
            case "Internship": return JobType.INTERNSHIP;
            default: return JobType.FULLTIME;
        }
    }

    public void loadRecruitmentForEdit(String recruitmentId) {
        this.editingRecruitmentId = recruitmentId;
        var dto = recruitmentService.getRecruitmentById(recruitmentId);
        if (dto == null) return;
        txtTitle.setText(dto.getTitle());
        txtSalary.setText(dto.getSalary() != null ? String.valueOf(dto.getSalary()) : "");
        txtDueDate.setText(dto.getDueDate() != null ? dto.getDueDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
        txtLocation.setText(dto.getLocation() != null ? dto.getLocation() : "");
        txtDescription.setText(dto.getDescription() != null ? dto.getDescription() : "");
        if (dto.getJobType() != null) {
            cbJobType.setSelectedItem(dto.getJobType() == JobType.FULLTIME ? "Full-time" :
                    dto.getJobType() == JobType.PARTTIME ? "Part-time" : "Internship");
        }
        if (categories != null && dto.getCategoryId() != null) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getCategoryId().equals(dto.getCategoryId())) {
                    cbCategory.setSelectedIndex(i);
                    break;
                }
            }
        }
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

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(BG_SURFACE);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, SPACE_2, SPACE_5);
        gbc.weightx = 0.5;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, SPACE_2, 0);
        formGrid.add(createFieldLabel("TIÊU ĐỀ CÔNG VIỆC"), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        txtTitle = createFormTextField("VD: Senior Frontend Developer (Tailwind CSS)");
        formGrid.add(txtTitle, gbc);

        gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, SPACE_2, SPACE_5);
        gbc.gridx = 0; gbc.gridy = 2;
        formGrid.add(createFieldLabel("NGÀNH NGHỀ"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_2, 0);
        formGrid.add(createFieldLabel("LOẠI HÌNH"), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(0, 0, SPACE_5, SPACE_5);
        cbCategory = createFormComboBox(new String[]{});
        formGrid.add(cbCategory, gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        cbJobType = createFormComboBox(new String[]{"Full-time", "Part-time", "Internship"});
        formGrid.add(cbJobType, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.insets = new Insets(0, 0, SPACE_2, SPACE_5);
        formGrid.add(createFieldLabel("MỨC LƯƠNG"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_2, 0);
        formGrid.add(createFieldLabel("HẠN NỘP HỒ SƠ"), gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.insets = new Insets(0, 0, SPACE_5, SPACE_5);
        txtSalary = createFormTextField("VD: 20 - 30 triệu");
        formGrid.add(txtSalary, gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        txtDueDate = createFormTextField("dd/MM/yyyy");
        formGrid.add(txtDueDate, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, SPACE_2, 0);
        formGrid.add(createFieldLabel("ĐỊA ĐIỂM"), gbc);

        gbc.gridy = 7; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        txtLocation = createFormTextField("VD: Hà Nội");
        formGrid.add(txtLocation, gbc);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, SPACE_2, 0);
        formGrid.add(createFieldLabel("MÔ TẢ CÔNG VIỆC"), gbc);

        gbc.gridy = 9; gbc.insets = new Insets(0, 0, SPACE_5, 0);
        txtDescription = new JTextArea("Nhập chi tiết công việc, yêu cầu và quyền lợi...");
        txtDescription.setFont(body());
        txtDescription.setForeground(TEXT_MUTED);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_4, SPACE_4, SPACE_4, SPACE_4)
        ));
        txtDescription.setRows(6);
        formGrid.add(new JScrollPane(txtDescription), gbc);

        card.add(formGrid);

        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_LIGHT);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(separator);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

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

    private JPanel createActionButtons() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_4, 0));
        actionPanel.setBackground(BG_SURFACE);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnPublish = createPrimaryButton("▶  Lưu & Đăng tin");
        btnPublish.setPreferredSize(new Dimension(180, BUTTON_HEIGHT));
        btnPublish.addActionListener(e -> saveRecruitment());
        actionPanel.add(btnPublish);

        return actionPanel;
    }

    private void saveRecruitment() {
        String title = txtTitle.getText().trim();
        String categoryId = getSelectedCategoryId();
        JobType jobType = getSelectedJobType();
        double salary = 0;
        try { salary = Double.parseDouble(txtSalary.getText().trim()); } catch (Exception ignored) {}
        LocalDate dueDate = null;
        try { dueDate = LocalDate.parse(txtDueDate.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")); } catch (Exception ignored) {}
        String description = txtDescription.getText().trim();
        String location = txtLocation.getText().trim();

        boolean ok;
        if (editingRecruitmentId != null) {
            ok = recruitmentService.updateRecruitment(editingRecruitmentId, title, categoryId, jobType, salary, dueDate, description, location);
        } else {
            ok = recruitmentService.postRecruitment(title, categoryId, jobType, salary, dueDate, description, location);
        }
        if (ok) {
            JOptionPane.showMessageDialog(this, "Lưu tin thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Lưu tin thất bại.");
        }
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
