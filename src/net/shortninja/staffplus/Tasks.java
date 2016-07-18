package net.shortninja.staffplus;

import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.infraction.InfractionCoordinator;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.player.attribute.mode.handler.FreezeHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.GadgetHandler;
import net.shortninja.staffplus.server.AlertCoordinator;

import org.bukkit.scheduler.BukkitRunnable;

//TODO: Remove debug.

public class Tasks extends BukkitRunnable
{
	private Options options = StaffPlus.get().options;
	private UserManager userManager = StaffPlus.get().userManager;
	private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;
	private GadgetHandler gadgetHandler = StaffPlus.get().gadgetHandler;
	private InfractionCoordinator infractionCoordinator = StaffPlus.get().infractionCoordinator;
	private AlertCoordinator alertCoordinator = StaffPlus.get().alertCoordinator;
	private int interval;
	private long now;
	private long later;
	
	public Tasks()
	{
		interval = 0;
		now = System.currentTimeMillis();
		runTaskTimer(StaffPlus.get(), options.clock, options.clock);
	}
	
	@Override
	public void run()
	{
		checkWarnings();
		decideAutosave();
		freezeHandler.checkLocations();
		gadgetHandler.updateGadgets();
	}
	
	private void checkWarnings()
	{
		for(Warning warning : infractionCoordinator.getWarnings())
		{
			if(warning.shouldRemove())
			{
				User user = userManager.getUser(warning.getUuid());
				
				if(user == null)
				{
					continue;
				}
				
				user.removeWarning(warning.getUuid());
			}
		}
	}
	
	private void decideAutosave()
	{
		later = System.currentTimeMillis();
		
		if((later - now) >= 1000)
		{
			interval += (int) ((later - now) / 1000);
			now = System.currentTimeMillis();
		}
		
		if(interval >= options.autoSave && interval != 0)
		{
			StaffPlus.get().saveUsers();
			StaffPlus.get().message.sendConsoleMessage("Staff+ is now auto saving...", false);
			interval = 0;
		}else if(options.autoSave <= 0 && interval >= 1800)
		{
			alertCoordinator.clearNotified();
		}
	}
}