package org.jobportal.view.employer;

import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.enums.AdminStatus;
import org.jobportal.enums.JobType;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.utils.SessionManager;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class RecruitmentListPanel extends JPanel {

    private static final String STATUS_ALL = "Tất cả trạng thái";
    private static final String STATUS_OPEN = "Đang hoạt động";
    private static final String STATUS_PENDING = "Chờ duyệt";
    private static final String STATUS_REJECTED = "Bị từ chối";
    private static final String STATUS_CLOSED = "Đã đóng";
    private static final String STATUS_EXPIRED = "Hết hạn";
    private static final String STATUS_DRAFT = "Bản nháp";
    private static final String SEARCH_PLACEHOLDER = "Tìm kiếm theo tiêu đề...";

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private JPanel tableContainer;
    private JLabel lblCount;
    private JPanel paginationPanel;
    private JTextField txtSearch;
    private JComboBox<String> cbStatus;

    private int currentPage = 1;
    private int pageSize = 10;
    private String currentKeyword = "";
    private String currentStatusFilter = STATUS_ALL;

    public RecruitmentListPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        mainContent.add(Box.createRigidArea(new Dimension(0, 16)));

        mainContent.add(createToolbarSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        mainContent.add(tableContainer);

        loadData();

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createToolbarSection() {
        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBackground(new Color(248, 249, 250));
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 38));
        txtSearch.setText(SEARCH_PLACEHOLDER);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
           
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (SEARCH_PLACEHOLDER.equals(txtSearch.getText())) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText(SEARCH_PLACEHOLDER);
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        cbStatus = new JComboBox<>(new String[]{
                STATUS_ALL,
                STATUS_OPEN,
                STATUS_CLOSED,
        });
        cbStatus.setPreferredSize(new Dimension(160, 38));
        cbStatus.setBackground(Color.WHITE);

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setPreferredSize(new Dimension(96, 38));
        btnSearch.setBackground(Color.WHITE);
        btnSearch.setBorder(new LineBorder(new Color(210, 210, 210), 1));
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (SEARCH_PLACEHOLDER.equals(keyword)) {
                keyword = "";
            }
            currentKeyword = keyword.toLowerCase();
            currentStatusFilter = (String) cbStatus.getSelectedItem();
            currentPage = 1;
            loadData();
        });

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        toolbar.add(txtSearch, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        toolbar.add(cbStatus, gbc);
        gbc.gridx = 2;
        toolbar.add(btnSearch, gbc);

        JButton btnCreate = new JButton("+ Đăng tin mới");
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCreate.setBackground(new Color(13, 110, 253));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFocusPainted(false);
        btnCreate.setBorderPainted(false);
        btnCreate.setPreferredSize(new Dimension(150, 40));
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreate.addActionListener(e -> openCreateDialog());

        gbc.gridx = 3;
        gbc.weightx = 1.0;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        toolbar.add(spacer, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        toolbar.add(btnCreate, gbc);
        return toolbar;
    }

    private void openCreateDialog() {
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        if (ancestor instanceof Frame) {
            JDialog dialog = new JDialog((Frame) ancestor, "Đăng tin tuyển dụng", true);
            dialog.setSize(950, 750);
            dialog.setLocationRelativeTo(ancestor);
            dialog.add(new RecruitmentFormPanel());
            dialog.setVisible(true);
            loadData();
        }
    }

    private void loadData() {
        tableContainer.removeAll();
        tableContainer.add(createRow("TIÊU ĐỀ CÔNG VIỆC", "NGÀY ĐĂNG - HẾT HẠN", "TRẠNG THÁI", "ỨNG VIÊN", "THAO TÁC", true, null));

        String employerId = SessionManager.getInstance().getEmployerId();
        List<RecruitmentDTO> allJobs = new ArrayList<>();
        if (employerId != null && !employerId.isEmpty()) {
            List<RecruitmentDTO> list = recruitmentService.getRecruitmentsByEmployer(employerId);
            if (list != null) {
                allJobs.addAll(list);
            }
        }

        List<RecruitmentDTO> jobs = new ArrayList<>();
        for (RecruitmentDTO job : allJobs) {
            String title = job.getTitle() != null ? job.getTitle().toLowerCase() : "";
            if (!currentKeyword.isEmpty() && !title.contains(currentKeyword)) {
                continue;
            }

            String status = resolveStatusText(job);
            if (!STATUS_ALL.equals(currentStatusFilter) && !status.equals(currentStatusFilter)) {
                continue;
            }
            jobs.add(job);
        }

        int maxPage = Math.max(1, (int) Math.ceil((double) jobs.size() / pageSize));
        if (currentPage > maxPage) {
            currentPage = maxPage;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, jobs.size());

        for (int i = start; i < end; i++) {
            RecruitmentDTO job = jobs.get(i);
            String title = job.getTitle() != null ? job.getTitle() : "(Trống tiêu đề)";
            String dateStr = (job.getCreatedDate() != null ? job.getCreatedDate().format(formatter) : "N/A")
                    + " - "
                    + (job.getDueDate() != null ? job.getDueDate().format(formatter) : "N/A");
            String status = resolveStatusText(job);
            String applicants = String.valueOf(job.getApplicationCount());
            tableContainer.add(createRow(title, dateStr, status, applicants, "", false, job));
        }

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));

        if (lblCount == null) {
            lblCount = new JLabel();
            lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblCount.setForeground(Color.GRAY);
        }
        lblCount.setText("Hiển thị " + jobs.size() + " tin đăng");
        footer.add(lblCount, BorderLayout.WEST);

        paginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        paginationPanel.setBackground(Color.WHITE);
        updatePaginationUI(jobs.size());
        footer.add(paginationPanel, BorderLayout.EAST);

        tableContainer.add(footer);
        tableContainer.revalidate();
        tableContainer.repaint();
        revalidate();
        repaint();
    }

    private String resolveStatusText(RecruitmentDTO job) {
        if (job == null) return STATUS_DRAFT;
        if (job.getAdminStatus() == AdminStatus.PENDING) return STATUS_PENDING;
        if (job.getAdminStatus() == AdminStatus.REJECTED) return STATUS_REJECTED;
        if (job.getAdminStatus() == AdminStatus.APPROVED) {
            if (job.getStatus() == RecruitmentStatus.OPEN) return STATUS_OPEN;
            if (job.getStatus() == RecruitmentStatus.CLOSED) return STATUS_CLOSED;
            if (job.getStatus() == RecruitmentStatus.EXPIRED) return STATUS_EXPIRED;
        }
        return STATUS_DRAFT;
    }

    private void updatePaginationUI(int totalItems) {
        if (paginationPanel == null) return;
        paginationPanel.removeAll();

        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / pageSize));

        JButton btnPrev = createPageBtn("<", false);
        if (currentPage > 1) {
            btnPrev.addActionListener(e -> {
                currentPage--;
                loadData();
            });
        } else {
            btnPrev.setEnabled(false);
        }
        paginationPanel.add(btnPrev);

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        if (startPage > 1) {
            JButton btnFirst = createPageBtn("1", false);
            btnFirst.addActionListener(e -> {
                currentPage = 1;
                loadData();
            });
            paginationPanel.add(btnFirst);
            if (startPage > 2) {
                JLabel dots = new JLabel("...");
                dots.setBorder(new EmptyBorder(0, 5, 0, 5));
                paginationPanel.add(dots);
            }
        }

        for (int i = startPage; i <= endPage; i++) {
            final int pageToLoad = i;
            boolean isCurrent = (i == currentPage);
            JButton btnPage = createPageBtn(String.valueOf(i), isCurrent);
            if (!isCurrent) {
                btnPage.addActionListener(e -> {
                    currentPage = pageToLoad;
                    loadData();
                });
            }
            paginationPanel.add(btnPage);
        }

        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                JLabel dots = new JLabel("...");
                dots.setBorder(new EmptyBorder(0, 5, 0, 5));
                paginationPanel.add(dots);
            }
            JButton btnLast = createPageBtn(String.valueOf(totalPages), false);
            btnLast.addActionListener(e -> {
                currentPage = totalPages;
                loadData();
            });
            paginationPanel.add(btnLast);
        }

        JButton btnNext = createPageBtn(">", false);
        if (currentPage < totalPages) {
            btnNext.addActionListener(e -> {
                currentPage++;
                loadData();
            });
        } else {
            btnNext.setEnabled(false);
        }
        paginationPanel.add(btnNext);

        paginationPanel.revalidate();
        paginationPanel.repaint();
    }

    private JPanel createRow(String col1, String col2, String status, String applicants, String action, boolean isHeader, RecruitmentDTO job) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? new Color(250, 250, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        row.setPreferredSize(new Dimension(0, isHeader ? 56 : 84));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, isHeader ? 56 : 84));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color headerColor = Color.GRAY;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Cột tiêu đề
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(8, 18, 8, 8);
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setOpaque(false);
        if (isHeader) {
            JLabel l1 = new JLabel(col1, SwingConstants.CENTER);
            l1.setFont(font);
            l1.setForeground(headerColor);
            p1.add(l1, BorderLayout.CENTER);
        } else {
            JTextArea l1 = new JTextArea(col1);
            l1.setEditable(false);
            l1.setOpaque(false);
            l1.setLineWrap(true);
            l1.setWrapStyleWord(true);
            l1.setFont(font);
            l1.setForeground(Color.BLACK);
            l1.setBorder(null);
            p1.add(l1, BorderLayout.CENTER);
        }
        row.add(p1, gbc);

        // Cột ngày
        gbc.gridx = 1;
        gbc.weightx = 0.22;
        gbc.insets = new Insets(8, 8, 8, 8);
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2, SwingConstants.CENTER);
        l2.setFont(font);
        l2.setForeground(isHeader ? headerColor : Color.BLACK);
        p2.add(l2);
        row.add(p2, gbc);

        // Cột trạng thái
        gbc.gridx = 2;
        gbc.weightx = 0.16;
        JPanel p3 = new JPanel(new GridBagLayout());
        p3.setOpaque(false);
        if (isHeader) {
            JLabel l3 = new JLabel(status, SwingConstants.CENTER);
            l3.setFont(font);
            l3.setForeground(headerColor);
            p3.add(l3);
        } else {
            p3.add(createStatusBadge(status));
        }
        row.add(p3, gbc);

        // Cột ứng viên
        gbc.gridx = 3;
        gbc.weightx = 0.11;
        JPanel p4 = new JPanel(new GridBagLayout());
        p4.setOpaque(false);
        if (isHeader) {
            JLabel l4 = new JLabel(applicants, SwingConstants.CENTER);
            l4.setFont(font);
            l4.setForeground(headerColor);
            p4.add(l4);
        } else {
            JLabel l4 = new JLabel(applicants + " ứng viên", SwingConstants.CENTER);
            l4.setFont(new Font("Segoe UI", Font.BOLD, 13));
            l4.setForeground(new Color(13, 110, 253));
            p4.add(l4);
        }
        row.add(p4, gbc);

        // Cột thao tác
        gbc.gridx = 4;
        gbc.weightx = 0.17;
        gbc.insets = new Insets(8, 8, 8, 16);
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 16));
        p5.setOpaque(false);

        if (isHeader) {
            JLabel l5 = new JLabel(action, SwingConstants.CENTER);
            l5.setFont(font);
            l5.setForeground(headerColor);
            p5.add(l5);
        } else {
            JButton btnDetail = createActionButton("Chi tiết", new Color(13, 110, 253), 78);
            btnDetail.addActionListener(e -> showDetailDialog(job));

            JButton btnClose = createActionButton("Đóng", new Color(108, 117, 125), 58);
            btnClose.addActionListener(e -> {
                if (job != null && job.getAdminStatus() == AdminStatus.APPROVED && job.getStatus() == RecruitmentStatus.OPEN) {
                    boolean success = recruitmentService.closeRecruitment(job.getRecruitmentId());
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Đã đóng tin tuyển dụng thành công!");
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể đóng tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Chỉ có thể đóng tin đang hoạt động!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            });

            JButton btnDelete = createActionButton("Xóa", new Color(220, 53, 69), 54);
            btnDelete.addActionListener(e -> {
                if (job != null) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Bạn có chắc muốn xóa tin này?",
                            "Xác nhận",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = recruitmentService.deleteRecruitment(job.getRecruitmentId());
                        if (success) {
                            JOptionPane.showMessageDialog(this, "Đã xóa tin tuyển dụng!");
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(this, "Không thể xóa tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            p5.add(btnDetail);
            p5.add(btnClose);
            p5.add(btnDelete);
        }

        row.add(p5, gbc);
        return row;
    }

    private void showDetailDialog(RecruitmentDTO selected) {
        if (selected == null) return;
        RecruitmentDTO job = recruitmentService.getRecruitmentById(selected.getRecruitmentId());
        if (job == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin tin đăng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        JTextField txtTitleEdit = new JTextField(valueOrEmpty(job.getTitle()));
        JTextField txtSalaryEdit = new JTextField(job.getSalary() != null ? String.valueOf(job.getSalary().longValue()) : "");
        JTextField txtDueDateEdit = new JTextField(job.getDueDate() != null ? job.getDueDate().format(df) : "");
        JTextField txtLocationEdit = new JTextField(valueOrEmpty(job.getLocation()));
        JTextArea txtDescEdit = new JTextArea(valueOrEmpty(job.getDescription()), 6, 30);
        txtDescEdit.setLineWrap(true);
        txtDescEdit.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(8, 8, 8, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Tiêu đề"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; form.add(txtTitleEdit, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; form.add(new JLabel("Lương"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; form.add(txtSalaryEdit, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; form.add(new JLabel("Hạn nộp (dd/MM/yyyy)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; form.add(txtDueDateEdit, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; form.add(new JLabel("Địa điểm"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; form.add(txtLocationEdit, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.NORTHWEST; form.add(new JLabel("Mô tả"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        form.add(new JScrollPane(txtDescEdit), gbc);

        JLabel lblHint = new JLabel("Chỉnh sửa trực tiếp thông tin tin tuyển dụng.");
        lblHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHint.setForeground(new Color(108, 117, 125));
        lblHint.setBorder(new EmptyBorder(8, 10, 0, 10));

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Chi tiết tin tuyển dụng", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.add(lblHint, BorderLayout.NORTH);
        dialog.add(new JScrollPane(form), BorderLayout.CENTER);

        JButton btnSave = new JButton("Lưu");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setBackground(new Color(13, 110, 253));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(120, 34));
        btnSave.addActionListener(e -> {
            String newTitle = txtTitleEdit.getText().trim();
            String salaryRaw = txtSalaryEdit.getText().trim().replace(",", "");
            String dueRaw = txtDueDateEdit.getText().trim();

            if (newTitle.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tiêu đề không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double salary;
            try {
                salary = salaryRaw.isEmpty() ? 0 : Double.parseDouble(salaryRaw);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Lương không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dueDate;
            try {
                dueDate = LocalDate.parse(dueRaw, df);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Hạn nộp không đúng định dạng dd/MM/yyyy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JobType jobType = job.getJobType() != null ? job.getJobType() : JobType.FULLTIME;
            boolean updated = recruitmentService.updateRecruitment(
                    job.getRecruitmentId(),
                    newTitle,
                    job.getCategoryId(),
                    jobType,
                    salary,
                    dueDate,
                    txtDescEdit.getText().trim(),
                    txtLocationEdit.getText().trim()
            );

            if (updated) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật tin tuyển dụng thành công!");
                dialog.dispose();
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Không thể cập nhật tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnClose = new JButton("Đóng");
        btnClose.setPreferredSize(new Dimension(90, 34));
        btnClose.addActionListener(e -> dialog.dispose());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        footer.add(btnClose);
        footer.add(btnSave);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.setSize(760, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String valueOrEmpty(String value) {
        return value != null ? value : "";
    }

    private JButton createActionButton(String text, Color color, int width) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width, 28));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(color);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new LineBorder(new Color(210, 210, 210), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel("  " + status + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setOpaque(true);

        switch (status) {
            case STATUS_OPEN:
                badge.setBackground(new Color(230, 250, 240));
                badge.setForeground(new Color(40, 167, 69));
                break;
            case STATUS_PENDING:
                badge.setBackground(new Color(255, 243, 205));
                badge.setForeground(new Color(133, 100, 4));
                break;
            case STATUS_REJECTED:
            case STATUS_CLOSED:
            case STATUS_EXPIRED:
                badge.setBackground(new Color(250, 230, 230));
                badge.setForeground(new Color(220, 53, 69));
                break;
            default:
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

            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.EMPLOYER);
            frame.add(sidebar, BorderLayout.WEST);

            RecruitmentListPanel listPanel = new RecruitmentListPanel();
            frame.add(listPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
