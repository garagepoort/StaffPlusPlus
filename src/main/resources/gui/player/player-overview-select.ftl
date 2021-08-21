<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGUi size="27">
    <title>Select player overview</title>
    <GuiItem slot="10"
             material="PLAYER_HEAD"
             name="Online players"
             onLeftClick="players/view/overview/online?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all online players</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="11"
             material="SKELETON_SKULL"
             name="Offline players"
             onLeftClick="players/view/overview/offline?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all offline players</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="12"
             material="TOTEM_OF_UNDYING"
             name="Online staff members"
             onLeftClick="membersGUI?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all online staff members</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="13"
             material="DIAMOND_PICKAXE"
             name="Miners"
             onLeftClick="miners/view?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all miners</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="14"
             if="config|protect-module.player-enabled"
             material="SHIELD"
             name="Protected Players"
             onLeftClick="protected-players/view?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show players protected by Staff++</LoreLine>
        </Lore>
    </GuiItem>
</TubingGUi>