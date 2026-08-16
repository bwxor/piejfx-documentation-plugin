package com.bwxor.piejfxsdk.type;

public enum Status {
    DRAFT("Draft"), IN_REVIEW("In Review"), APPROVED("Approved"), UNKNOWN("Unknown");
    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status match(String input) {
        if (input == null) {
            return UNKNOWN;
        }

        for(Status s : Status.values()) {
            if (s.value.equals(input)) {
                return s;
            }
        }

        return UNKNOWN;
    }

    public String getValue() {
        return value;
    }
}
