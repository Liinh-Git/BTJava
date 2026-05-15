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
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(isHeader ? new Color(250, 250, 250) : Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        int rowHeight = isHeader ? 56 : 96;
        row.setPreferredSize(new Dimension(0, rowHeight));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));

        Font font = new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13);
        Color textColor = isHeader ? Color.GRAY : Color.DARK_GRAY;

        JPanel applicantCell = createReviewBaseCell(FlowLayout.LEFT, isHeader);
        if (isHeader) {
            JLabel label = new JLabel(col1);
            label.setFont(font);
            label.setForeground(textColor);
            applicantCell.add(label);
        } else {
            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            info.setOpaque(false);
            JLabel name = new JLabel(col1);
            name.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JLabel mail = new JLabel(valueOrDefault(email, ""));
            mail.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            mail.setForeground(Color.GRAY);
            info.add(name);
            info.add(Box.createRigidArea(new Dimension(0, 4)));
            info.add(mail);
            applicantCell.add(info);
        }
        addReviewCell(row, applicantCell, 0, 0.25);

        addReviewCell(row, createReviewTextCell(col2, font, textColor, FlowLayout.CENTER, isHeader), 1, 0.12);
        addReviewCell(row, createReviewTextCell(col3, font, textColor, FlowLayout.LEFT, isHeader), 2, 0.25);

        JPanel statusCell = createReviewBaseCell(FlowLayout.CENTER, isHeader);
        if (isHeader) {
            JLabel label = new JLabel(status);
            label.setFont(font);
            label.setForeground(textColor);
            statusCell.add(label);
        } else {
            statusCell.add(createStatusBadge(status));
        }
        addReviewCell(row, statusCell, 3, 0.14);

        JPanel actionCell = isHeader ? createReviewBaseCell(FlowLayout.CENTER, true) : createTwoRowActionPanel();
        if (isHeader) {
            JLabel label = new JLabel(action);
            label.setFont(font);
            label.setForeground(textColor);
            actionCell.add(label);
        } else {
            boolean canReview = app != null && app.getStatus() == ApplicationStatus.PENDING;

            JButton linkView = createReviewActionButton("Xem", new Color(13, 110, 253), true, () -> {
                if (app == null) return;
                UserDTO candidateInfo = applicationService.getCandidateInfo(app.getCandidateId());
                if (candidateInfo != null) {
                    showCVDetailDialog(app, candidateInfo);
                } else {
                    org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Kh\u00f4ng t\u00ecm th\u1ea5y th\u00f4ng tin \u1ee9ng vi\u00ean!");
                }
            });
            JButton linkApprove = createReviewActionButton("Chấp nhận", new Color(40, 167, 69), canReview, () -> {
                UserDTO current = SessionManager.getInstance().getCurrentUser();
                if (app != null && current != null) {
                    boolean success = applicationService.approveApplication(current.getUserId(), app.getApplicationId());
                    if (success) {
                        org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "\u0110\u00e3 ch\u1ea5p nh\u1eadn h\u1ed3 s\u01a1!");
                        loadData();
                    } else {
                        org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Kh\u00f4ng th\u1ec3 thao t\u00e1c!", "L\u1ed7i", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            JButton linkReject = createReviewActionButton("Từ chối", new Color(220, 53, 69), canReview, () -> {
                UserDTO current = SessionManager.getInstance().getCurrentUser();
                if (app != null && current != null) {
                    boolean success = applicationService.rejectApplication(current.getUserId(), app.getApplicationId());
                    if (success) {
                        org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "\u0110\u00e3 t\u1eeb ch\u1ed1i h\u1ed3 s\u01a1!");
                        loadData();
                    } else {
                        org.jobportal.view.common.ModernDialogUtils.showMessageDialog(this, "Kh\u00f4ng th\u1ec3 thao t\u00e1c!", "L\u1ed7i", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            addTwoRowButtons(actionCell, linkView, linkApprove, linkReject);
        }
        addReviewCell(row, actionCell, 4, 0.24);
        return row;
    }

    private void addReviewCell(JPanel row, JComponent cell, int column, double weight) {
        cell.setMinimumSize(new Dimension(0, 0));
        cell.setPreferredSize(new Dimension(0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = 0;
        gbc.weightx = weight;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        row.add(cell, gbc);
    }

    private JPanel createReviewBaseCell(int alignment, boolean header) {
        JPanel panel = new JPanel(new FlowLayout(alignment, alignment == FlowLayout.CENTER ? 4 : 20, header ? 16 : 18));
        panel.setOpaque(false);
        return panel;
    }

    private JPanel createTwoRowActionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        return panel;
    }

    private void addTwoRowButtons(JPanel panel, JButton topButton, JButton leftButton, JButton rightButton) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 4, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(topButton, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 8, 3);
        panel.add(leftButton, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 3, 8, 0);
        panel.add(rightButton, gbc);
    }

    private JPanel createReviewTextCell(String text, Font font, Color color, int alignment, boolean header) {
        JPanel panel = createReviewBaseCell(alignment, header);
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label);
        return panel;
    }

    private void setFixedColumnWidth(JComponent component, int width, int height) {
        Dimension d = new Dimension(width, height);
        component.setPreferredSize(d);
        component.setMinimumSize(d);
        component.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
    }

    private void showCVDetailDialog(ApplicationDTO app, UserDTO candidateInfo) {
        CVDTO cv = cvService.getCV(app.getCandidateId());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(20, 24, 20, 24)
        ));

        JLabel lblHeader = new JLabel("CHI TIẾT CV ỨNG VIÊN");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblHeader);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        addSectionTitle(card, "Thông tin cá nhân");
        card.add(createInfoLine("Họ tên", candidateInfo.getFullName()));
        card.add(createInfoLine("Email", candidateInfo.getEmail()));
        card.add(createInfoLine("Điện thoại", candidateInfo.getPhoneNumber()));
        card.add(createInfoLine("Địa điểm", cv != null ? cv.getLocation() : null));
        card.add(createInfoLine("Vị trí mong muốn", cv != null ? cv.getDesiredPosition() : null));

        addSectionTitle(card, "Mục tiêu nghề nghiệp");
        card.add(createTextBlock(cv != null ? cv.getObjective() : null));

        addSectionTitle(card, "Kỹ năng");
        card.add(createTextBlock(cv != null ? cv.getSkills() : null));

        addSectionTitle(card, "Học vấn");
        if (cv != null && cv.getEducations() != null && !cv.getEducations().isEmpty()) {
            for (Education education : cv.getEducations()) {
                card.add(createEducationBlock(education));
                card.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        } else {
            card.add(createTextBlock("Chưa có thông tin học vấn."));
        }

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(248, 249, 250));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));
        content.add(card);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(248, 249, 250));

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Chi tiết CV", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(248, 249, 250));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClose.setBackground(new Color(13, 110, 253));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setPreferredSize(new Dimension(100, 34));
        btnClose.addActionListener(e -> dialog.dispose());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        footer.add(btnClose);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.setSize(860, 700);
        dialog.setMinimumSize(new Dimension(760, 620));
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
        JLabel badge = new JLabel(status, SwingConstants.CENTER);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setOpaque(true);
        badge.setPreferredSize(new Dimension(94, 22));

        if (status.equals(toStatusLabel(ApplicationStatus.APPROVED))) {
            badge.setBackground(new Color(230, 250, 240));
            badge.setForeground(new Color(40, 167, 69));
        } else if (status.equals(toStatusLabel(ApplicationStatus.REJECTED))) {
            badge.setBackground(new Color(250, 230, 230));
            badge.setForeground(new Color(220, 53, 69));
        } else {
            badge.setBackground(new Color(255, 243, 205));
            badge.setForeground(new Color(133, 100, 4));
        }

        badge.setBorder(new LineBorder(badge.getForeground(), 1, false));
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

    private JButton createReviewActionButton(String text, Color color, boolean enabled, Runnable onClick) {
        JButton button = new JButton(text);
        int width = "Chấp nhận".equals(text) ? 92 : ("Từ chối".equals(text) ? 78 : 78);
        button.setPreferredSize(new Dimension(width, 30));
        button.setMinimumSize(new Dimension(width, 30));
        button.setMaximumSize(new Dimension(width, 30));
        button.setMargin(new Insets(0, 4, 0, 4));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(enabled ? color : new Color(185, 185, 185));
        button.setCursor(enabled ? new Cursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
        button.setEnabled(enabled);
        if (enabled && onClick != null) {
            button.addActionListener(e -> onClick.run());
        }
        return button;
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
}
