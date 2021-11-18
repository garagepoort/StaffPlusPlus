<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#import "/gui/bans/ban-commons.ftl" as banCommons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>

<TubingGui size="54" id="investigation-detail">
    <title class="gui-title">Player: ${ban.targetName}</title>

    <@banCommons.banitem itemId="ban-info" slot=13 ban=ban/>

    <#list [30,31,32,39,40,41] as slot>
        <GuiItem slot="${slot}"
                 id="unban-${slot?index}"
                 material="RED_STAINED_GLASS_PANE"
                 onLeftClick="manage-bans/unban?banId=${ban.id}">
            <name class="item-name">Unban player</name>
            <Lore>
                <LoreLine>
                    <t id="unban-action-label">Click to unban this player</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>

    <#if backAction??>
        <@commons.backButton action=backAction/>
    </#if>
</TubingGui>