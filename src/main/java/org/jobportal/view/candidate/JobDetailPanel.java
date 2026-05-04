package org.jobportal.view.candidate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static org.jobportal.view.util.DesignSystem.*;

/**
 * Job detail panel matching chi_ti_t_c_ng_vi_c_jobportal_1 stitch design.
 * Layout:
 *   - Title card (Senior Software Engineer + company + FULLTIME/OPEN badges)
 *   - 3-column grid: left 2-col metadata (3 rows) + right column (Company Location + Hiring Process)
 *   - Description card (left col only): Description, Responsibilities, Requirements, Benefits
 *   - Bottom action bar INSIDE scroll content: Save for later / Add to Bookmarks | SHARE JOB | Apply / Send CV
 */
public class JobDetailPanel extends JPanel {

    public JobDetailPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        JPanel mainContent = createContentPanel();

        // 1. Title card
        mainContent.add(createTitleCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 2. 3-column area: metadata left (2 cols, 3 rows) + right column (location + hiring)
        mainContent.add(createMetadataAndSidebar());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 3. Description card (left portion only, full width here)
        mainContent.add(createDescriptionCard());
        mainContent.add(Box.createRigidArea(new Dimension(0, SPACE_5)));

        // 4. Bottom action bar (inside scroll, at end of page)
        mainContent.add(createBottomBar());

        add(createScrollPane(mainContent), BorderLayout.CENTER);
    }

    private JPanel createTitleCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_SURFACE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        // Left: title + company
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(BG_SURFACE);

        JLabel lblTitle = new JLabel("Senior Software Engineer");
        lblTitle.setFont(heading2());
        lblTitle.setForeground(TEXT_PRIMARY);

        JLabel lblCompany = new JLabel("🏢  TechFlow Systems Inc.");
        lblCompany.setFont(body());
        lblCompany.setForeground(TEXT_SECONDARY);

        left.add(lblTitle);
        left.add(Box.createRigidArea(new Dimension(0, SPACE_2)));
        left.add(lblCompany);
        card.add(left, BorderLayout.CENTER);

        // Right: FULLTIME + OPEN badges
        JPanel badges = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_2, 0));
        badges.setBackground(BG_SURFACE);
        badges.add(createBadge("FULLTIME", BADGE_BG, BADGE_FG));
        badges.add(createBadge("OPEN", BADGE_SUCCESS_BG, BADGE_SUCCESS_FG));
        card.add(badges, BorderLayout.EAST);

        return card;
    }

    private JPanel createMetadataAndSidebar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_PAGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Metadata grid (2 rows x 3 columns)
        panel.add(createMetadataGrid(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMetadataGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 3, SPACE_4, SPACE_4));
        grid.setBackground(BG_PAGE);

        grid.add(createMetaCard("📍", "LOCATION", "San Francisco, CA"));
        grid.add(createMetaCard("💰", "SALARY", "$120k - $150k"));
        grid.add(createMetaCard("📋", "EXPERIENCE", "3-5 years"));
        grid.add(createMetaCard("👥", "APPLICANTS", "12 applicants"));
        grid.add(createMetaCard("📅", "CREATED DATE", "Oct 1, 2023"));
        grid.add(createMetaCard("📆", "DUE DATE", "Oct 31, 2023"));

        return grid;
    }

    private JPanel createMetaCard(String icon, String label, String value) {
        JPanel card = new JPanel(new BorderLayout(SPACE_3, 0));
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_4, SPACE_5, SPACE_4, SPACE_5)
        ));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(fontRegular(20));
        card.add(lblIcon, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(BG_SURFACE);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(caption());
        lblLabel.setForeground(TEXT_MUTED);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(fontBold(FONT_SIZE_BASE));
        lblValue.setForeground(TEXT_PRIMARY);

        textPanel.add(lblLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, SPACE_1)));
        textPanel.add(lblValue);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }


    private JPanel createDescriptionCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_SURFACE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(SPACE_6, SPACE_6, SPACE_6, SPACE_6)
        ));

        // "Description" heading
        JLabel lblDesc = new JLabel("Description");
        lblDesc.setFont(heading3());
        lblDesc.setForeground(TEXT_PRIMARY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblDesc);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        JSeparator sep1 = new JSeparator();
        sep1.setForeground(BORDER_LIGHT);
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep1.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sep1);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_4)));

        JTextArea txtDescription = new JTextArea(
                "We are seeking a high-performing Senior Software Engineer to join our core platform team. " +
                "You will be responsible for architecting and implementing scalable backend services that " +
                "power millions of real-time transactions daily. The ideal candidate has a deep understanding " +
                "of distributed systems and a passion for clean, maintainable code."
        );
        txtDescription.setFont(body());
        txtDescription.setForeground(TEXT_SECONDARY);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setEditable(false);
        txtDescription.setFocusable(false);
        txtDescription.setBackground(BG_SURFACE);
        txtDescription.setBorder(null);
        txtDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtDescription);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_6)));

        // Responsibilities
        addBulletSection(card, "Responsibilities", new String[]{
                "Design and maintain high-availability APIs and microservices.",
                "Collaborate with product managers to define technical roadmaps.",
                "Mentor junior engineers and conduct thorough code reviews.",
                "Optimize system performance and solve complex scalability bottlenecks.",
                "Participate in on-call rotations to ensure 99.9% service uptime."
        });
        card.add(Box.createRigidArea(new Dimension(0, SPACE_6)));

        // Requirements
        addBulletSection(card, "Requirements", new String[]{
                "BS/MS in Computer Science or equivalent practical experience.",
                "5+ years of professional experience with Node.js, Go, or Python.",
                "Strong experience with SQL (PostgreSQL) and NoSQL databases.",
                "Proficiency in cloud infrastructure (AWS/GCP) and Docker/Kubernetes.",
                "Experience with message brokers like RabbitMQ or Kafka."
        });
        card.add(Box.createRigidArea(new Dimension(0, SPACE_6)));

        // Benefits
        addBulletSection(card, "Benefits", new String[]{
                "Competitive salary and equity packages.",
                "Comprehensive medical, dental, and vision insurance.",
                "Unlimited PTO and flexible working hours.",
                "$2,000 annual professional development budget.",
                "Remote-first culture with optional co-working spaces."
        });

        return card;
    }

    private void addBulletSection(JPanel card, String title, String[] items) {
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(heading4());
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, SPACE_3)));

        for (String item : items) {
            JLabel lbl = new JLabel("•  " + item);
            lbl.setFont(body());
            lbl.setForeground(TEXT_SECONDARY);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            lbl.setBorder(new EmptyBorder(SPACE_1, 0, SPACE_1, 0));
            card.add(lbl);
        }
    }

    private JPanel createBottomBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_SURFACE);
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(SPACE_4, SPACE_6, SPACE_4, SPACE_6)
        ));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        // Left: Save for later + Add to Bookmarks
        JPanel leftActions = new JPanel();
        leftActions.setLayout(new BoxLayout(leftActions, BoxLayout.Y_AXIS));
        leftActions.setBackground(BG_SURFACE);

        JLabel lblSave = new JLabel("Save for later");
        lblSave.setFont(bodySmall());
        lblSave.setForeground(TEXT_PRIMARY);

        JPanel bookmarkRow = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACE_1, 0));
        bookmarkRow.setBackground(BG_SURFACE);
        JLabel lblBookmarkIcon = new JLabel("🔖");
        lblBookmarkIcon.setFont(fontRegular(14));
        JLabel lblAddBookmark = new JLabel("Add to Bookmarks");
        lblAddBookmark.setFont(bodySmall());
        lblAddBookmark.setForeground(TEXT_SECONDARY);
        bookmarkRow.add(lblBookmarkIcon);
        bookmarkRow.add(lblAddBookmark);

        leftActions.add(lblSave);
        leftActions.add(bookmarkRow);
        bar.add(leftActions, BorderLayout.WEST);

        // Right: SHARE JOB + Apply / Send CV
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, SPACE_3, 0));
        rightActions.setBackground(BG_SURFACE);

        JButton btnShare = createOutlineButton("SHARE JOB");
        btnShare.setPreferredSize(new Dimension(130, 40));
        rightActions.add(btnShare);

        JButton btnApply = createPrimaryButton("Apply / Send CV");
        btnApply.setPreferredSize(new Dimension(180, 40));
        rightActions.add(btnApply);

        bar.add(rightActions, BorderLayout.EAST);

        return bar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Job Detail");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 900);
            frame.setLayout(new BorderLayout());

            frame.add(new org.jobportal.view.common.HeaderPanel(), BorderLayout.NORTH);
            frame.add(new org.jobportal.view.common.SidebarPanel(org.jobportal.view.common.SidebarPanel.Role.CANDIDATE), BorderLayout.WEST);
            frame.add(new JobDetailPanel(), BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}