package org.jobportal.view.candidate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

/**
 * CV editor panel matching qu_n_l_cv_minimal_1 stitch design.
 * Layout: PERSONAL INFORMATION card (2-col fields) + OBJECTIVE card
 *         + SKILLS card (input + tags) + EDUCATION HISTORY card (table)
 *         + bottom action buttons (DISCARD CHANGES / SAVE PROFILE).
 */
public class CVEditorPanel extends JPanel {

    public CVEditorPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        // 1. the thong tin ca nhan
        mainContent.add(createPersonalInfoCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 2. the muc tieu nghe nghiep
        mainContent.add(createObjectiveCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 3. the ky nang
        mainContent.add(createSkillsCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 4. the hoc van
        mainContent.add(createEducationCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        // 5. nut hanh dong
        mainContent.add(createActionButtons());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        // 6. footer
        JLabel copyrightFooter = new JLabel("© 2024 JobPortal Professional Services. All rights reserved.");
        copyrightFooter.setFont(bodySmall());
        copyrightFooter.setForeground(TEXT_MUTED);
        copyrightFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        copyrightFooter.setHorizontalAlignment(SwingConstants.CENTER);
        mainContent.add(copyrightFooter);

        add(createScrollPane(mainContent), BorderLayout.CENTER);
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

        // header: icon + title + badge + Save Changes button
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

        JLabel badgeActive = createBadge("ACTIVE PROFILE", BADGE_SUCCESS_BG, BADGE_SUCCESS_FG);
        titlePanel.add(badgeActive);
        header.add(titlePanel, BorderLayout.WEST);

        JButton btnSave = createPrimaryButton("SAVE CHANGES");
        btnSave.setPreferredSize(new Dimension(150, 36));
        btnSave.setMaximumSize(new Dimension(150, 36));
        header.add(btnSave, BorderLayout.EAST);

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // form 2 cot
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(BG_SURFACE);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        // Full Name + Professional Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, SPACE_1, SPACE_4);
        formGrid.add(createSmallLabel("Full Name"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_1, 0);
        formGrid.add(createSmallLabel("Professional Title"), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(0, 0, SPACE_4, SPACE_4);
        formGrid.add(createInputField("Alex Thorne"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_4, 0);
        formGrid.add(createInputField("Senior Frontend Architect"), gbc);

        // Location + Email Address
        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(0, 0, SPACE_1, SPACE_4);
        formGrid.add(createSmallLabel("Location"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_1, 0);
        formGrid.add(createSmallLabel("Email Address"), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(0, 0, SPACE_4, SPACE_4);
        formGrid.add(createInputField("San Francisco, CA"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, SPACE_4, 0);
        formGrid.add(createInputField("a.thorne@example.com"), gbc);

        // Phone Number (1 cot)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, SPACE_1, SPACE_4);
        formGrid.add(createSmallLabel("Phone Number"), gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, SPACE_4);
        formGrid.add(createInputField("+1 (555) 012-3456"), gbc);

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

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SURFACE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        titlePanel.setBackground(BG_SURFACE);
        titlePanel.add(new JLabel("◎") {{ setFont(fontRegular(16)); setForeground(PRIMARY); }});
        titlePanel.add(new JLabel("OBJECTIVE") {{ setFont(fontBold(FONT_SIZE_BASE)); setForeground(TEXT_PRIMARY); }});
        header.add(titlePanel, BorderLayout.WEST);

        JLabel lblReset = new JLabel("RESET TO DEFAULT");
        lblReset.setFont(fontBold(FONT_SIZE_SM));
        lblReset.setForeground(PRIMARY);
        lblReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        header.add(lblReset, BorderLayout.EAST);

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        // text area
        JTextArea txtObjective = new JTextArea(
                "Innovative and results-driven Frontend Developer with over 8 years of experience in building and maintaining " +
                "responsive web applications. Expert in React, Tailwind CSS, and system architecture. Committed to delivering high-" +
                "quality, scalable code and exceptional user experiences."
        );
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

        // header
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        titlePanel.setBackground(BG_SURFACE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(new JLabel("◉") {{ setFont(fontRegular(16)); setForeground(PRIMARY); }});
        titlePanel.add(new JLabel("SKILLS") {{ setFont(fontBold(FONT_SIZE_BASE)); setForeground(TEXT_PRIMARY); }});
        card.add(titlePanel);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        // input + add skill button
        JPanel inputRow = new JPanel(new BorderLayout(SPACE_3, 0));
        inputRow.setBackground(BG_SURFACE);
        inputRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, INPUT_HEIGHT));

        JTextField txtSkill = new JTextField("Add a skill (e.g. React, Project Management)");
        txtSkill.setFont(body());
        txtSkill.setForeground(TEXT_MUTED);
        txtSkill.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_2, SPACE_3, SPACE_2, SPACE_3)
        ));
        inputRow.add(txtSkill, BorderLayout.CENTER);

        JButton btnAddSkill = createPrimaryButton("ADD SKILL");
        btnAddSkill.setPreferredSize(new Dimension(120, INPUT_HEIGHT));
        btnAddSkill.setMaximumSize(new Dimension(120, INPUT_HEIGHT));
        inputRow.add(btnAddSkill, BorderLayout.EAST);

        card.add(inputRow);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        // skill tags
        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, SPACE_2));
        tagsPanel.setBackground(BG_SURFACE);
        tagsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String skill : new String[]{"React.js", "TypeScript", "Tailwind CSS", "UI/UX Design", "Node.js", "System Architecture"}) {
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
            tagsPanel.add(chip);
        }
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

        // header
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

        // bang hoc van
        JPanel table = new JPanel();
        table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));
        table.setBackground(BG_SURFACE);
        table.setAlignmentX(Component.LEFT_ALIGNMENT);

        // header row
        table.add(createEduRow("SCHOOL", "MAJOR", "YEAR", "ACTIONS", true));
        table.add(createEduRow("Stanford University", "M.S. in Computer Science", "2016", "", false));
        table.add(createEduRow("UC Berkeley", "B.S. in Software Engineering", "2014", "", false));

        card.add(table);

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

        JButton btnDiscard = createOutlineButton("DISCARD CHANGES");
        btnDiscard.setPreferredSize(new Dimension(180, BUTTON_HEIGHT));
        panel.add(btnDiscard);

        JButton btnSaveProfile = new JButton("SAVE PROFILE");
        btnSaveProfile.setFont(fontBold(FONT_SIZE_BASE));
        btnSaveProfile.setBackground(TEXT_PRIMARY);
        btnSaveProfile.setForeground(BG_SURFACE);
        btnSaveProfile.setPreferredSize(new Dimension(160, BUTTON_HEIGHT));
        btnSaveProfile.setFocusPainted(false);
        btnSaveProfile.setBorderPainted(false);
        btnSaveProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnSaveProfile);

        return panel;
    }

    // helper methods
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

    // ham test giao dien doc lap
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