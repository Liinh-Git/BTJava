package org.jobportal.view.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class JobCardPanel extends JPanel {

    private static final int ACTION_BUTTON_WIDTH = 80;
    private static final int ACTION_BUTTON_HEIGHT = 30;
    private static final int CARD_HEIGHT = 245;
    private static final int DESCRIPTION_HEIGHT = 40;

    private JPanel bottomPanel;
    private Component actionComponent;

    public JobCardPanel(String title, String company, String salary, String location, String description, String[] tags) {
        // thiet lap layout chinh cho the
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, CARD_HEIGHT));
        setMinimumSize(new Dimension(0, CARD_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, CARD_HEIGHT));
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // phan top: tieu de, cong ty, icon bookmark
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblCompany = new JLabel(company);
        lblCompany.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCompany.setForeground(Color.GRAY);

        titlePanel.add(lblTitle);
        titlePanel.add(lblCompany);
        topPanel.add(titlePanel, BorderLayout.CENTER);

        add(topPanel);
        add(Box.createRigidArea(new Dimension(0, 15)));

        // phan middle: thu nhap va dia diem
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        // panel con để xếp Thu nhập và Địa điểm theo chiều dọc
        JPanel infoContentPanel = new JPanel();
        infoContentPanel.setLayout(new BoxLayout(infoContentPanel, BoxLayout.Y_AXIS));
        infoContentPanel.setBackground(Color.WHITE);
        infoContentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dòng thu nhập
        JPanel salaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        salaryPanel.setBackground(Color.WHITE);
        salaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitleSalary = new JLabel("Thu nhập:");
        lblTitleSalary.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitleSalary.setPreferredSize(new Dimension(85, 15));

        JLabel lblSalary = new JLabel(salary);
        lblSalary.setFont(new Font("Segoe UI", Font.BOLD, 12));

        salaryPanel.add(lblTitleSalary);
        salaryPanel.add(lblSalary);

        // Dòng địa điểm
        JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        locationPanel.setBackground(Color.WHITE);
        locationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitleLocation = new JLabel("Địa điểm:");
        lblTitleLocation.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitleLocation.setPreferredSize(new Dimension(85, 15));

        JLabel lblLocation = new JLabel(location);
        lblLocation.setFont(new Font("Segoe UI", Font.BOLD, 12));

        locationPanel.add(lblTitleLocation);
        locationPanel.add(lblLocation);

        // add 2 dòng vào panel con
        infoContentPanel.add(salaryPanel);
        infoContentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoContentPanel.add(locationPanel);

        // add panel con vào infoPanel ngoài
        infoPanel.add(infoContentPanel);

        add(infoPanel);
        add(Box.createRigidArea(new Dimension(0, 15)));

        // phan mo ta
        JTextArea txtDesc = new JTextArea(description);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDesc.setForeground(new Color(80, 80, 80));
        txtDesc.setBackground(Color.WHITE);
        txtDesc.setRows(3);
        txtDesc.setPreferredSize(new Dimension(0, DESCRIPTION_HEIGHT));
        txtDesc.setMinimumSize(new Dimension(0, DESCRIPTION_HEIGHT));
        txtDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, DESCRIPTION_HEIGHT));

        add(txtDesc);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // phan bottom: the tag va nut apply
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        bottomPanel.setBackground(Color.WHITE);

        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tagPanel.setBackground(Color.WHITE);
        for (String tag : tags) {
            tagPanel.add(createTag(tag));
        }
        bottomPanel.add(tagPanel, BorderLayout.CENTER);

        JButton btnApply = new JButton("Chi tiết");
        btnApply.setBackground(new Color(13, 110, 253));
        btnApply.setForeground(Color.WHITE);
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnApply.setFocusPainted(false);
        applyActionButtonSizing(btnApply);
        
        actionComponent = btnApply;
        bottomPanel.add(actionComponent, BorderLayout.EAST);
        bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, ACTION_BUTTON_HEIGHT));

        add(bottomPanel);
    }

    public void setActionComponent(Component comp) {
        if (actionComponent != null) {
            bottomPanel.remove(actionComponent);
        }
        actionComponent = comp;
        if (actionComponent instanceof AbstractButton) {
            applyActionButtonSizing((AbstractButton) actionComponent);
        }
        Dimension actionSize = actionComponent.getPreferredSize();
        int bottomHeight = Math.max(ACTION_BUTTON_HEIGHT, actionSize.height);
        bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, bottomHeight));
        bottomPanel.add(actionComponent, BorderLayout.EAST);
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    private void applyActionButtonSizing(AbstractButton button) {
        Dimension size = new Dimension(ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
    }

    // ham tao label the tag
    private JLabel createTag(String text) {
        JLabel label = new JLabel(text);

        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(60, 60, 60));
        label.setBackground(new Color(245, 247, 250));
        label.setOpaque(true);

        // khoảng cách trong tag
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        return label;
    }
}
