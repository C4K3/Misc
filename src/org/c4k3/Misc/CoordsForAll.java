package org.c4k3.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoordsForAll implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}
		
		if(cmd.getName().equalsIgnoreCase("coords")){
			
			if (player == null) {
				
				if (args.length < 1){
					Bukkit.getLogger().info("You need to specify an argument.");
					return true;
				
				} else {
					
					if ( Bukkit.getPlayerExact(args[0]) != null ) {
					Player ctarget = Bukkit.getServer().getPlayer(args[0]);
					Location loc = ctarget.getLocation();
					int x = loc.getBlockX();
					int y = loc.getBlockY();
					int z = loc.getBlockZ();
					String world = loc.getWorld().getName();
					Bukkit.getLogger().info(args[0] + " is at " + x + " " + y + " " + z + " in " + world);
					return true;
				} else {
					Bukkit.getLogger().info("Target player is not online.");
					return true;
					}
				}
			
			} else {
				Location loc = player.getLocation();
				int x = loc.getBlockX();
				int y = loc.getBlockY();
				int z = loc.getBlockZ();
				sender.sendMessage(ChatColor.GOLD + "Your coordinates are:\n" +
						"World = " + loc.getWorld().getName() + "\n" +
						"X = " + x + "\n" +
						"Y = " + y + "\n" +
						"Z = " + z);
				return true;
			}
			
		}
		
		return false;
	}
	
}
