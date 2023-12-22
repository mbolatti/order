package com.tui.proof.model.jpa;

public enum OrderQuanity {
    FIVE(5),
    TEN(10),
    FIFTEEN(15);

    private final int value;

    public int getValue() {
        return value;
    }

    OrderQuanity(int value) {
        this.value = value;
    }

    public static OrderQuanity valueOf(int intValue) {
        for (OrderQuanity enumValue : values()) {
            if (enumValue.getValue() == intValue) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("No enum constant with int value: " + intValue);
    }


}
