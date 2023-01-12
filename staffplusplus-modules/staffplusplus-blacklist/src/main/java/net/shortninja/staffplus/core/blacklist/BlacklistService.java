package net.shortninja.staffplus.core.blacklist;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import net.shortninja.staffplus.core.blacklist.censors.BlacklistCensor;

import java.util.List;

@IocBean
public class BlacklistService {

    private final List<BlacklistCensor> blacklistCensors;

    public BlacklistService(@IocMulti(BlacklistCensor.class) List<BlacklistCensor> blacklistCensors) {
        this.blacklistCensors = blacklistCensors;
    }

    public String censorMessage(String originalMessage) {
        String censoredMessage = originalMessage;
        for (BlacklistCensor blacklistCensor : blacklistCensors) {
            censoredMessage = blacklistCensor.censor(censoredMessage);
        }
        return censoredMessage;
    }
}