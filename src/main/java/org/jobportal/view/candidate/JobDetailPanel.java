package org.jobportal.view.candidate;
import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IApplicationService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.enums.JobType;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class JobDetailPanel extends JPanel {

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private final IApplicationService applicationService = new ApplicationService();

    private final String recruitmentId;
    @SuppressWarnings("unused")
    private final JDialog parentDialog;
    private final RecruitmentDTO recruitment;

    public JobDetailPanel(String recruitmentId, JDialog parentDialog) {
        this.recruitmentId = recruitmentId;
        this.parentDialog = parentDialog;
        this.recruitment = recruitmentService.getRecruitmentById(recruitmentId);

        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(16, 20, 16, 20));
        mainContent.setPreferredSize(new Dimension(700, 800));
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.setAlignmentY(Component.TOP_ALIGNMENT);

        mainContent.add(createHeaderCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, 14)));
        mainContent.add(createSummaryGrid());
        mainContent.add(Box.createRigidArea(new Dimension(0, 14)));
        mainContent.add(createDetailsCard());

        JPanel centeredWrapper = new JPanel();
        centeredWrapper.setLayout(new BoxLayout(centeredWrapper, BoxLayout.X_AXIS));
        centeredWrapper.setBackground(new Color(248, 249, 250));
        centeredWrapper.add(Box.createHorizontalGlue());
        centeredWrapper.add(mainContent);
        centeredWrapper.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(centeredWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        add(createBottomBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
        headerPanel.setBackground(Color.WHITE);

        JPanel leftInfo = new JPanel();
        leftInfo.setLayout(new BoxLayout(leftInfo, BoxLayout.Y_AXIS));
        leftInfo.setBackground(Color.WHITE);
        leftInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(recruitment != null ? recruitment.getTitle() : "Không tìm thấy tin");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftInfo.add(lblTitle);

        if (recruitment != null) {
            leftInfo.add(Box.createRigidArea(new Dimension(0, 3)));
            leftInfo.add(createIconTextRow("::", safeText(recruitment.getCompanyName(), "Chưa rõ công ty"), false));
        }

        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tagsPanel.setBackground(Color.WHITE);
        if (recruitment != null) {
            tagsPanel.add(createTag(toJobTypeLabel(recruitment.getJobType()), new Color(225, 230, 255), new Color(50, 70, 150)));
        }

        headerPanel.add(leftInfo, BorderLayout.CENTER);
        headerPanel.add(tagsPanel, BorderLayout.EAST);
        card.add(headerPanel);
        return card;
    }

    private JPanel createSummaryGrid() {
        JPanel grid = new JPanel(new GridLayout(3, 2, 14, 10));
        grid.setBackground(new Color(248, 249, 250));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String location = recruitment != null ? safeText(recruitment.getLocation(), "Chưa cập nhật địa điểm") : "N/A";
        String salary = recruitment != null && recruitment.getSalary() != null ? String.format("%,.0f VND", recruitment.getSalary()) : "Thỏa thuận";
        String exp = recruitment != null ? safeText(recruitment.getExperienceRequired(), "Chưa cập nhật") : "N/A";
        String apps = recruitment != null ? recruitment.getApplicationCount() + " ứng viên" : "0 ứng viên";
        String created = recruitment != null && recruitment.getCreatedDate() != null ? recruitment.getCreatedDate().format(formatter) : "N/A";
        String due = recruitment != null && recruitment.getDueDate() != null ? recruitment.getDueDate().format(formatter) : "N/A";

        grid.add(createSummaryBox("ĐỊA ĐIỂM", location));
        grid.add(createSummaryBox("THU NHẬP", salary));
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

        JButton btnApply = new JButton("Nộp hồ sơ");
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnApply.setBackground(new Color(13, 110, 253));
        btnApply.setForeground(Color.WHITE);
        btnApply.setPreferredSize(new Dimension(160, 45));
        btnApply.setFocusPainted(false);
        btnApply.setBorderPainted(false);
        btnApply.addActionListener(e -> applyJob());

        rightPanel.add(btnApply);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
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
                new EmptyBorder(18, 22, 18, 22)
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", isBold ? Font.BOLD : Font.PLAIN, 13));
        lblText.setForeground(isBold ? Color.DARK_GRAY : Color.GRAY);
        lblText.setHorizontalAlignment(SwingConstants.LEFT);
        lblText.setAlignmentX(Component.LEFT_ALIGNMENT);
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
}
