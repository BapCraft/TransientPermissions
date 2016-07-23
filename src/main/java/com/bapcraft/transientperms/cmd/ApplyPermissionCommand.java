package com.bapcraft.transientperms.cmd;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bapcraft.transientperms.Perms;
import com.bapcraft.transientperms.TempPerm;
import com.bapcraft.transientperms.TransientPermissionsPlugin;

public class ApplyPermissionCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender.isOp() || sender.hasPermission(Perms.CMD_APPLY_PERMISSION))) {
			
			sender.sendMessage("You do not have permission to use this command");
			return true;
			
		}
		
		// Invalid arguments.
		if (args.length != 3) return false;
		
		// Pull values.
		String applyeeStr = args[0];
		String group = args[1];
		String durationStr = args[2];
		
		// Parse username
		Player applyee = Bukkit.getPlayer(applyeeStr);
		if (applyee == null) {
			
			sender.sendMessage("That player is not online!  They must be online for this command to work!");
			return true;
			
		}
		
		// Parse group
		List<String> permissions = TransientPermissionsPlugin.instance.getGroupPermissions(group);
		if (permissions == null) {
			
			sender.sendMessage("That's not a valid group name.  Did you check capitalization?");
			return true;
			
		}
		
		// Parse duration
		long duration = -1;
		try {
			duration = Long.parseLong(durationStr);
		} catch (NumberFormatException nfe) {
			
			sender.sendMessage("That's not a valid number!  Duration must be in set " + ChatColor.ITALIC + "[0, " + Long.MAX_VALUE + "] U " + ChatColor.BOLD + "Z" + ChatColor.RESET + ".");
			return true;
			
		}
		
		// Assemble the permissions.
		TempPerm perm = new TempPerm(applyee, permissions, duration);
		
		// Then apply and report.
		TransientPermissionsPlugin.instance.applyTempPermissions(perm);
		long secs = duration / 20;
		sender.sendMessage(ChatColor.GREEN + "Applied " + permissions.size() + " permission(s) to " + applyee.getName() + "!  They will expire in ~" + secs + " tick(s), or when they log out.");
		applyee.sendMessage(ChatColor.GREEN + "You have been given the \"" + group + "\" set of permissions for ~" + secs + " seconds!");
		
		return true;
		
	}

}
