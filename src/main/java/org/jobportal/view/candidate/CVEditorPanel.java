package org.jobportal.view.candidate;

import org.jobportal.bll.impl.CVService;
import org.jobportal.bll.interfaces.ICVService;
import org.jobportal.dto.CVDTO;
import org.jobportal.dto.UserDTO;
import org.jobportal.model.Education;
import org.jobportal.utils.SessionManager;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CVEditorPanel extends JPanel {

    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final String OBJECTIVE_PLACEHOLDER = "T\u00f3m t\u1eaft m\u1ee5c ti\u00eau ngh\u1ec1 nghi\u1ec7p c\u1ee7a b\u1ea1n...";
    private static final String SKILL_PLACEHOLDER = "Th\u00eam k\u1ef9 n\u0103ng, v\u00ed d\u1ee5: Java, React, Qu\u1ea3n l\u00fd d\u1ef1 \u00e1n";

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
    private JTable educationTable;
    private DefaultTableModel educationModel;
    private final List<String> skillList = new ArrayList<>();

    public CVEditorPanel() {
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(20, 30, 20, 30));

        mainContent.add(createPersonalSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(createObjectiveSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(createSkillsSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(createEducationSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContent.add(createActionSection());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        UserDTO user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;

        txtFullName.setText(valueOrEmpty(user.getFullName()));
        txtEmail.setText(valueOrEmpty(user.getEmail()));
        txtPhone.setText(valueOrEmpty(user.getPhoneNumber()));

        String candidateId = SessionManager.getInstance().getCandidateId();
        currentCV = cvService.getCV(candidateId);
        if (currentCV == null) {
            currentCV = new CVDTO();
            currentCV.setCandidateId(candidateId);
            currentCV.setEducations(new ArrayList<>());
        }
        if (currentCV.getEducations() == null) {
            currentCV.setEducations(new ArrayList<>());
        }

        txtTitle.setText(valueOrEmpty(currentCV.getDesiredPosition()));
        txtLocation.setText(valueOrEmpty(currentCV.getLocation()));
        setObjectiveText(currentCV.getObjective());

        skillList.clear();
        tagsPanel.removeAll();
        if (currentCV.getSkills() != null && !currentCV.getSkills().isBlank()) {
            for (String skill : currentCV.getSkills().split(",")) {
                addSkillTag(skill.trim());
            }
        }
        tagsPanel.revalidate();
        tagsPanel.repaint();
        loadEducations();
    }

    private void saveCVData() {
        if (currentCV == null) return;

        currentCV.setDesiredPosition(txtTitle.getText().trim());
        currentCV.setLocation(txtLocation.getText().trim());
        currentCV.setSkills(String.join(", ", skillList));
        String objective = txtObjective.getText().trim();
        currentCV.setObjective(objective.equals(OBJECTIVE_PLACEHOLDER) ? "" : objective);
        if (currentCV.getEducations() == null) {
            currentCV.setEducations(new ArrayList<>());
        }

        boolean success = cvService.saveCV(currentCV);
        if (success) {
            currentCV = cvService.getCV(SessionManager.getInstance().getCandidateId());
            loadEducations();
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "L\u01b0u CV th\u00e0nh c\u00f4ng.");
        } else {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Kh\u00f4ng th\u1ec3 l\u01b0u CV.", "L\u1ed7i", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createPersonalSection() {
        JPanel card = createCardPanel();
        JPanel header = createSectionHeader("TH\u00d4NG TIN C\u00c1 NH\u00c2N", "L\u01afU THAY \u0110\u1ed4I", e -> saveCVData());
        card.add(header);
        addSeparator(card);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);

        txtFullName = addTextField(form, "H\u1ecd t\u00ean", "Nguy\u1ec5n V\u0103n A", 0, 0);
        txtTitle = addTextField(form, "V\u1ecb tr\u00ed mong mu\u1ed1n", "L\u1eadp tr\u00ecnh vi\u00ean Java", 1, 0);
        txtLocation = addTextField(form, "\u0110\u1ecba \u0111i\u1ec3m", "TP. H\u1ed3 Ch\u00ed Minh", 0, 1);
        txtEmail = addTextField(form, "Email", "email@example.com", 1, 1);
        txtPhone = addTextField(form, "S\u1ed1 \u0111i\u1ec7n tho\u1ea1i", "0900000000", 0, 2);

        card.add(form);
        return card;
    }

    private JPanel createObjectiveSection() {
        JPanel card = createCardPanel();
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        JLabel title = createHeaderLabel("M\u1ee4C TI\u00caU NGH\u1ec0 NGHI\u1ec6P");
        JLabel reset = new JLabel("\u0110\u1eb6T L\u1ea0I");
        reset.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reset.setForeground(new Color(13, 110, 253));
        reset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reset.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                setObjectiveText(null);
            }
        });
        header.add(title, BorderLayout.WEST);
        header.add(reset, BorderLayout.EAST);
        card.add(header);
        addSeparator(card);

        txtObjective = new JTextArea(OBJECTIVE_PLACEHOLDER);
        txtObjective.setRows(3);
        txtObjective.setForeground(Color.GRAY);
        txtObjective.setLineWrap(true);
        txtObjective.setWrapStyleWord(true);
        txtObjective.setFont(INPUT_FONT);
        txtObjective.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        txtObjective.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtObjective.getText().equals(OBJECTIVE_PLACEHOLDER)) {
                    txtObjective.setText("");
                    txtObjective.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtObjective.getText().trim().isEmpty()) {
                    setObjectiveText(null);
                }
            }
        });
        card.add(txtObjective);
        return card;
    }

    private JPanel createSkillsSection() {
        JPanel card = createCardPanel();
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.add(createHeaderLabel("K\u1ef8 N\u0102NG"), BorderLayout.WEST);
        card.add(header);
        addSeparator(card);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        txtSkill = new JTextField(SKILL_PLACEHOLDER);
        txtSkill.setFont(INPUT_FONT);
        txtSkill.setForeground(Color.GRAY);
        txtSkill.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSkill.getText().equals(SKILL_PLACEHOLDER)) {
                    txtSkill.setText("");
                    txtSkill.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSkill.getText().trim().isEmpty()) {
                    txtSkill.setText(SKILL_PLACEHOLDER);
                    txtSkill.setForeground(Color.GRAY);
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 16);
        inputPanel.add(txtSkill, gbc);

        JButton addButton = createButton("TH\u00caM K\u1ef8 N\u0102NG", new Color(13, 110, 253), Color.WHITE);
        addButton.setPreferredSize(new Dimension(160, 40));
        addButton.addActionListener(e -> addSkillFromInput());
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        inputPanel.add(addButton, gbc);

        tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tagsPanel.setBackground(Color.WHITE);

        card.add(inputPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(tagsPanel);
        return card;
    }

    private JPanel createEducationSection() {
        JPanel card = createCardPanel();
        card.add(createSectionHeader("H\u1eccC V\u1ea4N", "TH\u00caM H\u1eccC V\u1ea4N", e -> openEducationDialog(null)));
        addSeparator(card);

        educationModel = new DefaultTableModel(new Object[]{"TR\u01af\u1edcNG", "CHUY\u00caN NG\u00c0NH", "N\u0102M", "THAO T\u00c1C"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        educationTable = new JTable(educationModel);
        configureEducationTable();

        JScrollPane scrollPane = new JScrollPane(educationTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(0, 180));
        scrollPane.getViewport().setBackground(Color.WHITE);
        if (scrollPane.getColumnHeader() != null) {
            scrollPane.getColumnHeader().setBackground(Color.WHITE);
            scrollPane.getColumnHeader().setBorder(BorderFactory.createEmptyBorder());
        }
        card.add(scrollPane);
        return card;
    }

    private JPanel createActionSection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(248, 249, 250));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(new Color(248, 249, 250));

        JButton reloadButton = createButton("H\u1ee6Y THAY \u0110\u1ed4I", Color.WHITE, Color.DARK_GRAY);
        reloadButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        reloadButton.setPreferredSize(new Dimension(150, 42));
        reloadButton.addActionListener(e -> loadData());

        JButton saveButton = createButton("L\u01afU CV", new Color(33, 37, 41), Color.WHITE);
        saveButton.setPreferredSize(new Dimension(150, 42));
        saveButton.addActionListener(e -> saveCVData());

        actionPanel.add(reloadButton);
        actionPanel.add(saveButton);
        wrapper.add(actionPanel, BorderLayout.NORTH);
        return wrapper;
    }

    private void configureEducationTable() {
        educationTable.setRowHeight(48);
        educationTable.setFont(INPUT_FONT);
        educationTable.setBackground(Color.WHITE);
        educationTable.setSelectionBackground(new Color(245, 248, 255));
        educationTable.setSelectionForeground(Color.BLACK);
        educationTable.setFillsViewportHeight(true);
        educationTable.setBorder(BorderFactory.createEmptyBorder());
        educationTable.setShowGrid(false);
        educationTable.setShowVerticalLines(false);
        educationTable.setShowHorizontalLines(false);
        educationTable.setGridColor(Color.WHITE);
        educationTable.setIntercellSpacing(new Dimension(0, 0));

        educationTable.getTableHeader().setFont(HEADER_FONT);
        educationTable.getTableHeader().setForeground(Color.GRAY);
        educationTable.getTableHeader().setBackground(Color.WHITE);
        educationTable.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        educationTable.getTableHeader().setDefaultRenderer(new HeaderCellRenderer());
        educationTable.getTableHeader().setReorderingAllowed(false);
        educationTable.getTableHeader().setResizingAllowed(false);

        TableColumnModel columns = educationTable.getColumnModel();
        columns.getColumn(0).setPreferredWidth(360);
        columns.getColumn(1).setPreferredWidth(280);
        columns.getColumn(2).setPreferredWidth(130);
        columns.getColumn(3).setPreferredWidth(170);

        columns.getColumn(0).setCellRenderer(new TextCellRenderer(SwingConstants.LEFT));
        columns.getColumn(1).setCellRenderer(new TextCellRenderer(SwingConstants.LEFT));
        columns.getColumn(2).setCellRenderer(new TextCellRenderer(SwingConstants.CENTER));
        columns.getColumn(3).setCellRenderer(new EducationActionRenderer());
        columns.getColumn(3).setCellEditor(new EducationActionEditor());
    }

    private void loadEducations() {
        if (educationModel == null) return;
        educationModel.setRowCount(0);
        if (currentCV != null && currentCV.getEducations() != null) {
            for (Education education : currentCV.getEducations()) {
                educationModel.addRow(new Object[]{
                        valueOrEmpty(education.getSchool()),
                        valueOrEmpty(education.getMajor()),
                        formatEducationYear(education),
                        ""
                });
            }
        }
    }

    private JTextField addTextField(JPanel form, String label, String placeholder, int gridx, int gridy) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(Color.DARK_GRAY);

        JTextField field = new JTextField();
        field.setHorizontalAlignment(JTextField.LEFT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setFont(INPUT_FONT);
        field.setPreferredSize(new Dimension(0, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(0, 10, 0, 10)
        ));
        field.setToolTipText(placeholder);

        group.add(lbl);
        group.add(Box.createRigidArea(new Dimension(0, 6)));
        group.add(field);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, gridx == 0 ? 0 : 10, 15, gridx == 0 ? 10 : 0);
        form.add(group, gbc);
        return field;
    }

    private JPanel createSectionHeader(String title, String buttonText, java.awt.event.ActionListener action) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.add(createHeaderLabel(title), BorderLayout.WEST);
        JButton button = createButton(buttonText, new Color(13, 110, 253), Color.WHITE);
        button.setPreferredSize(new Dimension(150, 38));
        button.addActionListener(action);
        header.add(button, BorderLayout.EAST);
        return header;
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        label.setForeground(new Color(33, 37, 41));
        return label;
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return card;
    }

    private void addSeparator(JPanel card) {
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 18)));
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addSkillFromInput() {
        String skill = txtSkill.getText().trim();
        if (skill.isEmpty() || skill.equals(SKILL_PLACEHOLDER)) return;
        addSkillTag(skill);
        tagsPanel.revalidate();
        tagsPanel.repaint();
        txtSkill.setText("");
        txtSkill.requestFocusInWindow();
    }

    private void addSkillTag(String skill) {
        if (skill.isBlank() || skillList.contains(skill)) return;
        skillList.add(skill);
        tagsPanel.add(createSkillTag(skill));
    }

    private JPanel createSkillTag(String skill) {
        JPanel tag = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        tag.setBackground(Color.WHITE);
        tag.setBorder(new LineBorder(new Color(220, 220, 220), 1));

        JLabel label = new JLabel(skill);
        label.setFont(INPUT_FONT);
        JLabel close = new JLabel("x");
        close.setFont(new Font("Segoe UI", Font.BOLD, 12));
        close.setForeground(Color.GRAY);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                skillList.remove(skill);
                tagsPanel.remove(tag);
                tagsPanel.revalidate();
                tagsPanel.repaint();
            }
        });

        tag.add(label);
        tag.add(close);
        return tag;
    }

    private String formatEducationYear(Education education) {
        if (education.getStartYear() != null && education.getEndYear() != null) {
            return education.getStartYear() + " - " + education.getEndYear();
        }
        if (education.getStartYear() != null) {
            return String.valueOf(education.getStartYear());
        }
        if (education.getEndYear() != null) {
            return String.valueOf(education.getEndYear());
        }
        return "";
    }

    private JPanel createEducationActionPanel(int row, boolean editable) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 7));
        panel.setBackground(Color.WHITE);

        JButton edit = new JButton("S\u1eeda");
        edit.setFont(INPUT_FONT);
        JButton delete = new JButton("X\u00f3a");
        delete.setFont(INPUT_FONT);
        delete.setForeground(Color.RED);

        if (editable) {
            edit.addActionListener(e -> editEducationAt(row));
            delete.addActionListener(e -> deleteEducationAt(row));
        }

        panel.add(edit);
        panel.add(delete);
        return panel;
    }

    private void editEducationAt(int viewRow) {
        stopEducationEditing();
        Education education = getEducationAt(viewRow);
        if (education != null) {
            openEducationDialog(education);
        }
    }

    private void deleteEducationAt(int viewRow) {
        stopEducationEditing();
        Education education = getEducationAt(viewRow);
        if (education != null && currentCV != null && currentCV.getEducations() != null) {
            currentCV.getEducations().remove(education);
            loadEducations();
        }
    }

    private Education getEducationAt(int viewRow) {
        if (currentCV == null || currentCV.getEducations() == null || viewRow < 0) return null;
        int modelRow = educationTable.convertRowIndexToModel(viewRow);
        if (modelRow < 0 || modelRow >= currentCV.getEducations().size()) return null;
        return currentCV.getEducations().get(modelRow);
    }

    private void stopEducationEditing() {
        if (educationTable.isEditing() && educationTable.getCellEditor() != null) {
            educationTable.getCellEditor().stopCellEditing();
        }
    }

    private static class TextCellRenderer extends DefaultTableCellRenderer {
        TextCellRenderer(int alignment) {
            setHorizontalAlignment(alignment);
            setBorder(new EmptyBorder(0, 12, 0, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            component.setFont(table.getFont());
            return component;
        }
    }

    private static class HeaderCellRenderer extends DefaultTableCellRenderer {
        HeaderCellRenderer() {
            setBorder(new EmptyBorder(0, 12, 0, 12));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            setHorizontalAlignment(column == 0 || column == 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
            setFont(HEADER_FONT);
            setForeground(Color.GRAY);
            setBackground(Color.WHITE);
            return this;
        }
    }

    private class EducationActionRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focused, int row, int column) {
            return createEducationActionPanel(row, false);
        }
    }

    private class EducationActionEditor extends AbstractCellEditor implements TableCellEditor {
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean selected,
                                                     int row, int column) {
            return createEducationActionPanel(row, true);
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    private void openEducationDialog(Education existing) {
        if (currentCV == null) return;
        if (currentCV.getEducations() == null) currentCV.setEducations(new ArrayList<>());

        JTextField school = dialogText(existing != null ? existing.getSchool() : null);
        JTextField degree = dialogText(existing != null ? existing.getDegree() : null);
        JTextField major = dialogText(existing != null ? existing.getMajor() : null);
        JTextField startYear = dialogText(existing != null && existing.getStartYear() != null ? String.valueOf(existing.getStartYear()) : null);
        JTextField endYear = dialogText(existing != null && existing.getEndYear() != null ? String.valueOf(existing.getEndYear()) : null);
        JTextArea description = new JTextArea(existing != null ? valueOrEmpty(existing.getDescription()) : "");
        description.setFont(INPUT_FONT);
        description.setRows(3);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(12, 12, 12, 12));
        addDialogField(form, "Tr\u01b0\u1eddng", school, 0);
        addDialogField(form, "B\u1eb1ng c\u1ea5p", degree, 1);
        addDialogField(form, "Chuy\u00ean ng\u00e0nh", major, 2);
        addDialogField(form, "N\u0103m b\u1eaft \u0111\u1ea7u", startYear, 3);
        addDialogField(form, "N\u0103m k\u1ebft th\u00fac", endYear, 4);
        addDialogField(form, "M\u00f4 t\u1ea3", new JScrollPane(description), 5);

        Object oldOk = UIManager.get("OptionPane.okButtonText");
        Object oldCancel = UIManager.get("OptionPane.cancelButtonText");
        UIManager.put("OptionPane.okButtonText", "L\u01b0u");
        UIManager.put("OptionPane.cancelButtonText", "H\u1ee7y");
        int option = JOptionPane.showConfirmDialog(
                this,
                form,
                existing == null ? "Th\u00eam h\u1ecdc v\u1ea5n" : "S\u1eeda h\u1ecdc v\u1ea5n",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        UIManager.put("OptionPane.okButtonText", oldOk);
        UIManager.put("OptionPane.cancelButtonText", oldCancel);
        if (option != JOptionPane.OK_OPTION) return;

        if (school.getText().trim().isEmpty()) {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "Vui l\u00f2ng nh\u1eadp t\u00ean tr\u01b0\u1eddng.", "C\u1ea3nh b\u00e1o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer start = parseYear(startYear.getText().trim());
        Integer end = parseYear(endYear.getText().trim());
        if ((!startYear.getText().trim().isEmpty() && start == null)
                || (!endYear.getText().trim().isEmpty() && end == null)) {
            org.jobportal.view.common.SuccessDialog.showMessageDialog(this, "N\u0103m ph\u1ea3i l\u00e0 s\u1ed1 h\u1ee3p l\u1ec7.", "C\u1ea3nh b\u00e1o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Education education = existing != null ? existing : new Education();
        education.setSchool(school.getText().trim());
        education.setDegree(emptyToNull(degree.getText()));
        education.setMajor(emptyToNull(major.getText()));
        education.setStartYear(start);
        education.setEndYear(end);
        education.setDescription(emptyToNull(description.getText()));
        if (existing == null) currentCV.getEducations().add(education);
        loadEducations();
    }

    private JTextField dialogText(String value) {
        JTextField field = new JTextField(valueOrEmpty(value));
        field.setFont(INPUT_FONT);
        return field;
    }

    private void addDialogField(JPanel form, String label, Component field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row * 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        form.add(lbl, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 0);
        field.setPreferredSize(new Dimension(340, field instanceof JScrollPane ? 80 : 36));
        form.add(field, gbc);
    }

    private void setObjectiveText(String value) {
        if (value == null || value.isBlank()) {
            txtObjective.setText(OBJECTIVE_PLACEHOLDER);
            txtObjective.setForeground(Color.GRAY);
        } else {
            txtObjective.setText(value);
            txtObjective.setForeground(Color.BLACK);
        }
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

    private String valueOrEmpty(String value) {
        return value != null ? value : "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Qu\u1ea3n l\u00fd CV");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 800);
            frame.setLayout(new BorderLayout());
            frame.add(new HeaderPanel(), BorderLayout.NORTH);
            frame.add(new SidebarPanel(org.jobportal.enums.Role.CANDIDATE), BorderLayout.WEST);
            frame.add(new CVEditorPanel(), BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

