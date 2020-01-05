package net.simpvp.Misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.block.Block;
import org.bukkit.Location;

/**
 * Logs info about player's bed use.
 */
public class BedLog implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=false)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		String msg = event.getPlayer().getName();
		if (event.isBedSpawn()) {
			msg += " is respawning at their bed at ";
		} else {
			msg += " is respawning at spawn at ";
		}

		Location loc = event.getRespawnLocation();
		msg += loc.getWorld().getName()
			+ " " + loc.getBlockX()
			+ " " + loc.getBlockY()
			+ " " + loc.getBlockZ();

		Misc.instance.getLogger().info(msg);

	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=false)
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		Block block = event.getBed();
		String msg = event.getPlayer().getName() + " entered a bed at "
			+ block.getWorld().getName()
			+ " " + block.getX()
			+ " " + block.getY()
			+ " " + block.getZ();

		Misc.instance.getLogger().info(msg);

	}

}

