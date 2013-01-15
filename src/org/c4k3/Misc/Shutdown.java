package org.c4k3.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/* Class responsible for handling /shutdown command
 * 
 * Shuts down server after 30 seconds, and announces
 * the shutdown to all players every 10 seconds
 */
public class Shutdown implements CommandExecutor {
	
	/* Only run on /shutdown command
	 * because executor is set in Misc.onEnable
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}
		
		if ( player == null || player.isOp() ) {
			
			
			
			return true;
			
		} else {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
		}
		
		return false;
	}

}
