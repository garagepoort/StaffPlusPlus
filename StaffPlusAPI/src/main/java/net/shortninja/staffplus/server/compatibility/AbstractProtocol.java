package net.shortninja.staffplus.server.compatibility;

import net.shortninja.staffplus.IStaffPlus;

public abstract class AbstractProtocol implements IProtocol {

    protected IStaffPlus staffPlus;


    public AbstractProtocol(IStaffPlus staffPlus) {
        this.staffPlus = staffPlus;
    }


    @SuppressWarnings("Duplicates") // Should probably clean this up at some point
    public boolean shouldIgnorePacket(Object packetValue)
    {
        boolean shouldIgnore = true;

        String sound = getSound(packetValue);

        if (staffPlus.getOptions() == null || sound == null) {
            return shouldIgnore;
        } else {
            for (String string : staffPlus.getOptions().getSoundNames()) {
                if (string.equalsIgnoreCase(sound)) {
                    shouldIgnore = false;
                }
            }
        }

        return shouldIgnore;

    }

}
