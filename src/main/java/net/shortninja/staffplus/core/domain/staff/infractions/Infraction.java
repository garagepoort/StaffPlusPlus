package net.shortninja.staffplus.core.domain.staff.infractions;

public interface Infraction {
    InfractionType getInfractionType();

    Long getCreationTimestamp();
}
