package net.simpvp.Misc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * Provides console logging of inventory actions for selected players.
 */
public class InventoryLog implements Listener {
	private static ArrayList<UUID> log_players = null;

	public InventoryLog(Misc plugin) {
		load_config(plugin);
		if (log_players != null) {
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		}
	}

	private void load_config(Misc plugin) {
		log_players = null;
		try {
			File f = new File(plugin.getDataFolder(), "inventory_log.yaml");
			if (f.exists()) {
				log_players = new ArrayList<>();
				Configuration c = YamlConfiguration.loadConfiguration(f);
				for (String value : (List<String>) c.getList("inventorylog")) {
					UUID uuid = UUID.fromString(value);
					log_players.add(uuid);
				}

				plugin.getLogger().info(String.format("Enabled inventory logging for %d players", log_players.size()));
			}
		} catch (Exception e) {
			log_players = null;
			plugin.getLogger().severe("Error enabling inventory logging: " + e);
			e.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=false)
	public void onInventoryClick(InventoryClickEvent event) {
		if (log_players == null) {
			return;
		}

		HumanEntity e = event.getWhoClicked();
		if (!(e instanceof Player)) {
			return;
		}
		Player p = (Player) e;
		if (!log_player(p)) {
			return;
		}

		String s = String.format("InventoryClickEvent %s: Action(%s) Click(%s) Inventory(%s) CurrentItem(%s) Cursor(%s)",
				p.getName(),
				event.getAction(),
				event.getClick(),
				event.getInventory().getType(),
				event.getCurrentItem(),
				event.getCursor()
				);

		if (event.getHotbarButton() >= 0) {
			s += String.format(" Hotbar(%d)", event.getHotbarButton());
		}
		if (event.getSlot() >= 0) {
			s += String.format(" Slot(%d)", event.getSlot());
		}
		if (event.isLeftClick()) {
			s += " LeftClick";
		}
		if (event.isRightClick()) {
			s += " RightClick";
		}
		if (event.isShiftClick()) {
			s += " ShiftClick";
		}
		if (event.isCancelled()) {
			s += " Cancelled";
		}

		Misc.instance.getLogger().info(s);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=false)
	public void onInventoryOpen(InventoryOpenEvent event) {
		HumanEntity e = event.getPlayer();
		if (!(e instanceof Player)) {
			return;
		}
		Player p = (Player) e;
		if (!log_player(p)) {
			return;
		}

		Misc.instance.getLogger().info(String.format("InventoryOpenEvent %s: %s", p.getName(), event.getInventory().getType()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=false)
	public void onInventoryClose(InventoryCloseEvent event) {
		HumanEntity e = event.getPlayer();
		if (!(e instanceof Player)) {
			return;
		}
		Player p = (Player) e;
		if (!log_player(p)) {
			return;
		}

		Misc.instance.getLogger().info(String.format("InventoryOpenEvent %s: %s", p.getName(), event.getInventory().getType()));
	}

	private boolean log_player(Player player) {
		UUID uuid = player.getUniqueId();

		if (log_players.contains(uuid)) {
			return true;
		} else {
			return false;
		}
	}
}

