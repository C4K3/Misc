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

		String death_msg = String.format("%s at '%d %d %d %s' (%s died)",
				event.getDeathMessage(),
				loc.getBlockX(),
				loc.getBlockY(),
				loc.getBlockZ(),
				loc.getWorld().getName(),
				event.getEntity().getName());
		/* Log player level and inventory contents just in case.
		 * The output log format should be consistent with the
		 * /eventrestore command format */
		ItemStack[] inventory = event.getEntity().getInventory().getContents();
		String inventoryString;
		try {
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

			inventoryString = new String(bytearray.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		/* Intentional use of bukkit logger */
		Bukkit.getLogger().info(String.format("%s: %d %s", death_msg, event.getEntity().getLevel(), inventoryString));
	}
}

