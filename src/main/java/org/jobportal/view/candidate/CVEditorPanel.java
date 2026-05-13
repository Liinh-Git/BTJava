package org.jobportal.view.candidate;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.CVService;
import org.jobportal.bll.interfaces.ICVService;
import org.jobportal.dto.CVDTO;
import org.jobportal.dto.UserDTO;
import org.jobportal.model.Education;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CVEditorPanel extends JPanel {

    private final ICVService cvService = new CVService();
    private CVDTO currentCV;

    private JTextField txtFullName;
    private JTextField txtTitle;
    private JTextField txtLocation;
    private JTextField txtEmail;
    private JTextField txtPhone;
    
    private JTextArea txtObjective;
    private JTextField txtSkill;
    private JPanel tagsPanel;
    private List<String> skillList = new ArrayList<>();
    
    private JPanel educationTable;

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
        
        loadData();
    }
    
    private void loadData() {
        UserDTO user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            txtFullName.setText(user.getFullName() != null && !user.getFullName().isEmpty() ? user.getFullName() : user.getUsername());
            txtEmail.setText(user.getEmail() != null ? user.getEmail() : "");
            txtPhone.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
            txtLocation.setText("");

            String candidateId = SessionManager.getInstance().getCandidateId();
            currentCV = cvService.getCV(candidateId);
            if (currentCV == null) {
                currentCV = new CVDTO();
                currentCV.setCandidateId(candidateId);
                currentCV.setEducations(new ArrayList<>());
            }

            if (currentCV.getDesiredPosition() != null) txtTitle.setText(currentCV.getDesiredPosition());
            if (currentCV.getObjective() != null && !currentCV.getObjective().isEmpty()) {
                txtObjective.setText(currentCV.getObjective());
                txtObjective.setForeground(Color.BLACK);
            } else {
                txtObjective.setText("Innovative and results-driven...");
                txtObjective.setForeground(Color.GRAY);
            }
            
            skillList.clear();
            tagsPanel.removeAll();
            if (currentCV.getSkills() != null && !currentCV.getSkills().isEmpty()) {
                String[] savedSkills = currentCV.getSkills().split(",");
                for (String s : savedSkills) {
                    addSkillTag(s.trim());
                }
            }
            tagsPanel.revalidate();
            tagsPanel.repaint();

            loadEducations();
        }
    }
    
    private void saveCVData() {
        if (currentCV != null) {
            currentCV.setDesiredPosition(txtTitle.getText().trim());
            String objText = txtObjective.getText().trim();
            if (objText.equals("Innovative and results-driven...")) objText = "";
            currentCV.setObjective(objText);
            currentCV.setSkills(String.join(", ", skillList));
            
            boolean success = cvService.saveCV(currentCV);
            if (success) {
                JOptionPane.showMessageDialog(this, "Lưu CV thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu CV!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
        btnSave.addActionListener(e -> saveCVData());
        headerPanel.add(btnSave, BorderLayout.EAST);

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // form nhap lieu
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 20, 15));
        formPanel.setBackground(Color.WHITE);

        JPanel p1 = createFormGroup("Full Name", "Alex Thorne");
        txtFullName = (JTextField) p1.getComponent(2);
        formPanel.add(p1);
        
        JPanel p2 = createFormGroup("Professional Title", "Senior Frontend Architect");
        txtTitle = (JTextField) p2.getComponent(2);
        formPanel.add(p2);
        
        JPanel p3 = createFormGroup("Location", "San Francisco, CA");
        txtLocation = (JTextField) p3.getComponent(2);
        formPanel.add(p3);
        
        JPanel p4 = createFormGroup("Email Address", "a.thorne@example.com");
        txtEmail = (JTextField) p4.getComponent(2);
        formPanel.add(p4);
        
        JPanel p5 = createFormGroup("Phone Number", "+1 (555) 012-3456");
        txtPhone = (JTextField) p5.getComponent(2);
        formPanel.add(p5);

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

        txtObjective = new JTextArea("Innovative and results-driven...");
        txtObjective.setForeground(Color.GRAY);
        txtObjective.setLineWrap(true);
        txtObjective.setWrapStyleWord(true);
        txtObjective.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtObjective.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        txtObjective.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtObjective.getText().equals("Innovative and results-driven...")) {
                    txtObjective.setText("");
                    txtObjective.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtObjective.getText().isEmpty()) {
                    txtObjective.setText("Innovative and results-driven...");
                    txtObjective.setForeground(Color.GRAY);
                }
            }
        });
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
        txtSkill = new JTextField("Add a skill (e.g. React, Project Management)");
        txtSkill.setForeground(Color.GRAY);
        txtSkill.setPreferredSize(new Dimension(0, 40));
        txtSkill.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSkill.getText().equals("Add a skill (e.g. React, Project Management)")) {
                    txtSkill.setText("");
                    txtSkill.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSkill.getText().isEmpty()) {
                    txtSkill.setText("Add a skill (e.g. React, Project Management)");
                    txtSkill.setForeground(Color.GRAY);
                }
            }
        });
        inputPanel.add(txtSkill, BorderLayout.CENTER);

        JButton btnAddSkill = createButton("ADD SKILL", new Color(13, 110, 253), Color.WHITE);
        btnAddSkill.setPreferredSize(new Dimension(120, 40));
        btnAddSkill.addActionListener(e -> {
            String s = txtSkill.getText().trim();
            if (!s.isEmpty() && !s.equals("Add a skill (e.g. React, Project Management)")) {
                addSkillTag(s);
                tagsPanel.revalidate();
                tagsPanel.repaint();
                txtSkill.setText("");
            }
        });
        inputPanel.add(btnAddSkill, BorderLayout.EAST);

        card.add(inputPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // danh sach the skill
        tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tagsPanel.setBackground(Color.WHITE);
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
        educationTable = new JPanel();
        educationTable.setLayout(new BoxLayout(educationTable, BoxLayout.Y_AXIS));
        educationTable.setBackground(Color.WHITE);
        card.add(educationTable);

        return card;
    }
    
    private void loadEducations() {
        educationTable.removeAll();
        educationTable.add(createTableRow("SCHOOL", "MAJOR", "YEAR", "ACTIONS", true));
        if (currentCV != null && currentCV.getEducations() != null) {
            for (Education edu : currentCV.getEducations()) {
                educationTable.add(createTableRow(edu.getSchool(), edu.getMajor(), edu.getEndYear() != null ? String.valueOf(edu.getEndYear()) : "", "edit_delete", false));
            }
        }
        educationTable.revalidate();
        educationTable.repaint();
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
        btnSave.addActionListener(e -> saveCVData());

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
        lblClose.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                skillList.remove(skill);
                tagsPanel.remove(tag);
                tagsPanel.revalidate();
                tagsPanel.repaint();
            }
        });

        tag.add(lblSkill);
        tag.add(lblClose);
        return tag;
    }

    // them the tag vao panel
    private void addSkillTag(String skill) {
        if (!skillList.contains(skill)) {
            skillList.add(skill);
            tagsPanel.add(createSkillTag(skill));
        }
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

            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.CANDIDATE);
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