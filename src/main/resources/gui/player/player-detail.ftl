<#import "/gui/bans/ban-commons.ftl" as bancommons/>
<#import "/gui/mutes/mute-commons.ftl" as mutecommons/>
<#import "/gui/warnings/warning-commons.ftl" as warningcommons/>
<#import "/gui/player/player-commons.ftl" as playercommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="27" id="player-detail">
    <title class="gui-title">${target.username}</title>
    <@playercommons.playerhead itemId="player-info" slot=0 sppPlayer=target />
    <GuiItem
             id="reports-summary"
             slot="2"
             if="config|reports-module.enabled"
             permission="config|permissions:reports.manage.view"
             onLeftClick="manage-reports/view/find-reports?reporter=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             onRightClick="manage-reports/view/find-reports?culprit=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             material="BANNER">
        <name class="item-name">Reports</name>
        <Lore>
            <LoreLine>
                <t id="reports-created" color="&7">(</t>
                <t id="reports-created-amount" color="&2" >${model.reports?size}</t>
                <t id="reports-created" color="&7">) reports created</t>
            </LoreLine>
            <LoreLine>
                <t id="times-reported" color="&7">(</t>
                <t id="times-reported-amount" color="&C" >${model.reported?size}</t>
                <t id="times-reported" color="&7">) times reported</t>
            </LoreLine>
            <LoreLine></LoreLine>
            <LoreLine>&7Left click to &6view reports created</LoreLine>
            <LoreLine>&7Right click to &6view reported reports</LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem
             id="warnings-summary"
             slot="3"
             material="BANNER"
             if="config|warnings-module.enabled"
             onLeftClick="manage-warnings/view/overview?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             permission="config|permissions:warnings.manage.view">
        <name class="item-name">Warnings</name>
        <Lore>
            <LoreLine>&7(&C${model.warnings?size}&7) times warned</LoreLine>
            <#if model.warnings?has_content>
                <#assign lastWarning = model.warnings[0]>
                <@commons.line />
                <LoreLine>
                    <t color="&6" id="last-warning-label">Last warning:</t>
                </LoreLine>
                <LoreLine></LoreLine>
                <@warningcommons.warninglorelines warning=lastWarning/>
            </#if>

            <LoreLine></LoreLine>
            <@commons.line />
            <LoreLine>&7Left click to &6view warning history</LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem
             id="mute-summary"
             slot="6"
             if="config|mute-module.enabled"
             permission="config|permissions:mute-view"
             onLeftClick="manage-mutes/view/history?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             material="<#if model.mute.isPresent()>REDSTONE_TORCH<#else>LEVER</#if>">
        <#if model.mute.isPresent()>
            <name id="name-muted" class="item-name" color="&C">Muted</name>
        <#else>
            <name id="name-not-muted" class="item-name" color="&2">Not muted</name>
        </#if>
        <Lore>
            <#if model.mute.isPresent()>
                <LoreLine>
                    <t color="&6" id="current-mute-label">Current mute:</t>
                </LoreLine>
                <@mutecommons.mutelorelines mute=model.mute.get()/>
            </#if>

            <LoreLine></LoreLine>
            <@commons.line />
            <LoreLine>&7Left click to &6view mute history</LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem id="ban-summary"
             slot="7"
             if="config|ban-module.enabled"
             permission="config|permissions:ban-view"
             onLeftClick="manage-bans/view/history?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             material="<#if model.ban.isPresent()>BARRIER<#else>STONE</#if>">
        <#if model.ban.isPresent()>
            <name id="name-banned" class="item-name" color="&C">Banned</name>
        <#else>
            <name id="name-not-banned" class="item-name" color="&2">Not banned</name>
        </#if>
        <Lore>
            <#if model.ban.isPresent()>
                <LoreLine>&6Current ban:</LoreLine>
                <@bancommons.banlorelines ban=model.ban.get()/>
            </#if>
            <LoreLine></LoreLine>
            <@commons.line />
            <LoreLine>
                &7Left click to &6view ban history
            </LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem
             id="ipban-summary"
             slot="8"
             if="config|ban-module.ipban.enabled"
             permission="config|permissions:ipban.ban-view"
             material="<#if model.ipbans?has_content>TARGET<#else>COBBLESTONE</#if>">
        <#if model.ipbans?has_content>
            <name id="name-ip-banned" class="item-name" color="&C">Ip Banned</name>
        <#else>
            <name id="name-not-ip-banned" class="item-name" color="&2">Not Ip Banned</name>
        </#if>
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

    <GuiItem id="follow"
             if="${target.online?c}"
             permission="config|permissions:follow"
             slot="18"
             onLeftClick="staff-mode/follow?targetPlayerName=${target.username}"
             material="LEASH">
        <name class="item-name">Follow</name>
    </GuiItem>

    <GuiItem id="freeze"
             if="${target.online?c}"
             permission="config|permissions:freeze"
             slot="19"
             onLeftClick="manage-frozen/freeze?targetPlayerName=${target.username}"
             material="PACKED_ICE">
        <name class="item-name">Freeze</name>
    </GuiItem>

    <GuiItem id="cps"
             if="${target.online?c}"
             slot="20"
             onLeftClick="staff-mode/cps?targetPlayerName=${target.username}"
             material="WATCH">
        <name class="item-name">Cps</name>
    </GuiItem>

    <GuiItem id="teleport-to-player"
             if="${target.online?c}"
             slot="21"
             permission="config|permissions:teleport-to-player"
             onLeftClick="teleport?targetPlayerName=${target.username}"
             material="MAGENTA_GLAZED_TERRACOTTA">
        <name class="item-name">Teleport to</name>
    </GuiItem>

    <GuiItem id="teleport-here"
             if="${target.online?c}"
             permission="config|permissions:teleport-here"
             slot="22"
             onLeftClick="teleport-here?targetPlayerName=${target.username}" material="NETHER_STAR">
        <name class="item-name">Teleport here</name>
    </GuiItem>

    <GuiItem id="view-inventory"
             slot="23"
             onLeftClick="manage-inventory/open?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
            <#if target.online>
                permission="config|permissions:examine-inventory-interaction.online"
            <#else >
                permission="config|permissions:examine-inventory-interaction.offline"
            </#if>
             material="CHEST">
        <name class="item-name">View inventory</name>
    </GuiItem>
    <GuiItem id="View Enderchest"
             slot="24"
             onLeftClick="manage-enderchest/open?targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}"
             material="ENDER_CHEST">
        <name class="item-name">View Enderchest</name>
    </GuiItem>

    <#if backAction??>
        <@commons.backButton action=backAction backSlot=26/>
    </#if>
</TubingGui>