package be.garagepoort.staffplusplus.trello.reports;

import org.bukkit.configuration.file.FileConfiguration;

public class ReportTrelloConfiguration {

    private String boardId;
    private String apiKey;
    private String userToken;
    private String openListName;
    private String rejectedListName;
    private String acceptedListName;
    private String resolvedListName;

    public ReportTrelloConfiguration(FileConfiguration config) {
        String boardId = config.getString("StaffPlusPlusTrello.reports.boardId");
        String apiKey = config.getString("StaffPlusPlusTrello.reports.apiKey");
        String userToken = config.getString("StaffPlusPlusTrello.reports.userToken");
        String openListName = config.getString("StaffPlusPlusTrello.reports.openListName");
        String rejectedListName = config.getString("StaffPlusPlusTrello.reports.rejectedListName");
        String acceptedListName = config.getString("StaffPlusPlusTrello.reports.acceptedListName");
        String resolvedListName = config.getString("StaffPlusPlusTrello.reports.resolvedListName");

        assertNotEmpty(boardId, "reports.boardId");
        assertNotEmpty(apiKey, "reports.apiKey");
        assertNotEmpty(userToken, "reports.userToken");
        assertNotEmpty(openListName, "reports.openListName");
        assertNotEmpty(rejectedListName, "reports.rejectedListName");
        assertNotEmpty(acceptedListName, "reports.acceptedListName");
        assertNotEmpty(resolvedListName, "reports.resolvedListName");

        this.boardId = boardId;
        this.apiKey = apiKey;
        this.userToken = userToken;
        this.openListName = openListName;
        this.rejectedListName = rejectedListName;
        this.acceptedListName = acceptedListName;
        this.resolvedListName = resolvedListName;

    }

    public String getBoardId() {
        return boardId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getOpenListName() {
        return openListName;
    }

    public String getRejectedListName() {
        return rejectedListName;
    }

    public String getAcceptedListName() {
        return acceptedListName;
    }

    public String getResolvedListName() {
        return resolvedListName;
    }

    private void assertNotEmpty(String boardId, String property) {
        if (boardId == null || boardId.isEmpty()) {
            throw new RuntimeException("Missing configuration: [" + property + "]");
        }
    }
}
