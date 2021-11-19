<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="kick-reason-select">
    <title class="gui-title">Select reason</title>

    <#list reasons as reason >
        <GuiItem id="kick-reason-${reason?index}"
                 class="kick-reason kick-reason-${reason.reason}"
                 slot="${reason?index}"
                 material="${reason.material}"
                 onLeftClick="manage-kicks/kick?reason=${reason.reason}&targetPlayerName=${target.username}&backAction=${URLEncoder.encode(currentAction)}">
            <name class="item-name">${reason.reason}</name>
            <Lore>
                <LoreLine>
                    <t id="kick-reason-label">${reason.lore}</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>
</TubingGui>