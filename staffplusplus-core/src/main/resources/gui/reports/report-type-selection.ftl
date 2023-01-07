<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="report-type-select">
    <title class="gui-title">Select type for report</title>

    <#list types as type >
        <#if skipReasonSelect>
            <GuiItem id="report-type-${type?index}"
                     class="report-type report-type-${type.type}"
                     slot="${type?index}"
                     material="${type.material}"
                    <#if culprit?has_content>
                        onLeftClick="reports/create?reason=${URLEncoder.encode(reason)}&type=${type.type}&culprit=${culprit}"
                    <#else >
                        onLeftClick="reports/create?reason=${URLEncoder.encode(reason)}&type=${type.type}"
                    </#if>
            >
                <name class="item-name">${type.type}</name>
                <Lore>
                    <LoreLine>
                        <t id="report-type-label">${type.lore}</t>
                    </LoreLine>
                </Lore>
            </GuiItem>
        <#else >
            <GuiItem id="report-type-${type?index}"
                     class="report-type report-type-${type.type}"
                     slot="${type?index}"
                     material="${type.material}"
                    <#if culprit?has_content>
                        onLeftClick="reports/view/reason-select?type=${type.type}&culprit=${culprit}"
                    <#else >
                        onLeftClick="reports/view/reason-select?type=${type.type}"
                    </#if>
            >
                <name class="item-name">${type.type}</name>
                <Lore>
                    <LoreLine>
                        <t id="report-type-label">${type.lore}</t>
                    </LoreLine>
                </Lore>
            </GuiItem>
        </#if>
    </#list>
</TubingGui>