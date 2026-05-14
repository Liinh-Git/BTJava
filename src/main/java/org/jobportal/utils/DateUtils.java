package org.jobportal.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    private static final DateTimeFormatter DB_DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter UI_DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private DateUtils() {}

    // Backward-compatible old API: DB yyyy-MM-dd -> UI dd-MM-yyyy.
    public static String formatDate(String dateStr) {
        return dbDateToUiDate(dateStr);
    }

    // Format LocalDate for UI as dd-MM-yyyy.
    public static String toUiDate(LocalDate date) {
        if (date == null) return "";
        return date.format(UI_DATE_FMT);
    }

    // Parse UI dd-MM-yyyy to LocalDate.
    public static LocalDate parseUiDate(String uiValue) {
        if (uiValue == null || uiValue.isBlank()) return null;
        return LocalDate.parse(uiValue.trim(), UI_DATE_FMT);
    }

    // Flexible parse for existing screens: try dd-MM-yyyy, fallback yyyy-MM-dd.
    public static LocalDate parseUiOrDbDate(String value) {
        if (value == null || value.isBlank()) return null;
        String trimmed = value.trim();
        try {
            return parseUiDate(trimmed);
        } catch (DateTimeParseException ignored) {
            return LocalDate.parse(trimmed, DB_DATE_FMT);
        }
    }

    // Format LocalDate for DB as yyyy-MM-dd.
    public static String toDbDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DB_DATE_FMT);
    }

    // Convert DB text yyyy-MM-dd to UI dd-MM-yyyy.
    public static String dbDateToUiDate(String dbDate) {
        if (dbDate == null || dbDate.isBlank()) return "";
        LocalDate parsed = LocalDate.parse(dbDate.trim(), DB_DATE_FMT);
        return toUiDate(parsed);
    }

    // Convert UI text dd-MM-yyyy to DB yyyy-MM-dd.
    public static String uiDateToDbDate(String uiValue) {
        LocalDate parsed = parseUiDate(uiValue);
        return toDbDate(parsed);
    }

    // Legacy aliases kept to avoid breaking old calls.
    public static String toUiMonthYear(LocalDate date) {
        return toUiDate(date);
    }

    public static LocalDate parseUiMonthYear(String uiValue) {
        return parseUiDate(uiValue);
    }

    public static String dbDateToUiMonthYear(String dbDate) {
        return dbDateToUiDate(dbDate);
    }

    public static String uiMonthYearToDbDate(String uiValue) {
        return uiDateToDbDate(uiValue);
    }
}
