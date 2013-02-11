package org.c4k3.Misc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/* This class is responsible for handling the /setspawn command
 * Setspawn changes the spawn of the world the sender is
 * to the location the sender is at
 */
public class Setspawn implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}
		
		if ( player != null ) {
			
			if ( player.isOp()) {
				
				Location location = player.getLocation();
				
				int x = location.getBlockX();
				
				int y = location.getBlockY();
				
				int z = location.getBlockZ();
				
				player.getWorld().setSpawnLocation(x, y, z);
				
				player.sendMessage(ChatColor.GOLD + "Spawn location has been set");
				
			} else {
				/* player is not OP */
				player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return true;
			}
			
			
		} else {
			/* Player is null */
			sender.sendMessage("You must be a player to use this command");
			return true;
		}
		
		return false;
	}

}
