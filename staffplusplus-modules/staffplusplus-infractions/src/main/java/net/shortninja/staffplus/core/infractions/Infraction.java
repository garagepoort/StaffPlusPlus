package net.shortninja.staffplus.core.infractions;

public interface Infraction {
    InfractionType getInfractionType();

    Long getCreationTimestamp();
}
