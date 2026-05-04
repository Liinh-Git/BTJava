package org.jobportal.view.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Centralized design tokens for the Functional Utility design system.
 * All UI panels must reference these constants to ensure 100% design compliance.
 *
 * Design system: Functional Utility (wireframe-like, high-density)
 * Font: Inter (fallback: Segoe UI, SansSerif)
 * Grid: 4px / 8px
 * Radius: 4px (0.25rem)
 * Elevation: Flat (no shadows), 1px borders
 */
public final class DesignSystem {

    private DesignSystem() {} // prevent instantiation

    // ─── Font Family ──────────────────────────────────────────────────────────
    public static final String FONT_FAMILY = "Inter";

    // ─── Primary Colors ───────────────────────────────────────────────────────
    public static final Color PRIMARY = new Color(0, 123, 255);       // #007BFF
    public static final Color PRIMARY_DARK = new Color(0, 65, 162);   // #0041a2
    public static final Color PRIMARY_HOVER = new Color(0, 86, 179);  // hover state

    // ─── Neutral Colors ───────────────────────────────────────────────────────
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);   // #212529
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125); // #6c757d
    public static final Color TEXT_MUTED = new Color(134, 142, 150);  // lighter muted
    public static final Color TEXT_LINK = PRIMARY;

    // ─── Surface / Background ─────────────────────────────────────────────────
    public static final Color BG_PAGE = new Color(248, 249, 250);     // #f8f9fa
    public static final Color BG_SURFACE = Color.WHITE;               // #ffffff
    public static final Color BG_SURFACE_ALT = new Color(248, 249, 250); // table header bg
    public static final Color BG_INPUT = Color.WHITE;
    public static final Color BG_DARK = new Color(52, 58, 64);       // #343a40 (login page bg)

    // ─── Borders ──────────────────────────────────────────────────────────────
    public static final Color BORDER = new Color(222, 226, 230);      // #dee2e6
    public static final Color BORDER_LIGHT = new Color(233, 236, 239); // #e9ecef
    public static final Color BORDER_INPUT = new Color(206, 212, 218); // #ced4da

    // ─── Status Colors ────────────────────────────────────────────────────────
    public static final Color SUCCESS = new Color(40, 167, 69);       // #28a745
    public static final Color DANGER = new Color(220, 53, 69);        // #dc3545
    public static final Color WARNING = new Color(255, 193, 7);       // #ffc107
    public static final Color INFO = new Color(23, 162, 184);         // #17a2b8

    // ─── Badge Colors ─────────────────────────────────────────────────────────
    public static final Color BADGE_BG = new Color(226, 232, 240);    // slate-200
    public static final Color BADGE_FG = new Color(71, 85, 105);      // slate-600
    public static final Color BADGE_SUCCESS_BG = new Color(220, 252, 231);
    public static final Color BADGE_SUCCESS_FG = new Color(22, 101, 52);
    public static final Color BADGE_DANGER_BG = new Color(254, 226, 226);
    public static final Color BADGE_DANGER_FG = new Color(153, 27, 27);
    public static final Color BADGE_WARNING_BG = new Color(254, 249, 195);
    public static final Color BADGE_WARNING_FG = new Color(133, 77, 14);
    public static final Color BADGE_INFO_BG = new Color(219, 234, 254);
    public static final Color BADGE_INFO_FG = new Color(30, 64, 175);

    // ─── Spacing (4px grid) ───────────────────────────────────────────────────
    public static final int SPACE_1 = 4;
    public static final int SPACE_2 = 8;
    public static final int SPACE_3 = 12;
    public static final int SPACE_4 = 16;
    public static final int SPACE_5 = 20;
    public static final int SPACE_6 = 24;
    public static final int SPACE_8 = 32;
    public static final int SPACE_10 = 40;

    // ─── Layout Constants ─────────────────────────────────────────────────────
    public static final int SIDEBAR_WIDTH = 220;
    public static final int HEADER_HEIGHT = 56;
    public static final int CONTENT_PADDING = 32;
    public static final int CARD_PADDING = 20;
    public static final int TABLE_ROW_HEIGHT = 64;
    public static final int INPUT_HEIGHT = 40;
    public static final int BUTTON_HEIGHT = 40;

    // ─── Font Sizes ───────────────────────────────────────────────────────────
    public static final int FONT_SIZE_XS = 10;
    public static final int FONT_SIZE_SM = 12;
    public static final int FONT_SIZE_BASE = 14;
    public static final int FONT_SIZE_LG = 16;
    public static final int FONT_SIZE_XL = 20;
    public static final int FONT_SIZE_2XL = 24;
    public static final int FONT_SIZE_3XL = 30;

    // ─── Font Constructors ────────────────────────────────────────────────────
    public static Font font(int size, int style) {
        return new Font(FONT_FAMILY, style, size);
    }

    public static Font fontRegular(int size) {
        return font(size, Font.PLAIN);
    }

    public static Font fontBold(int size) {
        return font(size, Font.BOLD);
    }

    // common presets
    public static Font heading1() { return fontBold(FONT_SIZE_3XL); }
    public static Font heading2() { return fontBold(FONT_SIZE_2XL); }
    public static Font heading3() { return fontBold(FONT_SIZE_XL); }
    public static Font heading4() { return fontBold(FONT_SIZE_LG); }
    public static Font body() { return fontRegular(FONT_SIZE_BASE); }
    public static Font bodySmall() { return fontRegular(FONT_SIZE_SM); }
    public static Font caption() { return fontRegular(FONT_SIZE_XS); }
    public static Font label() { return fontBold(FONT_SIZE_SM); }
    public static Font tableHeader() { return fontBold(11); }

    // ─── Component Factories ──────────────────────────────────────────────────

    /** Create a standard card panel with 1px border and padding */
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING)
        ));
        return card;
    }

    /** Create a styled text field matching design system */
    public static JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(body());
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_INPUT);
        field.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, INPUT_HEIGHT));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_INPUT, 1),
            new EmptyBorder(SPACE_2, SPACE_3, SPACE_2, SPACE_3)
        ));
        // placeholder text via a focus listener would go here
        field.setText(placeholder);
        field.setForeground(TEXT_MUTED);
        return field;
    }

    /** Create a primary button (blue bg, white text) */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(fontBold(FONT_SIZE_BASE));
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, BUTTON_HEIGHT));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Create an outline button (white bg, dark border) */
    public static JButton createOutlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(fontRegular(FONT_SIZE_BASE));
        btn.setBackground(BG_SURFACE);
        btn.setForeground(TEXT_PRIMARY);
        btn.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(BORDER, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Create a field label (uppercase, small, bold) */
    public static JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(label());
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    /** Create a page title label */
    public static JLabel createPageTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(heading2());
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    /** Create a page subtitle label */
    public static JLabel createPageSubtitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(body());
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    /** Create a status badge (colored text with dot) */
    public static JLabel createStatusDot(String status) {
        Color dotColor;
        switch (status.toLowerCase()) {
            case "active": dotColor = SUCCESS; break;
            case "suspended": dotColor = DANGER; break;
            case "pending": dotColor = TEXT_MUTED; break;
            default: dotColor = TEXT_SECONDARY; break;
        }
        String html = "<html><font color='#" + Integer.toHexString(dotColor.getRGB()).substring(2) +
                "'>●</font>&nbsp;" + status + "</html>";
        JLabel lbl = new JLabel(html);
        lbl.setFont(body());
        return lbl;
    }

    /** Create a role badge (pill-like label) */
    public static JLabel createBadge(String text, Color bg, Color fg) {
        JLabel badge = new JLabel(text);
        badge.setFont(fontBold(FONT_SIZE_XS));
        badge.setOpaque(true);
        badge.setBackground(bg);
        badge.setForeground(fg);
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        return badge;
    }

    /** Create a pagination button */
    public static JButton createPageButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(36, 36));
        btn.setFocusPainted(false);
        btn.setFont(fontRegular(FONT_SIZE_SM));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (active) {
            btn.setBackground(BG_SURFACE);
            btn.setForeground(PRIMARY);
            btn.setBorder(new LineBorder(PRIMARY, 1));
        } else if (text.equals("...")) {
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setBackground(BG_PAGE);
        } else {
            btn.setBackground(BG_SURFACE);
            btn.setForeground(TEXT_PRIMARY);
            btn.setBorder(new LineBorder(BORDER, 1));
        }
        return btn;
    }

    /** Create a main content panel with standard padding */
    public static JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PAGE);
        panel.setBorder(new EmptyBorder(CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING));
        return panel;
    }

    /** Wrap content in a scroll pane with no border */
    public static JScrollPane createScrollPane(JComponent content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
}
