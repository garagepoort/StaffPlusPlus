package net.shortninja.staffplus.core.domain.staff.mode.config;

import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.World;

import java.util.List;
import java.util.Map;

public class GeneralModeConfiguration {

    private String name;
    private String permission;
    private int weight;
    private VanishType modeVanish;
    private boolean modeItemPickup;
    private boolean modeItemDrop;
    private boolean modeDamage;
    private boolean modeHungerLoss;
    private List<ConfiguredAction> modeEnableCommands;
    private List<ConfiguredAction> modeDisableCommands;
    private boolean disableOnWorldChange;
    private boolean modeBlockManipulation;
    private boolean modeInventoryInteraction;
    private boolean modeSilentChestInteraction;
    private boolean modeInvincible;
    private boolean modeFlight;
    private boolean modeCreative;
    private boolean modeOriginalLocation;
    private boolean modeEnableOnLogin;
    private boolean modeDisableOnLogout;
    private Map<String, Integer> itemSlots;

    private List<String> validWorlds;


    public GeneralModeConfiguration() {
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isModeItemPickup() {
        return modeItemPickup;
    }

    public boolean isModeItemDrop() {
        return modeItemDrop;
    }

    public VanishType getModeVanish() {
        return modeVanish;
    }

    public boolean isModeDamage() {
        return modeDamage;
    }

    public boolean isModeHungerLoss() {
        return modeHungerLoss;
    }

    public List<ConfiguredAction> getModeEnableCommands() {
        return modeEnableCommands;
    }

    public List<ConfiguredAction> getModeDisableCommands() {
        return modeDisableCommands;
    }

    public boolean isDisableOnWorldChange() {
        return disableOnWorldChange;
    }

    public boolean isModeBlockManipulation() {
        return modeBlockManipulation;
    }

    public boolean isModeInventoryInteraction() {
        return modeInventoryInteraction;
    }

    public boolean isModeSilentChestInteraction() {
        return modeSilentChestInteraction;
    }

    public boolean isModeInvincible() {
        return modeInvincible;
    }

    public boolean isModeFlight() {
        return modeFlight;
    }

    public boolean isModeCreative() {
        return modeCreative;
    }

    public boolean isModeOriginalLocation() {
        return modeOriginalLocation;
    }

    public boolean isModeEnableOnLogin() {
        return modeEnableOnLogin;
    }

    public boolean isModeDisableOnLogout() {
        return modeDisableOnLogout;
    }

    public void setModeVanish(VanishType modeVanish) {
        this.modeVanish = modeVanish;
    }

    public void setModeItemPickup(boolean modeItemPickup) {
        this.modeItemPickup = modeItemPickup;
    }

    public void setModeItemDrop(boolean modeItemDrop) {
        this.modeItemDrop = modeItemDrop;
    }

    public void setModeDamage(boolean modeDamage) {
        this.modeDamage = modeDamage;
    }

    public void setModeHungerLoss(boolean modeHungerLoss) {
        this.modeHungerLoss = modeHungerLoss;
    }

    public void setDisableOnWorldChange(boolean disableOnWorldChange) {
        this.disableOnWorldChange = disableOnWorldChange;
    }

    public void setModeBlockManipulation(boolean modeBlockManipulation) {
        this.modeBlockManipulation = modeBlockManipulation;
    }

    public void setModeInventoryInteraction(boolean modeInventoryInteraction) {
        this.modeInventoryInteraction = modeInventoryInteraction;
    }

    public void setModeSilentChestInteraction(boolean modeSilentChestInteraction) {
        this.modeSilentChestInteraction = modeSilentChestInteraction;
    }

    public void setModeInvincible(boolean modeInvincible) {
        this.modeInvincible = modeInvincible;
    }

    public void setModeFlight(boolean modeFlight) {
        this.modeFlight = modeFlight;
    }

    public void setModeCreative(boolean modeCreative) {
        this.modeCreative = modeCreative;
    }

    public void setModeOriginalLocation(boolean modeOriginalLocation) {
        this.modeOriginalLocation = modeOriginalLocation;
    }

    public void setModeEnableOnLogin(boolean modeEnableOnLogin) {
        this.modeEnableOnLogin = modeEnableOnLogin;
    }

    public void setModeDisableOnLogout(boolean modeDisableOnLogout) {
        this.modeDisableOnLogout = modeDisableOnLogout;
    }

    public void setModeEnableCommands(List<ConfiguredAction> modeEnableCommands) {
        this.modeEnableCommands = modeEnableCommands;
    }

    public void setModeDisableCommands(List<ConfiguredAction> modeDisableCommands) {
        this.modeDisableCommands = modeDisableCommands;
    }

    public Map<String, Integer> getItemSlots() {
        return itemSlots;
    }

    public void setItemSlots(Map<String, Integer> itemSlots) {
        this.itemSlots = itemSlots;
    }

    public void setValidWorlds(List<String> validWorlds) {
        this.validWorlds = validWorlds;
    }

    public boolean isModeValidInWorld(World world) {
        if(validWorlds == null) {
            return true;
        }
        return validWorlds.contains(world.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
