package org.jobportal.view.candidate;

import org.jobportal.bll.impl.CVService;
import org.jobportal.dto.CVDTO;
import org.jobportal.model.Education;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.jobportal.view.util.DesignSystem.*;

public class CVEditorPanel extends JPanel {

    private final CVService cvService = new CVService();
    private JTextField txtFullName;
    private JTextField txtDesiredPosition;
    private JTextField txtDesiredSalary;
    private JTextField txtLocation;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JLabel lblLastUpdated;
    private JTextArea txtObjective;
    private JTextField txtSkillInput;
    private JPanel tagsPanel;
    private List<String> skills = new ArrayList<>();
    private List<Education> educations = new ArrayList<>();
    private JPanel eduTable;

    public CVEditorPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        mainContent.add(createPersonalInfoCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createObjectiveCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createSkillsCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        mainContent.add(createEducationCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        mainContent.add(createActionButtons());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        JLabel copyrightFooter = new JLabel("© 2024 JobPortal Professional Services. All rights reserved.");
        copyrightFooter.setFont(bodySmall());
        copyrightFooter.setForeground(TEXT_MUTED);
        copyrightFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        copyrightFooter.setHorizontalAlignment(SwingConstants.CENTER);
        mainContent.add(copyrightFooter);

        add(createScrollPane(mainContent), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        String candidateId = org.jobportal.utils.SessionManager.getCurrentUser().getUserId();
        CVDTO cv = cvService.getCV(candidateId);
        if (cv == null) cv = new CVDTO();

        org.jobportal.dto.UserDTO user = org.jobportal.utils.SessionManager.getCurrentUser();
        txtFullName.setText(user.getFullName());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhoneNumber());
        txtDesiredPosition.setText(cv.getDesiredPosition() != null ? cv.getDesiredPosition() : "");
        txtDesiredSalary.setText(cv.getDesiredSalary() != null ? String.valueOf(cv.getDesiredSalary()) : "");
        txtObjective.setText(cv.getObjective() != null ? cv.getObjective() : "");
        lblLastUpdated.setText(cv.getLastUpdated() != null ? cv.getLastUpdated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A");

        skills.clear();
        if (cv.getSkills() != null && !cv.getSkills().isEmpty()) {
            for (String s : cv.getSkills().split(",")) {
                skills.add(s.trim());
            }
        }
        refreshSkillTags();

        educations = cv.getEducations() != null ? new ArrayList<>(cv.getEducations()) : new ArrayList<>();
        refreshEducationTable();
    }

    private void refreshSkillTags() {
        tagsPanel.removeAll();
        for (String skill : skills) {
            JLabel chip = new JLabel(skill + "  ×");
            chip.setFont(body());
            chip.setOpaque(true);
            chip.setBackground(BG_PAGE);
            chip.setForeground(TEXT_PRIMARY);
            chip.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER, 1),
                    new EmptyBorder(SPACE_1, SPACE_3, SPACE_1, SPACE_3)
            ));
            chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
            chip.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    skills.remove(skill);
                    refreshSkillTags();
                }
            });
            tagsPanel.add(chip);
        }
        tagsPanel.revalidate();
        tagsPanel.repaint();
    }

    private void refreshEducationTable() {
        eduTable.removeAll();
        eduTable.add(createEduRow("SCHOOL", "MAJOR", "YEAR", "ACTIONS", true));
        for (Education edu : educations) {
            eduTable.add(createEduRow(edu.getSchool(), edu.getMajor(), String.valueOf(edu.getEndYear()), "", false));
        }
        eduTable.revalidate();
        eduTable.repaint();
    }

    private JPanel createPersonalInfoCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SURFACE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        titlePanel.setBackground(BG_SURFACE);

        JLabel lblIcon = new JLabel("👤");
        lblIcon.setFont(fontRegular(18));
        titlePanel.add(lblIcon);

        JLabel lblTitle = new JLabel("PERSONAL INFORMATION");
        lblTitle.setFont(fontBold(FONT_SIZE_BASE));
        lblTitle.setForeground(TEXT_PRIMARY);
        titlePanel.add(lblTitle);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(lblLastUpdated = new JLabel("Last updated: N/A") {{ setFont(bodySmall()); setForeground(TEXT_MUTED); }}, BorderLayout.EAST);
        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(BG_SURFACE);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, SPACE_1, SPACE_4);
        formGrid.add(createSmallLabel("Full Name"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_1, 0);
        formGrid.add(createSmallLabel("Desired Position"), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(0, 0, SPACE_4, SPACE_4);
        txtFullName = createInputField("");
        txtFullName.setEditable(false);
        formGrid.add(txtFullName, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_4, 0);
        txtDesiredPosition = createInputField("");
        formGrid.add(txtDesiredPosition, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(0, 0, SPACE_1, SPACE_4);
        formGrid.add(createSmallLabel("Desired Salary"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_1, 0);
        formGrid.add(createSmallLabel("Email Address"), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(0, 0, SPACE_4, SPACE_4);
        txtDesiredSalary = createInputField("");
        formGrid.add(txtDesiredSalary, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_4, 0);
        txtEmail = createInputField("");
        txtEmail.setEditable(false);
        formGrid.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, SPACE_1, SPACE_4);
        formGrid.add(createSmallLabel("Phone Number"), gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, SPACE_4);
        txtPhone = createInputField("");
        txtPhone.setEditable(false);
        formGrid.add(txtPhone, gbc);

        card.add(formGrid);
        return card;
    }

    private JPanel createObjectiveCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SURFACE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        titlePanel.setBackground(BG_SURFACE);
        titlePanel.add(new JLabel("◎") {{ setFont(fontRegular(16)); setForeground(PRIMARY); }});
        titlePanel.add(new JLabel("OBJECTIVE") {{ setFont(fontBold(FONT_SIZE_BASE)); setForeground(TEXT_PRIMARY); }});
        header.add(titlePanel, BorderLayout.WEST);

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        txtObjective = new JTextArea("");
        txtObjective.setFont(body());
        txtObjective.setForeground(TEXT_PRIMARY);
        txtObjective.setLineWrap(true);
        txtObjective.setWrapStyleWord(true);
        txtObjective.setBackground(BG_PAGE);
        txtObjective.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_4, SPACE_4, SPACE_4, SPACE_4)
        ));
        txtObjective.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtObjective);

        return card;
    }

    private JPanel createSkillsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        titlePanel.setBackground(BG_SURFACE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(new JLabel("◉") {{ setFont(fontRegular(16)); setForeground(PRIMARY); }});
        titlePanel.add(new JLabel("SKILLS") {{ setFont(fontBold(FONT_SIZE_BASE)); setForeground(TEXT_PRIMARY); }});
        card.add(titlePanel);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        JPanel inputRow = new JPanel(new BorderLayout(SPACE_3, 0));
        inputRow.setBackground(BG_SURFACE);
        inputRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, INPUT_HEIGHT));

        txtSkillInput = new JTextField("Add a skill (e.g. React, Project Management)");
        txtSkillInput.setFont(body());
        txtSkillInput.setForeground(TEXT_MUTED);
        txtSkillInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_2, SPACE_3, SPACE_2, SPACE_3)
        ));
        inputRow.add(txtSkillInput, BorderLayout.CENTER);

        JButton btnAddSkill = createPrimaryButton("ADD SKILL");
        btnAddSkill.setPreferredSize(new Dimension(120, INPUT_HEIGHT));
        btnAddSkill.setMaximumSize(new Dimension(120, INPUT_HEIGHT));
        btnAddSkill.addActionListener(e -> {
            String s = txtSkillInput.getText().trim();
            if (!s.isEmpty() && !skills.contains(s)) {
                skills.add(s);
                refreshSkillTags();
                txtSkillInput.setText("");
            }
        });
        inputRow.add(btnAddSkill, BorderLayout.EAST);

        card.add(inputRow);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, SPACE_2));
        tagsPanel.setBackground(BG_SURFACE);
        tagsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(tagsPanel);

        return card;
    }

    private JPanel createEducationCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SURFACE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        titlePanel.setBackground(BG_SURFACE);
        titlePanel.add(new JLabel("🎓") {{ setFont(fontRegular(16)); }});
        titlePanel.add(new JLabel("EDUCATION HISTORY") {{ setFont(fontBold(FONT_SIZE_BASE)); setForeground(TEXT_PRIMARY); }});
        header.add(titlePanel, BorderLayout.WEST);

        JButton btnAddEdu = new JButton("ADD EDUCATION");
        btnAddEdu.setFont(fontBold(FONT_SIZE_SM));
        btnAddEdu.setBackground(TEXT_PRIMARY);
        btnAddEdu.setForeground(BG_SURFACE);
        btnAddEdu.setFocusPainted(false);
        btnAddEdu.setBorderPainted(false);
        btnAddEdu.setPreferredSize(new Dimension(150, 32));
        btnAddEdu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        header.add(btnAddEdu, BorderLayout.EAST);

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        eduTable = new JPanel();
        eduTable.setLayout(new BoxLayout(eduTable, BoxLayout.Y_AXIS));
        eduTable.setBackground(BG_SURFACE);
        eduTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(eduTable);

        return card;
    }

    private JPanel createEduRow(String school, String major, String year, String action, boolean isHeader) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? BG_PAGE : BG_SURFACE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setPreferredSize(new Dimension(0, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        Font font = isHeader ? tableHeader() : body();
        Color fg = isHeader ? TEXT_MUTED : TEXT_PRIMARY;

        gbc.gridx = 0; gbc.weightx = 0.3; gbc.insets = new Insets(0, SPACE_4, 0, SPACE_3);
        JLabel l1 = new JLabel(school); l1.setFont(font); l1.setForeground(fg);
        row.add(l1, gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        JLabel l2 = new JLabel(major); l2.setFont(font); l2.setForeground(fg);
        row.add(l2, gbc);

        gbc.gridx = 2; gbc.weightx = 0.15;
        JLabel l3 = new JLabel(year); l3.setFont(font); l3.setForeground(fg);
        l3.setHorizontalAlignment(SwingConstants.CENTER);
        row.add(l3, gbc);

        gbc.gridx = 3; gbc.weightx = 0.15; gbc.insets = new Insets(0, SPACE_3, 0, SPACE_4);
        if (isHeader) {
            JLabel l4 = new JLabel("ACTIONS"); l4.setFont(font); l4.setForeground(fg);
            l4.setHorizontalAlignment(SwingConstants.RIGHT);
            row.add(l4, gbc);
        } else {
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_2, 0));
            actions.setOpaque(false);
            JLabel edit = new JLabel("✎"); edit.setFont(fontRegular(16)); edit.setForeground(TEXT_MUTED);
            edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            JLabel del = new JLabel("🗑"); del.setFont(fontRegular(16)); del.setForeground(TEXT_MUTED);
            del.setCursor(new Cursor(Cursor.HAND_CURSOR));
            actions.add(edit);
            actions.add(del);
            row.add(actions, gbc);
        }

        return row;
    }

    private JPanel createActionButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_4, 0));
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnSaveProfile = new JButton("SAVE PROFILE");
        btnSaveProfile.setFont(fontBold(FONT_SIZE_BASE));
        btnSaveProfile.setBackground(TEXT_PRIMARY);
        btnSaveProfile.setForeground(BG_SURFACE);
        btnSaveProfile.setPreferredSize(new Dimension(160, BUTTON_HEIGHT));
        btnSaveProfile.setFocusPainted(false);
        btnSaveProfile.setBorderPainted(false);
        btnSaveProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSaveProfile.addActionListener(e -> saveCV());
        panel.add(btnSaveProfile);

        return panel;
    }

    private void saveCV() {
        CVDTO cv = new CVDTO();
        cv.setCandidateId(org.jobportal.utils.SessionManager.getCurrentUser().getUserId());
        cv.setObjective(txtObjective.getText());
        cv.setDesiredPosition(txtDesiredPosition.getText());
        try { cv.setDesiredSalary(Double.parseDouble(txtDesiredSalary.getText())); } catch (Exception ignored) {}
        cv.setSkills(String.join(",", skills));
        cv.setEducations(educations);
        cvService.saveCV(cv);
        loadData();
    }

    private JLabel createSmallLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(bodySmall());
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    private JTextField createInputField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(body());
        field.setForeground(TEXT_PRIMARY);
        field.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_2, SPACE_3, SPACE_2, SPACE_3)
        ));
        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Candidate - CV Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 900);
            frame.setLayout(new BorderLayout());

            frame.add(new org.jobportal.view.common.HeaderPanel(), BorderLayout.NORTH);
            frame.add(new org.jobportal.view.common.SidebarPanel(org.jobportal.view.common.SidebarPanel.Role.CANDIDATE), BorderLayout.WEST);
            frame.add(new CVEditorPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}