package org.jobportal.view.employer;
import org.jobportal.bll.impl.CategoryService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.model.Category;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.enums.JobType;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecruitmentFormPanel extends JPanel {

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private final CategoryService categoryService = new CategoryService();
    private List<Category> categoryList = new ArrayList<>();

    private JTextField txtTitle;
    private JComboBox<String> cbCategory;
    private JComboBox<String> cbJobType;
    private JTextField txtSalary;
    private JSpinner txtDueDate; // Doi thanh JSpinner de chon ngay thang
    private JTextArea txtDescription;

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
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        formGrid.add(createLabel("TIÊU ĐỀ CÔNG VIỆC"), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 20, 0);
        txtTitle = createTextField("VD: Senior Frontend Developer (Tailwind CSS)");
        formGrid.add(txtTitle, gbc);

        // dong 2: Nganh nghe & Loai hinh
        gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, 8, 20);
        gbc.gridx = 0; gbc.gridy = 2;
        formGrid.add(createLabel("NGÀNH NGHỀ"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 8, 0);
        formGrid.add(createLabel("LOẠI HÌNH"), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(0, 0, 20, 20);
        categoryList = categoryService.getAllCategories();
        String[] catNames = categoryList.stream().map(Category::getCategoryName).toArray(String[]::new);
        cbCategory = createComboBox(catNames.length > 0 ? catNames : new String[]{"Công nghệ thông tin", "Marketing", "Kế toán"});
        formGrid.add(cbCategory, gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 20, 0);
        cbJobType = createComboBox(new String[]{"Full-time", "Part-time", "Internship"});
        formGrid.add(cbJobType, gbc);

        // dong 3: Muc luong & Han nop ho so
        gbc.gridx = 0; gbc.gridy = 4; gbc.insets = new Insets(0, 0, 8, 20);
        formGrid.add(createLabel("MỨC LƯƠNG"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 8, 0);
        formGrid.add(createLabel("HẠN NỘP HỒ SƠ"), gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.insets = new Insets(0, 0, 20, 20);
        txtSalary = createTextField("VD: 20000000"); // Numeric only for BLL double parsing
        formGrid.add(txtSalary, gbc);

        // Thay the TextField bang JSpinner chon ngay thang nam
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 20, 0);
        txtDueDate = createDateSpinner();
        formGrid.add(txtDueDate, gbc);

        // dong 4: Mo ta cong viec (chiem 2 cot)
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 8, 0);
        formGrid.add(createLabel("MÔ TẢ CÔNG VIỆC"), gbc);

        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 20, 0);
        JPanel editorPanel = createEditorField("Nhập chi tiết công việc, yêu cầu và quyền lợi...");
        txtDescription = (JTextArea) ((JScrollPane) editorPanel.getComponent(0)).getViewport().getView();
        formGrid.add(editorPanel, gbc);

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

    // Tao text field voi FocusListener de xoa/hien thi lai placeholder khi click
    private JTextField createTextField(String placeholder) {
        JTextField txt = new JTextField(placeholder);
        txt.setPreferredSize(new Dimension(0, 42));
        txt.setForeground(new Color(156, 163, 175)); // Mau xam cho placeholder
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        // Focus listener: xoa placeholder khi click vao, hien lai khi de trong
        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txt.getText().equals(placeholder)) {
                    txt.setText("");
                    txt.setForeground(new Color(33, 37, 41)); // Mau chu binh thuong
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txt.getText().trim().isEmpty()) {
                    txt.setText(placeholder);
                    txt.setForeground(new Color(156, 163, 175)); // Mau xam placeholder
                }
            }
        });
        return txt;
    }

    // Tao JSpinner chon ngay thang nam cho han nop ho so
    private JSpinner createDateSpinner() {
        // Mac dinh hien thi ngay mai
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = cal.getTime();

        // Gioi han: tu hom nay tro di
        SpinnerDateModel model = new SpinnerDateModel(
            tomorrow,       // Gia tri ban dau
            new Date(),     // Min: hom nay
            null,           // Max: khong gioi han
            Calendar.DAY_OF_MONTH
        );
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        spinner.setPreferredSize(new Dimension(0, 42));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinner.setBorder(new LineBorder(new Color(206, 212, 218), 1));
        // Style cho text ben trong
        editor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        editor.getTextField().setHorizontalAlignment(JTextField.LEFT);
        editor.getTextField().setBorder(new EmptyBorder(5, 10, 5, 5));
        return spinner;
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

        // phan text
        JTextArea textArea = new JTextArea(placeholder);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setForeground(new Color(156, 163, 175));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        // Focus listener cho textarea
        textArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(new Color(33, 37, 41));
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textArea.getText().trim().isEmpty()) {
                    textArea.setText(placeholder);
                    textArea.setForeground(new Color(156, 163, 175));
                }
            }
        });

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

        JButton btnPublish = new JButton("Lưu & Đăng tin");
        btnPublish.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPublish.setBackground(new Color(13, 110, 253));
        btnPublish.setForeground(Color.WHITE);
        btnPublish.setBorderPainted(false);
        btnPublish.setPreferredSize(new Dimension(160, 45));
        btnPublish.setFocusPainted(false);
        btnPublish.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPublish.addActionListener(e -> submitRecruitment());

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

    private void submitRecruitment() {
        if (SessionManager.getInstance().getCurrentUser() == null) return;
        
        try {
            String titleVal = txtTitle.getText().trim();
            // Kiem tra neu van la placeholder thi bao loi
            if (titleVal.isEmpty() || titleVal.equals("VD: Senior Frontend Developer (Tailwind CSS)")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tiêu đề công việc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String salaryText = txtSalary.getText().trim();
            if (salaryText.isEmpty() || salaryText.equals("VD: 20000000")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mức lương!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double salary = Double.parseDouble(salaryText);

            // Doc ngay tu JSpinner
            Date selectedDate = (Date) txtDueDate.getValue();
            LocalDate dueDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            String desc = txtDescription.getText().trim();
            if (desc.isEmpty() || desc.equals("Nhập chi tiết công việc, yêu cầu và quyền lợi...")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mô tả công việc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int catIdx = cbCategory.getSelectedIndex();
            String catId = (catIdx >= 0 && catIdx < categoryList.size()) ? categoryList.get(catIdx).getCategoryId() : null;
            
            JobType jobType = JobType.FULLTIME;
            if (cbJobType.getSelectedIndex() == 1) jobType = JobType.PARTTIME;
            else if (cbJobType.getSelectedIndex() == 2) jobType = JobType.INTERNSHIP;
            // No FREELANCE in enum, fallback to PARTTIME
            else if (cbJobType.getSelectedIndex() == 3) jobType = JobType.PARTTIME;
            
            boolean success = recruitmentService.postRecruitment(titleVal, catId, jobType, salary, dueDate, desc, null); // location will be resolved from company info in BLL
            if (success) {
                JOptionPane.showMessageDialog(this, "Đăng tin tuyển dụng thành công, chờ kiểm duyệt!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null) {
                    window.dispose();
                    return;
                }
                // Reset form: thay placeholder
                txtTitle.setText("VD: Senior Frontend Developer (Tailwind CSS)");
                txtTitle.setForeground(new Color(156, 163, 175));
                txtSalary.setText("VD: 20000000");
                txtSalary.setForeground(new Color(156, 163, 175));
                txtDescription.setText("Nhập chi tiết công việc, yêu cầu và quyền lợi...");
                txtDescription.setForeground(new Color(156, 163, 175));
                // Reset spinner ve ngay mai
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 1);
                txtDueDate.setValue(cal.getTime());
            } else {
                JOptionPane.showMessageDialog(this, "Không thể đăng tin. Vui lòng kiểm tra lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng lương (chỉ nhập số)!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
    }
}
