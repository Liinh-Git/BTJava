package org.jobportal.view.employer;

import org.jobportal.bll.impl.ApplicationService;
import org.jobportal.bll.impl.CVService;
import org.jobportal.bll.impl.RecruitmentService;
import org.jobportal.bll.interfaces.IApplicationService;
import org.jobportal.bll.interfaces.ICVService;
import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.dto.ApplicationDTO;
import org.jobportal.dto.CVDTO;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.model.Education;
import org.jobportal.utils.SessionManager;
import org.jobportal.view.common.HeaderPanel;
import org.jobportal.view.common.SidebarPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApplicationReviewPanel extends JPanel {

    private static class ReviewStats {
        int total;
        int pending;
        int approved;
        int today;
    }

    private final IApplicationService applicationService = new ApplicationService();
    private final IRecruitmentService recruitmentService = new RecruitmentService();
    private final ICVService cvService = new CVService();

    private JPanel tableContainer;
    private JLabel lblCount;
    private JPanel paginationPanel;
    private int currentPage = 1;
    private final int pageSize = 10;

    public ApplicationReviewPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(createStatsRow());
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        mainContent.add(tableContainer);

        loadData();

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private ReviewStats loadReviewStats() {
        ReviewStats s = new ReviewStats();
        String employerId = SessionManager.getInstance().getEmployerId();
        if (employerId == null || employerId.isBlank()) {
            return s;
        }

        s.total = applicationService.getTotalApplicationCount(employerId);
        s.today = applicationService.getNewApplicantsToday(employerId);

        List<RecruitmentDTO> recruitments = recruitmentService.getRecruitmentsByEmployer(employerId);
        if (recruitments != null) {
            for (RecruitmentDTO r : recruitments) {
                List<ApplicationDTO> apps = applicationService.getApplicationsByRecruitmentId(r.getRecruitmentId());
                if (apps == null) continue;
                for (ApplicationDTO a : apps) {
                    if (a.getStatus() == ApplicationStatus.PENDING) s.pending++;
                    else if (a.getStatus() == ApplicationStatus.APPROVED) s.approved++;
                }
            }
        }
        return s;
    }

    private JPanel createStatsRow() {
        ReviewStats stats = loadReviewStats();

        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(new Color(248, 249, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        double approveRate = stats.total > 0 ? (stats.approved * 100.0 / stats.total) : 0.0;
        String approveRateText = String.format("%.1f%%", approveRate);

        panel.add(createStatCard("TỔNG SỐ ỨNG VIÊN", String.valueOf(stats.total), "Mới hôm nay: " + stats.today, new Color(13, 110, 253)));
        panel.add(createStatCard("ĐANG CHỜ DUYỆT", String.valueOf(stats.pending), null, new Color(200, 80, 20)));
        panel.add(createStatCard("ĐÃ DUYỆT", String.valueOf(stats.approved), null, new Color(13, 110, 253)));
        panel.add(createStatCard("TỈ LỆ CHẤP THUẬN", approveRateText, null, Color.BLACK));

        return panel;
    }

    private JPanel createStatCard(String title, String value, String subText, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(valueColor);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblValue);

        if (subText != null) {
            JLabel lblSub = new JLabel(subText);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblSub.setForeground(new Color(13, 110, 253));
            card.add(Box.createRigidArea(new Dimension(0, 5)));
            card.add(lblSub);
        }

        return card;
    }

    private void loadData() {
        tableContainer.removeAll();
        tableContainer.add(createTableRow("ỨNG VIÊN", "NGÀY NỘP", "VỊ TRÍ ỨNG TUYỂN", "TRẠNG THÁI", "THAO TÁC", true, null, null));

        String employerId = SessionManager.getInstance().getEmployerId();
        List<ApplicationDTO> allApps = new ArrayList<>();
        if (employerId != null && !employerId.isEmpty()) {
            List<RecruitmentDTO> recruitments = recruitmentService.getRecruitmentsByEmployer(employerId);
            if (recruitments != null) {
                for (RecruitmentDTO r : recruitments) {
                    List<ApplicationDTO> apps = applicationService.getApplicationsByRecruitmentId(r.getRecruitmentId());
                    if (apps != null) {
                        allApps.addAll(apps);
                    }
                }
            }
        }

        int maxPage = Math.max(1, (int) Math.ceil((double) allApps.size() / pageSize));
        if (currentPage > maxPage) currentPage = maxPage;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, allApps.size());

        for (int i = start; i < end; i++) {
            ApplicationDTO app = allApps.get(i);
            String name = valueOrDefault(app.getCandidateName(), "(Không rõ)");
            String email = valueOrDefault(app.getEmail(), "");
            String dateStr = app.getAppliedDate() != null ? app.getAppliedDate().format(formatter) : "N/A";
            String pos = valueOrDefault(app.getJobTitle(), "");
            String status = toStatusLabel(app.getStatus());
            tableContainer.add(createTableRow(name, dateStr, pos, status, "", false, email, app));
        }

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));

        if (lblCount == null) {
            lblCount = new JLabel();
            lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblCount.setForeground(Color.GRAY);
        }
        lblCount.setText("Tổng cộng: " + allApps.size() + " hồ sơ");
        footer.add(lblCount, BorderLayout.WEST);

        paginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        paginationPanel.setBackground(Color.WHITE);
        updatePaginationUI(allApps.size());
        footer.add(paginationPanel, BorderLayout.EAST);

        tableContainer.add(footer);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    private void updatePaginationUI(int totalItems) {
        if (paginationPanel == null) return;
        paginationPanel.removeAll();

        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / pageSize));

        JButton btnPrev = createPageBtn("<", false);
        if (currentPage > 1) {
            btnPrev.addActionListener(e -> {
                currentPage--;
                loadData();
            });
        } else {
            btnPrev.setEnabled(false);
        }
        paginationPanel.add(btnPrev);

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        if (startPage > 1) {
            JButton btnFirst = createPageBtn("1", false);
            btnFirst.addActionListener(e -> {
                currentPage = 1;
                loadData();
            });
            paginationPanel.add(btnFirst);
            if (startPage > 2) {
                JLabel dots = new JLabel("...");
                dots.setBorder(new EmptyBorder(0, 5, 0, 5));
                paginationPanel.add(dots);
            }
        }

        for (int i = startPage; i <= endPage; i++) {
            final int pageToLoad = i;
            boolean isCurrent = (i == currentPage);
            JButton btnPage = createPageBtn(String.valueOf(i), isCurrent);
            if (!isCurrent) {
                btnPage.addActionListener(e -> {
                    currentPage = pageToLoad;
                    loadData();
                });
            }
            paginationPanel.add(btnPage);
        }

        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                JLabel dots = new JLabel("...");
                dots.setBorder(new EmptyBorder(0, 5, 0, 5));
                paginationPanel.add(dots);
            }
            JButton btnLast = createPageBtn(String.valueOf(totalPages), false);
            btnLast.addActionListener(e -> {
                currentPage = totalPages;
                loadData();
            });
            paginationPanel.add(btnLast);
        }

        JButton btnNext = createPageBtn(">", false);
        if (currentPage < totalPages) {
            btnNext.addActionListener(e -> {
                currentPage++;
                loadData();
            });
        } else {
            btnNext.setEnabled(false);
        }
        paginationPanel.add(btnNext);

        paginationPanel.revalidate();
        paginationPanel.repaint();
    }

    private JPanel createTableRow(String col1, String col2, String col3, String status, String action,
                                  boolean isHeader, String email, ApplicationDTO app) {
        final int rowHeight = isHeader ? 56 : 94;
        final int col1Width = 150; // Ứng viên
        final int col2Width = 100; // Ngày nộp
        final int col3Width = 280; // Vị trí ứng tuyển
        final int col4Width = 90; // Trạng thái
        final int col5Width = 180; // Thao tác

        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? new Color(248, 249, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setPreferredSize(new Dimension(col1Width + col2Width + col3Width + col4Width + col5Width, rowHeight));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1.0;

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.DARK_GRAY;

        // Cột 1: Ứng viên (không avatar)
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 12, 8, 8);
        JPanel p1 = new JPanel(new GridBagLayout());
        setFixedColumnWidth(p1, col1Width, rowHeight);
        p1.setOpaque(false);
        if (isHeader) {
            JLabel l1 = new JLabel(col1, SwingConstants.CENTER);
            l1.setFont(font);
            l1.setForeground(textColor);
            p1.add(l1);
        } else {
            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            info.setOpaque(false);

            JLabel lName = new JLabel(col1);
            lName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lName.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lEmail = new JLabel(email);
            lEmail.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lEmail.setForeground(Color.GRAY);
            lEmail.setAlignmentX(Component.CENTER_ALIGNMENT);

            info.add(lName);
            info.add(Box.createRigidArea(new Dimension(0, 4)));
            info.add(lEmail);
            p1.add(info);
        }
        row.add(p1, gbc);

        // Cột 2: Ngày nộp
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 8, 8, 8);
        JPanel p2 = new JPanel(new GridBagLayout());
        setFixedColumnWidth(p2, col2Width, rowHeight);
        p2.setOpaque(false);
        JLabel l2 = new JLabel(col2, SwingConstants.CENTER);
        l2.setFont(font);
        l2.setForeground(textColor);
        p2.add(l2);
        row.add(p2, gbc);

        // Cột 3: Vị trí hiện tại (center + wrap)
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JPanel p3 = new JPanel(new GridBagLayout());
        setFixedColumnWidth(p3, col3Width, rowHeight);
        p3.setOpaque(false);
        if (isHeader) {
            JLabel l3 = new JLabel(col3, SwingConstants.CENTER);
            l3.setFont(font);
            l3.setForeground(textColor);
            p3.add(l3);
        } else {
            int wrapWidth = Math.max(220, col3Width - 24);
            JLabel wrap = new JLabel("<html><div style='text-align:center; width:" + wrapWidth + "px;'>" + escapeHtml(col3) + "</div></html>", SwingConstants.CENTER);
            wrap.setFont(font);
            wrap.setForeground(textColor);
            p3.add(wrap);
        }
        row.add(p3, gbc);

        // Cột 4: Trạng thái
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        JPanel p4 = new JPanel(new GridBagLayout());
        setFixedColumnWidth(p4, col4Width, rowHeight);
        p4.setOpaque(false);
        if (isHeader) {
            JLabel l4 = new JLabel(status, SwingConstants.CENTER);
            l4.setFont(font);
            l4.setForeground(textColor);
            p4.add(l4);
        } else {
            p4.add(createStatusBadge(status));
        }
        row.add(p4, gbc);

        // Cột 5: Thao tác (text buttons)
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        JPanel p5 = isHeader
                ? new JPanel(new GridBagLayout())
                : new JPanel();
        setFixedColumnWidth(p5, col5Width, rowHeight);
        p5.setOpaque(false);
        if (!isHeader) {
            p5.setLayout(new BoxLayout(p5, BoxLayout.X_AXIS));
        }
        if (isHeader) {
            JLabel l5 = new JLabel(action, SwingConstants.CENTER);
            l5.setFont(font);
            l5.setForeground(textColor);
            p5.add(l5);
        } else {
            boolean canReview = app != null && app.getStatus() == ApplicationStatus.PENDING;

            JLabel linkView = createActionLink("Xem", new Color(13, 110, 253), true, () -> {
                if (app == null) return;
                UserDTO candidateInfo = applicationService.getCandidateInfo(app.getCandidateId());
                if (candidateInfo != null) {
                    showCVDetailDialog(app, candidateInfo);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin ứng viên!");
                }
            });

            JLabel linkApprove = createActionLink("Chấp nhận", new Color(40, 167, 69), canReview, () -> {
                UserDTO current = SessionManager.getInstance().getCurrentUser();
                if (app != null && current != null) {
                    boolean success = applicationService.approveApplication(current.getUserId(), app.getApplicationId());
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Đã chấp nhận hồ sơ!");
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể thao tác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JLabel linkReject = createActionLink("Từ chối", new Color(220, 53, 69), canReview, () -> {
                UserDTO current = SessionManager.getInstance().getCurrentUser();
                if (app != null && current != null) {
                    boolean success = applicationService.rejectApplication(current.getUserId(), app.getApplicationId());
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Đã từ chối hồ sơ!");
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể thao tác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JLabel sep1 = new JLabel("|");
            sep1.setForeground(new Color(180, 180, 180));
            JLabel sep2 = new JLabel("|");
            sep2.setForeground(new Color(180, 180, 180));

            p5.add(Box.createHorizontalGlue());
            p5.add(linkView);
            p5.add(sep1);
            p5.add(linkApprove);
            p5.add(sep2);
            p5.add(linkReject);
            p5.add(Box.createHorizontalGlue());
        }
        row.add(p5, gbc);

        // Cột đệm hấp thụ phần dư để 5 cột chính giữ nguyên bề rộng cố định
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        row.add(spacer, gbc);

        return row;
    }

    private void setFixedColumnWidth(JComponent component, int width, int height) {
        Dimension d = new Dimension(width, height);
        component.setPreferredSize(d);
        component.setMinimumSize(d);
        component.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
    }

    private void showCVDetailDialog(ApplicationDTO app, UserDTO candidateInfo) {
        CVDTO cv = cvService.getCV(app.getCandidateId());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 24, 20, 24));
        content.setBackground(Color.WHITE);

        addSectionTitle(content, "Thông tin cá nhân");
        content.add(createInfoLine("Họ tên", candidateInfo.getFullName()));
        content.add(createInfoLine("Email", candidateInfo.getEmail()));
        content.add(createInfoLine("Điện thoại", candidateInfo.getPhoneNumber()));
        content.add(createInfoLine("Địa điểm", cv != null ? cv.getLocation() : null));
        content.add(createInfoLine("Vị trí mong muốn", cv != null ? cv.getDesiredPosition() : null));

        addSectionTitle(content, "Mục tiêu nghề nghiệp");
        content.add(createTextBlock(cv != null ? cv.getObjective() : null));

        addSectionTitle(content, "Kỹ năng");
        content.add(createTextBlock(cv != null ? cv.getSkills() : null));

        addSectionTitle(content, "Học vấn");
        if (cv != null && cv.getEducations() != null && !cv.getEducations().isEmpty()) {
            for (Education education : cv.getEducations()) {
                content.add(createEducationBlock(education));
            }
        } else {
            content.add(createTextBlock("Chưa có thông tin học vấn."));
        }

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(620, 620));

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Chi tiết CV", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dialog.dispose());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(btnClose);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addSectionTitle(JPanel parent, String title) {
        parent.add(Box.createRigidArea(new Dimension(0, 14)));
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        parent.add(label);
        parent.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private JPanel createInfoLine(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(3, 0, 3, 0));
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel val = new JLabel(valueOrDefault(value, ""));
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }

    private JTextArea createTextBlock(String value) {
        JTextArea text = new JTextArea(valueOrDefault(value, "Chưa cập nhật."));
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        text.setOpaque(false);
        text.setBorder(new EmptyBorder(0, 0, 8, 0));
        return text;
    }

    private JPanel createEducationBlock(Education education) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        panel.add(createInfoLine("Trường", education.getSchool()));
        panel.add(createInfoLine("Bằng cấp", education.getDegree()));
        panel.add(createInfoLine("Chuyên ngành", education.getMajor()));
        String years = "";
        if (education.getStartYear() != null) years += education.getStartYear();
        if (education.getEndYear() != null) years += (years.isEmpty() ? "" : " - ") + education.getEndYear();
        panel.add(createInfoLine("Thời gian", years));
        if (education.getDescription() != null && !education.getDescription().isBlank()) {
            panel.add(createTextBlock(education.getDescription()));
        }
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel("  " + status + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setOpaque(true);

        switch (status) {
            case "Đang duyệt":
                badge.setBackground(new Color(230, 230, 230));
                badge.setForeground(Color.DARK_GRAY);
                break;
            case "Đã duyệt":
                badge.setBackground(new Color(230, 250, 240));
                badge.setForeground(new Color(40, 167, 69));
                break;
            case "Bị từ chối":
                badge.setBackground(new Color(255, 243, 230));
                badge.setForeground(new Color(253, 126, 20));
                break;
            default:
                badge.setBackground(new Color(230, 230, 230));
                badge.setForeground(Color.DARK_GRAY);
                break;
        }
        return badge;
    }

    private String toStatusLabel(ApplicationStatus status) {
        if (status == ApplicationStatus.APPROVED) return "Đã duyệt";
        if (status == ApplicationStatus.REJECTED) return "Bị từ chối";
        return "Đang duyệt";
    }

    private JLabel createActionLink(String text, Color color, boolean enabled, Runnable onClick) {
        JLabel link = new JLabel();
        link.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (enabled) {
            link.setText("<html><u>" + text + "</u></html>");
            link.setForeground(color);
            link.setCursor(new Cursor(Cursor.HAND_CURSOR));
            link.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (onClick != null) onClick.run();
                }
            });
        } else {
            link.setText(text);
            link.setForeground(new Color(170, 170, 170));
            link.setCursor(Cursor.getDefaultCursor());
        }
        return link;
    }

    private JButton createPageBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (active) {
            btn.setBackground(new Color(200, 220, 255));
            btn.setForeground(new Color(13, 110, 253));
            btn.setBorder(new LineBorder(new Color(13, 110, 253), 1));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        }
        return btn;
    }

    private String valueOrDefault(String value, String fallback) {
        return value != null && !value.isBlank() ? value : fallback;
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employer Portal - Duyệt hồ sơ");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 850);
            frame.setLayout(new BorderLayout());

            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            SidebarPanel sidebar = new SidebarPanel(org.jobportal.enums.Role.EMPLOYER);
            frame.add(sidebar, BorderLayout.WEST);

            ApplicationReviewPanel reviewPanel = new ApplicationReviewPanel();
            frame.add(reviewPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
