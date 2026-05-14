package org.jobportal.view.employer;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.dto.UserDTO;
import org.jobportal.utils.SessionManager;
import org.jobportal.enums.AdminStatus;
import org.jobportal.enums.RecruitmentStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RecruitmentListPanel extends JPanel {

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private JPanel tableContainer;
    private JTable recruitmentTable;
    private DefaultTableModel recruitmentModel;
    private List<RecruitmentDTO> displayedJobs = new java.util.ArrayList<>();
    private JLabel lblCount;
    private JPanel paginationPanel;
    private JPanel footerPanel;
    private int currentPage = 1;
    private int pageSize = 10;
    private String currentKeyword = "";
    private String currentStatusFilter = "Tất cả trạng thái";

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
        tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new EmptyBorder(24, 26, 18, 26));

        recruitmentModel = new DefaultTableModel(
                new Object[]{"TIÊU ĐỀ CÔNG VIỆC", "NGÀY ĐĂNG - HẾT HẠN", "TRẠNG THÁI", "ỨNG VIÊN", "THAO TÁC"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        recruitmentTable = new JTable(recruitmentModel);
        configureRecruitmentTable();

        JScrollPane tableScroll = new JScrollPane(recruitmentTable);
        tableScroll.setBorder(BorderFactory.createEmptyBorder());
        tableScroll.setViewportBorder(BorderFactory.createEmptyBorder());
        tableScroll.getViewport().setBackground(Color.WHITE);
        if (tableScroll.getColumnHeader() != null) {
            tableScroll.getColumnHeader().setBackground(Color.WHITE);
            tableScroll.getColumnHeader().setBorder(BorderFactory.createEmptyBorder());
        }
        tableContainer.add(tableScroll, BorderLayout.CENTER);
        
        mainContent.add(tableContainer);
        
        loadData();

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void configureRecruitmentTable() {
        recruitmentTable.setRowHeight(58);
        recruitmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        recruitmentTable.setBackground(Color.WHITE);
        recruitmentTable.setSelectionBackground(new Color(245, 248, 255));
        recruitmentTable.setSelectionForeground(Color.BLACK);
        recruitmentTable.setFillsViewportHeight(true);
        recruitmentTable.setBorder(BorderFactory.createEmptyBorder());
        recruitmentTable.setShowGrid(false);
        recruitmentTable.setShowVerticalLines(false);
        recruitmentTable.setShowHorizontalLines(false);
        recruitmentTable.setGridColor(Color.WHITE);
        recruitmentTable.setIntercellSpacing(new Dimension(0, 0));

        recruitmentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        recruitmentTable.getTableHeader().setForeground(Color.GRAY);
        recruitmentTable.getTableHeader().setBackground(Color.WHITE);
        recruitmentTable.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        recruitmentTable.getTableHeader().setDefaultRenderer(new RecruitmentHeaderRenderer());
        recruitmentTable.getTableHeader().setReorderingAllowed(false);
        recruitmentTable.getTableHeader().setResizingAllowed(false);

        TableColumnModel columns = recruitmentTable.getColumnModel();
        columns.getColumn(0).setPreferredWidth(240);
        columns.getColumn(1).setPreferredWidth(230);
        columns.getColumn(2).setPreferredWidth(190);
        columns.getColumn(3).setPreferredWidth(170);
        columns.getColumn(4).setPreferredWidth(190);

        columns.getColumn(0).setCellRenderer(new RecruitmentTextRenderer(SwingConstants.LEFT));
        columns.getColumn(1).setCellRenderer(new RecruitmentTextRenderer(SwingConstants.LEFT));
        columns.getColumn(2).setCellRenderer(new StatusCellRenderer());
        columns.getColumn(3).setCellRenderer(new RecruitmentTextRenderer(SwingConstants.LEFT));
        columns.getColumn(4).setCellRenderer(new RecruitmentActionRenderer());
        columns.getColumn(4).setCellEditor(new RecruitmentActionEditor());
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
                loadData();
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
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().equals("Tìm kiếm theo tiêu đề...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Tìm kiếm theo tiêu đề...");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang hoạt động", "Chờ duyệt", "Bị từ chối", "Đã đóng", "Hết hạn"});
        cbStatus.setPreferredSize(new Dimension(150, 38));
        cbStatus.setBackground(Color.WHITE);

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSearch.setPreferredSize(new Dimension(100, 38));
        btnSearch.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.equals("Tìm kiếm theo tiêu đề...")) keyword = "";
            currentKeyword = keyword.toLowerCase();
            currentStatusFilter = (String) cbStatus.getSelectedItem();
            currentPage = 1;
            loadData();
        });

        filterPanel.add(txtSearch);
        filterPanel.add(cbStatus);
        filterPanel.add(btnSearch);

        return filterPanel;
    }

    private void loadData() {
        if (footerPanel != null) {
            tableContainer.remove(footerPanel);
        }
        recruitmentModel.setRowCount(0);

        String employerId = SessionManager.getInstance().getEmployerId();
        List<RecruitmentDTO> allJobs = null;
        if (employerId != null && !employerId.isEmpty()) {
            allJobs = recruitmentService.getRecruitmentsByEmployer(employerId);
        }

        List<RecruitmentDTO> jobs = new java.util.ArrayList<>();
        if (allJobs != null) {
            for (RecruitmentDTO job : allJobs) {
                if (!currentKeyword.isEmpty() && !job.getTitle().toLowerCase().contains(currentKeyword)) {
                    continue;
                }
                String statusStr = "Bản nháp";
                if (job.getAdminStatus() == AdminStatus.PENDING) {
                    statusStr = "Chờ duyệt";
                } else if (job.getAdminStatus() == AdminStatus.REJECTED) {
                    statusStr = "Bị từ chối";
                } else if (job.getAdminStatus() == AdminStatus.APPROVED) {
                    if (job.getStatus() == RecruitmentStatus.OPEN) statusStr = "Đang hoạt động";
                    else if (job.getStatus() == RecruitmentStatus.CLOSED) statusStr = "Đã đóng";
                    else if (job.getStatus() == RecruitmentStatus.EXPIRED) statusStr = "Hết hạn";
                }
                
                if (!currentStatusFilter.equals("Tất cả trạng thái") && !statusStr.equals(currentStatusFilter)) {
                    continue;
                }
                jobs.add(job);
            }
        }

        displayedJobs = jobs != null ? jobs : new java.util.ArrayList<>();
        if (jobs != null) {
            int maxPage = Math.max(1, (int) Math.ceil((double) jobs.size() / pageSize));
            if (currentPage > maxPage) currentPage = maxPage;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, jobs.size());
            
            for (int i = start; i < end; i++) {
                RecruitmentDTO job = jobs.get(i);
                String title = job.getTitle();
                String dateStr = (job.getCreatedDate() != null ? job.getCreatedDate().format(formatter) : "N/A") + " - " + 
                                 (job.getDueDate() != null ? job.getDueDate().format(formatter) : "N/A");
                
                String statusStr = "Bản nháp";
                if (job.getAdminStatus() == AdminStatus.PENDING) {
                    statusStr = "Chờ duyệt";
                } else if (job.getAdminStatus() == AdminStatus.REJECTED) {
                    statusStr = "Bị từ chối";
                } else if (job.getAdminStatus() == AdminStatus.APPROVED) {
                    if (job.getStatus() == RecruitmentStatus.OPEN) statusStr = "Đang hoạt động";
                    else if (job.getStatus() == RecruitmentStatus.CLOSED) statusStr = "Đã đóng";
                    else if (job.getStatus() == RecruitmentStatus.EXPIRED) statusStr = "Hết hạn";
                }
                
                String applicants = String.valueOf(job.getApplicationCount());
                
                recruitmentModel.addRow(new Object[]{
                        title,
                        dateStr,
                        statusStr,
                        applicants + " ứng viên",
                        ""
                });
            }
        }

        // phan trang
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(18, 0, 0, 0));

        if (lblCount == null) {
            lblCount = new JLabel();
            lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblCount.setForeground(Color.GRAY);
        }
        lblCount.setText("Hiển thị " + (jobs != null ? jobs.size() : 0) + " tin đăng");
        footerPanel.add(lblCount, BorderLayout.WEST);

        paginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        paginationPanel.setBackground(Color.WHITE);
        updatePaginationUI(jobs != null ? jobs.size() : 0);
        footerPanel.add(paginationPanel, BorderLayout.EAST);

        tableContainer.add(footerPanel, BorderLayout.SOUTH);
        tableContainer.revalidate();
        tableContainer.repaint();
        this.revalidate();
        this.repaint();
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
            btnFirst.addActionListener(e -> { currentPage = 1; loadData(); });
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
            btnLast.addActionListener(e -> { currentPage = totalPages; loadData(); });
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
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(isHeader ? new Color(250, 250, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        row.setPreferredSize(new Dimension(0, 70));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.BLACK;

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 25));
        p1.setOpaque(false);
        JLabel l1 = new JLabel(col1);
        l1.setFont(font); l1.setForeground(textColor);
        p1.add(l1);
        row.add(p1);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 25));
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2);
        l2.setFont(font); l2.setForeground(textColor);
        p2.add(l2);
        row.add(p2);

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

        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        p5.setOpaque(false);
        if (isHeader) {
            JLabel l5 = new JLabel(action);
            l5.setFont(font); l5.setForeground(textColor);
            p5.add(l5);
        } else {
            JButton btnEdit = new JButton("Đóng");
            btnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnEdit.setBackground(Color.WHITE);
            btnEdit.addActionListener(e -> {
                if (job != null && status.equals("Đang hoạt động")) {
                    boolean success = recruitmentService.closeRecruitment(job.getRecruitmentId());
                    if (success) {
                        org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đã đóng tin tuyển dụng thành công!");
                        loadData();
                    } else {
                        org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không thể đóng tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Chỉ có thể đóng tin đang hoạt động!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            });

            JButton btnDelete = new JButton("Xóa");
            btnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnDelete.setBackground(Color.WHITE);
            btnDelete.setForeground(Color.RED);
            btnDelete.addActionListener(e -> {
                if (job != null) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tin này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = recruitmentService.deleteRecruitment(job.getRecruitmentId());
                        if (success) {
                            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đã xóa tin tuyển dụng!");
                            loadData();
                        } else {
                            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không thể xóa tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            p5.add(btnEdit);
            p5.add(btnDelete);
        }
        row.add(p5);

        return row;
    }

    private RecruitmentDTO getJobAt(int viewRow) {
        if (viewRow < 0 || displayedJobs == null) return null;
        int modelRow = recruitmentTable.convertRowIndexToModel(viewRow);
        int jobIndex = (currentPage - 1) * pageSize + modelRow;
        if (jobIndex < 0 || jobIndex >= displayedJobs.size()) return null;
        return displayedJobs.get(jobIndex);
    }

    private JPanel createRecruitmentActionPanel(int row, boolean editable) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        panel.setBackground(Color.WHITE);

        JButton close = new JButton("Đóng");
        close.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        close.setBackground(Color.WHITE);

        JButton delete = new JButton("Xóa");
        delete.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        delete.setBackground(Color.WHITE);
        delete.setForeground(Color.RED);

        if (editable) {
            close.addActionListener(e -> closeRecruitmentAt(row));
            delete.addActionListener(e -> deleteRecruitmentAt(row));
        }

        panel.add(close);
        panel.add(delete);
        return panel;
    }

    private void closeRecruitmentAt(int viewRow) {
        stopRecruitmentEditing();
        RecruitmentDTO job = getJobAt(viewRow);
        if (job == null) return;

        if (job.getAdminStatus() == AdminStatus.APPROVED && job.getStatus() == RecruitmentStatus.OPEN) {
            boolean success = recruitmentService.closeRecruitment(job.getRecruitmentId());
            if (success) {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đã đóng tin tuyển dụng thành công!");
                loadData();
            } else {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không thể đóng tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Chỉ có thể đóng tin đang hoạt động!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteRecruitmentAt(int viewRow) {
        stopRecruitmentEditing();
        RecruitmentDTO job = getJobAt(viewRow);
        if (job == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tin này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = recruitmentService.deleteRecruitment(job.getRecruitmentId());
            if (success) {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đã xóa tin tuyển dụng!");
                loadData();
            } else {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không thể xóa tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void stopRecruitmentEditing() {
        if (recruitmentTable.isEditing() && recruitmentTable.getCellEditor() != null) {
            recruitmentTable.getCellEditor().stopCellEditing();
        }
    }

    private static class RecruitmentHeaderRenderer extends DefaultTableCellRenderer {
        RecruitmentHeaderRenderer() {
            setBorder(new EmptyBorder(0, 12, 0, 12));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            setHorizontalAlignment(column == 4 ? SwingConstants.CENTER : SwingConstants.LEFT);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(Color.GRAY);
            setBackground(Color.WHITE);
            return this;
        }
    }

    private static class RecruitmentTextRenderer extends DefaultTableCellRenderer {
        RecruitmentTextRenderer(int alignment) {
            setHorizontalAlignment(alignment);
            setBorder(new EmptyBorder(0, 12, 0, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            component.setFont(table.getFont());
            return component;
        }
    }

    private class StatusCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 18));
            panel.setBackground(Color.WHITE);
            panel.add(createStatusBadge(value != null ? value.toString() : ""));
            return panel;
        }
    }

    private class RecruitmentActionRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            return createRecruitmentActionPanel(row, false);
        }
    }

    private class RecruitmentActionEditor extends AbstractCellEditor implements TableCellEditor {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean selected,
                                                     int row, int column) {
            return createRecruitmentActionPanel(row, true);
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
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
            case "Chờ duyệt":
                badge.setBackground(new Color(255, 243, 205));
                badge.setForeground(new Color(133, 100, 4));
                break;
            case "Bị từ chối":
            case "Đã đóng":
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
            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.EMPLOYER);
            frame.add(sidebar, BorderLayout.WEST);

            // Panel danh sach tin dang add vao CENTER
            RecruitmentListPanel listPanel = new RecruitmentListPanel();
            frame.add(listPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
