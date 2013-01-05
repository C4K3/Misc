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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Follow implements CommandExecutor {
	
	private static HashMap<String, GameMode> backGameMode = new HashMap<String, GameMode>();
	
	private static HashMap<String, Location> backLoc = new HashMap<String, Location>();
	
	/* Create final variable of potion effect applied to players for nightvision
	 * Time limit is set to 10 minutes (counted in ticks)
	 * Amplifier (level) is set to 1
	 */
	private static final PotionEffect nightVisionPotionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, 10 * 60 * 20, 1);
	
	/* Create final variable of potion effect applied to players for invisibility
	 * Time limit is set to 10 minutes (counted in ticks)
	 * Amplifier (level) is set to 1
	 */
	private static final PotionEffect invisibilityPotionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, 10 * 60 * 20, 1);
	
	private String stplayer; //String of target players name
	
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
				Player tplayer = Bukkit.getPlayer(args[0]);
				
				if ( tplayer != null ) {
					
					// sLoc == senders location
					Location sLoc = player.getLocation();
					backGameMode.put(splayer, player.getGameMode());
					backLoc.put(splayer, sLoc);
					
					player.setGameMode(GameMode.getByValue(1));
					player.performCommand("vanish on");
					
					stplayer = tplayer.getName(); // String of target players name
					
					// tloc == target players location
					player.teleport(tplayer);
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					player.removePotionEffect(PotionEffectType.INVISIBILITY);
					player.addPotionEffect(nightVisionPotionEffect);
					player.addPotionEffect(invisibilityPotionEffect);
					
					/* Notifying all OPs on the server of this */
					for (Player onlinePlayer : Bukkit.getOnlinePlayers() ) {
					  
					  if ( onlinePlayer.isOp()) {
					    
					    onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + splayer + ": Following " + stplayer + "]");
					    
					    }
					  
					  }
				
				} else {
					sender.sendMessage(ChatColor.RED + "Target player not found");
				}
			
			} else {
				// If player is not OP
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			}
			
			return false;
			
		}
		// End of /follow
		
		/* /goback command */
		if ( cmd.getName().equals("goback") == true ) {
			
			if ( player.isOp() == true ) {
				
				Location bLoc = backLoc.get(splayer);
				if ( bLoc != null ) {
					
					backLoc.remove(splayer);
					player.teleport(bLoc);
					player.setGameMode(backGameMode.get(splayer));
					backGameMode.remove(splayer);
					player.performCommand("vanish off");
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					player.removePotionEffect(PotionEffectType.INVISIBILITY);
					/* Notifying all OPs on the server of this */
					for (Player onlinePlayer : Bukkit.getOnlinePlayers() ) {
					  
					  if ( onlinePlayer.isOp()) {
					    
					    onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + splayer + ": /goback]");
					    
					    }
					  
					  }
					
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
