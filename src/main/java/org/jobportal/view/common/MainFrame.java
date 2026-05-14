package org.jobportal.view.common;

import org.jobportal.view.admin.AdminDashboardPanel;
import org.jobportal.view.admin.CategoryManagementPanel;
import org.jobportal.view.admin.JobModerationPanel;
import org.jobportal.view.admin.UserManagementPanel;
import org.jobportal.view.candidate.AppliedJobsPanel;
import org.jobportal.view.candidate.CVEditorPanel;
import org.jobportal.view.candidate.JobSearchPanel;
import org.jobportal.view.employer.ApplicationReviewPanel;
import org.jobportal.view.employer.CompanyInfoPanel;
import org.jobportal.view.employer.EmployerDashboardPanel;
import org.jobportal.view.employer.RecruitmentFormPanel;
import org.jobportal.view.employer.RecruitmentListPanel;
import org.jobportal.view.common.UserProfilePanel;
import org.jobportal.enums.Role;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String CARD_USER_PROFILE = "CARD_USER_PROFILE";
    private static final String CARD_SETTINGS = "CARD_SETTINGS";
    
    private CardLayout rootCardLayout;
    private JPanel rootPanel;
    
    private CardLayout mainCardLayout;
    private JPanel mainContentPanel;
    
    private JPanel appPanel;
    private HeaderPanel headerPanel;
    private SidebarPanel sidebarPanel;

    public MainFrame() {
        setTitle("Hệ thống Tìm kiếm Việc làm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1200, 850);
        
        rootCardLayout = new CardLayout();
        rootPanel = new JPanel(rootCardLayout);
        
        LoginPanel loginPanel = new LoginPanel(this);
        RegisterPanel registerPanel = new RegisterPanel(this);
        
        rootPanel.add(loginPanel, "Đăng nhập");
        rootPanel.add(registerPanel, "Đăng ký");
        
        add(rootPanel);
        rootCardLayout.show(rootPanel, "Đăng nhập"); // Show login initially
        
        setLocationRelativeTo(null);
    }
    
    public void onLoginSuccess() {
        try {
            Role currentRole = SessionManager.getInstance().getCurrentRole();
            
            if (appPanel != null) {
                rootPanel.remove(appPanel);
            }

            appPanel = new JPanel(new BorderLayout());

            // Header
            headerPanel = new HeaderPanel();
            appPanel.add(headerPanel, BorderLayout.NORTH);

            // Sidebar
            sidebarPanel = new SidebarPanel(currentRole);
            appPanel.add(sidebarPanel, BorderLayout.WEST);

            // Center content area with CardLayout
            mainCardLayout = new CardLayout();
            mainContentPanel = new JPanel(mainCardLayout);

            String firstMenu = "";

            if (currentRole == Role.ADMIN) {
                mainContentPanel.add(new AdminDashboardPanel(), "Thống kê hệ thống");
                mainContentPanel.add(new UserManagementPanel(), "Quản lý người dùng");
                mainContentPanel.add(new CategoryManagementPanel(), "Quản lý danh mục");
                mainContentPanel.add(new JobModerationPanel(), "Kiểm duyệt tin tuyển dụng");
                firstMenu = "Thống kê hệ thống";
            } else if (currentRole == Role.EMPLOYER) {
                mainContentPanel.add(new EmployerDashboardPanel(), "Tổng quan");
                mainContentPanel.add(new RecruitmentFormPanel(), "Đăng tin tuyển dụng");
                mainContentPanel.add(new RecruitmentListPanel(), "Quản lý tin tuyển dụng");
                mainContentPanel.add(new ApplicationReviewPanel(), "Danh sách các ứng viên");
                mainContentPanel.add(new CompanyInfoPanel(), "Thông tin công ty");
                firstMenu = "Tổng quan";
            } else if (currentRole == Role.CANDIDATE) {
                mainContentPanel.add(new JobSearchPanel(), "Tìm việc");
                mainContentPanel.add(new AppliedJobsPanel(), "Đã ứng tuyển");
                mainContentPanel.add(new CVEditorPanel(), "Quản lý CV");
                firstMenu = "Tìm việc";
            }
            
            // Common panels
            mainContentPanel.add(new UserProfilePanel(), CARD_USER_PROFILE);
            JPanel settingsPanel = new JPanel(new BorderLayout());
            JLabel lblSettings = new JLabel("Chức năng Cài đặt đang được phát triển", SwingConstants.CENTER);
            lblSettings.setFont(new Font("Segoe UI", Font.BOLD, 24));
            settingsPanel.add(lblSettings, BorderLayout.CENTER);
            mainContentPanel.add(settingsPanel, CARD_SETTINGS);

            if (!firstMenu.isEmpty()) {
                mainCardLayout.show(mainContentPanel, firstMenu);
                sidebarPanel.setActiveMenu(firstMenu);
            }

            // Handle menu selection from SidebarPanel
            sidebarPanel.setMenuListener(menuTitle -> {
                if ("Đăng xuất".equals(menuTitle)) {
                    SessionManager.getInstance().logout();
                    showLogin();
                } else {
                    mainCardLayout.show(mainContentPanel, resolveCardKey(menuTitle));
                    sidebarPanel.setActiveMenu(menuTitle);
                }
            });

            appPanel.add(mainContentPanel, BorderLayout.CENTER);
            
            rootPanel.add(appPanel, "App");
            rootPanel.revalidate();
            rootPanel.repaint();
            rootCardLayout.show(rootPanel, "App");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải giao diện: " + e.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void showRegister() {
        rootCardLayout.show(rootPanel, "Đăng ký");
    }
    
    public void showLogin() {
        rootCardLayout.show(rootPanel, "Đăng nhập");
    }
    
    public void navigateToMenu(String menuTitle) {
        if (mainContentPanel != null && mainCardLayout != null) {
            mainCardLayout.show(mainContentPanel, resolveCardKey(menuTitle));
            if (sidebarPanel != null) {
                sidebarPanel.setActiveMenu(menuTitle);
            }
        }
    }

    private String resolveCardKey(String menuTitle) {
        if (menuTitle == null) return "";
        String normalized = menuTitle.trim();
        if ("Thông tin người dùng".equalsIgnoreCase(normalized) || "Thong tin nguoi dung".equalsIgnoreCase(normalized)) {
            return CARD_USER_PROFILE;
        }
        if ("Cài đặt".equalsIgnoreCase(normalized) || "Cai dat".equalsIgnoreCase(normalized)) {
            return CARD_SETTINGS;
        }
        return normalized;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}


