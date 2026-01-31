package com.amanvishnani.sim8085;

import com.amanvishnani.sim8085.domain.*;
import com.amanvishnani.sim8085.domain.Impl.*;

import java.util.*;

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

    public static List<Object[]> getMemoryTableData(I8085 simulator, int head, int max) {
        List<Object[]> data = new ArrayList<>();
        for (int i = head; i < head + 20 && i <= max; i++) {
            data.add(new Object[] { Address.from(i).hexValue(), simulator.getData(i).hexValue() });
        }
        return data;
    }

    public static List<Object[]> getCodeTableData(List<InstructionRow> rows, int head) {
        List<Object[]> data = new ArrayList<>();
        for (int i = head; i < head + 20 && i < rows.size(); i++) {
            InstructionRow row = rows.get(i);
            data.add(new Object[] {
                    Address.from(i).hexValue(),
                    row.getLabel(),
                    row.getInstruction(),
                    row.getData().hexValue()
            });
        }
        return data;
    }

    public static Map<String, String> getRegistersMap(I8085 simulator) {
        Map<String, String> map = new HashMap<>();
        map.put("A", simulator.getA().hexValue());
        map.put("B", simulator.getB().hexValue());
        map.put("C", simulator.getC().hexValue());
        map.put("D", simulator.getD().hexValue());
        map.put("E", simulator.getE().hexValue());
        map.put("H", simulator.getH().hexValue());
        map.put("L", simulator.getL().hexValue());
        return map;
    }

    public static Map<String, String> getFlagsMap(I8085 simulator) {
        com.amanvishnani.sim8085.domain.Impl.Flags flags = simulator.getFlags();
        Map<String, String> map = new HashMap<>();
        map.put("S", flags.getFlag(Flag.S).toString());
        map.put("Cy", flags.getFlag(Flag.Cy).toString());
        map.put("Z", flags.getFlag(Flag.Z).toString());
        map.put("Ac", flags.getFlag(Flag.Ac).toString());
        map.put("P", flags.getFlag(Flag.P).toString());
        return map;
    }
}
