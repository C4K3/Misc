package net.simpvp.Misc;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * A simple block logger for low-activity worlds.
 */
public class BlockLog implements Listener {

	private static HashSet<String> log_worlds = new HashSet<>();

	public BlockLog(Misc plugin) {
		for (String w : (List<String>) plugin.getConfig().getList("blockLogWorlds")) {
			log_worlds.add(w);
		}

		if (log_worlds.size() > 0) {
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Block b = event.getBlockPlaced();

		if (!log_worlds.contains(b.getWorld().getName())) {
			return;
		}

		String msg = String.format("%s placed a %s block at '%d %d %d %s'",
				event.getPlayer().getName(),
				b.getType(),
				b.getX(),
				b.getY(),
				b.getZ(),
				b.getWorld().getName());
		Misc.instance.getLogger().info(msg);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();

		if (!log_worlds.contains(b.getWorld().getName())) {
			return;
		}

		String msg = String.format("%s broke a %s block at '%d %d %d %s'",
				event.getPlayer().getName(),
				b.getType(),
				b.getX(),
				b.getY(),
				b.getZ(),
				b.getWorld().getName());
		Misc.instance.getLogger().info(msg);
	}
}
