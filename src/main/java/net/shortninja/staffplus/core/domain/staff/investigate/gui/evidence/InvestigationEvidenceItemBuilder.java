package net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.investigate.EvidenceEntity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.core.common.JavaUtils.formatLines;

@IocBean
public class InvestigationEvidenceItemBuilder {
    private final IProtocolService protocolService;
    private final Options options;

    public InvestigationEvidenceItemBuilder(IProtocolService protocolService, Options options) {
        this.protocolService = protocolService;
        this.options = options;
    }


    public ItemStack build(EvidenceEntity evidence) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(evidence.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(options.timestampFormat));

        List<String> lore = new ArrayList<>();

        lore.add("&bId: " + evidence.getId());
        lore.add("&bType: " + evidence.getEvidenceType() + " (ID=" + evidence.getEvidenceId() + ")");
        lore.add("&bLinked by: " + evidence.getLinkedByName());
        lore.add("&bLinked on: " + time);
        lore.add("&bDescription:");
        for (String line : formatLines(evidence.getDescription(), 30)) {
            lore.add("  &b" + line);
        }

        lore.add("");
        lore.add("&CRight click to unlink evidence");
        ItemStack item = Items.builder()
            .setMaterial(Material.PAPER)
            .setName("&c" + evidence.getEvidenceType())
            .addLore(lore)
            .build();

        return protocolService.getVersionProtocol().addNbtString(item, evidence.getId() + ";" + evidence.getEvidenceType() + ";" + evidence.getEvidenceId());
    }
}
