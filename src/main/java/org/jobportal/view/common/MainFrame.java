package org.jobportal.view.common;

import org.jobportal.view.admin.AdminDashboardPanel;
import org.jobportal.view.admin.CategoryManagementPanel;
import org.jobportal.view.admin.JobModerationPanel;
import org.jobportal.view.admin.UserManagementPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    private CardLayout rootCardLayout;
    private JPanel rootPanel;
    
    private CardLayout mainCardLayout;
    private JPanel mainContentPanel;

    public MainFrame() {
        setTitle("Job Portal Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        
        rootCardLayout = new CardLayout();
        rootPanel = new JPanel(rootCardLayout);
        
        LoginPanel loginPanel = new LoginPanel(this);
        RegisterPanel registerPanel = new RegisterPanel(this);
        
        JPanel appPanel = new JPanel(new BorderLayout());

        // Header
        HeaderPanel headerPanel = new HeaderPanel();
        appPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        SidebarPanel sidebarPanel = new SidebarPanel(SidebarPanel.Role.ADMIN);
        appPanel.add(sidebarPanel, BorderLayout.WEST);

        // Center content area with CardLayout
        mainCardLayout = new CardLayout();
        mainContentPanel = new JPanel(mainCardLayout);

        // Add admin panels to the CardLayout
        mainContentPanel.add(new AdminDashboardPanel(), "Thống kê hệ thống");
        mainContentPanel.add(new UserManagementPanel(), "Quản lý người dùng");
        mainContentPanel.add(new CategoryManagementPanel(), "Quản lý danh mục");
        mainContentPanel.add(new JobModerationPanel(), "Kiểm duyệt tin tuyển dụng");

        // Set default view
        mainCardLayout.show(mainContentPanel, "Thống kê hệ thống");

        // Handle menu selection from SidebarPanel
        sidebarPanel.setMenuListener(menuTitle -> {
            if ("Đăng xuất".equals(menuTitle)) {
                showLogin();
            } else {
                mainCardLayout.show(mainContentPanel, menuTitle);
            }
        });

        appPanel.add(mainContentPanel, BorderLayout.CENTER);
        
        rootPanel.add(loginPanel, "Login");
        rootPanel.add(registerPanel, "Register");
        rootPanel.add(appPanel, "App");
        
        add(rootPanel);
        rootCardLayout.show(rootPanel, "Login"); // Show login initially
        
        setLocationRelativeTo(null);
    }
    
    public void showApp() {
        rootCardLayout.show(rootPanel, "App");
    }
    
    public void showRegister() {
        rootCardLayout.show(rootPanel, "Register");
    }
    
    public void showLogin() {
        rootCardLayout.show(rootPanel, "Login");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
