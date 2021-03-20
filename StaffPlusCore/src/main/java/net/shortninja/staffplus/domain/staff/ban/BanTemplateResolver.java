package net.shortninja.staffplus.domain.staff.ban;

import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.ban.config.BanReasonConfiguration;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class BanTemplateResolver {

    private final Options options;
    private final PermissionHandler permission;
    private final Messages messages;

    public BanTemplateResolver(Options options, PermissionHandler permission, Messages messages) {
        this.options = options;
        this.permission = permission;
        this.messages = messages;
    }

    public String resolveTemplate(CommandSender player, String reason, String providedTemplate, BanType banType) {
        if (providedTemplate != null) {
            permission.validate(player, options.banConfiguration.getPermissionBanTemplateOverwrite());
            return getTemplate(providedTemplate);
        }

        Optional<BanReasonConfiguration> banReasonConfig = options.banConfiguration.getBanReason(reason, banType);
        if (banReasonConfig.isPresent() && banReasonConfig.get().getTemplate().isPresent()) {
            return getTemplate(banReasonConfig.get().getTemplate().get());
        }

        Optional<String> defaultTemplate = options.banConfiguration.getDefaultBanTemplate(banType);
        if (defaultTemplate.isPresent()) {
            return getTemplate(defaultTemplate.get());
        }

        return banType == BanType.PERM_BAN ? messages.permanentBannedKick : messages.tempBannedKick;
    }

    private String getTemplate(String providedTemplate) {
        return options.banConfiguration.getTemplate(providedTemplate)
            .orElseThrow(() -> new BusinessException("&CCannot find ban template with name [" + providedTemplate + "]"));
    }
}
