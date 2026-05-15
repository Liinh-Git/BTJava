package org.jobportal.view.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CvDetailDialog extends JDialog {

    private static final Color BLUE = new Color(13, 110, 253);
    private static final Color BLUE_HOVER = new Color(9, 88, 202);
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_MUTED = new Color(108, 117, 125);
    private static final Color BORDER = new Color(230, 233, 238);

    public CvDetailDialog(Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setSize(720, 780);
        setBackground(new Color(0, 0, 0, 0));

        RoundedPanel root = new RoundedPanel(18);
        root.setBackground(Color.WHITE);
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(18, 24, 20, 24));
        setContentPane(root);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createBody(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    public static void showCvDetail(Component parent) {
        Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        new CvDetailDialog(owner).setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(4, 4, 10, 4));

        JLabel brand = new JLabel("JobPortal");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brand.setForeground(BLUE);

        JButton close = new JButton("X");
        close.setFont(new Font("Segoe UI", Font.BOLD, 16));
        close.setForeground(TEXT_MUTED);
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setFocusPainted(false);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dispose());
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                close.setForeground(BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setForeground(TEXT_MUTED);
            }
        });

        header.add(brand, BorderLayout.WEST);
        header.add(close, BorderLayout.EAST);
        return header;
    }

    private JComponent createBody() {
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JPanel intro = new JPanel();
        intro.setOpaque(false);
        intro.setLayout(new BoxLayout(intro, BoxLayout.Y_AXIS));
        intro.setBorder(new EmptyBorder(0, 4, 12, 4));

        JButton viewCv = createPrimaryButton("Xem chi tiet CV");
        viewCv.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel name = label("Hoang Minh Duc", 20, true, TEXT_DARK);
        JLabel email = label("Email: duc.hoang97@gmail.com", 14, false, TEXT_MUTED);
        JLabel phone = label("Dien thoai: 0978444555", 14, false, TEXT_MUTED);
        JLabel role = label("Vi tri mong muon: Ky su Mang Vien thong / Network Engineer", 14, true, TEXT_DARK);

        intro.add(viewCv);
        intro.add(Box.createRigidArea(new Dimension(0, 12)));
        intro.add(name);
        intro.add(Box.createRigidArea(new Dimension(0, 6)));
        intro.add(email);
        intro.add(Box.createRigidArea(new Dimension(0, 4)));
        intro.add(phone);
        intro.add(Box.createRigidArea(new Dimension(0, 8)));
        intro.add(role);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(8, 4, 4, 4));

        content.add(sectionTitle("Muc tieu nghe nghiep"));
        content.add(sectionText("Ky su Vien thong voi hon 3 nam kinh nghiem van hanh he thong mang di dong LTE. "
                + "Da trien khai thanh cong du an nang cap mang luoi cho hon 200 tram BTS tai mien Bac."));
        content.add(Box.createRigidArea(new Dimension(0, 14)));

        content.add(sectionTitle("Ky nang"));
        content.add(sectionText("Mang LTE/5G, Cisco CCNA, Quan tri Router/Switch, Python scripting, Linux, "
                + "Wireshark, Phan tich luu luong mang"));
        content.add(Box.createRigidArea(new Dimension(0, 16)));

        content.add(sectionTitle("Hoc van"));
        content.add(Box.createRigidArea(new Dimension(0, 8)));
        content.add(educationCard(
                "Hoc vien Cong nghe Buu chinh Vien thong",
                "Bang cap: Ky su",
                "Chuyen nganh: Dien tu Vien thong",
                "Thoi gian: 2016 - 2021",
                "GPA: 3.4/4.0. De tai tot nghiep loai Xuat sac: 'Nghien cuu va mo phong he thong anten Massive MIMO cho 5G'."
        ));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(educationCard(
                "Cao dang Ky thuat Ly Tu Trong, TP.HCM",
                "Bang cap: Cao dang",
                "Chuyen nganh: Dien tu - Vien thong",
                "Thoi gian: 2013 - 2016",
                "Tot nghiep loai Gioi."
        ));

        body.add(intro);
        body.add(content);
        return body;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(16, 4, 4, 4));

        JButton close = createPrimaryButton("Dong");
        close.setFont(new Font("Segoe UI", Font.BOLD, 13));
        close.addActionListener(e -> dispose());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(close);

        footer.add(right, BorderLayout.CENTER);
        return footer;
    }

    private JLabel label(String text, int size, boolean bold, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(BLUE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel sectionText(String text) {
        JLabel label = new JLabel(toHtml(text, 620));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BLUE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 36));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BLUE_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BLUE);
            }
        });
        return button;
    }

    private JPanel educationCard(String school, String degree, String major, String period, String note) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel title = label(school, 14, true, TEXT_DARK);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(label(degree, 13, false, TEXT_MUTED));
        card.add(label(major, 13, false, TEXT_MUTED));
        card.add(label(period, 13, false, TEXT_MUTED));
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(label(note, 13, false, TEXT_DARK));

        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }

    private String toHtml(String text, int width) {
        String safe = text == null ? "" : text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");
        return "<html><div style='width:" + width + "px;'>" + safe + "</div></html>";
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
