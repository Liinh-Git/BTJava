package org.jobportal.utils;

/**
 * Helper sinh ID tuần tự theo format PREFIX-000001.
 */
public final class IdGenerator {

    private IdGenerator() {}

    public static String nextId(String latestId, String prefix, int width) {
        if (prefix == null || prefix.isBlank()) {
            throw new IllegalArgumentException("prefix khong hop le");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("width phai > 0");
        }

        int current = 0;
        String idPrefix = prefix + "-";

        if (latestId != null && latestId.startsWith(idPrefix)) {
            String numberPart = latestId.substring(idPrefix.length());
            try {
                current = Integer.parseInt(numberPart);
            } catch (NumberFormatException ignored) {
                current = 0;
            }
        }

        int next = current + 1;
        int max = (int) Math.pow(10, width) - 1;
        if (next > max) {
            throw new IllegalStateException("Vuot qua gioi han " + idPrefix + "9".repeat(width));
        }
        return idPrefix + String.format("%0" + width + "d", next);
    }
}
