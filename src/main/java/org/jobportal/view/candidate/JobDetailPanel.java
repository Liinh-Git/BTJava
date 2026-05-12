package org.jobportal.view.candidate;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IApplicationService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class JobDetailPanel extends JPanel {

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private final IApplicationService applicationService = new ApplicationService();
    
    private String recruitmentId;
    private JDialog parentDialog;
    private RecruitmentDTO recruitment;

    public JobDetailPanel(String recruitmentId, JDialog parentDialog) {
        this.recruitmentId = recruitmentId;
        this.parentDialog = parentDialog;
        this.recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        // thiet lap layout chinh la border layout de ghim thanh bottom va top
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        // 1. thanh dieu huong tren cung (Top Bar) chua nut Back
        add(createTopBar(), BorderLayout.NORTH);

        // 2. phan noi dung chinh (co the cuon)
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(20, 40, 20, 40));

        // them the tieu de cong viec
        mainContent.add(createHeaderCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // them luoi thong tin tom tat (6 o)
        mainContent.add(createSummaryGrid());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // them phan chi tiet (mo ta, yeu cau, phuc loi)
        mainContent.add(createDetailsCard());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // 3. thanh hanh dong co dinh o day (Bottom Action Bar)
        add(createBottomBar(), BorderLayout.SOUTH);
    }

    // --- tao cac thanh phan chinh ---

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panel.setBackground(new Color(248, 249, 250));

        JLabel lblBack = new JLabel("<- Back");
        lblBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBack.setForeground(Color.DARK_GRAY);
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (parentDialog != null) {
                    parentDialog.dispose();
                }
            }
        });

        panel.add(lblBack);
        return panel;
    }

    private JPanel createHeaderCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // dong 1: tieu de va the (tags)
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(recruitment != null ? recruitment.getTitle() : "Unknown");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(lblTitle, BorderLayout.WEST);

        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tagsPanel.setBackground(Color.WHITE);
        if (recruitment != null) {
            tagsPanel.add(createTag(recruitment.getJobType() != null ? recruitment.getJobType().name() : "FULL_TIME", new Color(225, 230, 255), new Color(50, 70, 150)));
            tagsPanel.add(createTag(recruitment.getStatus() != null ? recruitment.getStatus().name() : "OPEN", new Color(210, 245, 220), new Color(40, 120, 60)));
        }
        titlePanel.add(tagsPanel, BorderLayout.EAST);

        card.add(titlePanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // dong 2 & 3: thong tin cong ty va dia diem
        if (recruitment != null) {
            card.add(createIconTextRow("::", recruitment.getCompanyName(), true));
            card.add(Box.createRigidArea(new Dimension(0, 8)));
            card.add(createIconTextRow("o", recruitment.getLocation(), false));
        }

        return card;
    }

    private JPanel createSummaryGrid() {
        // tao luoi 3 hang 2 cot
        JPanel grid = new JPanel(new GridLayout(3, 2, 20, 15));
        grid.setBackground(new Color(248, 249, 250));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        String location = recruitment != null && recruitment.getLocation() != null ? recruitment.getLocation() : "N/A";
        String salary = recruitment != null && recruitment.getSalary() != null ? "$" + recruitment.getSalary().intValue() : "Negotiable";
        String exp = recruitment != null && recruitment.getExperienceRequired() != null ? recruitment.getExperienceRequired() : "Not specified";
        String apps = recruitment != null ? String.valueOf(recruitment.getApplicationCount()) + " applicants" : "0 applicants";
        String created = recruitment != null && recruitment.getCreatedDate() != null ? recruitment.getCreatedDate().format(formatter) : "N/A";
        String due = recruitment != null && recruitment.getDueDate() != null ? recruitment.getDueDate().format(formatter) : "N/A";

        grid.add(createSummaryBox("LOCATION", location));
        grid.add(createSummaryBox("SALARY", salary));
        grid.add(createSummaryBox("EXPERIENCE", exp));
        grid.add(createSummaryBox("APPLICANTS", apps));
        grid.add(createSummaryBox("CREATED DATE", created));
        grid.add(createSummaryBox("DUE DATE", due));

        return grid;
    }

    private JPanel createDetailsCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblHeader = new JLabel("Description");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(lblHeader);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // doan van mo ta chung
        String desc = recruitment != null ? recruitment.getDescription() : "No description available.";
        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDesc.setForeground(new Color(60, 60, 60));
        txtDesc.setBackground(Color.WHITE);
        card.add(txtDesc);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        // chia 2 cot cho responsibilities va requirements
        JPanel twoColPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        twoColPanel.setBackground(Color.WHITE);

        String respHtml = "<html><h3 style='font-family: Segoe UI;'>Responsibilities</h3>"
                + "<ul style='font-family: Segoe UI; font-size: 10px; color: #444;'>"
                + "<li>Design and maintain high-availability APIs and microservices.</li>"
                + "<li>Collaborate with product managers to define technical roadmaps.</li>"
                + "<li>Mentor junior engineers and conduct thorough code reviews.</li>"
                + "<li>Optimize system performance and solve complex scalability bottlenecks.</li>"
                + "<li>Participate in on-call rotations to ensure 99.9% service uptime.</li>"
                + "</ul></html>";
        twoColPanel.add(new JLabel(respHtml));

        String reqHtml = "<html><h3 style='font-family: Segoe UI;'>Requirements</h3>"
                + "<ul style='font-family: Segoe UI; font-size: 10px; color: #444;'>"
                + "<li>BS/MS in Computer Science or equivalent practical experience.</li>"
                + "<li>5+ years of professional experience with Node.js, Go, or Python.</li>"
                + "<li>Strong experience with SQL (PostgreSQL) and NoSQL databases.</li>"
                + "<li>Proficiency in cloud infrastructure (AWS/GCP) and Docker/Kubernetes.</li>"
                + "<li>Experience with message brokers like RabbitMQ or Kafka.</li>"
                + "</ul></html>";
        twoColPanel.add(new JLabel(reqHtml));

        card.add(twoColPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // phuc loi
        String benHtml = "<html><h3 style='font-family: Segoe UI;'>Benefits</h3>"
                + "<ul style='font-family: Segoe UI; font-size: 10px; color: #444;'>"
                + "<li>Unlimited PTO and flexible working hours.</li>"
                + "<li>$2,000 annual professional development budget.</li>"
                + "<li>Remote-first culture with optional co-working spaces.</li>"
                + "</ul></html>";
        card.add(new JLabel(benHtml));

        return card;
    }

    private JPanel createBottomBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                new EmptyBorder(15, 40, 15, 40)
        ));

        // ben phai: cac nut thao tac
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);

        JButton btnShare = new JButton("SHARE JOB");
        btnShare.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnShare.setBackground(Color.WHITE);
        btnShare.setForeground(Color.DARK_GRAY);
        btnShare.setPreferredSize(new Dimension(120, 45));
        btnShare.setFocusPainted(false);
        btnShare.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        btnShare.addActionListener(e -> {
            if (recruitment != null) {
                String shareText = "Job: " + recruitment.getTitle() + " - ID: " + recruitment.getRecruitmentId();
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(shareText), null);
                JOptionPane.showMessageDialog(this, "Đã copy thông tin công việc vào clipboard!");
            }
        });

        JButton btnApply = new JButton("Apply / Send CV");
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnApply.setBackground(new Color(13, 110, 253));
        btnApply.setForeground(Color.WHITE);
        btnApply.setPreferredSize(new Dimension(160, 45));
        btnApply.setFocusPainted(false);
        btnApply.setBorderPainted(false);
        btnApply.addActionListener(e -> {
            if (recruitmentId != null && SessionManager.getInstance().getCurrentUser() != null) {
                String candidateId = SessionManager.getInstance().getCandidateId();
                if (candidateId == null) {
                    JOptionPane.showMessageDialog(this, "Bạn phải là ứng viên (Candidate) mới có thể nộp hồ sơ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean success = applicationService.applyRecruitment(candidateId, recruitmentId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Nộp hồ sơ thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Bạn đã nộp hồ sơ hoặc có lỗi xảy ra!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        rightPanel.add(btnShare);
        rightPanel.add(btnApply);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    // --- cac ham tien ich ho tro tao UI ---

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

    // ham main de chay thu giao dien doc lap
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Job Detail");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);

            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.CANDIDATE);
            frame.add(sidebar, BorderLayout.WEST);

            JPanel rightPanel = new JPanel(new BorderLayout());

            JobDetailPanel jobDetailPanel = new JobDetailPanel(null, null);
            rightPanel.add(jobDetailPanel, BorderLayout.CENTER);

            frame.add(rightPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}