package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


/**
 * Logs death messages to console
 */
public class ConsoleDeathLog implements Listener {

	public ConsoleDeathLog(Misc plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Location loc = event.getEntity().getLocation();

		/* Intentional use of bukkit logger */
		Bukkit.getLogger().info(event.getDeathMessage() + " at "
				+ loc.getWorld().getName()
				+ " " + loc.getBlockX()
				+ " " + loc.getBlockY()
				+ " " + loc.getBlockZ()
				+ " for " + event.getEntity().getName());
	}

}

