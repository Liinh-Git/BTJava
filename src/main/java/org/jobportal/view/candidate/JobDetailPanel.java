package org.jobportal.view.candidate;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IApplicationService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.enums.JobType;
import org.jobportal.enums.Role;
import org.jobportal.utils.SessionManager;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class JobDetailPanel extends JPanel {

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private final IApplicationService applicationService = new ApplicationService();

    private final String recruitmentId;
    private final JDialog parentDialog;
    private final RecruitmentDTO recruitment;

    public JobDetailPanel(String recruitmentId, JDialog parentDialog) {
        this.recruitmentId = recruitmentId;
        this.parentDialog = parentDialog;
        this.recruitment = recruitmentService.getRecruitmentById(recruitmentId);

        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        add(createTopBar(), BorderLayout.NORTH);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(20, 40, 20, 40));

        mainContent.add(createHeaderCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(createSummaryGrid());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(createDetailsCard());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        add(createBottomBar(), BorderLayout.SOUTH);
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panel.setBackground(new Color(248, 249, 250));

        JLabel lblBack = new JLabel("<- Quay lại");
        lblBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBack.setForeground(Color.DARK_GRAY);
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (parentDialog != null) parentDialog.dispose();
            }
        });

        panel.add(lblBack);
        return panel;
    }

    private JPanel createHeaderCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(recruitment != null ? recruitment.getTitle() : "Không tìm thấy tin");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(lblTitle, BorderLayout.WEST);

        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tagsPanel.setBackground(Color.WHITE);
        if (recruitment != null) {
            tagsPanel.add(createTag(toJobTypeLabel(recruitment.getJobType()), new Color(225, 230, 255), new Color(50, 70, 150)));
        }
        titlePanel.add(tagsPanel, BorderLayout.EAST);

        card.add(titlePanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        if (recruitment != null) {
            card.add(createIconTextRow("::", safeText(recruitment.getCompanyName(), "Chưa rõ công ty"), true));
            card.add(Box.createRigidArea(new Dimension(0, 8)));
            card.add(createIconTextRow("o", safeText(recruitment.getLocation(), "Chưa cập nhật địa điểm"), false));
        }
        return card;
    }

    private JPanel createSummaryGrid() {
        JPanel grid = new JPanel(new GridLayout(3, 2, 20, 15));
        grid.setBackground(new Color(248, 249, 250));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String location = recruitment != null ? safeText(recruitment.getLocation(), "Chưa cập nhật địa điểm") : "N/A";
        String salary = recruitment != null && recruitment.getSalary() != null ? String.format("%,.0f VND", recruitment.getSalary()) : "Thỏa thuận";
        String exp = recruitment != null ? safeText(recruitment.getExperienceRequired(), "Chưa cập nhật") : "N/A";
        String apps = recruitment != null ? recruitment.getApplicationCount() + " ứng viên" : "0 ứng viên";
        String created = recruitment != null && recruitment.getCreatedDate() != null ? recruitment.getCreatedDate().format(formatter) : "N/A";
        String due = recruitment != null && recruitment.getDueDate() != null ? recruitment.getDueDate().format(formatter) : "N/A";

        grid.add(createSummaryBox("ĐỊA ĐIỂM", location));
        grid.add(createSummaryBox("LƯƠNG", salary));
        grid.add(createSummaryBox("KINH NGHIỆM", exp));
        grid.add(createSummaryBox("ỨNG VIÊN", apps));
        grid.add(createSummaryBox("NGÀY ĐĂNG", created));
        grid.add(createSummaryBox("HẠN NỘP", due));
        return grid;
    }

    private JPanel createDetailsCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblHeader = new JLabel("Mô tả công việc");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(lblHeader);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        String desc = recruitment != null ? safeText(recruitment.getDescription(), "Chưa có mô tả cho công việc này.") : "Không tìm thấy tin tuyển dụng.";
        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDesc.setForeground(new Color(60, 60, 60));
        txtDesc.setBackground(Color.WHITE);
        txtDesc.setBorder(null);
        card.add(txtDesc);
        return card;
    }

    private JPanel createBottomBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                new EmptyBorder(15, 40, 15, 40)
        ));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);

        JButton btnShare = new JButton("Chia sẻ");
        btnShare.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnShare.setBackground(Color.WHITE);
        btnShare.setForeground(Color.DARK_GRAY);
        btnShare.setPreferredSize(new Dimension(120, 45));
        btnShare.setFocusPainted(false);
        btnShare.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        btnShare.addActionListener(e -> copyShareText());

        JButton btnApply = new JButton("Nộp hồ sơ");
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnApply.setBackground(new Color(13, 110, 253));
        btnApply.setForeground(Color.WHITE);
        btnApply.setPreferredSize(new Dimension(160, 45));
        btnApply.setFocusPainted(false);
        btnApply.setBorderPainted(false);
        btnApply.addActionListener(e -> applyJob());

        rightPanel.add(btnShare);
        rightPanel.add(btnApply);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private void copyShareText() {
        if (recruitment == null) return;
        String shareText = "Tin tuyển dụng: " + recruitment.getTitle() + " - ID: " + recruitment.getRecruitmentId();
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(shareText), null);
        JOptionPane.showMessageDialog(this, "Đã sao chép thông tin công việc vào clipboard!");
    }

    private void applyJob() {
        if (recruitmentId == null || SessionManager.getInstance().getCurrentUser() == null) return;
        String candidateId = SessionManager.getInstance().getCandidateId();
        if (candidateId == null) {
            JOptionPane.showMessageDialog(this, "Bạn phải là ứng viên mới có thể nộp hồ sơ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean success = applicationService.applyRecruitment(candidateId, recruitmentId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Nộp hồ sơ thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Bạn đã nộp hồ sơ hoặc có lỗi xảy ra!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(25, 30, 25, 30)
        ));
        return card;
    }

    private JPanel createSummaryBox(String title, String value) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblValue.setForeground(Color.BLACK);

        box.add(lblTitle);
        box.add(Box.createRigidArea(new Dimension(0, 5)));
        box.add(lblValue);
        return box;
    }

    private JLabel createTag(String text, Color bg, Color fg) {
        JLabel lbl = new JLabel(" " + text + " ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setOpaque(true);
        lbl.setBackground(bg);
        lbl.setForeground(fg);
        lbl.setBorder(new EmptyBorder(3, 8, 3, 8));
        return lbl;
    }

    private JPanel createIconTextRow(String icon, String text, boolean isBold) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setForeground(Color.GRAY);

        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", isBold ? Font.BOLD : Font.PLAIN, 13));
        lblText.setForeground(isBold ? Color.DARK_GRAY : Color.GRAY);

        panel.add(lblIcon);
        panel.add(lblText);
        return panel;
    }

    private String toJobTypeLabel(JobType jobType) {
        if (jobType == JobType.PARTTIME) return "Part-time";
        if (jobType == JobType.INTERNSHIP) return "Internship";
        return "Full-time";
    }

    private String safeText(String value, String fallback) {
        return value != null && !value.isBlank() ? value : fallback;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chi tiết việc làm");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.add(new HeaderPanel(), BorderLayout.NORTH);
            frame.add(new SidebarPanel(Role.CANDIDATE), BorderLayout.WEST);
            frame.add(new JobDetailPanel(null, null), BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
