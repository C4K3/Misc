package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BedTrap implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
			return;
		}

		String hostname = event.getHostname()
			.replaceAll("\\x00", "\\\\x00")
			.toLowerCase();

		if (!hostname.startsWith("bedtrap.")) {
			return;
		}

		Misc.instance.getLogger().info("BedTrap resetting " + event.getPlayer().getName() + "'s bed.");

		final UUID uuid = event.getPlayer().getUniqueId();
		new BukkitRunnable() {
			private int counter = 0;
			@Override
			public void run() {
				// After 30 seconds the player will have timed
				// out anyway.
				if (counter > 60) {
					this.cancel();
					return;
				}
				counter++;
				Player player = Misc.instance.getServer().getPlayer(uuid);
				if (player == null) {
					return;
				}

				if (player.isDead()) {
					Misc.instance.getLogger().info("BedTrap successfully reset " + player.getName() + "'s bed.");
					player.setBedSpawnLocation(null);
				} else {
					player.sendMessage(ChatColor.RED + "You're connecting using the bedtrap service!");
				}

				this.cancel();
			}
		}.runTaskTimer(Misc.instance, 10, 10);

	}

}

