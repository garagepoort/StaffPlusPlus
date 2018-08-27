package net.shortninja.staffplus.util.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

/**
 * @author Shortninja, DarkSeraphim, ...
 */

public class JavaUtils
{
	/**
	 * Uses #valueOf() to check if an enum is valid.
	 * 
	 * @param enumClass Enum class to check from.
	 * @param enumName Enum value to check for.
	 * @return Whether or not the enum is valid for the given class.
	 */
    public static boolean isValidEnum(Class enumClass, String enumName)
    {
        if(enumClass == null || enumName == null)
        {
            return false;
        }
        
        try
        {
            Enum.valueOf(enumClass, enumName);
            return true;
        }catch(IllegalArgumentException ex)
        {
            return false;
        }
    }
    
    /**
     * Tries to parse an integer with #parseInt() and Catches NumberFormatException.
     * 
     * @param string The string to parse.
     * @return Whether or not the string can be parsed as an integer.
     */
	public static boolean isInteger(String string)
	{
		boolean isInteger = true;
		
		try
		{
			Integer.parseInt(string);
		}catch(NumberFormatException exception)
		{
			isInteger = false;
		}
		
		return isInteger;
	}
	
	/**
	 * Splits a string with commas in it into a string List.
	 * 
	 * @param commas The string to split.
	 * @return The split string List.
	 */
	public static List<String> stringToList(String commas)
	{
		List<String> list = new ArrayList<String>();
		ListIterator<String> iterator = Arrays.asList(commas.split("\\s*,\\s*")).listIterator();
		
		if(iterator != null)
		{
			while(iterator.hasNext())
			{
				list.add(iterator.next());
			}
		}else list = Arrays.asList(commas);
		
		return list;
	}
	
	/**
	 * Inserts a commas between each word in the given string with StringBuilder.
	 * 
	 * @param string The string to insert commas in.
	 * @return New string with commas.
	 */
	public static String insertCommas(String string)
	{
		StringBuilder builder = new StringBuilder();
		String[] words = string.split(" ");
		
		for(int i = 0; i < words.length; i++)
		{
			String word = words[i];
			String suffix = ",";
			
			if((i + 1) == words.length)
			{
				suffix = "";
			}
			
			builder.append(word + suffix);
		}
		
		return builder.toString();
	}
	
	/**
	 * Uses StringBuilder to compile a string from an array and a start index.
	 * 
	 * @param args The array of strings.
	 * @param index The index to start appending from.
	 * @return The built string.
	 */
	public static String compileWords(String[] args, int index)
	{
		StringBuilder builder = new StringBuilder();
		
		for(int i = index; i < args.length; i++)
		{
			builder.append(args[i] + " ");
		}
		
		return builder.toString().trim();
	}
	
	/**
	 * Directly reverses the order of an array. Copied from the
	 * org.apache.commons.ArrayUtils class.
	 * 
	 * @param array The array to reverse.
	 */
	public static void reverse(Object array[])
	{
		if(array == null)
		{
			return;
		}else
		{
			int i = 0;
			
			for(int j = Math.min(array.length, array.length) - 1; j > i; i++)
			{
				Object tmp = array[j];
				array[j] = array[i];
				array[i] = tmp;
				j--;
			}
		}
	}
	
	/**
	 * Gets the slot of the given item through iteration.
	 * 
	 * @param inventory The inventory to iterate through.
	 * @param item The ItemStack to check for.
	 * @return The found slot in the inventory.
	 */
	public static int getItemSlot(PlayerInventory inventory, ItemStack item)
	{
		int slot = 0;
		ItemStack[] contents = inventory.getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			ItemStack current = contents[i];
			
			if(current == null)
			{
				continue;
			}
			
			if(current.equals(item))
			{
				slot = i;
				break;
			}
		}
		
		return slot;
	}
	
	/**
	 * Completely clears a player's inventory, from armor to contents.
	 * 
	 * @param player Player with the inventory to be cleared.
	 */
	public static void clearInventory(Player player)
	{
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
	}
	
	/**
	 * "Serializes" the Location with simple string concatenation.
	 * 
	 * @param location The Location to serialize.
	 * @return String in the format of "x, y, z".
	 */
	public static String serializeLocation(Location location)
	{
		return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
	}
	
	/**
	 * Makes a Material enum type more readable.
	 * 
	 * @param type The Material type to format.
	 * @return The formatted string.
	 */
	public static String formatTypeName(Material type)
	{
		return type.name().replace("_", " ").toLowerCase();
	}
	
	/**
	 * A version independent way to get all online players. Some versions of Bukkit
	 * return an array while others return a Collection, so this is safe and easy.
	 * 
	 * @return List of all online players from Bukkit#getOnlinePlayers().
	 */
	public static List<Player> getOnlinePlayers()
	{
		List<Player> onlinePlayers = new ArrayList<Player>();
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			onlinePlayers.add(player);
		}
		
		return onlinePlayers;
	}
	
	/**
	 * Uses the player's location subtracted from a nearby player's location and
	 * then gets angle with Vector#dot().
	 * 
	 * @param player Player to check for direction.
	 * @return Gets target player by using all players within a radius of six blocks
	 * from the given player.
	 */
	public static Player getTargetPlayer(Player player)
	{
		Location location = player.getLocation();
		Player targetPlayer = null;
		List<Player> nearbyPlayers = new ArrayList<Player>();
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(player.getWorld() != p.getWorld())
			{
				continue;
			}
			
			if(location.distanceSquared(p.getLocation()) > 36 || player.getName().equals(p.getName()))
			{
				continue;
			}
			
			Vector targetVector = p.getLocation().toVector().subtract(location.toVector()).normalize();
			
			if(Math.round(targetVector.dot(location.getDirection())) == 1)
			{
				targetPlayer = p;
				break;
			}
		}
		
		return targetPlayer;
	}

	public static String fixString(String string) {
		String first = string.substring(0, 1);
		String rest = string.substring(1,string.length()).toLowerCase();
		string = first+rest;
		System.out.println(string);
		int index = string.indexOf("_");
		char upper = Character.toUpperCase(string.charAt(index+1));
		StringBuilder sb = new StringBuilder(string);
		sb.setCharAt(index+1,upper);
		return sb.toString();
	}

	/**
	 * Checks if the player's inventory has space with #firstEmpty()
	 * 
	 * @param player Player to check for inventory space.
	 * @return Whether or not the player has any inventory space.
	 */
	public static boolean hasInventorySpace(Player player)
	{
		return player.getInventory().firstEmpty() != -1;
	}
}