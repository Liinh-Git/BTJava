package org.jobportal.view.candidate;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.interfaces.IApplicationService;
import org.jobportal.dto.ApplicationDTO;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppliedJobsPanel extends JPanel {

    private final IApplicationService applicationService = new ApplicationService();
    private JPanel statsPanel;
    private JPanel tableContainer;
    private JLabel lblCount;
    private List<ApplicationDTO> currentApps = null; // cache danh sach hien tai

    public AppliedJobsPanel() {
        // thiet lap mau nen va layout chinh
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        // panel noi dung chinh co the cuon
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. tieu de trang va cac nut thao tac
        mainContent.add(createPageHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. phan thong ke (3 the: Total, Active, Success Rate)
        statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(248, 249, 250));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainContent.add(statsPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // 3. danh sach don ung tuyen (Gia lap bang)
        tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        mainContent.add(tableContainer);
        
        loadData();
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // boc vao scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ham tao tieu de trang
    private JPanel createPageHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // ben trai: Tieu de va mo ta
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(248, 249, 250));

        JLabel lblTitle = new JLabel("Việc làm đã ứng tuyển");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel lblSub = new JLabel("Theo dõi trạng thái các đơn ứng tuyển của bạn.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);

        leftPanel.add(lblTitle);
        leftPanel.add(lblSub);

        // ben phai: Export
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(248, 249, 250));

        JButton btnExport = createOutlineButton("↓ Xuất CSV", "");

        btnExport.addActionListener(e -> exportToCSV());

        rightPanel.add(btnExport);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    // Xuat danh sach ra file CSV
    private void exportToCSV() {
        if (currentApps == null || currentApps.isEmpty()) {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Không có dữ liệu để xuất!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file báo cáo");
        fileChooser.setSelectedFile(new java.io.File("applied_jobs_report.csv"));
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        java.io.File file = fileChooser.getSelectedFile();
        // Dam bao co duoi .csv
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new java.io.File(file.getAbsolutePath() + ".csv");
        }

        try (FileWriter fw = new FileWriter(file)) {
            // Ghi header
            fw.write("\uFEFF"); // BOM de Excel doc dung UTF-8
            fw.write("STT,Tên công việc,Công ty,Ngày ứng tuyển,Trạng thái\n");

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            int idx = 1;
            for (ApplicationDTO app : currentApps) {
                String title = csvEscape(app.getJobTitle());
                String company = csvEscape(app.getCompanyName());
                String date = app.getAppliedDate() != null ? app.getAppliedDate().format(fmt) : "N/A";
                String status = toStatusLabel(app.getStatus());
                fw.write(idx++ + "," + title + "," + company + "," + date + "," + status + "\n");
            }

            org.jobportal.view.common.SuccessDialog.showMessageDialog(this,
                "Xuất báo cáo thành công!\nFile: " + file.getAbsolutePath(),
                "Thành công", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this,
                "Lỗi khi xuất file: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Thoat ky tu dac biet trong CSV (bao ca dau phay va xuat tuyến trong gia tri)
    private String csvEscape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    // ham tao 3 the thong ke
    private void updateStats(String candidateId) {
        statsPanel.removeAll();
        if (candidateId != null) {
            int total = applicationService.getTotalApplyCountByUser(candidateId);
            int approved = applicationService.getApprovedApplicationByUser(candidateId);
            List<ApplicationDTO> apps = applicationService.getListOfApplicationByUser(candidateId);
            int pending = 0;
            for (ApplicationDTO app : apps) {
                if (app.getStatus() == ApplicationStatus.PENDING) {
                    pending++;
                }
            }
            double rate = total > 0 ? (approved * 100.0 / total) : 0;
            
            statsPanel.add(createStatCard("TỔNG ĐƠN", String.valueOf(total), null));
            statsPanel.add(createStatCard("ĐƠN CHỜ DUYỆT", String.valueOf(pending), null));
            statsPanel.add(createStatCard("TỈ LỆ ĐƯỢC DUYỆT", String.format("%.1f%%", rate), null));
        } else {
            statsPanel.add(createStatCard("TỔNG ĐƠN", "0", null));
            statsPanel.add(createStatCard("ĐƠN CHỜ DUYỆT", "0", null));
            statsPanel.add(createStatCard("TỈ LỆ ĐƯỢC DUYỆT", "0%", null));
        }
        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createStatCard(String label, String value, String badgeText) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblLabel.setForeground(Color.GRAY);

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        valuePanel.setBackground(Color.WHITE);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valuePanel.add(lblValue);

        if (badgeText != null) {
            JLabel badge = new JLabel(" " + badgeText + " ");
            badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            badge.setOpaque(true);
            badge.setBackground(new Color(230, 240, 255));
            badge.setForeground(new Color(0, 100, 250));
            valuePanel.add(badge);
        }

        card.add(lblLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(valuePanel);
        return card;
    }

    private void loadData() {
        String candidateId = SessionManager.getInstance().getCandidateId();
        updateStats(candidateId);
        
        tableContainer.removeAll();
        tableContainer.add(createRow("TIN TUYỂN DỤNG", "CÔNG TY", "NGÀY ỨNG TUYỂN", "TRẠNG THÁI", "THAO TÁC", true, null));

        if (candidateId != null) {
            currentApps = applicationService.getListOfApplicationByUser(candidateId);
        } else {
            currentApps = null;
        }

        if (currentApps != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            for (ApplicationDTO app : currentApps) {
                String title = app.getJobTitle();
                String company = app.getCompanyName(); 
                String dateStr = app.getAppliedDate() != null ? app.getAppliedDate().format(formatter) : "N/A";
                String statusStr = toStatusLabel(app.getStatus());
                
                tableContainer.add(createRow(title, company, dateStr, statusStr, "...", false, app));
            }
        }

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));

        if (lblCount == null) {
            lblCount = new JLabel();
            lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblCount.setForeground(Color.GRAY);
        }
        lblCount.setText("Hiển thị " + (currentApps != null ? currentApps.size() : 0) + " đơn ứng tuyển");
        footer.add(lblCount, BorderLayout.WEST);

        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pagination.setBackground(Color.WHITE);
        pagination.add(createPageBtn("1", true));
        footer.add(pagination, BorderLayout.EAST);

        tableContainer.add(footer);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private JPanel createRow(String col1, String col2, String col3, String status, String action, boolean isHeader, ApplicationDTO app) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? new Color(250, 250, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        row.setPreferredSize(new Dimension(0, 70));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.BLACK;

        row.add(createTextCell(col1, font, textColor), rowGbc(0, 0.30));
        row.add(createTextCell(col2, font, textColor), rowGbc(1, 0.24));
        row.add(createTextCell(col3, font, textColor), rowGbc(2, 0.18));

        JPanel p4 = new JPanel(new GridBagLayout());
        p4.setOpaque(false);
        if (isHeader) {
            JLabel l4 = new JLabel(status);
            l4.setFont(font); l4.setForeground(textColor);
            p4.add(l4);
        } else {
            p4.add(createStatusBadge(status));
        }
        row.add(p4, rowGbc(3, 0.16));

        JPanel p5 = new JPanel(new GridBagLayout());
        p5.setOpaque(false);
        if (isHeader) {
            JLabel l5 = new JLabel(action);
            l5.setFont(font);
            l5.setForeground(Color.GRAY);
            p5.add(l5);
        } else {
            JButton btnCancel = new JButton("Hủy");
            btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnCancel.setBackground(Color.WHITE);
            btnCancel.setForeground(Color.RED);
            btnCancel.addActionListener(e -> {
                if (app != null) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn hủy đơn ứng tuyển này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = applicationService.cancelApplication(app.getApplicationId());
                        if (success) {
                            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Đã hủy đơn ứng tuyển!");
                            loadData();
                        } else {
                            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Lỗi khi hủy đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            p5.add(btnCancel);
        }
        row.add(p5, rowGbc(4, 0.12));

        return row;
    }

    private JPanel createTextCell(String text, Font font, Color color) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(label, gbc);
        return panel;
    }

    private GridBagConstraints rowGbc(int x, double weight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = 0;
        gbc.weightx = weight;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 0, 20);
        return gbc;
    }

    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel("  " + status + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setOpaque(true);

        switch (status) {
            case "Đang duyệt":
            case "Chờ duyệt":
                badge.setBackground(new Color(230, 240, 255));
                badge.setForeground(new Color(13, 110, 253));
                break;
            case "Bị từ chối":
                badge.setBackground(new Color(255, 243, 230));
                badge.setForeground(new Color(253, 126, 20));
                break;
            case "Đã duyệt":
                badge.setBackground(new Color(230, 250, 240));
                badge.setForeground(new Color(40, 167, 69));
                break;
        }

        badge.setBorder(new LineBorder(badge.getForeground(), 1, true));
        return badge;
    }

    private String toStatusLabel(ApplicationStatus status) {
        if (status == ApplicationStatus.APPROVED) return "Đã duyệt";
        if (status == ApplicationStatus.REJECTED) return "Bị từ chối";
        return "Đang duyệt";
    }

    private JButton createOutlineButton(String text, String icon) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
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

    // ham main de kiem tra giao dien
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ứng viên - Việc làm đã ứng tuyển");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 850);
            frame.setLayout(new BorderLayout());

            // dung lai HeaderPanel
            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            // dung lai SidebarPanel (Role Candidate)
            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.CANDIDATE);
            frame.add(sidebar, BorderLayout.WEST);

            JPanel rightPanel = new JPanel(new BorderLayout());
            // them panel AppliedJobs vua code
            AppliedJobsPanel appliedPanel = new AppliedJobsPanel();
            rightPanel.add(appliedPanel, BorderLayout.CENTER);

            frame.add(rightPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
