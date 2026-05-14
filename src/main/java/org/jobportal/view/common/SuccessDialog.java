package org.jobportal.view.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SuccessDialog extends JDialog {

    private static final Color BLUE = new Color(13, 110, 253);
    private static final Color BLUE_HOVER = new Color(9, 88, 202);
    private static final Color TEXT_DARK = new Color(51, 51, 51);
    private static final Color SUCCESS_GREEN = new Color(75, 192, 192);
    private static final Color ERROR_RED = new Color(220, 53, 69);

    public SuccessDialog(Window owner, String message) {
        this(owner, "Success!", message, false);
    }

    public SuccessDialog(Window owner, String title, String message, boolean error) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setSize(400, 270);
        setBackground(Color.WHITE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(18, 24, 22, 24)
        ));
        setContentPane(root);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createBody(title, message, error), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    public static void showSuccess(Component parent, String message) {
        Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        new SuccessDialog(owner, message).setVisible(true);
    }

    public static void showError(Component parent, String message) {
        Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        new SuccessDialog(owner, "Failed!", message, true).setVisible(true);
    }

    public static void showMessageDialog(Component parent, Object message) {
        showMessageDialog(parent, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showMessageDialog(Component parent, Object message, String title, int messageType) {
        String text = String.valueOf(message);
        boolean error = messageType == JOptionPane.ERROR_MESSAGE
                || messageType == JOptionPane.WARNING_MESSAGE
                || looksUnsuccessful(text);
        Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        new SuccessDialog(owner, error ? "Failed!" : "Success!", text, error).setVisible(true);
    }

    private static boolean looksUnsuccessful(String message) {
        if (message == null) return false;
        String value = message.toLowerCase();
        return value.contains("không")
                || value.contains("kh\u00f4ng")
                || value.contains("lỗi")
                || value.contains("l\u1ed7i")
                || value.contains("sai")
                || value.contains("vui lòng")
                || value.contains("vui l\u00f2ng")
                || value.contains("bạn cần")
                || value.contains("b\u1ea1n c\u1ea7n")
                || value.contains("cảnh báo")
                || value.contains("c\u1ea3nh b\u00e1o");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel brand = new JLabel("JobPortal");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brand.setForeground(BLUE);
        header.add(brand);

        return header;
    }

    private JPanel createBody(String titleText, String message, boolean error) {
        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(2, 0, 14, 0));

        StatusIconPanel icon = new StatusIconPanel(error);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(error ? ERROR_RED : BLUE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel content = new JLabel(toHtmlMessage(message));
        content.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        content.setForeground(TEXT_DARK);
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        body.add(Box.createVerticalGlue());
        body.add(icon);
        body.add(Box.createRigidArea(new Dimension(0, 12)));
        body.add(title);
        body.add(Box.createRigidArea(new Dimension(0, 8)));
        body.add(content);
        body.add(Box.createVerticalGlue());
        return body;
    }

    private String toHtmlMessage(String message) {
        String safe = message == null ? "" : message
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");
        return "<html><div style='text-align:center; width:320px;'>" + safe + "</div></html>";
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setBackground(Color.WHITE);

        JButton ok = new JButton("OK");
        ok.setPreferredSize(new Dimension(110, 36));
        ok.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ok.setForeground(Color.WHITE);
        ok.setBackground(BLUE);
        ok.setBorderPainted(false);
        ok.setFocusPainted(false);
        ok.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ok.addActionListener(e -> dispose());
        ok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ok.setBackground(BLUE_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ok.setBackground(BLUE);
            }
        });

        footer.add(ok);
        return footer;
    }

    private static class StatusIconPanel extends JPanel {
        private final boolean error;

        StatusIconPanel(boolean error) {
            this.error = error;
            setPreferredSize(new Dimension(58, 58));
            setMaximumSize(new Dimension(58, 58));
            setMinimumSize(new Dimension(58, 58));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = 54;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            g2.setColor(error ? ERROR_RED : SUCCESS_GREEN);
            g2.fillOval(x, y, size, size);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            if (error) {
                g2.drawLine(x + 18, y + 18, x + 36, y + 36);
                g2.drawLine(x + 36, y + 18, x + 18, y + 36);
            } else {
                g2.drawLine(x + 15, y + 29, x + 25, y + 39);
                g2.drawLine(x + 25, y + 39, x + 40, y + 18);
            }

            g2.dispose();
        }
    }
}
