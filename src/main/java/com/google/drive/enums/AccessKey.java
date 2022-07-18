package com.google.drive.enums;

public enum AccessKey {
    GOOGLE_OAUTH_TOKEN("GOOGLE_OAUTH_TOKEN"),
    DRIVE("drive");

    public final String label;

    private AccessKey(String label) {
        this.label = label;
    }
}
