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

        // tao cac element tam thoi bang text
        JButton btnNotification = createFlatButton("Thong bao");
        JButton btnSettings = createFlatButton("Cai dat");
        UserDTO currentUser = SessionManager.getInstance().getCurrentUser();
        String usernameText = (currentUser != null) ? currentUser.getUsername() : "Tài khoản";
        JButton btnUser = createFlatButton(usernameText);

        // them cac element vao panel ben phai
        rightActionPanel.add(btnNotification);
        rightActionPanel.add(btnSettings);
        rightActionPanel.add(btnUser);

        add(rightActionPanel, BorderLayout.EAST);

        btnNotification.addActionListener(e -> openNotificationDialog());
    }

    private void openNotificationDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Thong bao", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(520, 600);
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