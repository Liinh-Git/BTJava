package org.jobportal.view.candidate;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.dto.RecruitmentDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

public class JobDetailPanel extends JPanel {

    private final RecruitmentService recruitmentService = new RecruitmentService();
    private final ApplicationService applicationService = new ApplicationService();
    private String recruitmentId;
    private JLabel lblTitle;
    private JLabel lblCompany;
    private JLabel lblCategory;
    private JLabel lblLocation;
    private JLabel lblSalary;
    private JLabel lblJobType;
    private JLabel lblStatus;
    private JLabel lblCreatedDate;
    private JLabel lblDueDate;
    private JLabel lblApplicants;
    private JTextArea txtDescription;
    private JButton btnApply;

    public JobDetailPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        mainContent.add(createTitleCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createMetadataAndSidebar());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createDescriptionCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createBottomBar());

        add(createScrollPane(mainContent), BorderLayout.CENTER);
    }

    public void loadRecruitment(String recruitmentId) {
        this.recruitmentId = recruitmentId;
        RecruitmentDTO dto = recruitmentService.getRecruitmentById(recruitmentId);
        if (dto == null) return;

        lblTitle.setText(dto.getTitle());
        lblCompany.setText("🏢  " + dto.getCompanyName());
        lblCategory.setText(dto.getCategoryName() != null ? dto.getCategoryName() : "");
        lblLocation.setText(dto.getLocation() != null ? dto.getLocation() : "");
        lblSalary.setText(dto.getSalary() != null ? String.valueOf(dto.getSalary()) : "");
        lblJobType.setText(dto.getJobType() != null ? dto.getJobType().name() : "");
        lblStatus.setText(dto.getStatus() != null ? dto.getStatus().name() : "");
        lblCreatedDate.setText(dto.getCreatedDate() != null ? dto.getCreatedDate().toLocalDate().toString() : "");
        lblDueDate.setText(dto.getDueDate() != null ? dto.getDueDate().toLocalDate().toString() : "");
        lblApplicants.setText(dto.getApplicationCount() + " applicants");
        txtDescription.setText(dto.getDescription() != null ? dto.getDescription() : "");

        boolean hasApplied = applicationService.hasApplied(
                org.jobportal.utils.SessionManager.getCurrentUser().getUserId(), recruitmentId);
        updateApplyButton(hasApplied);
    }

    private void updateApplyButton(boolean hasApplied) {
        if (hasApplied) {
            btnApply.setText("Cancel Application");
            btnApply.setBackground(DANGER);
        } else {
            btnApply.setText("Apply / Send CV");
            btnApply.setBackground(PRIMARY);
        }
    }

    private JPanel createTitleCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_SURFACE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(BG_SURFACE);

        lblTitle = new JLabel("Senior Software Engineer");
        lblTitle.setFont(heading2());
        lblTitle.setForeground(TEXT_PRIMARY);

        lblCompany = new JLabel("🏢  TechFlow Systems Inc.");
        lblCompany.setFont(body());
        lblCompany.setForeground(TEXT_SECONDARY);

        left.add(lblTitle);
        left.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        left.add(lblCompany);
        card.add(left, BorderLayout.CENTER);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_2, 0));
        badges.setBackground(BG_SURFACE);
        lblJobType = createBadge("FULLTIME", BADGE_BG, BADGE_FG);
        lblStatus = createBadge("OPEN", BADGE_SUCCESS_BG, BADGE_SUCCESS_FG);
        badges.add(lblJobType);
        badges.add(lblStatus);
        card.add(badges, BorderLayout.EAST);

        return card;
    }

    private JPanel createMetadataAndSidebar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(createMetadataGrid(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMetadataGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 3, SPACE_4, SPACE_4));
        grid.setBackground(BG_PAGE);

        lblLocation = createMetaCard("📍", "LOCATION", "San Francisco, CA");
        lblSalary = createMetaCard("💰", "SALARY", "$120k - $150k");
        lblCategory = createMetaCard("�", "CATEGORY", "IT - Software");
        lblApplicants = createMetaCard("👥", "APPLICANTS", "12 applicants");
        lblCreatedDate = createMetaCard("📅", "CREATED DATE", "Oct 1, 2023");
        lblDueDate = createMetaCard("📆", "DUE DATE", "Oct 31, 2023");

        grid.add(lblLocation);
        grid.add(lblSalary);
        grid.add(lblCategory);
        grid.add(lblApplicants);
        grid.add(lblCreatedDate);
        grid.add(lblDueDate);

        return grid;
    }

    private JLabel createMetaCard(String icon, String label, String value) {
        JPanel card = new JPanel(new BorderLayout(SPACE_3, 0));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_4, SPACE_5, SPACE_4, SPACE_5)
        ));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(fontRegular(20));
        card.add(lblIcon, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(BG_SURFACE);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(caption());
        lblLabel.setForeground(TEXT_MUTED);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(fontBold(FONT_SIZE_BASE));
        lblValue.setForeground(TEXT_PRIMARY);

        textPanel.add(lblLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        textPanel.add(lblValue);
        card.add(textPanel, BorderLayout.CENTER);

        return lblValue;
    }

    private JPanel createDescriptionCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JLabel lblDesc = new JLabel("Description");
        lblDesc.setFont(heading3());
        lblDesc.setForeground(TEXT_PRIMARY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblDesc);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        JSeparator sep1 = new JSeparator();
        sep1.setForeground(BORDER_LIGHT);
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep1.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sep1);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        txtDescription = new JTextArea("");
        txtDescription.setFont(body());
        txtDescription.setForeground(TEXT_SECONDARY);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setEditable(false);
        txtDescription.setFocusable(false);
        txtDescription.setBackground(BG_SURFACE);
        txtDescription.setBorder(null);
        txtDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtDescription);

        return card;
    }

    private JPanel createBottomBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_SURFACE);
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(SPACE_4, SPACE_6, SPACE_4, SPACE_6)
        ));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        JPanel leftActions = new JPanel();
        leftActions.setLayout(new BoxLayout(leftActions, BoxLayout.Y_AXIS));
        leftActions.setBackground(BG_SURFACE);

        JButton btnBack = createOutlineButton("Back");
        btnBack.setPreferredSize(new Dimension(100, 40));
        leftActions.add(btnBack);
        bar.add(leftActions, BorderLayout.WEST);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_3, 0));
        rightActions.setBackground(BG_SURFACE);

        btnApply = createPrimaryButton("Apply / Send CV");
        btnApply.setPreferredSize(new Dimension(180, 40));
        btnApply.addActionListener(e -> {
            if (recruitmentId == null) return;
            String candidateId = org.jobportal.utils.SessionManager.getCurrentUser().getUserId();
            if (btnApply.getText().equals("Apply / Send CV")) {
                boolean ok = applicationService.applyRecruitment(candidateId, recruitmentId);
                if (ok) updateApplyButton(true);
            } else {
                // cancel not directly supported by service without applicationId in this view
            }
        });
        rightActions.add(btnApply);

        bar.add(rightActions, BorderLayout.EAST);

        return bar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Job Detail");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 900);
            frame.setLayout(new BorderLayout());

            frame.add(new org.jobportal.view.common.HeaderPanel(), BorderLayout.NORTH);
            frame.add(new org.jobportal.view.common.SidebarPanel(org.jobportal.view.common.SidebarPanel.Role.CANDIDATE), BorderLayout.WEST);
            frame.add(new JobDetailPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}