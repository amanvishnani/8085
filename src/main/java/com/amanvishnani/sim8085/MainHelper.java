package com.amanvishnani.sim8085;

import java.util.function.Consumer;

public class MainHelper {

    @FunctionalInterface
    public interface ErrorReporter {
        void reportError(String message);
    }

    public static int parseHex(String s, int defaultVal, int min, int max, String label, ErrorReporter reporter) {
        try {
            int val = s.isEmpty() ? defaultVal : Integer.parseInt(s.replace("H", "").trim(), 16);
            if (val < min || val > max) {
                reporter.reportError(String.format("Make sure given %s is in range %04X - %04X", label, min, max));
                return -1;
            }
            return val;
        } catch (NumberFormatException e) {
            reporter.reportError("Make sure given " + label + " is a Hex Number");
            return -1;
        }
    }
}
