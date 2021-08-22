<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGUi size="27">
    <title>Select player overview</title>
    <GuiItem slot="10"
             material="SKULL_ITEM"
             name="Online players"
             permission="config|permissions:playerView.overview.online-players"
             onLeftClick="players/view/overview/online?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all online players</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="11"
             material="SKULL_ITEM"
             name="Offline players"
             permission="config|permissions:playerView.overview.offline-players"
             onLeftClick="players/view/overview/offline?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all offline players</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="12"
             permission="config|permissions:playerView.overview.staff-members"
             material="TOTEM"
             name="Online staff members"
             onLeftClick="membersGUI?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all online staff members</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="13"
             material="DIAMOND_PICKAXE"
             permission="config|permissions:playerView.overview.miners"
             name="Miners"
             onLeftClick="miners/view?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all miners</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="14"
             if="config|protect-module.player-enabled"
             material="SHIELD"
             permission="config|permissions:playerView.overview.protected"
             name="Protected Players"
             onLeftClick="protected-players/view?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show players protected by Staff++</LoreLine>
        </Lore>
    </GuiItem>
</TubingGUi>