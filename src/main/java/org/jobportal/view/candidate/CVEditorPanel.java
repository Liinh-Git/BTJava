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
    private JTextField txtUsername;
    private JTextField txtTitle;
    private JTextField txtAddress;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtBirthday;
    private JTextField txtGender;
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
            txtUsername.setText(user.getUsername() != null ? user.getUsername() : "");
            txtFullName.setText(user.getFullName() != null ? user.getFullName() : "");
            txtEmail.setText(user.getEmail() != null ? user.getEmail() : "");
            txtPhone.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
            txtAddress.setText(user.getAddress() != null ? user.getAddress() : "");
            txtBirthday.setText(user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : "");
            txtGender.setText(user.getGender() != null ? user.getGender().name() : "");

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
            
            if (currentCV.getEducations() == null) {
                currentCV.setEducations(new ArrayList<>());
            }
            boolean success = cvService.saveCV(currentCV);
            if (success) {
                currentCV = cvService.getCV(SessionManager.getInstance().getCandidateId());
                loadEducations();
                JOptionPane.showMessageDialog(this, "LÆ°u CV thÃ nh cÃ´ng!");
            } else {
                JOptionPane.showMessageDialog(this, "Lá»—i khi lÆ°u CV!", "Lá»—i", JOptionPane.ERROR_MESSAGE);
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
        JLabel lblTitle = new JLabel("THÃ”NG TIN CÃ NHÃ‚N");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblBadge = new JLabel(" Há»’ SÆ  ÄANG HOáº T Äá»˜NG ");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblBadge.setForeground(new Color(40, 167, 69)); // mau xanh la
        lblBadge.setBackground(new Color(233, 245, 236));
        lblBadge.setOpaque(true);
        titleBox.add(lblTitle);
        titleBox.add(lblBadge);
        headerPanel.add(titleBox, BorderLayout.WEST);

        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionButtons.setBackground(Color.WHITE);

        JButton btnReloadProfile = createButton("Táº¢I Láº I", Color.WHITE, new Color(13, 110, 253));
        btnReloadProfile.setBorder(new LineBorder(new Color(13, 110, 253), 1));
        btnReloadProfile.addActionListener(e -> loadData());

        JButton btnAccountSettings = createButton("TÃ€I KHOáº¢N", Color.WHITE, new Color(13, 110, 253));
        btnAccountSettings.setBorder(new LineBorder(new Color(13, 110, 253), 1));
        btnAccountSettings.addActionListener(e -> openAccountSettingsTab());

        JButton btnSave = createButton("SAVE CHANGES", new Color(13, 110, 253), Color.WHITE);
        btnSave.addActionListener(e -> saveCVData());

        actionButtons.add(btnReloadProfile);
        actionButtons.add(btnAccountSettings);
        actionButtons.add(btnSave);
        headerPanel.add(actionButtons, BorderLayout.EAST);

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // form nhap lieu
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 20, 15));
        formPanel.setBackground(Color.WHITE);

        JPanel p0 = createFormGroup("Tài khoản", "");
        txtUsername = (JTextField) p0.getComponent(2);
        txtUsername.setEditable(false);
        txtUsername.setBackground(new Color(245, 247, 250));
        formPanel.add(p0);

        JPanel p1 = createFormGroup("Há» vÃ  tÃªn", "Nguyá»…n VÄƒn A");
        txtFullName = (JTextField) p1.getComponent(2);
        txtFullName.setEditable(false);
        txtFullName.setBackground(new Color(245, 247, 250));
        formPanel.add(p1);
        
        JPanel p2 = createFormGroup("Sá»‘ Ä‘iá»‡n thoáº¡i", "0900000000");
        txtPhone = (JTextField) p2.getComponent(2);
        txtPhone.setEditable(false);
        txtPhone.setBackground(new Color(245, 247, 250));
        formPanel.add(p2);

        JPanel p3 = createFormGroup("Email", "email@example.com");
        txtEmail = (JTextField) p3.getComponent(2);
        txtEmail.setEditable(false);
        txtEmail.setBackground(new Color(245, 247, 250));
        formPanel.add(p3);

        JPanel p4 = createFormGroup("Äá»‹a chá»‰ hiá»‡n táº¡i", "123 ÄÆ°á»ng ABC, Quáº­n 1, TP.HCM");
        txtAddress = (JTextField) p4.getComponent(2);
        txtAddress.setEditable(false);
        txtAddress.setBackground(new Color(245, 247, 250));
        formPanel.add(p4);

        JPanel p5 = createFormGroup("NgÃ y sinh", "01/01/1990");
        txtBirthday = (JTextField) p5.getComponent(2);
        txtBirthday.setEditable(false);
        txtBirthday.setBackground(new Color(245, 247, 250));
        formPanel.add(p5);

        JPanel p6 = createFormGroup("Giá»›i tÃ­nh", "Nam");
        txtGender = (JTextField) p6.getComponent(2);
        txtGender.setEditable(false);
        txtGender.setBackground(new Color(245, 247, 250));
        formPanel.add(p6);

        JPanel p7 = createFormGroup("Vá»‹ trÃ­ mong muá»‘n", "Láº­p trÃ¬nh viÃªn Java");
        txtTitle = (JTextField) p7.getComponent(2);
        formPanel.add(p7);      
        

        card.add(formPanel);
        return card;
    }

    // 2. phan objective
    private JPanel createObjectiveSection() {
        JPanel card = createCardPanel();

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("Má»¤C TIÃŠU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblReset = new JLabel("Äáº¶T Láº I");
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
        JLabel lblTitle = new JLabel("Ká»¸ NÄ‚NG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // thanh them skill
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        txtSkill = new JTextField("ThÃªm ká»¹ nÄƒng (vÃ­ dá»¥: React, Quáº£n lÃ½ dá»± Ã¡n)");
        txtSkill.setForeground(Color.GRAY);
        txtSkill.setPreferredSize(new Dimension(0, 40));
        txtSkill.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSkill.getText().equals("ThÃªm ká»¹ nÄƒng (vÃ­ dá»¥: React, Quáº£n lÃ½ dá»± Ã¡n)")) {
                    txtSkill.setText("");
                    txtSkill.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSkill.getText().isEmpty()) {
                    txtSkill.setText("ThÃªm ká»¹ nÄƒng (vÃ­ dá»¥: React, Quáº£n lÃ½ dá»± Ã¡n)");
                    txtSkill.setForeground(Color.GRAY);
                }
            }
        });
        inputPanel.add(txtSkill, BorderLayout.CENTER);

        JButton btnAddSkill = createButton("THÃŠM", new Color(13, 110, 253), Color.WHITE);
        btnAddSkill.setPreferredSize(new Dimension(120, 40));
        btnAddSkill.addActionListener(e -> {
            String s = txtSkill.getText().trim();
            if (!s.isEmpty() && !s.equals("ThÃªm ká»¹ nÄƒng (vÃ­ dá»¥: React, Quáº£n lÃ½ dá»± Ã¡n)")) {
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
        JLabel lblTitle = new JLabel("Há»ŒC Váº¤N");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnAddEdu = createButton("THÃŠM", new Color(13, 110, 253), Color.WHITE);
        btnAddEdu.addActionListener(e -> openEducationDialog(null));
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
        educationTable.add(createTableRow("TRÆ¯á»œNG", "CHUYÃŠN NGÃ€NH", "NÄ‚M", "THAO TÃC", true));
        educationTable.removeAll();
        educationTable.add(createTableRow("TRÆ¯á»œNG", "CHUYÃŠN NGÃ€NH", "NÄ‚M", "THAO TÃC", true));
        if (currentCV != null && currentCV.getEducations() != null) {
            for (Education edu : currentCV.getEducations()) {
                educationTable.add(createTableRow(edu, false));
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

        JButton btnSave = createButton("SAVE PROFILE", new Color(13, 110, 253), Color.WHITE);
        btnSave.setPreferredSize(new Dimension(160, 45));
        btnSave.addActionListener(e -> saveCVData());

        actionPanel.add(btnSave);

        wrapper.add(actionPanel);
        wrapper.add(Box.createRigidArea(new Dimension(0, 30)));

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

    private void openAccountSettingsTab() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof org.jobportal.view.common.MainFrame mainFrame) {
            mainFrame.navigateToMenu("Thong tin nguoi dung");
        }
    }

    private void openEducationDialog(Education existing) {
        if (currentCV == null) return;
        if (currentCV.getEducations() == null) {
            currentCV.setEducations(new ArrayList<>());
        }

        JTextField txtSchool = new JTextField(existing != null ? safeText(existing.getSchool()) : "");
        JTextField txtDegree = new JTextField(existing != null ? safeText(existing.getDegree()) : "");
        JTextField txtMajor = new JTextField(existing != null ? safeText(existing.getMajor()) : "");
        JTextField txtStartYear = new JTextField(existing != null && existing.getStartYear() != null ? String.valueOf(existing.getStartYear()) : "");
        JTextField txtEndYear = new JTextField(existing != null && existing.getEndYear() != null ? String.valueOf(existing.getEndYear()) : "");
        JTextArea txtDescription = new JTextArea(existing != null ? safeText(existing.getDescription()) : "");
        txtDescription.setRows(3);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(12, 12, 12, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 6, 0);

        addEducationField(form, gbc, "TrÆ°á»ng", txtSchool);
        addEducationField(form, gbc, "Báº±ng cáº¥p", txtDegree);
        addEducationField(form, gbc, "ChuyÃªn ngÃ nh", txtMajor);
        addEducationField(form, gbc, "NÄƒm báº¯t Ä‘áº§u", txtStartYear);
        addEducationField(form, gbc, "NÄƒm káº¿t thÃºc", txtEndYear);
        addEducationField(form, gbc, "MÃ´ táº£", new JScrollPane(txtDescription));

        Object oldOkText = UIManager.get("OptionPane.okButtonText");
        Object oldCancelText = UIManager.get("OptionPane.cancelButtonText");
        UIManager.put("OptionPane.okButtonText", "LÆ°u");
        UIManager.put("OptionPane.cancelButtonText", "Há»§y");
        int option = JOptionPane.showConfirmDialog(
                this,
                form,
                existing == null ? "ThÃªm há»c váº¥n" : "Sá»­a há»c váº¥n",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        UIManager.put("OptionPane.okButtonText", oldOkText);
        UIManager.put("OptionPane.cancelButtonText", oldCancelText);
        if (option != JOptionPane.OK_OPTION) return;

        String school = txtSchool.getText().trim();
        if (school.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lÃ²ng nháº­p tÃªn trÆ°á»ng.", "Cáº£nh bÃ¡o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer startYear = parseYear(txtStartYear.getText().trim());
        Integer endYear = parseYear(txtEndYear.getText().trim());
        if ((!txtStartYear.getText().trim().isEmpty() && startYear == null)
                || (!txtEndYear.getText().trim().isEmpty() && endYear == null)) {
            JOptionPane.showMessageDialog(this, "NÄƒm pháº£i lÃ  sá»‘ há»£p lá»‡.", "Cáº£nh bÃ¡o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Education education = existing != null ? existing : new Education();
        education.setSchool(school);
        education.setDegree(emptyToNull(txtDegree.getText()));
        education.setMajor(emptyToNull(txtMajor.getText()));
        education.setStartYear(startYear);
        education.setEndYear(endYear);
        education.setDescription(emptyToNull(txtDescription.getText()));

        if (existing == null) {
            currentCV.getEducations().add(education);
        }
        loadEducations();
    }

    private void addEducationField(JPanel form, GridBagConstraints gbc, String label, Component field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        form.add(lbl, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 0);
        field.setPreferredSize(new Dimension(300, field instanceof JScrollPane ? 70 : 34));
        form.add(field, gbc);
    }

    private Integer parseYear(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String emptyToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String safeText(String value) {
        return value != null ? value : "";
    }

    private JPanel createTableRow(Education education, boolean isHeader) {
        String year = "";
        if (education.getStartYear() != null && education.getEndYear() != null) {
            year = education.getStartYear() + " - " + education.getEndYear();
        } else if (education.getEndYear() != null) {
            year = String.valueOf(education.getEndYear());
        } else if (education.getStartYear() != null) {
            year = String.valueOf(education.getStartYear());
        }

        JPanel row = createTableRow(safeText(education.getSchool()), safeText(education.getMajor()), year, "", isHeader);
        Component actionComponent = row.getComponent(3);
        if (actionComponent instanceof JPanel actionPanel) {
            actionPanel.removeAll();

            JButton btnEdit = new JButton("Sá»­a");
            btnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEdit.addActionListener(e -> openEducationDialog(education));

            JButton btnDelete = new JButton("XÃ³a");
            btnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnDelete.setForeground(Color.RED);
            btnDelete.addActionListener(e -> {
                currentCV.getEducations().remove(education);
                loadEducations();
            });

            actionPanel.add(btnEdit);
            actionPanel.add(btnDelete);
        }
        return row;
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
            JLabel btnEdit = new JLabel("âœŽ"); // icon but chi
            btnEdit.setForeground(Color.GRAY);
            btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            JLabel btnDel = new JLabel("ðŸ—‘"); // icon thung rac
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



