package org.jobportal.view.common;
import org.jobportal.bll.impl.NotificationService;
import org.jobportal.bll.interfaces.INotificationService;
import org.jobportal.dto.NotificationDTO;
import org.jobportal.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationPanel extends JPanel {

    private final INotificationService notificationService = new NotificationService();
    private JPanel listContainer;

    public NotificationPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Thông báo");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnRefresh = new JButton("Tải lại");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBackground(new Color(13, 110, 253));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadData());

        header.add(title, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);
        listContainer.setBorder(new LineBorder(new Color(226, 230, 234), 1));

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        listContainer.removeAll();

        String userId = SessionManager.getInstance().getCurrentUserId();
        List<NotificationDTO> notifications = (userId == null || userId.isBlank())
                ? java.util.Collections.emptyList()
                : notificationService.getNotifications(userId);

        if (notifications == null || notifications.isEmpty()) {
            listContainer.add(createEmptyRow());
        } else {
            for (NotificationDTO n : notifications) {
                listContainer.add(createNotificationRow(n));
            }
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel createNotificationRow(NotificationDTO notification) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(true);

        // Hien thi dau cham trang thai doc (mau khac nhau neu chua doc)
        Color rowBg = notification.isRead() ? Color.WHITE : new Color(240, 247, 255);
        row.setBackground(rowBg);
        row.setBorder(BorderFactory.createMatteBorder(0, notification.isRead() ? 0 : 3, 1, 0,
            notification.isRead() ? new Color(230, 230, 230) : new Color(13, 110, 253)));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(rowBg);
        left.setBorder(new EmptyBorder(12, 15, 12, 10));

        String senderName = notification.getSenderName() != null ? notification.getSenderName() : "System";
        JLabel lblSender = new JLabel(senderName);
        lblSender.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lblContent = new JLabel("<html>" + escapeHtml(notification.getContent()) + "</html>");
        lblContent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblContent.setForeground(Color.DARK_GRAY);

        String timeText = notification.getDateTime() != null
                ? notification.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "N/A";
        JLabel lblTime = new JLabel(timeText);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTime.setForeground(Color.GRAY);

        left.add(lblSender);
        left.add(Box.createRigidArea(new Dimension(0, 4)));
        left.add(lblContent);
        left.add(Box.createRigidArea(new Dimension(0, 6)));
        left.add(lblTime);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        right.setBackground(rowBg);

        JButton btnRead = new JButton(notification.isRead() ? "✓ Đã đọc" : "Đánh dấu đã đọc");
        btnRead.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnRead.setFocusPainted(false);
        btnRead.setBackground(notification.isRead() ? new Color(240, 240, 240) : new Color(230, 245, 255));
        btnRead.setForeground(notification.isRead() ? Color.GRAY : new Color(13, 110, 253));
        btnRead.setBorder(new LineBorder(
            notification.isRead() ? new Color(220, 220, 220) : new Color(13, 110, 253), 1));
        btnRead.setEnabled(!notification.isRead());
        btnRead.addActionListener(e -> {
            boolean updated = notificationService.markAsRead(notification.getNotificationId());
            if (updated) {
                loadData();
            }
        });

        right.add(btnRead);

        row.add(left, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);

        return row;
    }

    private JPanel createEmptyRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));
        row.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Chưa có thông báo nào.");
        lbl.setForeground(Color.GRAY);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        row.add(lbl);
        return row;
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
