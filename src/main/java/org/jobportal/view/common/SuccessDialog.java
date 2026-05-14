package org.jobportal.view.common;

import javax.swing.*;
import java.awt.*;

/**
 * Compatibility wrapper for older code. New code should call ModernDialogUtils directly.
 */
@Deprecated
public final class SuccessDialog {
    private SuccessDialog() {
    }

    public static void showSuccess(Component parent, String message) {
        ModernDialogUtils.showSuccess(parent, message);
    }

    public static void showError(Component parent, String message) {
        ModernDialogUtils.showError(parent, message);
    }

    public static void showMessageDialog(Component parent, Object message) {
        ModernDialogUtils.showMessageDialog(parent, message);
    }

    public static void showMessageDialog(Component parent, Object message, String title, int messageType) {
        ModernDialogUtils.showMessageDialog(parent, message, title, messageType);
    }
}
