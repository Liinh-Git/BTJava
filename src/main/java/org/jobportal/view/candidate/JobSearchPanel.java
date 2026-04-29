package org.jobportal.view.candidate;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.JobCardPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class JobSearchPanel extends JPanel {

    public JobSearchPanel() {
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. phan tim kiem
        mainContent.add(createSearchSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // 2. tieu de
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(248, 249, 250));
        JLabel lblTitle = new JLabel("Recommended Jobs");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titlePanel.add(lblTitle, BorderLayout.WEST);

        JPanel viewTogglePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        viewTogglePanel.setBackground(new Color(248, 249, 250));
        viewTogglePanel.add(new JButton("::"));
        viewTogglePanel.add(new JButton("="));
        titlePanel.add(viewTogglePanel, BorderLayout.EAST);

        mainContent.add(titlePanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // 3. danh sach cong viec
        JPanel jobsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        jobsGrid.setBackground(new Color(248, 249, 250));

        // khoi tao the viec lam thong qua component dung chung
        jobsGrid.add(new JobCardPanel("Senior UI/UX Designer", "Aperture Systems", "$120k - $150k", "San Francisco (Hybrid)", "We are looking for a creative UI/UX Designer to join our team. You will be responsible for creating amazing user experiences and...", new String[]{"Full-time", "Remote-friendly"}));
        jobsGrid.add(new JobCardPanel("Frontend Developer", "Nebula Cloud Services", "$90k - $130k", "Remote, USA", "Join our fast-growing engineering team as a Frontend Developer. You'll be working with React, Tailwind CSS, and TypeScript to bui...", new String[]{"Contract", "Junior-Mid"}));
        jobsGrid.add(new JobCardPanel("Product Manager", "Zenith FinTech", "$140k - $180k", "New York, NY", "Zenith FinTech is looking for a strategic Product Manager to lead our mobile banking initiative. You will drive the product vision and...", new String[]{"Full-time", "Senior"}));
        jobsGrid.add(new JobCardPanel("Sustainability Analyst", "GreenPath Solutions", "$75k - $95k", "Austin, TX", "Passionate about the planet? We are seeking an Analyst to evaluate corporate carbon footprints and suggest actionable...", new String[]{"Full-time", "Entry Level"}));

        mainContent.add(jobsGrid);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        // 4. phan trang
        mainContent.add(createPaginationSection());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    // ham tao khu vuc tim kiem
    private JPanel createSearchSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.7;
        panel.add(createLabel("What job are you looking for?"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.2;
        panel.add(createLabel("Category"), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.7;
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(0, 35));
        panel.add(txtSearch, gbc);

        gbc.gridx = 1; gbc.weightx = 0.2;
        // IMPLEMENT: THÊM CÁC CATEGORIES VÀO
        JComboBox<String> cbCategory = new JComboBox<>(new String[]{"All Categories", "IT", "Design", "Marketing"});
        cbCategory.setPreferredSize(new Dimension(0, 35));
        cbCategory.setBackground(Color.WHITE);
        panel.add(cbCategory, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(13, 110, 253));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setPreferredSize(new Dimension(100, 35));
        panel.add(btnSearch, gbc);

        return panel;
    }

    // ham tao nut phan trang
    private JPanel createPaginationSection() {
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        paginationPanel.setBackground(new Color(248, 249, 250));

        String[] pages = {"<", "1", "2", "3", "...", "12", ">"};
        for (String p : pages) {
            JButton btnPage = new JButton(p);
            btnPage.setBackground(Color.WHITE);
            btnPage.setFocusPainted(false);
            if (p.equals("1")) {
                btnPage.setBorder(new LineBorder(new Color(13, 110, 253), 2));
                btnPage.setForeground(new Color(13, 110, 253));
            } else if (p.equals("...")) {
                btnPage.setBorderPainted(false);
                btnPage.setContentAreaFilled(false);
            } else {
                btnPage.setBorder(new LineBorder(new Color(220, 220, 220), 1));
            }
            paginationPanel.add(btnPage);
        }

        return paginationPanel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
    }

    private JLabel createTag(String text) {
        JLabel lbl = new JLabel(" " + text + " ");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(235, 238, 242));
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Job Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            SidebarPanel sidebar = new SidebarPanel(SidebarPanel.Role.CANDIDATE);
            frame.add(sidebar, BorderLayout.WEST);

            JPanel rightPanel = new JPanel(new BorderLayout());

            JobSearchPanel mainDashboard = new JobSearchPanel();
            rightPanel.add(mainDashboard, BorderLayout.CENTER);

            frame.add(rightPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}