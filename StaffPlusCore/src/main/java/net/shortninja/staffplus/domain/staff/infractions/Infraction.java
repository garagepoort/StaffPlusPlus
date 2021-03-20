package net.shortninja.staffplus.domain.staff.infractions;

public interface Infraction {
    InfractionType getInfractionType();

    Long getCreationTimestamp();
}
