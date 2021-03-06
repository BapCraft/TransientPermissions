package com.bapcraft.transientperms;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class TempPerm {
	
	public final Player player;
	public final List<String> permissions;
	
	public final long durationTicks;
	
	private int applications = 0;
	private int expirations = 0;
	
	public TempPerm(Player p, List<String> perms, long dur) {
		
		this.player = p;
		this.permissions = perms;
		
		this.durationTicks = dur;
		
	}
	
	public void apply() {
		
		final PermissionAttachment at = this.player.addAttachment(TransientPermissionsPlugin.instance);
		
		// Add the permission.
		this.permissions.forEach(s -> at.setPermission(s, true));
		this.player.recalculatePermissions();
		
		this.applications++;
		
		Bukkit.getLogger().info("Applied " + this.permissions.size() + " permissions (" + Arrays.toString(this.permissions.toArray()) + ") to " + this.player.getName());
		
		// Now remove it later.
		Bukkit.getScheduler().scheduleSyncDelayedTask(TransientPermissionsPlugin.instance, () -> {
			
			if (this.player.isOnline()) {
				
				this.player.removeAttachment(at);
				this.player.recalculatePermissions();
				this.player.sendMessage(ChatColor.RED + "Your extra permissions have expired.");
				
			}
			
			this.expirations++;
			
			Bukkit.getLogger().info("The " + this.permissions.size() + " on " + this.player.getName() + " have expired.");
			
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
