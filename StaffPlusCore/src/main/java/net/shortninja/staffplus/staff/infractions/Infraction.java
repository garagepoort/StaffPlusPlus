package net.shortninja.staffplus.staff.infractions;

public interface Infraction {
    InfractionType getInfractionType();

    Long getCreationTimestamp();
}
