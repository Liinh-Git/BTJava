package org.jobportal.view.employer;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.impl.CVService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IApplicationService;
import org.jobportal.bll.interfaces.ICVService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.ApplicationDTO;
import org.jobportal.dto.CVDTO;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.model.Education;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApplicationReviewPanel extends JPanel {

    private final IApplicationService applicationService = new ApplicationService();
    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private final ICVService cvService = new CVService();
    private JPanel tableContainer;
    private JTable applicationTable;
    private DefaultTableModel applicationModel;
    private List<ApplicationDTO> displayedApps = new ArrayList<>();
    private JLabel lblCount;
    private JPanel paginationPanel;
    private JPanel footerPanel;
    private int currentPage = 1;
    private int pageSize = 10;

    public ApplicationReviewPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. header va breadcrumb
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. the thong ke
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. bang danh sach ung vien
        tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new EmptyBorder(24, 26, 18, 26));

        applicationModel = new DefaultTableModel(
                new Object[]{"ỨNG VIÊN", "NGÀY NỘP", "VỊ TRÍ HIỆN TẠI", "TRẠNG THÁI", "THAO TÁC"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        applicationTable = new JTable(applicationModel);
        configureApplicationTable();

        JScrollPane tableScroll = new JScrollPane(applicationTable);
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
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 4. phan goi y AI va quang cao ben duoi
        mainContent.add(createBottomSection());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createPageHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // phan text ben trai
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(248, 249, 250));

        JLabel lblTitle = new JLabel("Duyệt hồ sơ ứng viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 37, 41));
;
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(lblTitle);

        // phan nut ben phai
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        rightPanel.setBackground(new Color(248, 249, 250));

        JButton btnExport = new JButton("v Xuất báo cáo");
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExport.setBackground(new Color(13, 110, 253));
        btnExport.setForeground(Color.WHITE);
        btnExport.setPreferredSize(new Dimension(140, 38));
        btnExport.setBorderPainted(false);
        btnExport.setFocusPainted(false);
        btnExport.addActionListener(e -> exportReportToCSV());

        rightPanel.add(btnExport);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createStatsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(new Color(248, 249, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        panel.add(createStatCard("TỔNG SỐ ỨNG VIÊN", "128", "~ 12% so với tháng trước", new Color(13, 110, 253)));
        panel.add(createStatCard("ĐANG CHỜ DUYỆT", "45", null, new Color(200, 80, 20)));
        panel.add(createStatCard("ĐÃ DUYỆT", "18", null, new Color(13, 110, 253)));
        panel.add(createStatCard("TỈ LỆ CHẤP THUẬN", "14.2%", null, Color.BLACK));

        return panel;
    }

    private JPanel createStatCard(String title, String value, String subText, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(valueColor);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblValue);

        if (subText != null) {
            JLabel lblSub = new JLabel(subText);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblSub.setForeground(new Color(13, 110, 253));
            card.add(Box.createRigidArea(new Dimension(0, 5)));
            card.add(lblSub);
        }

        return card;
    }

    private void configureApplicationTable() {
        applicationTable.setRowHeight(76);
        applicationTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        applicationTable.setBackground(Color.WHITE);
        applicationTable.setSelectionBackground(new Color(245, 248, 255));
        applicationTable.setSelectionForeground(Color.BLACK);
        applicationTable.setFillsViewportHeight(true);
        applicationTable.setBorder(BorderFactory.createEmptyBorder());
        applicationTable.setShowGrid(false);
        applicationTable.setShowVerticalLines(false);
        applicationTable.setShowHorizontalLines(false);
        applicationTable.setGridColor(Color.WHITE);
        applicationTable.setIntercellSpacing(new Dimension(0, 0));

        applicationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        applicationTable.getTableHeader().setForeground(Color.GRAY);
        applicationTable.getTableHeader().setBackground(Color.WHITE);
        applicationTable.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        applicationTable.getTableHeader().setDefaultRenderer(new ApplicationHeaderRenderer());
        applicationTable.getTableHeader().setReorderingAllowed(false);
        applicationTable.getTableHeader().setResizingAllowed(false);

        TableColumnModel columns = applicationTable.getColumnModel();
        columns.getColumn(0).setPreferredWidth(280);
        columns.getColumn(1).setPreferredWidth(130);
        columns.getColumn(2).setPreferredWidth(300);
        columns.getColumn(3).setPreferredWidth(150);
        columns.getColumn(4).setPreferredWidth(180);

        columns.getColumn(0).setCellRenderer(new ApplicantCellRenderer());
        columns.getColumn(1).setCellRenderer(new ApplicationTextRenderer(SwingConstants.LEFT));
        columns.getColumn(2).setCellRenderer(new ApplicationTextRenderer(SwingConstants.LEFT));
        columns.getColumn(3).setCellRenderer(new ApplicationStatusRenderer());
        columns.getColumn(4).setCellRenderer(new ApplicationActionRenderer());
        columns.getColumn(4).setCellEditor(new ApplicationActionEditor());
    }

    private void loadData() {
        if (footerPanel != null) {
            tableContainer.remove(footerPanel);
        }
        applicationModel.setRowCount(0);

        String employerId = SessionManager.getInstance().getEmployerId();
        List<ApplicationDTO> allApps = new ArrayList<>(applicationService.getPendingApplicationsByEmployer(employerId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, allApps.size());
        displayedApps = allApps;

        for (int i = start; i < end; i++) {
            ApplicationDTO app = allApps.get(i);
            String name = app.getCandidateName();
            String email = (app.getEmail() != null) ? app.getEmail() : "No email";
            String initials = name != null && name.length() > 0 ? name.substring(0, 1).toUpperCase() : "?";
            String dateStr = app.getAppliedDate() != null ? app.getAppliedDate().format(formatter) : "N/A";
            String pos = app.getJobTitle();
            String status = toStatusLabel(app.getStatus());

            applicationModel.addRow(new Object[]{name, dateStr, pos, status, new ApplicantMeta(email, initials)});
        }

        // pagination footer
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(18, 0, 0, 0));

        if (lblCount == null) {
            lblCount = new JLabel();
            lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblCount.setForeground(Color.GRAY);
        }
        lblCount.setText("Tổng cộng: " + allApps.size() + " hồ sơ");
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        paginationPanel.setBackground(Color.WHITE);
        updatePaginationUI(allApps.size());
        footerPanel.add(paginationPanel, BorderLayout.EAST);

        tableContainer.add(footerPanel, BorderLayout.SOUTH);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private void exportReportToCSV() {
        String employerId = SessionManager.getInstance().getEmployerId();
        List<ApplicationDTO> apps = applicationService.getPendingApplicationsByEmployer(employerId);
        if (apps == null || apps.isEmpty()) {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không có dữ liệu để xuất!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file báo cáo");
        fileChooser.setSelectedFile(new java.io.File("application_review_report.csv"));
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        java.io.File file = fileChooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new java.io.File(file.getAbsolutePath() + ".csv");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("\uFEFF");
            writer.write("STT,Ứng viên,Email,Ngày nộp,Vị trí ứng tuyển,Trạng thái\n");

            int index = 1;
            for (ApplicationDTO app : apps) {
                String appliedDate = app.getAppliedDate() != null ? app.getAppliedDate().format(formatter) : "N/A";
                writer.write(index++ + ","
                        + csvEscape(app.getCandidateName()) + ","
                        + csvEscape(app.getEmail()) + ","
                        + csvEscape(appliedDate) + ","
                        + csvEscape(app.getJobTitle()) + ","
                        + csvEscape(toStatusLabel(app.getStatus())) + "\n");
            }

            org.jobportal.view.common.SuccessDialog.showMessageDialog(this,
                    "Xuất báo cáo thành công!\nFile: " + file.getAbsolutePath(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this,
                    "Lỗi khi xuất file: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
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

    private JPanel createTableRow(String col1, String col2, String col3, String status, String action, boolean isHeader, String email, String initials, ApplicationDTO app) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? new Color(248, 249, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setPreferredSize(new Dimension(0, isHeader ? 55 : 90));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, isHeader ? 55 : 90));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.DARK_GRAY;

        // cot 1: Ung vien (Avatar + Name + Email)
        gbc.gridx = 0; gbc.weightx = 0.34; gbc.insets = new Insets(10, 20, 10, 10);
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        p1.setOpaque(false);
        if (isHeader) {
            JLabel l1 = new JLabel(col1);
            l1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            l1.setForeground(Color.GRAY);
            p1.add(l1);
        } else {
            p1.add(createAvatar(initials));
            JPanel namePanel = new JPanel();
            namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
            namePanel.setOpaque(false);
            JLabel lName = new JLabel(col1);
            lName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JLabel lEmail = new JLabel(email);
            lEmail.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lEmail.setForeground(Color.GRAY);
            namePanel.add(lName);
            namePanel.add(lEmail);
            p1.add(namePanel);
        }
        row.add(p1, gbc);

        // cot 2: Ngay nop
        gbc.gridx = 1; gbc.weightx = 0.14; gbc.insets = new Insets(10, 10, 10, 10);
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2);
        l2.setFont(isHeader ? new Font("Segoe UI", Font.PLAIN, 11) : font);
        l2.setForeground(textColor);
        GridBagConstraints c2 = new GridBagConstraints();
        c2.anchor = GridBagConstraints.CENTER;
        c2.fill = GridBagConstraints.NONE;
        p2.add(l2, c2);
        row.add(p2, gbc);

        // cot 3: Vi tri
        gbc.gridx = 2; gbc.weightx = 0.28;
        JPanel p3 = new JPanel(new GridBagLayout());
        p3.setOpaque(false);
        if (isHeader) {
            JLabel l3 = new JLabel(col3);
            l3.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            l3.setForeground(textColor);
            GridBagConstraints c3 = new GridBagConstraints();
            c3.anchor = GridBagConstraints.CENTER;
            c3.fill = GridBagConstraints.NONE;
            p3.add(l3, c3);
        } else {
            JTextArea txtPos = new JTextArea(col3 != null ? col3 : "");
            txtPos.setEditable(false);
            txtPos.setOpaque(false);
            txtPos.setFont(font);
            txtPos.setForeground(textColor);
            txtPos.setLineWrap(true);
            txtPos.setWrapStyleWord(true);
            txtPos.setBorder(null);
            GridBagConstraints c3 = new GridBagConstraints();
            c3.anchor = GridBagConstraints.CENTER;
            c3.fill = GridBagConstraints.BOTH;
            c3.weightx = 1.0;
            c3.weighty = 1.0;
            p3.add(txtPos, c3);
        }
        row.add(p3, gbc);

        // cot 4: Trang thai
        gbc.gridx = 3; gbc.weightx = 0.12;
        JPanel p4 = new JPanel(new GridBagLayout());
        p4.setOpaque(false);
        if (isHeader) {
            JLabel l4 = new JLabel(status);
            l4.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            l4.setForeground(textColor);
            GridBagConstraints c4 = new GridBagConstraints();
            c4.anchor = GridBagConstraints.CENTER;
            c4.fill = GridBagConstraints.NONE;
            p4.add(l4, c4);
        } else {
            GridBagConstraints c4 = new GridBagConstraints();
            c4.anchor = GridBagConstraints.CENTER;
            c4.fill = GridBagConstraints.NONE;
            p4.add(createStatusBadge(status), c4);
        }
        row.add(p4, gbc);

        // cot 5: Thao tac
        gbc.gridx = 4; gbc.weightx = 0.12;
        JPanel p5 = new JPanel(new GridBagLayout());
        p5.setOpaque(false);
        if (isHeader) {
            JLabel l5 = new JLabel(action);
            l5.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            l5.setForeground(textColor);
            GridBagConstraints c5 = new GridBagConstraints();
            c5.anchor = GridBagConstraints.CENTER;
            c5.fill = GridBagConstraints.NONE;
            p5.add(l5, c5);
        } else {
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            actions.setOpaque(false);
            JButton btnCheck = createActionBtn("V", new Color(160, 50, 50));
            btnCheck.addActionListener(e -> {
                UserDTO current = SessionManager.getInstance().getCurrentUser();
                if (app != null && current != null) {
                    boolean success = applicationService.approveApplication(current.getUserId(), app.getApplicationId());
                    if (success) {
                        org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đã chấp nhận hồ sơ!");
                        loadData();
                    } else {
                        org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không thể thao tác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (current == null) {
                    org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Bạn cần đăng nhập để thực hiện thao tác này.", "Chưa đăng nhập", JOptionPane.WARNING_MESSAGE);
                }
            });
            JButton btnCross = createActionBtn("X", new Color(220, 53, 69));
            btnCross.addActionListener(e -> {
                UserDTO current = SessionManager.getInstance().getCurrentUser();
                if (app != null && current != null) {
                    boolean success = applicationService.rejectApplication(current.getUserId(), app.getApplicationId());
                    if (success) {
                        org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đã từ chối hồ sơ!");
                        loadData();
                    } else {
                        org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không thể thao tác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (current == null) {
                    org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Bạn cần đăng nhập để thực hiện thao tác này.", "Chưa đăng nhập", JOptionPane.WARNING_MESSAGE);
                }
            });

            JButton btnView = createActionBtn("O", new Color(13, 110, 253));
            btnView.addActionListener(e -> {
                if (app != null) {
                    UserDTO candidateInfo = applicationService.getCandidateInfo(app.getCandidateId());
                    if (candidateInfo != null) {
                        showCandidateInfoDialog(app, candidateInfo);
                    } else {
                        org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không tìm thấy thông tin ứng viên!");
                    }
                }
            });
            actions.add(btnView); // eye
            actions.add(btnCheck);  // check
            actions.add(btnCross);  // cross
            GridBagConstraints c5 = new GridBagConstraints();
            c5.anchor = GridBagConstraints.CENTER;
            c5.fill = GridBagConstraints.NONE;
            p5.add(actions, c5);
        }
        row.add(p5, gbc);

        return row;
    }

    private ApplicationDTO getApplicationAt(int viewRow) {
        if (viewRow < 0 || displayedApps == null) return null;
        int modelRow = applicationTable.convertRowIndexToModel(viewRow);
        int appIndex = (currentPage - 1) * pageSize + modelRow;
        if (appIndex < 0 || appIndex >= displayedApps.size()) return null;
        return displayedApps.get(appIndex);
    }

    private JPanel createApplicationActionPanel(int row, boolean editable) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 13));
        panel.setBackground(Color.WHITE);

        JButton view = createActionBtn("O", new Color(13, 110, 253));
        JButton approve = createActionBtn("V", new Color(160, 50, 50));
        JButton reject = createActionBtn("X", new Color(220, 53, 69));

        if (editable) {
            view.addActionListener(e -> viewApplicationAt(row));
            approve.addActionListener(e -> approveApplicationAt(row));
            reject.addActionListener(e -> rejectApplicationAt(row));
        }

        panel.add(view);
        panel.add(approve);
        panel.add(reject);
        return panel;
    }

    private void viewApplicationAt(int viewRow) {
        stopApplicationEditing();
        ApplicationDTO app = getApplicationAt(viewRow);
        if (app == null) return;

        UserDTO candidateInfo = applicationService.getCandidateInfo(app.getCandidateId());
        if (candidateInfo != null) {
            showCandidateInfoDialog(app, candidateInfo);
        } else {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không tìm thấy thông tin ứng viên!");
        }
    }

    private void approveApplicationAt(int viewRow) {
        stopApplicationEditing();
        ApplicationDTO app = getApplicationAt(viewRow);
        UserDTO current = SessionManager.getInstance().getCurrentUser();
        if (app != null && current != null) {
            boolean success = applicationService.approveApplication(current.getUserId(), app.getApplicationId());
            if (success) {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đã chấp nhận hồ sơ!");
                loadData();
            } else {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không thể thao tác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (current == null) {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Bạn cần đăng nhập để thực hiện thao tác này.", "Chưa đăng nhập", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void rejectApplicationAt(int viewRow) {
        stopApplicationEditing();
        ApplicationDTO app = getApplicationAt(viewRow);
        UserDTO current = SessionManager.getInstance().getCurrentUser();
        if (app != null && current != null) {
            boolean success = applicationService.rejectApplication(current.getUserId(), app.getApplicationId());
            if (success) {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đã từ chối hồ sơ!");
                loadData();
            } else {
                org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không thể thao tác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (current == null) {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Bạn cần đăng nhập để thực hiện thao tác này.", "Chưa đăng nhập", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void stopApplicationEditing() {
        if (applicationTable.isEditing() && applicationTable.getCellEditor() != null) {
            applicationTable.getCellEditor().stopCellEditing();
        }
    }

    private static class ApplicantMeta {
        final String email;
        final String initials;

        ApplicantMeta(String email, String initials) {
            this.email = email;
            this.initials = initials;
        }
    }

    private static class ApplicationHeaderRenderer extends DefaultTableCellRenderer {
        ApplicationHeaderRenderer() {
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

    private static class ApplicationTextRenderer extends DefaultTableCellRenderer {
        ApplicationTextRenderer(int alignment) {
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

    private class ApplicantCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            ApplicationDTO app = getApplicationAt(row);
            String name = value != null ? value.toString() : "";
            String email = app != null && app.getEmail() != null ? app.getEmail() : "No email";
            String initials = name != null && !name.isEmpty() ? name.substring(0, 1).toUpperCase() : "?";

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12));
            panel.setBackground(Color.WHITE);
            panel.add(createAvatar(initials));

            JPanel namePanel = new JPanel();
            namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
            namePanel.setOpaque(false);
            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JLabel emailLabel = new JLabel(email);
            emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            emailLabel.setForeground(Color.GRAY);
            namePanel.add(nameLabel);
            namePanel.add(emailLabel);
            panel.add(namePanel);
            return panel;
        }
    }

    private class ApplicationStatusRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 26));
            panel.setBackground(Color.WHITE);
            panel.add(createStatusBadge(value != null ? value.toString() : ""));
            return panel;
        }
    }

    private class ApplicationActionRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            return createApplicationActionPanel(row, false);
        }
    }

    private class ApplicationActionEditor extends AbstractCellEditor implements TableCellEditor {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean selected,
                                                     int row, int column) {
            return createApplicationActionPanel(row, true);
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    private void showCandidateInfoDialog(ApplicationDTO app, UserDTO candidateInfo) {
        String message = "Thông tin ứng viên:\n"
                + "Họ tên: " + valueOrEmpty(candidateInfo.getFullName()) + "\n"
                + "Email: " + valueOrEmpty(candidateInfo.getEmail()) + "\n"
                + "Điện thoại: " + valueOrEmpty(candidateInfo.getPhoneNumber());

        Object[] options = {"OK", "Xem chi tiết CV"};
        int choice = JOptionPane.showOptionDialog(
                this,
                message,
                "Thông tin ứng viên",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 1) {
            showCVDetailDialog(app, candidateInfo);
        }
    }


    private void showCVDetailDialog(ApplicationDTO app, UserDTO candidateInfo) {
        CVDTO cv = cvService.getCV(app.getCandidateId());

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Chi ti\u1ebft CV", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(760, 680);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel root = new RoundedPanel(18, Color.WHITE);
        root.setLayout(new BorderLayout());
        root.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(18, 24, 20, 24)
        ));
        dialog.setContentPane(root);

        root.add(createCVDialogHeader(dialog), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(0, 2, 0, 12));

        content.add(createCVHero(candidateInfo, cv));
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(createCVInfoSection(candidateInfo, cv));
        content.add(createCVSection("M\u1ee5c ti\u00eau ngh\u1ec1 nghi\u1ec7p", createCVTextBlock(cv != null ? cv.getObjective() : null)));
        content.add(createCVSection("K\u1ef9 n\u0103ng", createCVTextBlock(cv != null ? cv.getSkills() : null)));
        content.add(createEducationSection(cv));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scrollPane, BorderLayout.CENTER);

        root.add(createCVDialogFooter(dialog), BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createCVDialogHeader(JDialog dialog) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 0, 14, 0));

        JLabel brand = new JLabel("JobPortal");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brand.setForeground(new Color(13, 110, 253));
        header.add(brand, BorderLayout.WEST);

        JButton close = new JButton("X");
        close.setFont(new Font("Segoe UI", Font.BOLD, 14));
        close.setForeground(Color.GRAY);
        close.setBackground(Color.WHITE);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setContentAreaFilled(false);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dialog.dispose());
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                close.setForeground(new Color(220, 53, 69));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                close.setForeground(Color.GRAY);
            }
        });
        header.add(close, BorderLayout.EAST);
        return header;
    }

    private JPanel createCVHero(UserDTO candidateInfo, CVDTO cv) {
        JPanel hero = new RoundedPanel(16, new Color(245, 248, 255));
        hero.setLayout(new BorderLayout(18, 0));
        hero.setBorder(new EmptyBorder(18, 18, 18, 18));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));

        String name = valueOrDefault(candidateInfo.getFullName(), "\u1ee8ng vi\u00ean");
        hero.add(createLargeAvatar(name.substring(0, 1).toUpperCase()), BorderLayout.WEST);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(new Color(33, 37, 41));
        JLabel titleLabel = new JLabel(valueOrDefault(cv != null ? cv.getDesiredPosition() : null, "Ch\u01b0a c\u1eadp nh\u1eadt v\u1ecb tr\u00ed mong mu\u1ed1n"));
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(85, 95, 110));
        text.add(nameLabel);
        text.add(Box.createRigidArea(new Dimension(0, 6)));
        text.add(titleLabel);
        hero.add(text, BorderLayout.CENTER);
        return hero;
    }

    private JPanel createCVInfoSection(UserDTO candidateInfo, CVDTO cv) {
        JPanel grid = new JPanel(new GridLayout(0, 2, 14, 10));
        grid.setBackground(Color.WHITE);
        grid.add(createInfoPill("Email", candidateInfo.getEmail()));
        grid.add(createInfoPill("\u0110i\u1ec7n tho\u1ea1i", candidateInfo.getPhoneNumber()));
        grid.add(createInfoPill("\u0110\u1ecba \u0111i\u1ec3m", cv != null ? cv.getLocation() : null));
        grid.add(createInfoPill("V\u1ecb tr\u00ed mong mu\u1ed1n", cv != null ? cv.getDesiredPosition() : null));
        return createCVSection("Th\u00f4ng tin c\u00e1 nh\u00e2n", grid);
    }

    private JPanel createInfoPill(String label, String value) {
        JPanel panel = new RoundedPanel(10, new Color(248, 249, 250));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 12, 10, 12));
        JLabel title = new JLabel(label);
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(new Color(13, 110, 253));
        JLabel content = new JLabel(valueOrDefault(value, "Ch\u01b0a c\u1eadp nh\u1eadt"));
        content.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        content.setForeground(new Color(51, 51, 51));
        panel.add(title, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCVSection(String title, Component body) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(new EmptyBorder(0, 0, 18, 0));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(new Color(33, 37, 41));
        heading.setBorder(new EmptyBorder(0, 0, 10, 0));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(heading);

        JPanel card = new RoundedPanel(12, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(232, 236, 242), 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));
        card.add(body, BorderLayout.CENTER);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(card);
        return section;
    }

    private JTextArea createCVTextBlock(String value) {
        JTextArea text = new JTextArea(valueOrDefault(value, "Ch\u01b0a c\u1eadp nh\u1eadt."));
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        text.setForeground(new Color(51, 51, 51));
        text.setOpaque(false);
        text.setBorder(null);
        return text;
    }

    private JPanel createEducationSection(CVDTO cv) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.WHITE);
        if (cv != null && cv.getEducations() != null && !cv.getEducations().isEmpty()) {
            for (Education education : cv.getEducations()) {
                list.add(createEducationBlock(education));
                list.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            list.add(createCVTextBlock("Ch\u01b0a c\u00f3 th\u00f4ng tin h\u1ecdc v\u1ea5n."));
        }
        return createCVSection("H\u1ecdc v\u1ea5n", list);
    }

    private JPanel createEducationBlock(Education education) {
        JPanel panel = new RoundedPanel(10, new Color(248, 249, 250));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(12, 14, 12, 14));
        panel.add(createEducationLine("Tr\u01b0\u1eddng", education.getSchool()));
        panel.add(createEducationLine("B\u1eb1ng c\u1ea5p", education.getDegree()));
        panel.add(createEducationLine("Chuy\u00ean ng\u00e0nh", education.getMajor()));
        String years = "";
        if (education.getStartYear() != null) years += education.getStartYear();
        if (education.getEndYear() != null) years += (years.isEmpty() ? "" : " - ") + education.getEndYear();
        panel.add(createEducationLine("Th\u1eddi gian", years));
        if (education.getDescription() != null && !education.getDescription().isBlank()) {
            panel.add(Box.createRigidArea(new Dimension(0, 6)));
            panel.add(createCVTextBlock(education.getDescription()));
        }
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private JPanel createEducationLine(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(3, 0, 3, 0));
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(33, 37, 41));
        JLabel val = new JLabel(valueOrDefault(value, "Ch\u01b0a c\u1eadp nh\u1eadt"));
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        val.setForeground(new Color(51, 51, 51));
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }

    private JPanel createCVDialogFooter(JDialog dialog) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(14, 0, 0, 0));
        JButton close = new JButton("\u0110\u00f3ng");
        close.setPreferredSize(new Dimension(110, 36));
        close.setFont(new Font("Segoe UI", Font.BOLD, 13));
        close.setForeground(Color.WHITE);
        close.setBackground(new Color(13, 110, 253));
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dialog.dispose());
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                close.setBackground(new Color(9, 88, 202));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                close.setBackground(new Color(13, 110, 253));
            }
        });
        footer.add(close);
        return footer;
    }

    private JPanel createLargeAvatar(String initials) {
        JPanel avatar = createAvatar(initials);
        avatar.setPreferredSize(new Dimension(64, 64));
        avatar.setMinimumSize(new Dimension(64, 64));
        avatar.setMaximumSize(new Dimension(64, 64));
        return avatar;
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fill;

        RoundedPanel(int radius, Color fill) {
            this.radius = radius;
            this.fill = fill;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private String valueOrEmpty(String value) {
        return value != null && !value.isBlank() ? value : "";
    }

    private String valueOrDefault(String value, String fallback) {
        return value != null && !value.isBlank() ? value : fallback;
    }

    private JPanel createAvatar(String initials) {
        // tao hinh tron gia lap bang JPanel
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 225, 235));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setLayout(new GridBagLayout());
        JLabel lbl = new JLabel(initials);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(50, 60, 80));
        avatar.add(lbl);
        return avatar;
    }

    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel("  " + status + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setOpaque(true);

        switch (status) {
            case "Đang duyệt":
                badge.setBackground(new Color(230, 230, 230));
                badge.setForeground(Color.DARK_GRAY);
                break;
            case "Đã duyệt":
                badge.setBackground(new Color(230, 250, 240));
                badge.setForeground(new Color(40, 167, 69));
                break;
            case "Bị từ chối":
                badge.setBackground(new Color(255, 243, 230));
                badge.setForeground(new Color(253, 126, 20));
                break;
        }
        return badge;
    }

    private String toStatusLabel(ApplicationStatus status) {
        if (status == ApplicationStatus.APPROVED) return "Đã duyệt";
        if (status == ApplicationStatus.REJECTED) return "Bị từ chối";
        return "Đang duyệt";
    }

    private JButton createActionBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(color);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createPageBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (active) {
            btn.setBackground(new Color(200, 220, 255));
            btn.setForeground(new Color(13, 110, 253));
            btn.setBorder(new LineBorder(new Color(13, 110, 253), 1));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        }
        return btn;
    }

    private JPanel createBottomSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // cot trai: AI Suggestion (chiem khoang 70%)
        gbc.gridx = 0; gbc.weightx = 0.7; gbc.insets = new Insets(0, 0, 0, 15);

        JPanel aiCard = new JPanel();
        aiCard.setLayout(new BoxLayout(aiCard, BoxLayout.Y_AXIS));
        aiCard.setBackground(Color.WHITE);
        aiCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));

        // cot phai: Promo Banner (chiem khoang 30%)
        gbc.gridx = 1; gbc.weightx = 0.3; gbc.insets = new Insets(0, 0, 0, 0);

        return panel;
    }

    private JPanel createAIHintItem(String title, String desc, Color borderColor) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(new Color(248, 249, 250));
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, borderColor),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lTitle = new JLabel("<html><b>" + title + "</b></html>");
        lTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextArea lDesc = new JTextArea(desc);
        lDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lDesc.setForeground(Color.DARK_GRAY);
        lDesc.setOpaque(false);
        lDesc.setLineWrap(true);
        lDesc.setWrapStyleWord(true);
        lDesc.setEditable(false);

        textPanel.add(lTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(lDesc);

        item.add(textPanel, BorderLayout.CENTER);
        return item;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Portal - Duyet ho so");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 850);
            frame.setLayout(new BorderLayout());

            // Header o NORTH
            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            // Sidebar o WEST
            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.EMPLOYER);
            frame.add(sidebar, BorderLayout.WEST);

            // Panel chinh o CENTER
            ApplicationReviewPanel reviewPanel = new ApplicationReviewPanel();
            frame.add(reviewPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
