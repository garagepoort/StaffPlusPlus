package net.shortninja.staffplus.core.domain.staff.mode;

public enum StaffModule {

    VANISH_MODULE("vanish-module");

    private String key;

    StaffModule(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
