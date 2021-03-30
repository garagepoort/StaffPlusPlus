package net.shortninja.staffplus.core.domain.staff.investigate;

import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;

import java.util.Optional;
import java.util.UUID;

public class Investigation implements IInvestigation {

    private int id;
    private Long creationDate;
    private Long conclusionDate;
    private String investigatorName;
    private UUID investigatorUuid;
    private String investigatedName;
    private UUID investigatedUuid;
    private String serverName;
    private InvestigationStatus status;


    public Investigation(int id, Long creationDate, Long conclusionDate, String investigatorName, UUID investigatorUuid, String investigatedName, UUID investigatedUuid, String serverName, InvestigationStatus status) {
        this.id = id;
        this.creationDate = creationDate;
        this.conclusionDate = conclusionDate;
        this.investigatorName = investigatorName;
        this.investigatorUuid = investigatorUuid;
        this.investigatedName = investigatedName;
        this.investigatedUuid = investigatedUuid;
        this.serverName = serverName;
        this.status = status;
    }

    public Investigation(String investigatorName, UUID investigatorUuid, String investigatedName, UUID investigatedUuid, String serverName) {
        this.investigatorName = investigatorName;
        this.investigatorUuid = investigatorUuid;
        this.investigatedName = investigatedName;
        this.investigatedUuid = investigatedUuid;
        this.serverName = serverName;
        this.status = InvestigationStatus.OPEN;
        this.creationDate = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public Optional<Long> getConclusionDate() {
        return Optional.ofNullable(conclusionDate);
    }

    public String getInvestigatorName() {
        return investigatorName;
    }

    public UUID getInvestigatorUuid() {
        return investigatorUuid;
    }

    public String getInvestigatedName() {
        return investigatedName;
    }

    public UUID getInvestigatedUuid() {
        return investigatedUuid;
    }

    public String getServerName() {
        return serverName;
    }

    public InvestigationStatus getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }
}
