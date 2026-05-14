package org.jobportal.view.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class ModernDialogUtils {
    private static final String APP_TITLE = "JobPortal";
    private static final Color BLUE = new Color(13, 110, 253);
    private static final Color BLUE_HOVER = new Color(9, 88, 202);
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color SUCCESS_COLOR = new Color(75, 192, 192);
    private static final Color ERROR_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color INFO_COLOR = new Color(13, 110, 253);
    private static final Dimension DIALOG_SIZE = new Dimension(400, 272);

    private ModernDialogUtils() {
    }

    public static void showSuccess(Component parent, String message) {
        show(parent, "Success!", message, DialogType.SUCCESS);
    }

    public static void showError(Component parent, String message) {
        show(parent, "Error!", message, DialogType.ERROR);
    }

    public static void showWarning(Component parent, String message) {
        show(parent, "Warning!", message, DialogType.WARNING);
    }

    public static void showInfo(Component parent, String message) {
        show(parent, "Info", message, DialogType.INFO);
    }

    public static boolean showConfirm(Component parent, String message) {
        return showConfirm(parent, "Confirm", message);
    }

    public static boolean showConfirm(Component parent, String title, String message) {
        Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        JDialog dialog = new JDialog(owner, APP_TITLE, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final boolean[] confirmed = {false};

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(20, 22, 22, 22)
        ));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createBody(title, message, DialogType.CONFIRM), BorderLayout.CENTER);
        root.add(createConfirmFooter(dialog, confirmed), BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setSize(DIALOG_SIZE);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return confirmed[0];
    }

    public static String showInputDialog(Component parent, String title, String label, String initialValue) {
        Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        JDialog dialog = new JDialog(owner, APP_TITLE, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final String[] result = {null};

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(20, 22, 22, 22)
        ));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createInputBody(title, label, initialValue), BorderLayout.CENTER);
        root.add(createInputFooter(dialog, result, root), BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setSize(new Dimension(430, 260));
        dialog.setLocationRelativeTo(parent);

        JTextField input = findInputField(root);
        dialog.getRootPane().setDefaultButton((JButton) root.getClientProperty("okButton"));
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                if (input != null) {
                    input.requestFocusInWindow();
                    input.selectAll();
                }
            }
        });

        dialog.setVisible(true);
        return result[0];
    }

    public static void showMessageDialog(Component parent, Object message) {
        showMessageDialog(parent, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showMessageDialog(Component parent, Object message, String title, int messageType) {
        String text = String.valueOf(message);
        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE -> show(parent, "Error!", text, DialogType.ERROR);
            case JOptionPane.WARNING_MESSAGE -> show(parent, "Warning!", text, DialogType.WARNING);
            case JOptionPane.INFORMATION_MESSAGE -> {
                String statusTitle = isSuccessTitle(title) ? "Success!" : "Info";
                DialogType type = isSuccessTitle(title) ? DialogType.SUCCESS : DialogType.INFO;
                show(parent, statusTitle, text, type);
            }
            default -> show(parent, isSuccessTitle(title) ? "Success!" : "Info", text,
                    isSuccessTitle(title) ? DialogType.SUCCESS : DialogType.INFO);
        }
    }

    private static boolean isSuccessTitle(String title) {
        if (title == null) return false;
        String value = title.toLowerCase();
        return value.contains("success") || value.contains("thành công") || value.contains("thanh cong");
    }

    private static void show(Component parent, String statusTitle, String message, DialogType type) {
        Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        JDialog dialog = new JDialog(owner, APP_TITLE, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(20, 22, 22, 22)
        ));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createBody(statusTitle, message, type), BorderLayout.CENTER);
        root.add(createFooter(dialog), BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setSize(DIALOG_SIZE);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static JComponent createHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        header.setBackground(Color.WHITE);

        JLabel brand = new JLabel(APP_TITLE);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brand.setForeground(BLUE);
        header.add(brand);

        return header;
    }

    private static JComponent createBody(String statusTitle, String message, DialogType type) {
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(14, 0, 18, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        StatusIcon icon = new StatusIcon(type);

        JLabel title = new JLabel(statusTitle);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(type == DialogType.ERROR ? ERROR_COLOR : BLUE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel content = new JLabel(toHtmlMessage(message));
        content.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        content.setForeground(TEXT_DARK);
        content.setHorizontalAlignment(SwingConstants.CENTER);
        content.setVerticalAlignment(SwingConstants.CENTER);
        content.setMinimumSize(new Dimension(0, 24));

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 14, 0);
        body.add(icon, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        body.add(title, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        body.add(content, gbc);
        return body;
    }

    private static JComponent createFooter(JDialog dialog) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setBackground(Color.WHITE);

        JButton ok = new JButton("OK");
        ok.setPreferredSize(new Dimension(120, 40));
        ok.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ok.setForeground(Color.WHITE);
        ok.setBackground(BLUE);
        ok.setBorderPainted(false);
        ok.setFocusPainted(false);
        ok.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ok.addActionListener(e -> dialog.dispose());
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

    private static JComponent createInputBody(String titleText, String labelText, String initialValue) {
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(18, 0, 18, 0));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(BLUE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);

        JTextField input = new JTextField(initialValue != null ? initialValue : "");
        input.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        input.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(7, 10, 7, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 16, 0);
        body.add(title, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 8, 0);
        body.add(label, gbc);

        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(input, gbc);

        body.putClientProperty("inputField", input);
        return body;
    }

    private static JComponent createInputFooter(JDialog dialog, String[] result, JPanel root) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        footer.setBackground(Color.WHITE);

        JButton ok = createFlatButton("OK", BLUE, BLUE_HOVER);
        JButton cancel = createFlatButton("Hủy", new Color(108, 117, 125), new Color(90, 98, 104));
        ok.addActionListener(e -> {
            JTextField input = findInputField(root);
            result[0] = input != null ? input.getText() : "";
            dialog.dispose();
        });
        cancel.addActionListener(e -> dialog.dispose());

        root.putClientProperty("okButton", ok);
        footer.add(ok);
        footer.add(cancel);
        return footer;
    }

    private static JTextField findInputField(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField textField) {
                return textField;
            }
            if (component instanceof Container child) {
                JTextField found = findInputField(child);
                if (found != null) return found;
            }
        }
        return null;
    }

    private static JComponent createConfirmFooter(JDialog dialog, boolean[] confirmed) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        footer.setBackground(Color.WHITE);

        JButton yes = createFlatButton("Yes", BLUE, BLUE_HOVER);
        yes.addActionListener(e -> {
            confirmed[0] = true;
            dialog.dispose();
        });

        JButton no = createFlatButton("No", new Color(108, 117, 125), new Color(90, 98, 104));
        no.addActionListener(e -> dialog.dispose());

        footer.add(yes);
        footer.add(no);
        return footer;
    }

    private static JButton createFlatButton(String text, Color normal, Color hover) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(104, 38));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(normal);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
            }
        });
        return button;
    }

    private static String toHtmlMessage(String message) {
        String safe = message == null ? "" : message
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");
        return "<html><div style='text-align:center; width:100%;'>" + safe + "</div></html>";
    }

    private enum DialogType {
        SUCCESS, ERROR, WARNING, INFO, CONFIRM
    }

    private static final class StatusIcon extends JPanel {
        private final DialogType type;

        private StatusIcon(DialogType type) {
            this.type = type;
            setOpaque(false);
            setPreferredSize(new Dimension(58, 58));
            setMaximumSize(new Dimension(58, 58));
            setMinimumSize(new Dimension(58, 58));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = 54;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            Color color = switch (type) {
                case SUCCESS -> SUCCESS_COLOR;
                case ERROR -> ERROR_COLOR;
                case WARNING -> WARNING_COLOR;
                case INFO, CONFIRM -> INFO_COLOR;
            };

            g2.setColor(color);
            g2.fillOval(x, y, size, size);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            switch (type) {
                case SUCCESS -> {
                    g2.drawLine(x + 15, y + 30, x + 25, y + 40);
                    g2.drawLine(x + 25, y + 40, x + 40, y + 19);
                }
                case ERROR -> {
                    g2.drawLine(x + 18, y + 18, x + 36, y + 36);
                    g2.drawLine(x + 36, y + 18, x + 18, y + 36);
                }
                case WARNING -> {
                    g2.drawLine(x + 27, y + 14, x + 27, y + 34);
                    g2.fillOval(x + 24, y + 41, 6, 6);
                }
                case INFO -> {
                    g2.fillOval(x + 24, y + 14, 6, 6);
                    g2.drawLine(x + 27, y + 27, x + 27, y + 42);
                }
                case CONFIRM -> {
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 34));
                    FontMetrics fm = g2.getFontMetrics();
                    String mark = "?";
                    int tx = x + (size - fm.stringWidth(mark)) / 2;
                    int ty = y + ((size - fm.getHeight()) / 2) + fm.getAscent();
                    g2.drawString(mark, tx, ty);
                }
            }

            g2.dispose();
        }
    }
}
