package com.bapcraft.transientperms;

import org.bukkit.plugin.java.JavaPlugin;

public class TransientPermissionsPlugin extends JavaPlugin {
	
	public static TransientPermissionsPlugin instance;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
	}
	
	@Override
	public void onDisable() {
		
		instance = null;
		
	}
	
}
