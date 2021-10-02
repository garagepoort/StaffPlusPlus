package net.shortninja.staffplus.core.domain.staff.freeze.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.application.config.CommaSplitTransformer;
import net.shortninja.staffplus.core.application.config.SoundsConfigTransformer;
import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandsConfigTransformer;

import java.util.List;

@IocBean
public class FreezeConfiguration {

    @ConfigProperty("freeze-module.enabled")
    public boolean enabled;
    @ConfigProperty("freeze-module.chat")
    public boolean chat;
    @ConfigProperty("freeze-module.damage")
    public boolean damage;
    @ConfigProperty("freeze-module.timer")
    public int timer;
    @ConfigProperty("freeze-module.sound")
    @ConfigTransformer(SoundsConfigTransformer.class)
    public Sounds sound;
    @ConfigProperty("freeze-module.prompt")
    public boolean prompt;
    @ConfigProperty("freeze-module.prompt-title")
    public String promptTitle;
    @ConfigProperty("freeze-module.logoutCommands")
    @ConfigTransformer(CommaSplitTransformer.class)
    public List<String> logoutCommands;
    @ConfigProperty("freeze-module.title-message-enabled")
    public boolean titleMessageEnabled;
    @ConfigProperty("freeze-module.allowed-commands")
    public List<String> allowedCommands;
    @ConfigProperty("freeze-module.freeze-commands")
    @ConfigTransformer(ConfiguredCommandsConfigTransformer.class)
    public List<ConfiguredCommand> freezeCommandHooks;
    @ConfigProperty("freeze-module.unfreeze-commands")
    @ConfigTransformer(ConfiguredCommandsConfigTransformer.class)
    public List<ConfiguredCommand> unfreezeCommandHooks;
}
