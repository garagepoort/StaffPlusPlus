
<#import "/gui/bans/ban-commons.ftl" as bancommons/>
<#import "/gui/mutes/mute-commons.ftl" as mutecommons/>
<TubingGui size="54">
    <GuiItem slot="0" name="<playername>" material="PLAYER_HEAD"/>
    <GuiItem slot="2" name="<Infraction history>" material="BOOK"/>
    <GuiItem slot="3" name="reports" material="FLOWER_BANNER_PATTERN"/>
    <GuiItem slot="4" name="warnings" material="FLOWER_BANNER_PATTERN"/>
    <GuiItem slot="5" name="mutes" material="FLOWER_BANNER_PATTERN"/>

    <GuiItem slot="39" if="${.data_model["mute-module.enabled"]?c}" name="<#if model.mute.isPresent()>&CBanned<#else>&2Not banned</#if>" material="<#if model.mute.isPresent()>REDSTONE_TORCH<#else>LEVER</#if>">
        <#if model.mute.isPresent()>
            <@mutecommons.mutelore mute=model.mute.get()/>
        </#if>
    </GuiItem>
    <GuiItem slot="40" if="${.data_model["ban-module.enabled"]?c}" name="<#if model.ban.isPresent()>&CBanned<#else>&2Not banned</#if>" material="<#if model.ban.isPresent()>BARRIER<#else>INFESTED_STONE</#if>">
        <#if model.ban.isPresent()>
            <@bancommons.banlore ban=model.ban.get()/>
        </#if>
    </GuiItem>

<#--    <GuiItem slot="41" name="Ip Banned" material="LEGACY_REDSTONE_COMPARATOR_ON"/>-->

    <GuiItem if="${player.online?c}" slot="45" name="Follow" onLeftClick="staff-mode/follow?targetPlayerName=${player.username}" material="LEAD"/>
    <GuiItem if="${player.online?c}" slot="46" name="Freeze" onLeftClick="manage-frozen/freeze?targetPlayerName=${player.username}" material="PACKED_ICE"/>
    <GuiItem if="${player.online?c}" slot="47" name="Cps" onLeftClick="staff-mode/cps?targetPlayerName=${player.username}" material="CLOCK"/>
    <GuiItem if="${player.online?c}" slot="50" name="Teleport to" onLeftClick="teleport?targetPlayerName=${player.username}" material="MAGENTA_GLAZED_TERRACOTTA"/>
    <GuiItem if="${player.online?c}" slot="51" name="Teleport here" onLeftClick="teleport-here?targetPlayerName=${player.username}" material="NETHER_STAR"/>

    <GuiItem slot="48" name="Examine" onLeftClick="manage-inventory/open?targetPlayerName=${player.username}" material="CHEST"/>
    <GuiItem slot="49" name="Enderchest View" onLeftClick="manage-enderchest/open?targetPlayerName=${player.username}" material="ENDER_CHEST"/>
</TubingGui>