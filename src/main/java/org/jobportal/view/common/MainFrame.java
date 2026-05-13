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
    
    private CardLayout rootCardLayout;
    private JPanel rootPanel;
    
    private CardLayout mainCardLayout;
    private JPanel mainContentPanel;
    
    private JPanel appPanel;
    private HeaderPanel headerPanel;
    private SidebarPanel sidebarPanel;

    public MainFrame() {
        setTitle("Ứng dụng JobPortal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        
        rootCardLayout = new CardLayout();
        rootPanel = new JPanel(rootCardLayout);
        
        LoginPanel loginPanel = new LoginPanel(this);
        RegisterPanel registerPanel = new RegisterPanel(this);
        
        rootPanel.add(loginPanel, "Login");
        rootPanel.add(registerPanel, "Register");
        
        add(rootPanel);
        rootCardLayout.show(rootPanel, "Login"); // Show login initially
        
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
            mainContentPanel.add(new UserProfilePanel(), "Thông tin người dùng");
            JPanel settingsPanel = new JPanel(new BorderLayout());
            JLabel lblSettings = new JLabel("Chức năng Cài đặt đang được phát triển", SwingConstants.CENTER);
            lblSettings.setFont(new Font("Segoe UI", Font.BOLD, 24));
            settingsPanel.add(lblSettings, BorderLayout.CENTER);
            mainContentPanel.add(settingsPanel, "Cài đặt");

            if (!firstMenu.isEmpty()) {
                mainCardLayout.show(mainContentPanel, firstMenu);
            }

            // Handle menu selection from SidebarPanel
            sidebarPanel.setMenuListener(menuTitle -> {
                if ("Đăng xuất".equals(menuTitle)) {
                    SessionManager.getInstance().logout();
                    showLogin();
                } else {
                    mainCardLayout.show(mainContentPanel, menuTitle);
                }
            });

            appPanel.add(mainContentPanel, BorderLayout.CENTER);
            
            rootPanel.add(appPanel, "App");
            rootPanel.revalidate();
            rootPanel.repaint();
            rootCardLayout.show(rootPanel, "App");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi nạp giao diện: " + e.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void showRegister() {
        rootCardLayout.show(rootPanel, "Register");
    }
    
    public void showLogin() {
        rootCardLayout.show(rootPanel, "Login");
    }
    
    public void navigateToMenu(String menuTitle) {
        if (mainContentPanel != null && mainCardLayout != null) {
            mainCardLayout.show(mainContentPanel, menuTitle);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
