package org.c4k3.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/* This class is responsible for handling the /forcefield command
 * Temporarily unvanishes admins to let them easily check for forcefield hacks
 */
public class Forcefield implements CommandExecutor {
	
	/* Create final variable of potion effect applied to players for invisibility
	 * Time limit is set to 10 minutes (counted in ticks)
	 * Amplifier (level) is set to 1
	 */
	private static final PotionEffect invisibilityPotionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, 10 * 60 * 20, 1);
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		final Player player = (Player) sender;
		
		if ( player != null ) {
			
			if ( player.isOp() ) {
				
				/* Notifying all OPs on the server of this */
				for (Player onlinePlayer : Bukkit.getOnlinePlayers() ) {
				  
				  if ( onlinePlayer.isOp()) {
				    
				    onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + player.getName() + ": Checking for forcefield hack]");
				    
				    }
				  
				  }
				
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				player.addPotionEffect(invisibilityPotionEffect);
				player.performCommand("vanish off");
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Misc.instance, new Runnable() {
				    @Override 
				    public void run() {
				    	player.performCommand("vanish on");
				    	player.removePotionEffect(PotionEffectType.INVISIBILITY);
				    }
				}, 5L);
				
				return true;
				
				
			} else {
				/* Player is not OP */
				player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return true;
			}
			
			
		} else {
			/* Player is null (sender is console or a plugin */
			sender.sendMessage("You must be a player to use this command");
			return true;
		}
				
	}

}
