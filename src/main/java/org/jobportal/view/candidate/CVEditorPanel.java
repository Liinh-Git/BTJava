package org.jobportal.view.candidate;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class CVEditorPanel extends JPanel {

    public CVEditorPanel() {
        // thiet lap mau nen cho toan trang
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        // panel chinh chua noi dung, sap xep theo chieu doc
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(20, 30, 20, 30));

        // them cac tung phan cua CV
        mainContent.add(createPersonalSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        mainContent.add(createObjectiveSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        mainContent.add(createSkillsSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        mainContent.add(createEducationSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        mainContent.add(createActionAndFooterSection());

        // boc trong thanh cuon
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    // 1. phan personal information
    private JPanel createPersonalSection() {
        JPanel card = createCardPanel();

        // header cua the
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JPanel titleBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBox.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("PERSONAL INFORMATION");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblBadge = new JLabel(" ACTIVE PROFILE ");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblBadge.setForeground(new Color(40, 167, 69)); // mau xanh la
        lblBadge.setBackground(new Color(233, 245, 236));
        lblBadge.setOpaque(true);
        titleBox.add(lblTitle);
        titleBox.add(lblBadge);
        headerPanel.add(titleBox, BorderLayout.WEST);

        JButton btnSave = createButton("SAVE CHANGES", new Color(13, 110, 253), Color.WHITE);
        headerPanel.add(btnSave, BorderLayout.EAST);

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // form nhap lieu
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 20, 15));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(createFormGroup("Full Name", "Alex Thorne"));
        formPanel.add(createFormGroup("Professional Title", "Senior Frontend Architect"));
        formPanel.add(createFormGroup("Location", "San Francisco, CA"));
        formPanel.add(createFormGroup("Email Address", "a.thorne@example.com"));
        formPanel.add(createFormGroup("Phone Number", "+1 (555) 012-3456"));

        card.add(formPanel);
        return card;
    }

    // 2. phan objective
    private JPanel createObjectiveSection() {
        JPanel card = createCardPanel();

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("OBJECTIVE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblReset = new JLabel("RESET TO DEFAULT");
        lblReset.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblReset.setForeground(new Color(13, 110, 253));
        lblReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(lblReset, BorderLayout.EAST);

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextArea txtObjective = new JTextArea("Innovative and results-driven Frontend Developer with over 8 years of experience in building and maintaining responsive web applications. Expert in React, Tailwind CSS, and system architecture. Committed to delivering high-quality, scalable code and exceptional user experiences.");
        txtObjective.setLineWrap(true);
        txtObjective.setWrapStyleWord(true);
        txtObjective.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtObjective.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.add(txtObjective);

        return card;
    }

    // 3. phan skills
    private JPanel createSkillsSection() {
        JPanel card = createCardPanel();

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("SKILLS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // thanh them skill
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        JTextField txtSkill = new JTextField("Add a skill (e.g. React, Project Management)");
        txtSkill.setForeground(Color.GRAY);
        txtSkill.setPreferredSize(new Dimension(0, 40));
        inputPanel.add(txtSkill, BorderLayout.CENTER);

        JButton btnAddSkill = createButton("ADD SKILL", new Color(13, 110, 253), Color.WHITE);
        btnAddSkill.setPreferredSize(new Dimension(120, 40));
        inputPanel.add(btnAddSkill, BorderLayout.EAST);

        card.add(inputPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // danh sach the skill
        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tagsPanel.setBackground(Color.WHITE);
        String[] skills = {"React.js", "TypeScript", "Tailwind CSS", "UI/UX Design", "Node.js", "System Architecture"};
        for (String skill : skills) {
            tagsPanel.add(createSkillTag(skill));
        }
        card.add(tagsPanel);

        return card;
    }

    // 4. phan education history
    private JPanel createEducationSection() {
        JPanel card = createCardPanel();

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("EDUCATION HISTORY");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnAddEdu = createButton("ADD EDUCATION", new Color(33, 37, 41), Color.WHITE);
        headerPanel.add(btnAddEdu, BorderLayout.EAST);

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // tao bang gia bang cach dung grid layout
        JPanel tablePanel = new JPanel(new GridLayout(3, 1));
        tablePanel.setBackground(Color.WHITE);

        // header cua bang
        tablePanel.add(createTableRow("SCHOOL", "MAJOR", "YEAR", "ACTIONS", true));
        // cac dong du lieu
        tablePanel.add(createTableRow("Stanford University", "M.S. in Computer Science", "2016", "edit_delete", false));
        tablePanel.add(createTableRow("UC Berkeley", "B.S. in Software Engineering", "2014", "edit_delete", false));

        card.add(tablePanel);

        return card;
    }

    // phan nut chuc nang cuoi trang va footer
    private JPanel createActionAndFooterSection() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(new Color(248, 249, 250));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(new Color(248, 249, 250));

        JButton btnDiscard = createButton("DISCARD CHANGES", Color.WHITE, Color.DARK_GRAY);
        btnDiscard.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        btnDiscard.setPreferredSize(new Dimension(160, 45));

        JButton btnSave = createButton("SAVE PROFILE", new Color(33, 37, 41), Color.WHITE);
        btnSave.setPreferredSize(new Dimension(160, 45));

        actionPanel.add(btnDiscard);
        actionPanel.add(btnSave);

        wrapper.add(actionPanel);
        wrapper.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(248, 249, 250));
        JLabel lblFooter = new JLabel("© 2024 JobPortal Professional Services. All rights reserved.");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(Color.GRAY);
        footerPanel.add(lblFooter);

        wrapper.add(footerPanel);

        return wrapper;
    }

    // --- cac ham tien ich (helpers) ---

    // tao the (card) co nen trang va vien
    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));
        return card;
    }

    // tao o nhap lieu co label ben tren
    private JPanel createFormGroup(String labelText, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(Color.DARK_GRAY);

        JTextField txt = new JTextField(value);
        txt.setPreferredSize(new Dimension(0, 35));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(txt);

        return panel;
    }

    // tao nut bam voi mau sac tuy chinh
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // tao the tag cho skill
    private JPanel createSkillTag(String skill) {
        JPanel tag = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        tag.setBackground(Color.WHITE);
        tag.setBorder(new LineBorder(new Color(220, 220, 220), 1));

        JLabel lblSkill = new JLabel(skill);
        lblSkill.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblClose = new JLabel(" x");
        lblClose.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblClose.setForeground(Color.GRAY);
        lblClose.setCursor(new Cursor(Cursor.HAND_CURSOR));

        tag.add(lblSkill);
        tag.add(lblClose);
        return tag;
    }

    // tao dong cho bang education
    private JPanel createTableRow(String col1, String col2, String col3, String action, boolean isHeader) {
        JPanel row = new JPanel(new GridLayout(1, 4, 10, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(15, 0, 15, 0));

        Font font = isHeader ? new Font("Segoe UI", Font.BOLD, 12) : new Font("Segoe UI", Font.PLAIN, 13);
        Color color = isHeader ? Color.DARK_GRAY : Color.BLACK;

        JLabel l1 = new JLabel(col1); l1.setFont(font); l1.setForeground(color);
        JLabel l2 = new JLabel(col2); l2.setFont(font); l2.setForeground(color);
        JLabel l3 = new JLabel(col3); l3.setFont(font); l3.setForeground(color);

        row.add(l1);
        row.add(l2);
        row.add(l3);

        if (isHeader) {
            JLabel l4 = new JLabel(action);
            l4.setFont(font);
            l4.setForeground(color);
            row.add(l4);
        } else {
            // tao nut edit/delete gia
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            actionPanel.setBackground(Color.WHITE);
            JLabel btnEdit = new JLabel("✎"); // icon but chi
            btnEdit.setForeground(Color.GRAY);
            btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            JLabel btnDel = new JLabel("🗑"); // icon thung rac
            btnDel.setForeground(Color.GRAY);
            btnDel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            actionPanel.add(btnEdit);
            actionPanel.add(btnDel);
            row.add(actionPanel);
        }

        return row;
    }

    // ham main de kiem tra giao dien
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CV Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 800);
            frame.setLayout(new BorderLayout());

            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            SidebarPanel sidebar = new SidebarPanel(SidebarPanel.Role.CANDIDATE);
            frame.add(sidebar, BorderLayout.WEST);

            JPanel rightPanel = new JPanel(new BorderLayout());

            CVEditorPanel cvPanel = new CVEditorPanel();
            rightPanel.add(cvPanel, BorderLayout.CENTER);

            frame.add(rightPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}