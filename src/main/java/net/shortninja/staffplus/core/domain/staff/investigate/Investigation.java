package net.shortninja.staffplus.core.domain.staff.investigate;

import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public Investigation(String investigatorName, UUID investigatorUuid, String serverName) {
        this.investigatorName = investigatorName;
        this.investigatorUuid = investigatorUuid;
        this.serverName = serverName;
        this.status = InvestigationStatus.OPEN;
        this.creationDate = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public Long getCreationTimestamp() {
        return creationDate;
    }

    public ZonedDateTime getCreationDate() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(creationDate), ZoneId.systemDefault());
    }

    public Optional<Long> getConclusionTimestamp() {
        return Optional.ofNullable(conclusionDate);
    }

    public Optional<ZonedDateTime> getConclusionDate() {
        if (conclusionDate == null) {
            return Optional.empty();
        }
        return Optional.of(ZonedDateTime.ofInstant(Instant.ofEpochMilli(conclusionDate), ZoneId.systemDefault()));
    }

    public String getInvestigatorName() {
        return investigatorName;
    }

    public UUID getInvestigatorUuid() {
        return investigatorUuid;
    }

    public Optional<String> getInvestigatedName() {
        return Optional.ofNullable(investigatedName);
    }

    public Optional<UUID> getInvestigatedUuid() {
        return Optional.ofNullable(investigatedUuid);
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

    public void setStatus(InvestigationStatus status) {
        this.status = status;
    }

    public void setConclusionDate(Long conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public void setInvestigatorName(String investigatorName) {
        this.investigatorName = investigatorName;
    }

    public void setInvestigatorUuid(UUID investigatorUuid) {
        this.investigatorUuid = investigatorUuid;
    }
}
