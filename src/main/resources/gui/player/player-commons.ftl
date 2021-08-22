<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign BukkitUtils=statics['net.shortninja.staffplus.core.common.utils.BukkitUtils']>
<#import "/gui/commons/commons.ftl" as commons/>
<#macro playerhead slot sppPlayer onRightClick="$NOOP" onLeftClick="$NOOP" onMiddleClick="$NOOP" actions=[]>
    <#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <#assign session=GuiUtils.getSession(sppPlayer)/>
    <GuiItem slot="${slot}" onLeftClick="${onLeftClick}" onRightClick="${onRightClick}" onMiddleClick="${onMiddleClick}"
             material="PLAYER_HEAD" name="&5${sppPlayer.username}">
        <Lore>

            <#if sppPlayer.online>
                <@commons.line />
                <LoreLine permission="config|permissions:playerView.detail.ip">&bIp: &6${BukkitUtils.getIpFromPlayer(sppPlayer.player)}</LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.gamemode">&bGamemode: &6${sppPlayer.player.gameMode}</LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.food">&bFood: &6${sppPlayer.player.foodLevel}</LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.location">&bLocation: &6${GuiUtils.parseLocation(sppPlayer.player.location)}</LoreLine>
            </#if>

            <#if session.isPresent()>
                <@commons.line />
                <LoreLine if="${session.get().frozen?c}"
                          permission="config|permissions:playerView.detail.frozen">
                    &bFrozen
                </LoreLine>
                <LoreLine if="${session.get().protected?c}"
                          permission="config|permissions:playerView.detail.protected">
                    &2Protected
                </LoreLine>
                <LoreLine if="${session.get().underInvestigation?c}"
                          permission="config|permissions:playerView.detail.investigation">
                    &6Under investigation
                </LoreLine>
                <LoreLine if="${session.get().muted?c}"
                          permission="config|permissions:playerView.detail.muted">
                    &CMuted
                </LoreLine>
            </#if>

            <@commons.line />
            <LoreLine if="${sppPlayer.online?c}">&2ONLINE</LoreLine>
            <LoreLine if="${(!sppPlayer.online)?c}">&COFFLINE</LoreLine>
            <#if actions?has_content >
                <LoreLine></LoreLine>
                <#list actions as actionLine>
                    <LoreLine>${actionLine}</LoreLine>
                </#list>
            </#if>
        </Lore>
    </GuiItem>
</#macro>