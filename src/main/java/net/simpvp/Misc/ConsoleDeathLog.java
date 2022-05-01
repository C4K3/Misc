package net.simpvp.Misc;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Base64;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;

/**
 * Logs death messages to console
 */
public class ConsoleDeathLog implements Listener {

	public ConsoleDeathLog(Misc plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	private Logger INVENTORY_LOGGER = LogManager.getLogger("InventoryLog");
	private static final Marker INVENTORY_MARKER = MarkerManager.getMarker("INVENTORY");

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Location loc = event.getEntity().getLocation();

		String death_msg = String.format("%s at '%d %d %d %s' (%s died)",
				event.getDeathMessage(),
				loc.getBlockX(),
				loc.getBlockY(),
				loc.getBlockZ(),
				loc.getWorld().getName(),
				event.getEntity().getName());

		Misc.instance.getLogger().info(death_msg);

		/* Log player level and inventory contents just in case.
		 * The output log format should be consistent with the
		 * /eventrestore command format */
		/* Log it all, just in case */
		if (INVENTORY_LOGGER.isDebugEnabled()) {
			try {
				ItemStack[] inventory = event.getEntity().getInventory().getContents();
				ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
				OutputStream base64 = Base64.getEncoder().wrap(bytearray);
				BukkitObjectOutputStream bukkitstream = new BukkitObjectOutputStream(base64);

				int len = 0;
				for (ItemStack itemStack : inventory) {
					if (itemStack == null) {
						continue;
					}
					len += 1;
				}
				bukkitstream.writeInt(len);

				for (ItemStack itemStack : inventory) {
					if (itemStack == null) {
						continue;
					}

					bukkitstream.writeObject(itemStack);
				}

				bukkitstream.close();
				base64.close();

				INVENTORY_LOGGER.debug(INVENTORY_MARKER, String.format("%s: %d %s", death_msg, event.getEntity().getLevel(), bytearray.toString()));
			} catch (Exception e) {
				Misc.instance.getLogger().warning("Failed to log inventory: " + e);
				e.printStackTrace();
			}
		}
	}
}
