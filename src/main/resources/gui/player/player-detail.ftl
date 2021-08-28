<#import "/gui/bans/ban-commons.ftl" as bancommons/>
<#import "/gui/mutes/mute-commons.ftl" as mutecommons/>
<#import "/gui/warnings/warning-commons.ftl" as warningcommons/>
<#import "/gui/player/player-commons.ftl" as playercommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="27">
    <title>${target.username}</title>
    <@playercommons.playerhead slot=0 sppPlayer=target />
    <GuiItem slot="2"
             if="config|reports-module.enabled"
             permission="config|permissions:reports.manage.view"
             name="Reports"
             onLeftClick="manage-reports/view/find-reports?reporter=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             onRightClick="manage-reports/view/find-reports?culprit=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             material="FLOWER_BANNER_PATTERN">
        <Lore>
            <LoreLine>&7(&2${model.reports?size}&7) reports created</LoreLine>
            <LoreLine>&7(&C${model.reported?size}&7) time reported)</LoreLine>
            <LoreLine></LoreLine>
            <LoreLine>&7Left click to &6view reports created</LoreLine>
            <LoreLine>&7Right click to &6view reported reports</LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem slot="3"
             material="FLOWER_BANNER_PATTERN"
             name="warnings"
             if="config|warnings-module.enabled"
             onLeftClick="manage-warnings/view/overview?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             permission="config|permissions:warnings.manage.view">
        <Lore>
            <LoreLine>&7(&C${model.warnings?size}&7) times warned</LoreLine>
            <#if model.warnings?has_content>
                <#assign lastWarning = model.warnings[0]>
                <LoreLine>&7---------------</LoreLine>
                <LoreLine>&6Last warning:</LoreLine>
                <LoreLine></LoreLine>
                <@warningcommons.warninglorelines warning=lastWarning/>
            </#if>

            <LoreLine></LoreLine>
            <LoreLine>&7---------------</LoreLine>
            <LoreLine>&7Left click to &6view warning history</LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem slot="6"
             if="config|mute-module.enabled"
             permission="config|permissions:mute-view"
             name="<#if model.mute.isPresent()>&CMuted<#else>&2Not muted</#if>"
             onLeftClick="manage-mutes/view/history?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             material="<#if model.mute.isPresent()>REDSTONE_TORCH<#else>LEVER</#if>">
        <Lore>
            <#if model.mute.isPresent()>
                <LoreLine>&6Current mute:</LoreLine>
                <@mutecommons.mutelorelines mute=model.mute.get()/>
            </#if>

            <LoreLine></LoreLine>
            <LoreLine>&7---------------</LoreLine>
            <LoreLine>&7Left click to &6view mute history</LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem slot="7"
             if="config|ban-module.enabled"
             permission="config|permissions:ban-view"
             name="<#if model.ban.isPresent()>&CBanned<#else>&2Not banned</#if>"
             onLeftClick="manage-bans/view/history?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             material="<#if model.ban.isPresent()>BARRIER<#else>INFESTED_STONE</#if>">
        <Lore>
            <#if model.ban.isPresent()>
                <LoreLine>&6Current ban:</LoreLine>
                <@bancommons.banlorelines ban=model.ban.get()/>
            </#if>
            <LoreLine></LoreLine>
            <LoreLine>&7---------------</LoreLine>
            <LoreLine>&7Left click to &6view ban history</LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem slot="8"
             if="config|ban-module.ipban.enabled"
             permission="config|permissions:ipban.ban-view"
             name="<#if model.ipbans?has_content>&CIp Banned<#else>&2Not Ip banned</#if>"
             material="<#if model.ipbans?has_content>TARGET<#else>CHISELED_STONE_BRICKS</#if>">
        <#if model.ipbans?has_content>
            <Lore>
                <LoreLine>&6Banned by following rule(s):</LoreLine>
                <#list model.ipbans as ipban>
                    <LoreLine>&7---------------------</LoreLine>
                    <@bancommons.ipbanlorelines ipban=ipban/>
                </#list>
            </Lore>
        </#if>
    </GuiItem>

    <#--    <GuiItem slot="41" name="Ip Banned" material="LEGACY_REDSTONE_COMPARATOR_ON"/>-->

    <GuiItem if="${target.online?c}"
             permission="config|permissions:follow"
             slot="18"
             name="Follow"
             onLeftClick="staff-mode/follow?targetPlayerName=${target.username}"
             material="LEAD"/>

    <GuiItem if="${target.online?c}"
             permission="config|permissions:freeze"
             slot="19"
             name="Freeze"
             onLeftClick="manage-frozen/freeze?targetPlayerName=${target.username}"
             material="PACKED_ICE"/>

    <GuiItem if="${target.online?c}"
             slot="20"
             name="Cps"
             onLeftClick="staff-mode/cps?targetPlayerName=${target.username}"
             material="CLOCK"/>

    <GuiItem if="${target.online?c}"
             slot="21"
             permission="config|permissions:teleport-to-player"
             name="Teleport to"
             onLeftClick="teleport?targetPlayerName=${target.username}"
             material="MAGENTA_GLAZED_TERRACOTTA"/>

    <GuiItem if="${target.online?c}"
             permission="config|permissions:teleport-here"
             slot="22"
             name="Teleport here"
             onLeftClick="teleport-here?targetPlayerName=${target.username}" material="NETHER_STAR"/>

    <GuiItem slot="23" name="Examine"
             onLeftClick="manage-inventory/open?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
            <#if target.online>
                permission="config|permissions:examine-inventory-interaction.online"
            <#else >
                permission="config|permissions:examine-inventory-interaction.offline"
            </#if>
             material="CHEST"/>
    <GuiItem slot="24" name="Enderchest View"
             onLeftClick="manage-enderchest/open?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             material="ENDER_CHEST"/>

    <#if backAction??>
        <@commons.backButton action=backAction backSlot=26/>
    </#if>
</TubingGui>