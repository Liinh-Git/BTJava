package org.jobportal.view.admin;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.CategoryService;
import org.jobportal.model.Category;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CategoryManagementPanel extends JPanel {

    private final CategoryService categoryService = new CategoryService();
    private JPanel tableContainer;
    private JLabel lblCount;
    private JTextField txtNewCategory;

    public CategoryManagementPanel() {
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

        // 2. form them danh muc moi
        mainContent.add(createAddCategorySection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 3. bang danh sach danh muc va phan trang
        tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableContainer.setBorder(new LineBorder(new Color(226, 230, 234), 1));
        
        mainContent.add(tableContainer);
        
        loadData();

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Quản lý danh mục");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 37, 41));

        JLabel lblSub = new JLabel("Cấu hình các nhóm ngành nghề và danh mục tuyển dụng trên hệ thống.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(108, 117, 125));

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(lblSub);

        return headerPanel;
    }

    private JPanel createAddCategorySection() {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        txtNewCategory = new JTextField("Tên danh mục mới...");
        txtNewCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNewCategory.setForeground(new Color(108, 117, 125));
        txtNewCategory.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 230, 234), 1),
                new EmptyBorder(5, 15, 5, 15)
        ));
        txtNewCategory.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtNewCategory.getText().equals("Tên danh mục mới...")) {
                    txtNewCategory.setText("");
                    txtNewCategory.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtNewCategory.getText().isEmpty()) {
                    txtNewCategory.setText("Tên danh mục mới...");
                    txtNewCategory.setForeground(new Color(108, 117, 125));
                }
            }
        });

        JButton btnAdd = new JButton("+ Thêm");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setBackground(new Color(13, 110, 253));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(120, 40));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> {
            String catName = txtNewCategory.getText().trim();
            if (catName.isEmpty() || catName.equals("Tên danh mục mới...")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên danh mục!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean success = categoryService.addCategory(catName);
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã thêm danh mục!");
                txtNewCategory.setText("Tên danh mục mới...");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm danh mục!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(txtNewCategory, BorderLayout.CENTER);
        card.add(btnAdd, BorderLayout.EAST);

        return card;
    }

    private void loadData() {
        tableContainer.removeAll();
        // Header cua bang
        tableContainer.add(createTableRow("MÃ\n(CATEGORYID)", "TÊN DANH MỤC (CATEGORYNAME)", "THAO TÁC", true, null));

        List<Category> categories = categoryService.getAllCategories();
        
        for (Category cat : categories) {
            tableContainer.add(createTableRow(String.valueOf(cat.getCategoryId()), cat.getCategoryName(), "", false, cat));
        }

        // Footer phan trang
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));

        if (lblCount == null) {
            lblCount = new JLabel();
            lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblCount.setForeground(Color.GRAY);
        }
        lblCount.setText("Hiển thị " + categories.size() + " danh mục");
        footer.add(lblCount, BorderLayout.WEST);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pagination.setBackground(Color.WHITE);
        pagination.add(createPageBtn("1", true));
        footer.add(pagination, BorderLayout.EAST);

        tableContainer.add(footer);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private JPanel createTableRow(String col1, String col2, String action, boolean isHeader, Category category) {
        // dung GridBagLayout de chia cot chinh xac hon (20% - 60% - 20%)
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? new Color(248, 249, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        row.setPreferredSize(new Dimension(0, 65));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? new Color(108, 117, 125) : new Color(33, 37, 41);

        // Cot 1: Ma danh muc
        gbc.gridx = 0; gbc.weightx = 0.2;
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        p1.setOpaque(false);
        JTextArea l1 = new JTextArea(col1); // dung JTextArea de ho tro xuong dong cho header
        l1.setFont(font); l1.setForeground(textColor);
        l1.setOpaque(false); l1.setEditable(false); l1.setFocusable(false);
        p1.add(l1);
        row.add(p1, gbc);

        // Cot 2: Ten danh muc
        gbc.gridx = 1; gbc.weightx = 0.6;
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2);
        l2.setFont(font); l2.setForeground(textColor);
        p2.add(l2);
        row.add(p2, gbc);

        // Cot 3: Thao tac
        gbc.gridx = 2; gbc.weightx = 0.2;
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20)); // Giam khoang cach
        p3.setOpaque(false);
        if (isHeader) {
            JLabel l3 = new JLabel("THAO TÁC");
            l3.setFont(font); l3.setForeground(textColor);
            p3.add(l3);
        } else {
            JButton btnEdit = new JButton("Đổi tên");
            btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnEdit.setBackground(new Color(13, 110, 253)); // Xanh nuoc bien
            btnEdit.setForeground(Color.WHITE);
            btnEdit.setFocusPainted(false);
            btnEdit.setBorderPainted(false);
            btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEdit.addActionListener(e -> {
                if (category != null) {
                    String newName = JOptionPane.showInputDialog(CategoryManagementPanel.this, "Nhập tên mới:", category.getCategoryName());
                    if (newName != null && !newName.trim().isEmpty()) {
                        boolean success = categoryService.updateCategory(category.getCategoryId(), newName.trim());
                        if (success) {
                            JOptionPane.showMessageDialog(CategoryManagementPanel.this, "Đã cập nhật thành công!");
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(CategoryManagementPanel.this, "Lỗi khi cập nhật!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            JButton btnDelete = new JButton("Xóa");
            btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnDelete.setBackground(new Color(220, 53, 69)); // Do
            btnDelete.setForeground(Color.WHITE);
            btnDelete.setFocusPainted(false);
            btnDelete.setBorderPainted(false);
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDelete.addActionListener(e -> {
                if (category != null) {
                    int choice = JOptionPane.showConfirmDialog(CategoryManagementPanel.this, "Bạn có chắc muốn xóa danh mục này?", "Cảnh báo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (choice == JOptionPane.YES_OPTION) {
                        boolean success = categoryService.deleteCategory(category.getCategoryId());
                        if (success) {
                            JOptionPane.showMessageDialog(CategoryManagementPanel.this, "Đã xóa danh mục!");
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(CategoryManagementPanel.this, "Lỗi khi xóa danh mục!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            p3.add(btnEdit);
            p3.add(btnDelete);
        }
        row.add(p3, gbc);

        return row;
    }

    private JButton createPageBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (active) {
            btn.setBackground(new Color(230, 240, 255));
            btn.setForeground(new Color(13, 110, 253));
            btn.setBorder(new LineBorder(new Color(13, 110, 253), 1));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.DARK_GRAY);
            if (text.equals("...")) {
                btn.setBorderPainted(false);
            } else {
                btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
            }
        }
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Ham main de kiem tra giao dien
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Portal - Quan ly danh muc");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            // Header o NORTH
            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            // Sidebar o WEST (Gia lap Role ADMIN)
            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.ADMIN);
            frame.add(sidebar, BorderLayout.WEST);

            // Giao dien chinh o CENTER
            CategoryManagementPanel categoryPanel = new CategoryManagementPanel();
            frame.add(categoryPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}