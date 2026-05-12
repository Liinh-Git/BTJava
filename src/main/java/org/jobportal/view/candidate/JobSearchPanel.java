package org.jobportal.view.candidate;

import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.JobCardPanel;
import org.jobportal.view.common.SidebarPanel;

import org.jobportal.bll.impl.CategoryService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.model.Category;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.bll.interfaces.IRecruitmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JobSearchPanel extends JPanel {

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private final CategoryService categoryService = new CategoryService();
    private List<Category> categoryList = new ArrayList<>();
    private java.util.Map<String, String> categoryMap = new java.util.HashMap<>();
    
    private JTextField txtSearch;
    private JComboBox<String> cbCategory;
    private JPanel jobsGrid;

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
        jobsGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        jobsGrid.setBackground(new Color(248, 249, 250));

        mainContent.add(jobsGrid);
        
        loadCategories();
        performSearch();
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
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(0, 35));
        panel.add(txtSearch, gbc);

        gbc.gridx = 1; gbc.weightx = 0.2;
        cbCategory = new JComboBox<>(new String[]{"All Categories"});
        cbCategory.setPreferredSize(new Dimension(0, 35));
        cbCategory.setBackground(Color.WHITE);
        panel.add(cbCategory, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(13, 110, 253));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setPreferredSize(new Dimension(100, 35));
        btnSearch.addActionListener(e -> performSearch());
        panel.add(btnSearch, gbc);

        return panel;
    }
    
    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        cbCategory.removeAllItems();
        cbCategory.addItem("All Categories");
        if (categories != null) {
            for (Category cat : categories) {
                categoryMap.put(cat.getCategoryName(), cat.getCategoryId());
                cbCategory.addItem(cat.getCategoryName());
            }
        }
    }
    
    private void performSearch() {
        String keyword = txtSearch != null ? txtSearch.getText().trim() : "";
        int catIdx = cbCategory != null ? cbCategory.getSelectedIndex() : 0;
        String catId = null;
        if (catIdx > 0 && categoryList != null && catIdx - 1 < categoryList.size()) {
            catId = categoryList.get(catIdx - 1).getCategoryId();
        }
        
        List<RecruitmentDTO> jobs = recruitmentService.searchRecruitments(keyword, catId, 1, 20);
        jobsGrid.removeAll();
        if (jobs != null) {
            for (RecruitmentDTO job : jobs) {
                String title = job.getTitle();
                String company = job.getCompanyName() != null ? job.getCompanyName() : "Unknown Company";
                String salary = job.getSalary() != null ? "$" + job.getSalary().intValue() : "Negotiable";
                String location = job.getLocation() != null ? job.getLocation() : "Unknown Location";
                String desc = job.getDescription() != null ? job.getDescription() : "";
                if (desc.length() > 100) desc = desc.substring(0, 100) + "...";
                String[] tags = {job.getJobType() != null ? job.getJobType().name() : "FULL_TIME"};
                
                JobCardPanel card = new JobCardPanel(title, company, salary, location, desc, tags);
                
                JButton btnApply = new JButton("Details & Apply");
                btnApply.setBackground(new Color(13, 110, 253));
                btnApply.setForeground(Color.WHITE);
                btnApply.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnApply.setFocusPainted(false);
                btnApply.addActionListener(e -> {
                    Window ancestor = SwingUtilities.getWindowAncestor(this);
                    if (ancestor instanceof Frame) {
                        JDialog dialog = new JDialog((Frame) ancestor, "Chi tiết việc làm", true);
                        dialog.setSize(1000, 800);
                        dialog.setLocationRelativeTo(ancestor);
                        dialog.add(new JobDetailPanel(job.getRecruitmentId(), dialog));
                        dialog.setVisible(true);
                    }
                });
                card.setActionComponent(btnApply);
                
                jobsGrid.add(card);
            }
        }
        jobsGrid.revalidate();
        jobsGrid.repaint();
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

            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.CANDIDATE);
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