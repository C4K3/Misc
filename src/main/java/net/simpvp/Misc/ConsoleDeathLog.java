package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

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

		String death_msg = event.getDeathMessage() + " at "
				+ loc.getWorld().getName()
				+ " " + loc.getBlockX()
				+ " " + loc.getBlockY()
				+ " " + loc.getBlockZ()
				+ " for " + event.getEntity().getName();
		/* Log player level and inventory contents just in case.
		 * The output log format should be consistent with the
		 * /eventrestore command format */
		String sInventoryContents = "";

		for (ItemStack itemStack : event.getEntity().getInventory().getContents()) {
			if (itemStack == null)
				continue;

			String tmp = itemStack.getType().toString()
				+ "." + itemStack.getAmount()
				+ "." + itemStack.getDurability()
				+ "." + itemStack.getEnchantments().toString();
			tmp = tmp.replaceAll(" ", "");
			sInventoryContents += " " + tmp;
		}

		/* Intentional use of bukkit logger */
		Bukkit.getLogger().info(death_msg + ": " + event.getEntity().getLevel() + sInventoryContents);
	}
}

