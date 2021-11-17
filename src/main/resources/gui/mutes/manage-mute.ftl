<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#import "/gui/mutes/mute-commons.ftl" as muteCommons/>
<TubingGui size="54" id="manage-mute">
    <title class="gui-title">Player: ${mute.targetName}</title>
    <GuiItem id="info"
             slot="13"
             onLeftClick="$NOOP"
             material="PLAYER_HEAD"
    >
        <name class="item-name" color="&3">Mute</name>
        <@muteCommons.mutelorelines mute=mute />
    </GuiItem>
    <@evidenceCommons.evidenceButton slot=14 evidence=mute backAction=currentAction />
    <#list [30,31,32,39,40,41] as slot>
        <GuiItem id="unmute-${slot?index}"
                 slot="${slot}"
                 onLeftClick="manage-mutes/unmute?muteId=${mute.id}"
                 material="RED_STAINED_GLASS_PANE">
            <name class="item-name">Unmute player</name>
            <Lore>
                <LoreLine>
                    <t id="unmute-label">Click to unmute this player</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>
    <#if backAction??>
        <@commons.backButton action=backAction backSlot=49/>
    </#if>
</TubingGui>