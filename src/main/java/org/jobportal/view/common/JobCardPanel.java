package org.jobportal.view.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class JobCardPanel extends JPanel {

    private static final int ACTION_BUTTON_WIDTH = 130;
    private static final int ACTION_BUTTON_HEIGHT = 38;

    private JPanel bottomPanel;
    private Component actionComponent;

    public JobCardPanel(String title, String company, String salary, String location, String description, String[] tags) {
        // thiet lap layout chinh cho the
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
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

        // phan middle: luong va dia diem
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        infoPanel.setBackground(Color.WHITE);

        JLabel lblSalary = new JLabel(salary);
        lblSalary.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lblLocation = new JLabel(location);
        lblLocation.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLocation.setForeground(Color.GRAY);

        infoPanel.add(lblSalary);
        infoPanel.add(lblLocation);

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

        add(txtDesc);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // phan bottom: the tag va nut apply
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);

        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tagPanel.setBackground(Color.WHITE);
        for (String tag : tags) {
            tagPanel.add(createTag(tag));
        }
        bottomPanel.add(tagPanel, BorderLayout.CENTER);

        JButton btnApply = new JButton("Details & Apply");
        btnApply.setBackground(new Color(13, 110, 253));
        btnApply.setForeground(Color.WHITE);
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnApply.setFocusPainted(false);
        applyActionButtonSizing(btnApply);
        
        actionComponent = btnApply;
        bottomPanel.add(actionComponent, BorderLayout.EAST);

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
        JLabel lbl = new JLabel(" " + text + " ");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(235, 238, 242));
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }
}