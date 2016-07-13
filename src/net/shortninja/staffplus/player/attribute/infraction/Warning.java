package net.shortninja.staffplus.player.attribute.infraction;

import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Options;

public class Warning
{
	private Options options = StaffPlus.get().options;
	private UUID uuid;
	private String name;
	private String reason;
	private String issuerName;
	private UUID issuerUuid;
	private long time;
	
	public Warning(UUID uuid, String name, String reason, String issuerName, UUID issuerUuid, long time)
	{
		this.uuid = uuid;
		this.name = name;
		this.reason = reason;
		this.issuerName = issuerName;
		this.issuerUuid = issuerUuid;
		this.time = time;
	}
	
	public UUID getUuid()
	{
		return uuid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getReason()
	{
		return reason;
	}
	
	public String getIssuerName()
	{
		return issuerName;
	}
	
	public UUID getIssuerUuid()
	{
		return issuerUuid;
	}
	
	public long getTime()
	{
		return time;
	}
	
	/*
	 * This is only required in order to keep warning names up to date when the
	 * issuer changes his or her name.
	 */
	public void setIssuerName(String issuerName)
	{
		this.issuerName = issuerName;
	}
	
	public boolean shouldRemove()
	{
		boolean shouldRemove = false;
		
		if((System.currentTimeMillis() - time) >= options.warningsClear)
		{
			shouldRemove = true;
		}
		
		return shouldRemove;
	}
}