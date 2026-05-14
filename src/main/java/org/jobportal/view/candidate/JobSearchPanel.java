package org.jobportal.view.candidate;

import org.jobportal.bll.impl.CategoryService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.enums.JobType;
import org.jobportal.enums.Role;
import org.jobportal.model.Category;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.JobCardPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JobSearchPanel extends JPanel {

    private static final String SEARCH_PLACEHOLDER = "Tìm kiếm việc làm, công ty...";

    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private final CategoryService categoryService = new CategoryService();
    private List<Category> categoryList = new ArrayList<>();

    private JTextField txtSearch;
    private JComboBox<String> cbCategory;
    private JPanel jobsGrid;
    private JPanel paginationPanel;
    private int currentPage = 1;
    private final int pageSize = 10;

    public JobSearchPanel() {
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(20, 30, 20, 30));

        mainContent.add(createSearchSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContent.add(createTitlePanel());
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));

        jobsGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        jobsGrid.setBackground(new Color(248, 249, 250));
        mainContent.add(jobsGrid);

        loadCategories();
        performSearch();

        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        paginationPanel = createPaginationSection();
        mainContent.add(paginationPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(248, 249, 250));
        JLabel lblTitle = new JLabel("Tin tuyển dụng phù hợp");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titlePanel.add(lblTitle, BorderLayout.WEST);
        return titlePanel;
    }

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
        panel.add(createLabel("Bạn đang tìm công việc gì?"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.2;
        panel.add(createLabel("Danh mục"), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.7;
        txtSearch = new JTextField(SEARCH_PLACEHOLDER);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().equals(SEARCH_PLACEHOLDER)) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText(SEARCH_PLACEHOLDER);
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });
        txtSearch.setPreferredSize(new Dimension(0, 35));
        panel.add(txtSearch, gbc);

        gbc.gridx = 1; gbc.weightx = 0.2;
        cbCategory = new JComboBox<>(new String[]{"Tất cả danh mục"});
        cbCategory.setPreferredSize(new Dimension(0, 35));
        cbCategory.setBackground(Color.WHITE);
        panel.add(cbCategory, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(13, 110, 253));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setPreferredSize(new Dimension(100, 35));
        btnSearch.addActionListener(e -> performSearch(1));
        panel.add(btnSearch, gbc);

        return panel;
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        categoryList = categories != null ? categories : new ArrayList<>();
        cbCategory.removeAllItems();
        cbCategory.addItem("Tất cả danh mục");
        for (Category cat : categoryList) {
            cbCategory.addItem(cat.getCategoryName());
        }
    }

    private void performSearch() {
        performSearch(1);
    }

    private void performSearch(int page) {
        currentPage = page;
        String keyword = txtSearch != null ? txtSearch.getText().trim() : "";
        if (keyword.equals(SEARCH_PLACEHOLDER)) keyword = "";

        int catIdx = cbCategory != null ? cbCategory.getSelectedIndex() : 0;
        String catId = null;
        if (catIdx > 0 && catIdx - 1 < categoryList.size()) {
            catId = categoryList.get(catIdx - 1).getCategoryId();
        }

        List<RecruitmentDTO> jobs = recruitmentService.searchRecruitments(keyword, catId, currentPage, pageSize);
        jobsGrid.removeAll();
        if (jobs != null && !jobs.isEmpty()) {
            for (RecruitmentDTO job : jobs) {
                jobsGrid.add(createJobCard(job));
            }
        } else {
            jobsGrid.add(createEmptyPanel());
        }
        jobsGrid.revalidate();
        jobsGrid.repaint();
        updatePaginationUI();
    }

    private JPanel createJobCard(RecruitmentDTO job) {
        String company = job.getCompanyName() != null ? job.getCompanyName() : "Chưa rõ công ty";
        String salary = job.getSalary() != null ? String.format("%,.0f VND", job.getSalary()) : "Thỏa thuận";
        String location = job.getLocation() != null ? job.getLocation() : "Chưa cập nhật địa điểm";
        String desc = job.getDescription() != null ? job.getDescription() : "";
        if (desc.length() > 100) desc = desc.substring(0, 100) + "...";

        JobCardPanel card = new JobCardPanel(
                job.getTitle(), company, salary, location, desc, new String[]{toJobTypeLabel(job.getJobType())});

        JButton btnDetail = new JButton("Chi tiết");
        btnDetail.setBackground(new Color(13, 110, 253));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDetail.setFocusPainted(false);
        btnDetail.addActionListener(e -> openJobDetail(job));
        card.setActionComponent(btnDetail);
        return card;
    }

    private void openJobDetail(RecruitmentDTO job) {
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        if (ancestor instanceof Frame) {
            JDialog dialog = new JDialog((Frame) ancestor, "Chi tiết việc làm", true);
            dialog.setSize(980, 680);
            dialog.setResizable(false);
            dialog.setLocationRelativeTo(null);
            dialog.add(new JobDetailPanel(job.getRecruitmentId(), dialog));
            dialog.setVisible(true);
        }
    }

    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        panel.add(new JLabel("Không có tin tuyển dụng phù hợp."));
        return panel;
    }

    private JPanel createPaginationSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(new Color(248, 249, 250));
        return panel;
    }

    private void updatePaginationUI() {
        if (paginationPanel == null) return;
        paginationPanel.removeAll();

        JButton btnPrev = createPageButton("<");
        btnPrev.setEnabled(currentPage > 1);
        btnPrev.addActionListener(e -> performSearch(currentPage - 1));
        paginationPanel.add(btnPrev);

        JButton btnPage = createPageButton(String.valueOf(currentPage));
        btnPage.setBorder(new LineBorder(new Color(13, 110, 253), 2));
        btnPage.setForeground(new Color(13, 110, 253));
        paginationPanel.add(btnPage);

        JButton btnNext = createPageButton(">");
        btnNext.setEnabled(jobsGrid.getComponentCount() == pageSize);
        btnNext.addActionListener(e -> performSearch(currentPage + 1));
        paginationPanel.add(btnNext);

        paginationPanel.revalidate();
        paginationPanel.repaint();
    }

    private JButton createPageButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
    }

    private String toJobTypeLabel(JobType jobType) {
        if (jobType == JobType.PARTTIME) return "Part-time";
        if (jobType == JobType.INTERNSHIP) return "Internship";
        return "Full-time";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ứng viên - Tìm việc");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            frame.add(new HeaderPanel(), BorderLayout.NORTH);
            frame.add(new SidebarPanel(Role.CANDIDATE), BorderLayout.WEST);
            frame.add(new JobSearchPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

