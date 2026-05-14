package org.jobportal.view.common;

import org.jobportal.dto.UserDTO;
import org.jobportal.utils.SessionManager;
import org.jobportal.view.common.NotificationPanel;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    public HeaderPanel() {
        // thiet lap layout chinh va mau nen cho header
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // dat chieu cao co dinh cho header la 40px
        setPreferredSize(new Dimension(0, 40));

        // tao mot duong ke mong mau xam o vien duoi de phan cach voi noi dung ben duoi
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // phan ben trai: ten he thong (JobPortal)
        JLabel lblLogo = new JLabel("JobPortal");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(new Color(30, 35, 41)); // mau chu xam den
        lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0)); // canh le trai 25px
        add(lblLogo, BorderLayout.WEST);

        // phan ben phai: chua cac nut thong bao, cai dat, va tai khoan
        // su dung FlowLayout canh phai de cac element tu dong day sang phai
        JPanel rightActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightActionPanel.setBackground(Color.WHITE);

        // tao cac element tam thoi bang text hoac icon
        JButton btnNotification = createFlatButton("");
        try {
            java.net.URL notiUrl = getClass().getResource("/images/noti/noti.jpg");
            if (notiUrl != null) {
                ImageIcon icon = new ImageIcon(notiUrl);
                Image img = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
                btnNotification.setIcon(new ImageIcon(img));
                btnNotification.setPreferredSize(new Dimension(30, 30));
            } else {
                btnNotification.setText("Thong bao");
            }
        } catch (Exception ex) {
            btnNotification.setText("Thong bao");
        }

        JButton btnSettings = createFlatButton("");
        try {
            java.net.URL settingUrl = getClass().getResource("/images/setting/3524659.png");
            if (settingUrl != null) {
                ImageIcon icon = new ImageIcon(settingUrl);
                Image img = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
                btnSettings.setIcon(new ImageIcon(img));
                btnSettings.setPreferredSize(new Dimension(30, 30));
            } else {
                btnSettings.setText("Cai dat");
            }
        } catch (Exception ex) {
            btnSettings.setText("Cai dat");
        }

        JButton btnUser = createFlatButton("");
        try {
            // Su dung mock data avt tu thu muc images
            java.net.URL avtUrl = getClass().getResource("/images/avt/6858504.png");
            if (avtUrl != null) {
                ImageIcon icon = new ImageIcon(avtUrl);
                // Resize anh cho vua voi top bar (HeaderPanel co chieu cao 40px)
                Image img = icon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
                btnUser.setIcon(new ImageIcon(img));
                btnUser.setPreferredSize(new Dimension(30, 30));
            } else {
                UserDTO currentUser = SessionManager.getInstance().getCurrentUser();
                btnUser.setText((currentUser != null) ? currentUser.getUsername() : "Tài khoản");
            }
        } catch (Exception ex) {
            UserDTO currentUser = SessionManager.getInstance().getCurrentUser();
            btnUser.setText((currentUser != null) ? currentUser.getUsername() : "Tài khoản");
        }

        // them cac element vao panel ben phai
        rightActionPanel.add(btnNotification);
        rightActionPanel.add(btnSettings);
        rightActionPanel.add(btnUser);

        add(rightActionPanel, BorderLayout.EAST);

        btnNotification.addActionListener(e -> openNotificationDialog());

        btnSettings.addActionListener(e -> {
            Window ancestor = SwingUtilities.getWindowAncestor(this);
            if (ancestor instanceof MainFrame) {
                ((MainFrame) ancestor).navigateToMenu("Cài đặt");
            }
        });

        btnUser.addActionListener(e -> {
            Window ancestor = SwingUtilities.getWindowAncestor(this);
            if (ancestor instanceof MainFrame) {
                ((MainFrame) ancestor).navigateToMenu("Thông tin người dùng");
            }
        });
    }

    private void openNotificationDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Thông báo", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(960, 640);
        dialog.setLocationRelativeTo(owner);
        dialog.setLayout(new BorderLayout());
        dialog.add(new NotificationPanel(), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // ham ho tro tao nut bam trong suot, khong vien de giong voi cac icon tren web
    private JButton createFlatButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(80, 80, 80));

        // bo cac hieu ung ve vien mac dinh cua JButton
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);

        // doi con tro chuot thanh hinh ban tay khi chi vao
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // them hieu ung hover (doi mau chu khi re chuot)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(0, 82, 204)); // mau xanh khi hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(80, 80, 80)); // tra lai mau cu
            }
        });

        return btn;
    }

    // ham main de kiem tra giao dien doc lap
    public static void main(String[] args) {
        // dam bao chay UI tren Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Kiem tra Header Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600); // kich thuoc frame test
            frame.setLayout(new BorderLayout());

            // khoi tao va them header vao phia tren cung (NORTH)
            HeaderPanel header = new HeaderPanel();
            frame.add(header, BorderLayout.NORTH);

            // panel chua noi dung gia lap o duoi de thay ro phan cach
            JPanel contentPanel = new JPanel();
            contentPanel.setBackground(new Color(248, 249, 250));
            frame.add(contentPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null); // giua man hinh
            frame.setVisible(true);
        });
    }
}
