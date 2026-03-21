package com.example.prm_center_kitchen_management.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Trích mã lô từ message FEFO của backend, ví dụ:
 * "Vi phạm FEFO: Đang xuất lô BATCH-A (...) nhưng lô BATCH-B (...) cần xuất trước."
 */
public final class FefoViolationParser {

    private static final Pattern PATTERN_WRONG =
            Pattern.compile("Đang xuất lô\\s+([^\\s(]+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern PATTERN_CORRECT =
            Pattern.compile("nhưng lô\\s+([^\\s(]+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private FefoViolationParser() {}

    /**
     * @return {@code [wrongBatchCode, correctBatchCode]} hoặc {@code null} nếu không khớp
     */
    public static String[] parseWrongAndCorrectBatchCodes(String message) {
        if (message == null || message.isEmpty()) return null;
        if (!message.contains("FEFO")) return null;
        Matcher mW = PATTERN_WRONG.matcher(message);
        Matcher mC = PATTERN_CORRECT.matcher(message);
        if (!mW.find() || !mC.find()) return null;
        String wrong = mW.group(1).trim();
        String correct = mC.group(1).trim();
        if (wrong.isEmpty() || correct.isEmpty()) return null;
        return new String[] { wrong, correct };
    }
}
