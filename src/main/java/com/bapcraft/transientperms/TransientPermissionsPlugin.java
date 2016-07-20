package com.bapcraft.transientperms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.bapcraft.transientperms.cmd.ApplyPermissionCommand;

public class TransientPermissionsPlugin extends JavaPlugin {
	
	public static TransientPermissionsPlugin instance;
	
	private Map<String, List<String>> permGroups;
	private List<TempPerm> activePermissions;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		this.saveDefaultConfig();
		this.loadConfig();
		
		this.getCommand("applytempperm").setExecutor(new ApplyPermissionCommand());
		
	}
	
	@Override
	public void onDisable() {
		
		this.permGroups = null;
		
		instance = null;
		
	}
	
	private void loadConfig() {
		
		this.permGroups = new HashMap<>();
		
		FileConfiguration fc = this.getConfig();
		
		ConfigurationSection groups = fc.getConfigurationSection("groups");
		Set<String> groupNames = groups.getValues(false).keySet();
		
		for (String group : groupNames) {
			
			List<String> permissions = groups.getStringList(group);
			this.permGroups.put(group, permissions);
			
		}
		
	}
	
	public List<String> getGroupPermissions(String groupName) {
		return this.permGroups.get(groupName);
	}
	
	public void applyTempPermissions(TempPerm perm) {
		
		perm.apply();
		this.validateTempPermissions();
		this.activePermissions.add(perm);
		
	}
	
	public void validateTempPermissions() {
		
		Iterator<TempPerm> iter = this.activePermissions.iterator();
		
		while (iter.hasNext()) {
			
			// Just remove the item if it's not active, it applies the permissions before adding it anyways.
			if (!iter.next().isActive()) iter.remove();
			
		}
		
	}
	
}
