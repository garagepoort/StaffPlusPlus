package net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks;

public class AltDetectInfo {
    private String username;
    private String ip;

    public AltDetectInfo(String username, String ip) {
        this.username = username;
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }
}
