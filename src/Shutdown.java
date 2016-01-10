package net.simpvp.Misc;

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
		
		/* If sender is either console or an OP */
		if ( player == null || player.isOp() ) {
			
			/* 30 second warning */
	        Misc.instance.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "[ANNOUNCEMENT] Server will restart in 30 seconds\n" +
	        		" You do not have to disconnect\n" +
	        		" The restart will only take a couple of seconds\n" +
	        		" You may attempt to reconnect immediately");
	        
			/* 20 second warning */
			Misc.instance.getServer().getScheduler().scheduleSyncDelayedTask(Misc.instance, new Runnable() {
			    @Override 
			    public void run() {
			        Misc.instance.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "[ANNOUNCEMENT] Server will restart in 20 seconds\n" +
			        		" You do not have to disconnect\n" +
			        		" The restart will only take a couple of seconds\n" +
			        		" You may attempt to reconnect immediately");
			    }
			}, 10 * 20L);
			
			/* 10 second warning */
			Misc.instance.getServer().getScheduler().scheduleSyncDelayedTask(Misc.instance, new Runnable() {
			    @Override 
			    public void run() {
			        Misc.instance.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "[ANNOUNCEMENT] Server will restart in 10 seconds\n" +
			        		" You do not have to disconnect\n" +
			        		" The restart will only take a couple of seconds\n" +
			        		" You may attempt to reconnect immediately");
			    }
			}, 20 * 20L);
			
			/* The delayed stop */
			Misc.instance.getServer().getScheduler().scheduleSyncDelayedTask(Misc.instance, new Runnable() {
			    @Override 
			    public void run() {
			    	
			    	for ( Player onlinePlayer : Misc.instance.getServer().getOnlinePlayers() ) {
			    		
			    		onlinePlayer.kickPlayer("Server is restarting. You may reconnect immediately");
			    		
			    	}
			    	
					Misc.instance.getServer().dispatchCommand(Misc.instance.getServer().getConsoleSender(), "stop");
			    }
			}, 30 * 20L);
						
			return true;
			
		} else {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}
		
	}

}
