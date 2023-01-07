package net.shortninja.staffplus.core.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.staffplusplus.craftbukkit.api.ProtocolFactory;
import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;

@IocBean
public class IProtocolService {

    private final IProtocol versionProtocol;

    public IProtocolService() {
        versionProtocol = ProtocolFactory.getProtocol();
    }

    public IProtocol getVersionProtocol() {
        return versionProtocol;
    }
}
