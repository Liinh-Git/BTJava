package org.jobportal.view.candidate;

import org.jobportal.view.util.DesignSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

/**
 * Job search panel matching t_m_ki_m_vi_c_l_m_minimal stitch design.
 * Layout: search bar card + category filter + "Recommended Jobs" title
 *         + 2-column grid of job cards + pagination.
 * Flat design, 1px borders, no shadows.
 */
public class JobSearchPanel extends JPanel {

    public JobSearchPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        // 1. thanh tim kiem
        mainContent.add(createSearchSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        // 2. tieu de "Recommended Jobs" va toggle view
        mainContent.add(createSectionHeader());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 3. luoi the cong viec 2 cot
        mainContent.add(createJobGrid());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_8)));

        // 4. phan trang
        mainContent.add(createPagination());

        add(createScrollPane(mainContent), BorderLayout.CENTER);
    }

    private JPanel createSearchSection() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);

        // dong nhan
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, SPACE_2, SPACE_5);
        JLabel lblSearch = new JLabel("What job are you looking for?");
        lblSearch.setFont(body());
        lblSearch.setForeground(TEXT_SECONDARY);
        card.add(lblSearch, gbc);

        gbc.gridx = 1; gbc.weightx = 0.25;
        gbc.insets = new Insets(0, 0, SPACE_2, SPACE_3);
        JLabel lblCategory = new JLabel("Category");
        lblCategory.setFont(body());
        lblCategory.setForeground(TEXT_SECONDARY);
        card.add(lblCategory, gbc);

        gbc.gridx = 2; gbc.weightx = 0.15;
        gbc.insets = new Insets(0, 0, SPACE_2, 0);
        card.add(new JLabel(""), gbc); // spacer

        // dong input
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, 0, SPACE_5);
        JTextField txtSearch = new JTextField("Job title, keywords, or company");
        txtSearch.setFont(body());
        txtSearch.setForeground(TEXT_MUTED);
        txtSearch.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_INPUT, 1),
                new EmptyBorder(SPACE_2, SPACE_3, SPACE_2, SPACE_3)
        ));
        card.add(txtSearch, gbc);

        gbc.gridx = 1; gbc.weightx = 0.25;
        gbc.insets = new Insets(0, 0, 0, SPACE_3);
        JComboBox<String> cbCategory = new JComboBox<>(new String[]{"All Categories", "IT - Phần mềm", "Marketing", "Kế toán"});
        cbCategory.setFont(body());
        cbCategory.setBackground(BG_SURFACE);
        cbCategory.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        card.add(cbCategory, gbc);

        gbc.gridx = 2; gbc.weightx = 0.15;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton btnSearch = createPrimaryButton("Search");
        btnSearch.setPreferredSize(new Dimension(100, INPUT_HEIGHT));
        card.add(btnSearch, gbc);

        // dong popular tags
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        gbc.insets = new Insets(SPACE_4, 0, 0, 0);
        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        tagsPanel.setBackground(BG_SURFACE);

        JLabel lblPopular = new JLabel("Popular:");
        lblPopular.setFont(bodySmall());
        lblPopular.setForeground(TEXT_SECONDARY);
        tagsPanel.add(lblPopular);

        for (String tag : new String[]{"Product Design", "React Engineer", "Data Analyst"}) {
            JLabel chip = new JLabel(tag);
            chip.setFont(bodySmall());
            chip.setOpaque(true);
            chip.setBackground(new Color(241, 245, 249));
            chip.setForeground(TEXT_PRIMARY);
            chip.setBorder(new EmptyBorder(SPACE_1, SPACE_2, SPACE_1, SPACE_2));
            tagsPanel.add(chip);
        }
        card.add(tagsPanel, gbc);

        return card;
    }

    private JPanel createSectionHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PAGE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblTitle = new JLabel("Recommended Jobs");
        lblTitle.setFont(heading3());
        lblTitle.setForeground(TEXT_PRIMARY);
        header.add(lblTitle, BorderLayout.WEST);

        // toggle view icons (grid / list)
        JPanel viewToggle = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_1, 0));
        viewToggle.setBackground(BG_PAGE);

        JButton btnGrid = new JButton("⊞");
        btnGrid.setFont(fontRegular(18));
        btnGrid.setPreferredSize(new Dimension(36, 36));
        btnGrid.setBackground(BG_SURFACE);
        btnGrid.setBorder(new LineBorder(BORDER, 1));
        btnGrid.setFocusPainted(false);

        JButton btnList = new JButton("☰");
        btnList.setFont(fontRegular(18));
        btnList.setPreferredSize(new Dimension(36, 36));
        btnList.setBackground(BG_SURFACE);
        btnList.setBorder(new LineBorder(BORDER, 1));
        btnList.setFocusPainted(false);

        viewToggle.add(btnGrid);
        viewToggle.add(btnList);
        header.add(viewToggle, BorderLayout.EAST);

        return header;
    }

    private JPanel createJobGrid() {
        JPanel grid = new JPanel(new GridLayout(0, 2, SPACE_5, SPACE_5));
        grid.setBackground(BG_PAGE);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        grid.add(createJobCard("Senior UI/UX Designer", "Aperture Systems",
                "$120k - $150k", "San Francisco (Hybrid)",
                "We are looking for a creative UI/UX Designer to join our team. You will be responsible for creating amazing user experiences and...",
                new String[]{"Full-time", "Remote-friendly"}));

        grid.add(createJobCard("Frontend Developer", "Nebula Cloud Services",
                "$90k - $130k", "Remote, USA",
                "Join our fast-growing engineering team as a Frontend Developer. You'll be working with React, Tailwind CSS, and TypeScript to bui...",
                new String[]{"Contract", "Junior-Mid"}));

        grid.add(createJobCard("Product Manager", "Zenith FinTech",
                "$140k - $180k", "New York, NY",
                "Zenith FinTech is looking for a strategic Product Manager to lead our mobile banking initiative. You will drive the product vision and...",
                new String[]{"Full-time", "Senior"}));

        grid.add(createJobCard("Sustainability Analyst", "GreenPath Solutions",
                "$75k - $95k", "Austin, TX",
                "Passionate about the planet? We are seeking an Analyst to evaluate corporate carbon footprints and suggest actionable...",
                new String[]{"Full-time", "Entry Level"}));

        return grid;
    }

    private JPanel createJobCard(String title, String company, String salary, String location, String description, String[] tags) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING)
        ));

        // dong 1: icon + title + company + bookmark
        JPanel topRow = new JPanel(new BorderLayout(SPACE_3, 0));
        topRow.setBackground(BG_SURFACE);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // icon cong ty (placeholder)
        JLabel iconCompany = new JLabel("▣");
        iconCompany.setFont(fontRegular(24));
        iconCompany.setForeground(PRIMARY);
        topRow.add(iconCompany, BorderLayout.WEST);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BG_SURFACE);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(fontBold(FONT_SIZE_LG));
        lblTitle.setForeground(TEXT_PRIMARY);
        JLabel lblCompany = new JLabel(company);
        lblCompany.setFont(body());
        lblCompany.setForeground(TEXT_SECONDARY);
        titlePanel.add(lblTitle);
        titlePanel.add(lblCompany);
        topRow.add(titlePanel, BorderLayout.CENTER);

        JLabel lblBookmark = new JLabel("☐");
        lblBookmark.setFont(fontRegular(20));
        lblBookmark.setForeground(TEXT_MUTED);
        lblBookmark.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topRow.add(lblBookmark, BorderLayout.EAST);

        card.add(topRow);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        // dong 2: salary + location
        JPanel metaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_6, 0));
        metaRow.setBackground(BG_SURFACE);
        metaRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSalary = new JLabel("💰  " + salary);
        lblSalary.setFont(body());
        lblSalary.setForeground(TEXT_PRIMARY);
        metaRow.add(lblSalary);

        JLabel lblLocation = new JLabel("📍  " + location);
        lblLocation.setFont(body());
        lblLocation.setForeground(TEXT_PRIMARY);
        metaRow.add(lblLocation);

        card.add(metaRow);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        // dong 3: mo ta ngan
        JTextArea txtDesc = new JTextArea(description);
        txtDesc.setFont(body());
        txtDesc.setForeground(TEXT_SECONDARY);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setFocusable(false);
        txtDesc.setBackground(BG_SURFACE);
        txtDesc.setBorder(null);
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtDesc);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        // separator
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_LIGHT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sep);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        // dong 4: tags + button "Details & Apply"
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(BG_SURFACE);
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_2, 0));
        tagsPanel.setBackground(BG_SURFACE);
        for (String tag : tags) {
            JLabel chip = new JLabel(tag);
            chip.setFont(bodySmall());
            chip.setOpaque(true);
            chip.setBackground(BG_PAGE);
            chip.setForeground(TEXT_PRIMARY);
            chip.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER, 1),
                    new EmptyBorder(SPACE_1, SPACE_2, SPACE_1, SPACE_2)
            ));
            tagsPanel.add(chip);
        }
        bottomRow.add(tagsPanel, BorderLayout.WEST);

        JButton btnApply = createPrimaryButton("Details & Apply");
        btnApply.setPreferredSize(new Dimension(140, 36));
        bottomRow.add(btnApply, BorderLayout.EAST);

        card.add(bottomRow);

        return card;
    }

    private JPanel createPagination() {
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, SPACE_1, 0));
        paginationPanel.setBackground(BG_PAGE);
        paginationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        paginationPanel.add(createPageButton("<", false));
        paginationPanel.add(createPageButton("1", true));
        paginationPanel.add(createPageButton("2", false));
        paginationPanel.add(createPageButton("3", false));
        paginationPanel.add(createPageButton("...", false));
        paginationPanel.add(createPageButton("12", false));
        paginationPanel.add(createPageButton(">", false));

        return paginationPanel;
    }

    // ham test giao dien doc lap
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Candidate - Job Search");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            frame.add(new org.jobportal.view.common.HeaderPanel(), BorderLayout.NORTH);
            frame.add(new org.jobportal.view.common.SidebarPanel(org.jobportal.view.common.SidebarPanel.Role.CANDIDATE), BorderLayout.WEST);
            frame.add(new JobSearchPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}