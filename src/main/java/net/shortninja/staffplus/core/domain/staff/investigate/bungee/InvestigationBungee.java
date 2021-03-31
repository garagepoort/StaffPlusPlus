package net.shortninja.staffplus.core.domain.staff.investigate.bungee;

import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;

import java.util.Optional;
import java.util.UUID;

public class InvestigationBungee extends BungeeMessage implements IInvestigation {

    private int id;
    private Long creationDate;
    private Long conclusionDate;
    private String investigatorName;
    private UUID investigatorUuid;
    private String investigatedName;
    private UUID investigatedUuid;
    private InvestigationStatus status;

    public InvestigationBungee(IInvestigation investigation) {
        super(investigation.getServerName());
        id = investigation.getId();
        creationDate = investigation.getCreationDate();
        conclusionDate = investigation.getConclusionDate().orElse(null);
        investigatorName = investigation.getInvestigatorName();
        investigatorUuid = investigation.getInvestigatorUuid();
        investigatedName = investigation.getInvestigatedName();
        investigatedUuid = investigation.getInvestigatedUuid();
        status = investigation.getStatus();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Long getCreationDate() {
        return creationDate;
    }

    @Override
    public Optional<Long> getConclusionDate() {
        return Optional.ofNullable(conclusionDate);
    }

    @Override
    public String getInvestigatorName() {
        return investigatorName;
    }

    @Override
    public UUID getInvestigatorUuid() {
        return investigatorUuid;
    }

    @Override
    public String getInvestigatedName() {
        return investigatedName;
    }

    @Override
    public UUID getInvestigatedUuid() {
        return investigatedUuid;
    }

    @Override
    public InvestigationStatus getStatus() {
        return status;
    }
}
