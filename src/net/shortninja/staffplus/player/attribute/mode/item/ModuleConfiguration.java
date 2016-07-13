package net.shortninja.staffplus.player.attribute.mode.item;

import org.bukkit.inventory.ItemStack;

public class ModuleConfiguration
{
	private ModuleType moduleType;
	private String identifier;
	private int slot;
	private ItemStack item;
	private String action;
	
	public ModuleConfiguration(String identifier, ModuleType moduleType, int slot, ItemStack item, String action)
	{
		this.moduleType = moduleType;
		this.slot = slot;
		this.item = item;
		this.action = action;
	}
	
	public String getIdentifier()
	{
		return identifier;
	}
	
	public ModuleType getType()
	{
		return moduleType;
	}
	
	public int getSlot()
	{
		return slot;
	}
	
	public ItemStack getItem()
	{
		return item;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public void setItem(ItemStack item)
	{
		this.item = item;
	}
	
	public enum ModuleType
	{
		COMMAND_STATIC, COMMAND_DYNAMIC, COMMAND_CONSOLE, ITEM;
	}
}