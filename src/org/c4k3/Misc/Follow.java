package org.c4k3.Misc;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Follow implements CommandExecutor {
	
	static HashMap<String, Location> backLoc = new HashMap<String, Location>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}
		
		String splayer = sender.getName();
		
		/* If sender is not a player (== console) */
		if ( player == null ) {
			sender.sendMessage("Only players can use this command");
			return false;
		}
		
		if ( cmd.getName().equals("follow") == true ) {
			
			if ( sender.isOp() == true ) {
			
				if ( args.length < 1  ) {
					sender.sendMessage(ChatColor.RED + "You need to specify a target");
					return false;
				}
				
				if ( args.length > 1 ) {
					sender.sendMessage(ChatColor.RED + "You cannot specify more than one target");
					return false;
				}
				
				// It is now presumed that the argument length is 1
				
				// tplayer == target player
				Player tplayer = Bukkit.getPlayerExact(args[0]);
				
				if ( tplayer != null ) {
					
					// sLoc == senders location
					Location sLoc = player.getLocation();
					backLoc.put(splayer, sLoc);
					
					player.setGameMode(GameMode.getByValue(1));
					player.performCommand("vanish on");
					
					// tloc == target players location
					Location tLoc = tplayer.getLocation();
					player.teleport(tLoc);
					sender.sendMessage(ChatColor.GOLD + "Teleporting you to target");
				
				} else {
					sender.sendMessage(ChatColor.RED + "Target is not online");
				}
			
			} else {
				// If player is not OP
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			}
			
			return false;
			
		}
		// End of /follow
		
		if ( cmd.getName().equals("goback") == true ) {
			
			if ( player.isOp() == true ) {
				
				Location bLoc = backLoc.get(splayer);
				if ( bLoc != null ) {
					
					backLoc.remove(splayer);
					player.teleport(bLoc);
					player.setGameMode(GameMode.getByValue(0));
					player.performCommand("vanish off");
					sender.sendMessage(ChatColor.GOLD + "Teleporting you home");
					return false;
					
				} else {
					// If bLoc == null
					sender.sendMessage(ChatColor.RED + "You do not have any location saved");
					return false;
				}
				
			} else {
				// If player is not OP
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return false;
			}
			
		}
		
		return false;
	}

}
