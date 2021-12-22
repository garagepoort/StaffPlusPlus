<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#import "/gui/investigations/investigation-commons.ftl" as investigationCommons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>

<TubingGui size="54" id="investigation-detail">
    <title class="gui-title">Manage Investigation</title>

    <@investigationCommons.investigationItem  itemId="investigation-info" slot=13 investigation=investigation/>

    <GuiItem slot="11"
             id="notes-overview"
             material="PAPER"
             onLeftClick="manage-investigation-notes/view?investigationId=${investigation.id}&backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Notes</name>
        <Lore>
            <LoreLine>
                <t id="notes-overview-label">Go to notes overview</t>
            </LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem slot="15"
             id="evidence-overview"
             material="ANVIL"
             onLeftClick="manage-investigation-evidence/view?investigationId=${investigation.id}&backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Evidence</name>
        <Lore>
            <LoreLine>
                <t id="evidence-overview-label">Go to evidence overview</t>
            </LoreLine>
        </Lore>
    </GuiItem>

    <#if investigation.status.name() == 'OPEN'
        && player.uniqueId == investigation.investigatorUuid
        && $permissions.has(player, $config.get("permissions:investigations.manage.investigate"))>
        <#list [27,28,36,37] as slot>
            <GuiItem slot="${slot}"
                     id="pause-${slot?index}"
                     material="WHITE_STAINED_GLASS_PANE"
                     onLeftClick="manage-investigations/pause">
                <name class="item-name">Pause</name>
                <Lore>
                    <LoreLine>
                        <t id="pause-action-label">Click to take a break investigating</t>
                    </LoreLine>
                </Lore>
            </GuiItem>
        </#list>
    </#if>

    <#if investigation.status.name() == 'PAUSED'
        && $permissions.has(player, $config.get("permissions:investigations.manage.investigate"))>
        <#list [27,28,36,37] as slot>
            <GuiItem slot="${slot}"
                     id="resume-${slot?index}"
                     material="GREEN_GLAZED_TERRACOTTA"
                     onLeftClick="manage-investigations/resume?investigationId=${investigation.id}">
                <name class="item-name">Resume</name>
                <Lore>
                    <LoreLine>
                        <t id="resume-action-label">Click to resume this investigation.</t>
                    </LoreLine>
                </Lore>
            </GuiItem>
        </#list>
    </#if>

    <#if investigation.status.name() != 'CONCLUDED'
        && $permissions.has(player, $config.get("permissions:investigations.manage.investigate"))>
        <#list [34,35,43,44] as slot>
            <GuiItem slot="${slot}"
                     id="resume-${slot?index}"
                     material="RED_GLAZED_TERRACOTTA"
                     onLeftClick="manage-investigations/conclude?investigationId=${investigation.id}">
                <name class="item-name">Conclude</name>
                <Lore>
                    <LoreLine>
                        <t id="resume-action-label">Click to conclude this investigation</t>
                    </LoreLine>
                </Lore>
            </GuiItem>
        </#list>
    </#if>

    <@commons.backButton action=backAction/>
</TubingGui>