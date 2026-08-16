package com.bwxor.piejfxsdk.type;

public enum WatermarkPosition {
    UNKNOWN("Unknown"), TOP_LEFT("Top-Left"), TOP_RIGHT("Top-Right"), BOTTOM_LEFT("Bottom-Left"), BOTTOM_RIGHT("Bottom-Right");

    private final String value;

    WatermarkPosition(String value) {
        this.value = value;
    }

    public static WatermarkPosition match(String input) {
        if (input == null) {
            return UNKNOWN;
        }

        for (WatermarkPosition wp : WatermarkPosition.values()) {
            if (wp.value.equals(input)) {
                return wp;
            }
        }

        return UNKNOWN;
    }

    public String getValue() {
        return value;
    }
}
