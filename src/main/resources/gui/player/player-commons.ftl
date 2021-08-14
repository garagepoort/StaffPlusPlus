<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#macro playerhead slot playername onRightClick="$NOOP" onLeftClick="$NOOP" onMiddleClick="$NOOP" actions=[]>
    <#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <GuiItem slot="${slot}" onLeftClick="${onLeftClick}" onRightClick="${onRightClick}" onMiddleClick="${onMiddleClick}"
             material="PLAYER_HEAD" name="&5${playername}">
        <Lore>
            <LoreLine></LoreLine>

            <#if actions?has_content >
                <LoreLine></LoreLine>
                <#list actions as actionLine>
                    <LoreLine>${actionLine}</LoreLine>
                </#list>
            </#if>
        </Lore>
    </GuiItem>
</#macro>