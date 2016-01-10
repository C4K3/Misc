package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;



public class ConsoleDeathLog implements Listener {
	/* Logs death messages to console */
	
	public ConsoleDeathLog(Misc plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		
		Location loc = event.getEntity().getLocation();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		String world = loc.getWorld().getName();
		String deathMsg = event.getDeathMessage();
		String splayer = event.getEntity().getName();
		Bukkit.getLogger().info(deathMsg + " at " + world + " " + x + " " + y + " " + z + " for " + splayer);
	}

}
