package net.shortninja.staffplus.core.domain.staff.investigate.gui.views;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationNoteEntity;
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
public class InvestigationNoteItemBuilder {
    private final IProtocolService protocolService;
    private final Options options;

    public InvestigationNoteItemBuilder(IProtocolService protocolService, Options options) {
        this.protocolService = protocolService;
        this.options = options;
    }


    public ItemStack build(InvestigationNoteEntity investigationNoteEntity) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(investigationNoteEntity.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(options.timestampFormat));

        List<String> lore = new ArrayList<>();

        lore.add("&bId: " + investigationNoteEntity.getId());
        lore.add("&bNoted by: " + investigationNoteEntity.getNotedByName());
        lore.add("&bNoted on: " + time);
        lore.add("&bNote:");
        for (String line : formatLines(investigationNoteEntity.getNote(), 30)) {
            lore.add("  &b" + line);
        }

        lore.add("");
        lore.add("&CRight click to delete note");
        ItemStack item = Items.builder()
            .setMaterial(Material.PAPER)
            .setName("&cNOTE: " + investigationNoteEntity.getId())
            .addLore(lore)
            .build();

        return protocolService.getVersionProtocol().addNbtString(item, String.valueOf(investigationNoteEntity.getId()));
    }
}
