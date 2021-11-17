<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="27" id="players-overview-select">
    <title class="gui-title">Select player overview</title>
    <GuiItem id="online-players"
             slot="10"
             material="SKULL_ITEM"
             permission="config|permissions:playerView.overview.online-players"
             onLeftClick="players/view/overview/online?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Online players</name>
        <Lore>
            <LoreLine>
                <t id="label">Show all online players</t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="offline-players"
             slot="11"
             material="SKULL_ITEM"
             permission="config|permissions:playerView.overview.offline-players"
             onLeftClick="players/view/overview/offline?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Offline players</name>
        <Lore>
            <LoreLine>
                <t id="label">Show all offline players</t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="online-staff"
             slot="12"
             permission="config|permissions:playerView.overview.staff-members"
             material="TOTEM"
             onLeftClick="membersGUI?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Online staff members</name>
        <Lore>
            <LoreLine>
                <t id="label">Show all offline staff members</t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="miners"
             slot="13"
             material="DIAMOND_PICKAXE"
             permission="config|permissions:playerView.overview.miners"
             onLeftClick="miners/view?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Miners</name>
        <Lore>
            <LoreLine>
                <t id="label">Show all miners</t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="protected-players"
             slot="14"
             if="config|protect-module.player-enabled"
             material="SHIELD"
             permission="config|permissions:playerView.overview.protected"
             onLeftClick="protected-players/view?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Protected Players</name>
        <Lore>
            <LoreLine>
                <t id="label">Show players protected by Staff++</t>
            </LoreLine>
        </Lore>
    </GuiItem>
</TubingGui>