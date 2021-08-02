<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#macro evidenceButton slot evidence backAction>
    <#if .data_model["investigations-module.enabled"]>
        <GuiItem slot="${slot}"
                 onLeftClick="manage-investigation-evidence/view/investigation-link?backAction=${URLEncoder.encode(backAction)}&evidenceId=${evidence.id}&evidenceType=${evidence.evidenceType}&evidenceDescription=${evidence.description}"
                 name="Add this as evidence to investigation"
                 material="ANVIL">
            <Lore>
                <LoreLine>Click to link evidence to investigation</LoreLine>
            </Lore>
        </GuiItem>
    </#if>
</#macro>