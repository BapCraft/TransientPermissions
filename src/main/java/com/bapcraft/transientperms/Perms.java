package com.bapcraft.transientperms;

public class Perms {
	
	public static final String CMD_APPLY_PERMISSION = "transientperms.cmd.apply";
	
	public static String CMD_APPLY_PERM_SPECIFIC(String group) {
		return String.format("transientperms.cmd.applyspecific.%s", group.toLowerCase());
	}
	
}
