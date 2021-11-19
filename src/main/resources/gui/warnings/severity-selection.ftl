<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="warning-severity-select">
    <title class="gui-title">Select severity</title>

    <#list severityLevels as level >
        <GuiItem id="severity-level-${level?index}"
                 class="severity-level severity-${level.name}"
                 slot="${level?index}"
                 material="PAPER"
                 onLeftClick="manage-warnings/warn?severity=${level.name}&targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}">
            <name class="item-name">${level.name}</name>
            <Lore>
                <LoreLine>
                    <t id="severity-label" color="&b" class="detail-label">Score: </t>
                    <t id="severity-value" color="&6" class="detail-value">${level.score}</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>

    <@commons.backButton action=backAction/>
</TubingGui>