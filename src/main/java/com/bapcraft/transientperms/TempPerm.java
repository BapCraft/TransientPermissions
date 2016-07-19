package com.bapcraft.transientperms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class TempPerm {
	
	public final Player player;
	public final String permission;
	public final boolean state;
	
	public final long durationTicks;
	
	private int applications = 0;
	private int expirations = 0;
	
	public TempPerm(Player p, String perm, boolean state, long dur) {
		
		this.player = p;
		this.permission = perm;
		this.state = state;
		
		this.durationTicks = dur;
		
	}
	
	public TempPerm(Player p, String perm, long dur) {
		this(p, perm, true, dur);
	}
	
	public void apply() {
		
		final PermissionAttachment at = this.player.addAttachment(TransientPermissionsPlugin.instance);
		
		// Add the permission.
		at.setPermission(this.permission, this.state);
		this.player.recalculatePermissions();
		
		this.applications++;
		
		// Now remove it later.
		Bukkit.getScheduler().scheduleSyncDelayedTask(TransientPermissionsPlugin.instance, () -> {
			
			this.player.removeAttachment(at);
			this.player.recalculatePermissions();
			
			this.expirations++;
			
		}, this.durationTicks);
		
	}
	
	public int getApplications() {
		return this.applications;
	}
	
	public int getExpirations() {
		return this.expirations;
	}
	
	public int getRunningApplications() {
		return this.getApplications() - this.getExpirations();
	}
	
	public boolean isActive() {
		return this.getRunningApplications() > 0;
	}
	
}
