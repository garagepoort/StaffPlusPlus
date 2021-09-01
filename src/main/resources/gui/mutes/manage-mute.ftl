<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#import "/gui/mutes/mute-commons.ftl" as muteCommons/>
<TubingGui size="54">
    <title>Player: ${mute.targetName}</title>
    <GuiItem slot="13"
             name="&3Mute"
             onLeftClick="$NOOP"
             material="BANNER"
    >
        <@muteCommons.mutelorelines mute=mute />
    </GuiItem>
    <@evidenceCommons.evidenceButton slot=14 evidence=mute backAction=currentAction />
    <#list [30,31,32,39,40,41] as slot>
        <GuiItem
                slot="${slot}"
                name="Unmute player"
                onLeftClick="manage-mutes/unmute?muteId=${mute.id}"
                material="RED_GLAZED_TERRACOTTA">
            <Lore>
                <LoreLine>"Click to unmute this player"</LoreLine>
            </Lore>
        </GuiItem>
    </#list>
    <#if backAction??>
        <@commons.backButton action=backAction backSlot=49/>
    </#if>
</TubingGui>