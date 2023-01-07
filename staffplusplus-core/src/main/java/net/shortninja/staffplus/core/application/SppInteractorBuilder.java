package net.shortninja.staffplus.core.application;

import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplusplus.session.SppInteractor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SppInteractorBuilder {

    public static SppInteractor fromSender(CommandSender sender) {
        UUID senderUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : Constants.CONSOLE_UUID;
        String senderName = sender instanceof Player ? sender.getName() : "Console";
        return new SppInteractor(senderUuid, senderName, sender);
    }
}
