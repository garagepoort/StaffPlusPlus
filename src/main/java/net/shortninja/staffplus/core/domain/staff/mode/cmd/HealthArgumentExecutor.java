package net.shortninja.staffplus.core.domain.staff.mode.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentExecutor;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@IocBean
@IocMultiProvider(ArgumentExecutor.class)
public class HealthArgumentExecutor implements ArgumentExecutor {

    @Override
    public boolean execute(CommandSender commandSender, String playerName, String value) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return false;
        }

        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        double maxHealth = attribute.getValue();
        if(value.isEmpty()) {
            player.setHealth(maxHealth);
        } else {
            double percentage = Double.parseDouble(value);
            double newHealth = (maxHealth*(percentage/100.0f));
            player.setHealth(newHealth);
        }
        return true;
    }

    @Override
    public ArgumentType getType() {
        return ArgumentType.HEALTH;
    }

    @Override
    public List<String> complete(String currentArg) {
        return Arrays.asList("-H", "-H50", "-H25");
    }
}
