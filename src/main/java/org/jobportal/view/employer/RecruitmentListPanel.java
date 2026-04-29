package org.jobportal.view.employer;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RecruitmentListPanel extends JPanel {

    public RecruitmentListPanel() {
        // thiet lap layout chinh
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. tieu de trang va nut tao moi
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. thanh tim kiem va loc
        mainContent.add(createFilterSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. bang danh sach tin dang
        mainContent.add(createJobListTable());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(248, 249, 250));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(248, 249, 250));

        JLabel lblTitle = new JLabel("Quản lý tin đăng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel lblSub = new JLabel("Theo dõi và quản lý các chiến dịch tuyển dụng của bạn.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);

        leftPanel.add(lblTitle);
        leftPanel.add(lblSub);

        JButton btnCreate = new JButton("+ Đăng tin mới");
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCreate.setBackground(new Color(13, 110, 253));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFocusPainted(false);
        btnCreate.setBorderPainted(false);
        btnCreate.setPreferredSize(new Dimension(140, 40));
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // DA SUA LOI XUNG DOT TAI DAY
        btnCreate.addActionListener(e -> {
            // lay frame cha chua panel de lam chu the cho popup
            Window ancestor = SwingUtilities.getWindowAncestor(this);
            if (ancestor instanceof Frame) {
                // tao mot JDialog de lam popup
                JDialog dialog = new JDialog((Frame) ancestor, "Đăng tin tuyển dụng", true);
                dialog.setSize(950, 750);
                dialog.setLocationRelativeTo(ancestor);

                // dua cai JPanel form cua ban vao trong JDialog nay
                dialog.add(new RecruitmentFormPanel());
                dialog.setVisible(true);
            }
        });

        header.add(leftPanel, BorderLayout.WEST);
        header.add(btnCreate, BorderLayout.EAST);

        return header;
    }

    private JPanel createFilterSection() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filterPanel.setBackground(new Color(248, 249, 250));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 38));
        txtSearch.setText("Tìm kiếm theo tiêu đề...");
        txtSearch.setForeground(Color.GRAY);

        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang hoạt động", "Hết hạn", "Bản nháp"});
        cbStatus.setPreferredSize(new Dimension(150, 38));
        cbStatus.setBackground(Color.WHITE);

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSearch.setPreferredSize(new Dimension(100, 38));
        btnSearch.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        filterPanel.add(txtSearch);
        filterPanel.add(cbStatus);
        filterPanel.add(btnSearch);

        return filterPanel;
    }

    private JPanel createJobListTable() {
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new LineBorder(new Color(230, 230, 230), 1));

        // header cua bang
        tableContainer.add(createRow("TIÊU ĐỀ CÔNG VIỆC", "NGÀY ĐĂNG - HẾT HẠN", "TRẠNG THÁI", "ỨNG VIÊN", "THAO TÁC", true));

        // du lieu mau cac tin dang
        tableContainer.add(createRow("Senior Frontend Developer", "01/04/2026 - 30/04/2026", "Đang hoạt động", "12", "", false));
        tableContainer.add(createRow("Backend Engineer (Java/Spring)", "15/03/2026 - 15/04/2026", "Hết hạn", "45", "", false));
        tableContainer.add(createRow("Product Manager", "25/04/2026 - 25/05/2026", "Bản nháp", "0", "", false));
        tableContainer.add(createRow("UI/UX Designer", "10/02/2026 - 10/03/2026", "Hết hạn", "28", "", false));

        // phan trang
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblCount = new JLabel("Hiển thị 4 trên tổng số 15 tin đăng");
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCount.setForeground(Color.GRAY);
        footer.add(lblCount, BorderLayout.WEST);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pagination.setBackground(Color.WHITE);
        pagination.add(createPageBtn("1", true));
        pagination.add(createPageBtn("2", false));
        pagination.add(createPageBtn("3", false));
        pagination.add(createPageBtn(">", false));
        footer.add(pagination, BorderLayout.EAST);

        tableContainer.add(footer);

        return tableContainer;
    }

    private JPanel createRow(String col1, String col2, String status, String applicants, String action, boolean isHeader) {
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(isHeader ? new Color(250, 250, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        row.setPreferredSize(new Dimension(0, 70));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.BLACK;

        // Tieu de
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 25));
        p1.setOpaque(false);
        JLabel l1 = new JLabel(col1);
        l1.setFont(font); l1.setForeground(textColor);
        p1.add(l1);
        row.add(p1);

        // Thoi gian
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 25));
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2);
        l2.setFont(font); l2.setForeground(textColor);
        p2.add(l2);
        row.add(p2);

        // Trang thai
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
        p3.setOpaque(false);
        if (isHeader) {
            JLabel l3 = new JLabel(status);
            l3.setFont(font); l3.setForeground(textColor);
            p3.add(l3);
        } else {
            p3.add(createStatusBadge(status));
        }
        row.add(p3);

        // So ung vien
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 25));
        p4.setOpaque(false);
        if (isHeader) {
            JLabel l4 = new JLabel(applicants);
            l4.setFont(font); l4.setForeground(textColor);
            p4.add(l4);
        } else {
            JLabel l4 = new JLabel(applicants + " ứng viên");
            l4.setFont(new Font("Segoe UI", Font.BOLD, 13));
            l4.setForeground(new Color(13, 110, 253));
            l4.setCursor(new Cursor(Cursor.HAND_CURSOR));
            p4.add(l4);
        }
        row.add(p4);

        // Thao tac
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        p5.setOpaque(false);
        if (isHeader) {
            JLabel l5 = new JLabel(action);
            l5.setFont(font); l5.setForeground(textColor);
            p5.add(l5);
        } else {
            JButton btnEdit = new JButton("Sửa");
            btnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnEdit.setBackground(Color.WHITE);

            JButton btnDelete = new JButton("Xóa");
            btnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnDelete.setBackground(Color.WHITE);
            btnDelete.setForeground(Color.RED);

            p5.add(btnEdit);
            p5.add(btnDelete);
        }
        row.add(p5);

        return row;
    }

    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel("  " + status + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setOpaque(true);

        switch (status) {
            case "Đang hoạt động":
                badge.setBackground(new Color(230, 250, 240));
                badge.setForeground(new Color(40, 167, 69));
                break;
            case "Hết hạn":
                badge.setBackground(new Color(250, 230, 230));
                badge.setForeground(new Color(220, 53, 69));
                break;
            case "Bản nháp":
                badge.setBackground(new Color(240, 240, 240));
                badge.setForeground(Color.GRAY);
                break;
        }

        badge.setBorder(new LineBorder(badge.getForeground(), 1, true));
        return badge;
    }

    private JButton createPageBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        if (active) {
            btn.setBackground(new Color(13, 110, 253));
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        }
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Portal - Quan ly tin dang");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            // Header add vao NORTH cua frame
            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            // Sidebar add vao WEST
            SidebarPanel sidebar = new SidebarPanel(SidebarPanel.Role.EMPLOYER);
            frame.add(sidebar, BorderLayout.WEST);

            // Panel danh sach tin dang add vao CENTER
            RecruitmentListPanel listPanel = new RecruitmentListPanel();
            frame.add(listPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}