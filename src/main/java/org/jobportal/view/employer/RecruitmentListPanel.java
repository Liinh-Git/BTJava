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
import javax.swing.border.CompoundBorder;
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

    public void refreshData() {
        loadData();
    }

    private JPanel createToolbarSection() {
        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBackground(new Color(248, 249, 250));
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        toolbar.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel left = new JPanel(new GridBagLayout());
        left.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.setOpaque(false);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);

        txtSearch = new JTextField();
        txtSearch.setColumns(24);
        txtSearch.setPreferredSize(new Dimension(420, 38));
        txtSearch.setMinimumSize(new Dimension(220, 38));
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
        cbStatus.setMinimumSize(new Dimension(140, 38));
        cbStatus.setBackground(Color.WHITE);

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setPreferredSize(new Dimension(96, 38));
        btnSearch.setMinimumSize(new Dimension(96, 38));
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

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridy = 0;
        leftGbc.insets = new Insets(0, 0, 0, 10);
        leftGbc.fill = GridBagConstraints.HORIZONTAL;

        leftGbc.gridx = 0;
        leftGbc.weightx = 1.0;
        left.add(txtSearch, leftGbc);

        leftGbc.gridx = 1;
        leftGbc.weightx = 0.0;
        left.add(cbStatus, leftGbc);

        leftGbc.gridx = 2;
        leftGbc.insets = new Insets(0, 0, 0, 0);
        left.add(btnSearch, leftGbc);

        JButton btnCreate = new JButton("+ Đăng tin mới");
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCreate.setBackground(new Color(13, 110, 253));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFocusPainted(false);
        btnCreate.setBorderPainted(false);
        btnCreate.setOpaque(true);
        btnCreate.setContentAreaFilled(true);
        btnCreate.setHorizontalAlignment(SwingConstants.CENTER);
        btnCreate.setMargin(new Insets(0, 12, 0, 12));
        btnCreate.setPreferredSize(new Dimension(176, 40));
        btnCreate.setMinimumSize(new Dimension(176, 40));
        btnCreate.setMaximumSize(new Dimension(176, 40));
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreate.addActionListener(e -> openCreateDialog());

        right.add(btnCreate);

        GridBagConstraints rootGbc = new GridBagConstraints();
        rootGbc.gridy = 0;
        rootGbc.fill = GridBagConstraints.HORIZONTAL;
        rootGbc.insets = new Insets(0, 0, 0, 0);

        rootGbc.gridx = 0;
        rootGbc.weightx = 1.0;
        rootGbc.anchor = GridBagConstraints.WEST;
        toolbar.add(left, rootGbc);

        rootGbc.gridx = 1;
        rootGbc.weightx = 0.0;
        rootGbc.anchor = GridBagConstraints.EAST;
        rootGbc.insets = new Insets(0, 14, 0, 0);
        toolbar.add(right, rootGbc);

        return toolbar;
    }

    private void openCreateDialog() {
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        if (ancestor instanceof Frame) {
            JDialog dialog = new JDialog((Frame) ancestor, "Đăng tin tuyển dụng", true);
            dialog.setSize(950, 750);
            dialog.setLocationRelativeTo(ancestor);
            RecruitmentFormPanel formPanel = new RecruitmentFormPanel();
            formPanel.setOnSubmitSuccess(this::loadData);
            dialog.add(formPanel);
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
        int rowHeight = isHeader ? 56 : 96;
        row.setPreferredSize(new Dimension(0, rowHeight));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.BLACK;

        addRecruitmentCell(row, createTextCell(col1, font, textColor, FlowLayout.LEFT, isHeader), 0, 0.30);
        addRecruitmentCell(row, createTextCell(col2, font, textColor, FlowLayout.CENTER, isHeader), 1, 0.20);

        JPanel statusCell = createBaseCell(FlowLayout.CENTER, isHeader);
        if (isHeader) {
            JLabel label = new JLabel(status);
            label.setFont(font);
            label.setForeground(textColor);
            statusCell.add(label);
        } else {
            statusCell.add(createStatusBadge(status));
        }
        addRecruitmentCell(row, statusCell, 2, 0.16);

        JPanel applicantCell = createBaseCell(FlowLayout.CENTER, isHeader);
        if (isHeader) {
            JLabel label = new JLabel(applicants);
            label.setFont(font);
            label.setForeground(textColor);
            applicantCell.add(label);
        } else {
            JLabel label = new JLabel(applicants + " \u1ee9ng vi\u00ean");
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setForeground(new Color(13, 110, 253));
            applicantCell.add(label);
        }
        addRecruitmentCell(row, applicantCell, 3, 0.12);

        JPanel actionCell = isHeader ? createBaseCell(FlowLayout.CENTER, true) : createTwoRowActionPanel();
        if (isHeader) {
            JLabel label = new JLabel(action);
            label.setFont(font);
            label.setForeground(textColor);
            actionCell.add(label);
        } else {
            JButton btnDetail = createActionButton("Chi ti\u1ebft", new Color(13, 110, 253), 88);
            btnDetail.addActionListener(e -> showDetailDialog(job));

            JButton btnClose = createActionButton("\u0110\u00f3ng", new Color(220, 53, 69), 68);
            btnClose.addActionListener(e -> {
                if (job != null && job.getAdminStatus() == AdminStatus.APPROVED && job.getStatus() == RecruitmentStatus.OPEN) {
                    boolean success = recruitmentService.closeRecruitment(job.getRecruitmentId());
                    if (success) {
                        org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "\u0110\u00e3 \u0111\u00f3ng tin tuy\u1ec3n d\u1ee5ng th\u00e0nh c\u00f4ng!");
                        loadData();
                    } else {
                        org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Kh\u00f4ng th\u1ec3 \u0111\u00f3ng tin!", "L\u1ed7i", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Ch\u1ec9 c\u00f3 th\u1ec3 \u0111\u00f3ng tin \u0111ang ho\u1ea1t \u0111\u1ed9ng!", "C\u1ea3nh b\u00e1o", JOptionPane.WARNING_MESSAGE);
                }
            });

            JButton btnDelete = createActionButton("X\u00f3a", new Color(220, 53, 69), 68);
            btnDelete.addActionListener(e -> {
                if (job != null) {
                    boolean confirmed = org.jobportal.view.common.ModernDialogUtils.showConfirm(this, "X\u00e1c nh\u1eadn", "B\u1ea1n c\u00f3 ch\u1eafc mu\u1ed1n x\u00f3a tin n\u00e0y?");
                    if (confirmed) {
                        boolean success = recruitmentService.deleteRecruitment(job.getRecruitmentId());
                        if (success) {
                            org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "\u0110\u00e3 x\u00f3a tin tuy\u1ec3n d\u1ee5ng!");
                            loadData();
                        } else {
                            org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Kh\u00f4ng th\u1ec3 x\u00f3a tin!", "L\u1ed7i", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            addTwoRowButtons(actionCell, btnDetail, btnClose, btnDelete);
        }
        addRecruitmentCell(row, actionCell, 4, 0.22);
        return row;
    }

    private void addRecruitmentCell(JPanel row, JComponent cell, int column, double weight) {
        cell.setMinimumSize(new Dimension(0, 0));
        cell.setPreferredSize(new Dimension(0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = 0;
        gbc.weightx = weight;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        row.add(cell, gbc);
    }

    private JPanel createBaseCell(int alignment, boolean header) {
        JPanel panel = new JPanel(new FlowLayout(alignment, alignment == FlowLayout.CENTER ? 4 : 20, header ? 16 : 18));
        panel.setOpaque(false);
        return panel;
    }

    private JPanel createTwoRowActionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        return panel;
    }

    private void addTwoRowButtons(JPanel panel, JButton topButton, JButton leftButton, JButton rightButton) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 4, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(topButton, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 8, 3);
        panel.add(leftButton, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 3, 8, 0);
        panel.add(rightButton, gbc);
    }

    private JPanel createTextCell(String text, Font font, Color color, int alignment, boolean header) {
        JPanel panel = createBaseCell(alignment, header);
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label);
        return panel;
    }

    private void showDetailDialog(RecruitmentDTO selected) {
        if (selected == null) return;
        RecruitmentDTO job = recruitmentService.getRecruitmentById(selected.getRecruitmentId());
        if (job == null) {
            org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Không tìm thấy thông tin tin đăng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        txtDescEdit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescEdit.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblCompany = new JLabel(valueOrEmpty(job.getCompanyName()));
        lblCompany.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCompany.setForeground(new Color(108, 117, 125));

        JLabel lblType = new JLabel(" " + toJobTypeLabel(job.getJobType()) + " ");
        lblType.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblType.setOpaque(true);
        lblType.setBackground(new Color(225, 230, 255));
        lblType.setForeground(new Color(50, 70, 150));
        lblType.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel headerCard = createDialogCard();
        headerCard.setLayout(new BorderLayout(12, 0));
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        JLabel lblTitle = new JLabel(valueOrEmpty(job.getTitle()));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        leftHeader.add(lblTitle);
        leftHeader.add(Box.createRigidArea(new Dimension(0, 6)));
        leftHeader.add(lblCompany);
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightHeader.setOpaque(false);
        rightHeader.add(lblType);
        headerCard.add(leftHeader, BorderLayout.CENTER);
        headerCard.add(rightHeader, BorderLayout.EAST);

        JPanel formCard = createDialogCard();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(createEditGroup("Tiêu đề công việc", txtTitleEdit), gbc);
        gbc.gridx = 1;
        formCard.add(createEditGroup("Địa điểm", txtLocationEdit), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(createEditGroup("Mức lương", txtSalaryEdit), gbc);
        gbc.gridx = 1;
        formCard.add(createEditGroup("Hạn nộp (dd/MM/yyyy)", txtDueDateEdit), gbc);

        JTextField txtCategory = new JTextField(valueOrEmpty(job.getCategoryName()));
        txtCategory.setEditable(false);
        JTextField txtCreatedDate = new JTextField(job.getCreatedDate() != null ? job.getCreatedDate().format(df) : "");
        txtCreatedDate.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(createEditGroup("Danh mục", txtCategory), gbc);
        gbc.gridx = 1;
        formCard.add(createEditGroup("Ngày đăng", txtCreatedDate), gbc);

        JPanel descCard = createDialogCard();
        descCard.setLayout(new BorderLayout());
        JLabel lblDesc = new JLabel("Mô tả công việc");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDesc.setBorder(new EmptyBorder(0, 0, 10, 0));
        descCard.add(lblDesc, BorderLayout.NORTH);
        JScrollPane descScroll = new JScrollPane(txtDescEdit);
        descScroll.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        descScroll.setPreferredSize(new Dimension(0, 200));
        descCard.add(descScroll, BorderLayout.CENTER);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(248, 249, 250));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));
        content.add(headerCard);
        content.add(Box.createRigidArea(new Dimension(0, 14)));
        content.add(formCard);
        content.add(Box.createRigidArea(new Dimension(0, 14)));
        content.add(descCard);

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Chi tiết tin tuyển dụng", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        dialog.add(scrollPane, BorderLayout.CENTER);

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
                org.jobportal.view.common.ModernDialogUtils.showMessageDialog(dialog, "Tiêu đề không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double salary;
            try {
                salary = salaryRaw.isEmpty() ? 0 : Double.parseDouble(salaryRaw);
            } catch (NumberFormatException ex) {
                org.jobportal.view.common.ModernDialogUtils.showMessageDialog(dialog, "Lương không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dueDate;
            try {
                dueDate = LocalDate.parse(dueRaw, df);
            } catch (DateTimeParseException ex) {
                org.jobportal.view.common.ModernDialogUtils.showMessageDialog(dialog, "Hạn nộp không đúng định dạng dd/MM/yyyy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
                org.jobportal.view.common.ModernDialogUtils.showMessageDialog(dialog, "Cập nhật tin tuyển dụng thành công!");
                dialog.dispose();
                loadData();
            } else {
                org.jobportal.view.common.ModernDialogUtils.showMessageDialog(dialog, "Không thể cập nhật tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnClose = new JButton("Đóng");
        btnClose.setPreferredSize(new Dimension(90, 34));
        btnClose.addActionListener(e -> dialog.dispose());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        footer.add(btnClose);
        footer.add(btnSave);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.setSize(980, 760);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String valueOrEmpty(String value) {
        return value != null ? value : "";
    }

    private String toJobTypeLabel(JobType jobType) {
        if (jobType == JobType.PARTTIME) return "Part-time";
        if (jobType == JobType.INTERNSHIP) return "Internship";
        return "Full-time";
    }

    private JPanel createDialogCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(16, 18, 16, 18)
        ));
        return card;
    }

    private JPanel createEditGroup(String label, JComponent input) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(108, 117, 125));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        input.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        input.setPreferredSize(new Dimension(320, 36));
        input.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        input.setAlignmentX(Component.LEFT_ALIGNMENT);

        group.add(lbl);
        group.add(Box.createRigidArea(new Dimension(0, 6)));
        group.add(input);
        return group;
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
        JLabel badge = new JLabel(status, SwingConstants.CENTER);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setOpaque(true);
        badge.setPreferredSize(new Dimension(104, 22));

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

        badge.setBorder(new LineBorder(badge.getForeground(), 1, false));
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
