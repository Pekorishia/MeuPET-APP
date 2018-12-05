package com.fepa.meupet.model.environment.enums;

public enum BottomTabs {
    SEARCHMAP(0), PETS(1), CALENDAR(2), LOSTPETS(3), SETTINGS(4);

    private final int value;
    private BottomTabs(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
