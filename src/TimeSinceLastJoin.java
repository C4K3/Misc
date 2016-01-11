package net.simpvp.Misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Displays information about when a player last logged in.
 */
public class TimeSinceLastJoin implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=false)
	public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
			return;

		OfflinePlayer player = Misc.instance.getServer().getOfflinePlayer(event.getUniqueId());

		if (!player.hasPlayedBefore()) {
			Misc.instance.getLogger().info(
					player.getName() + " joined for first time.");
			return;
		}

		Misc.instance.getLogger().info(player.getName()
				+ "'s last connect was " + time_since(player.getLastPlayed()) + " ago");

		if ((System.currentTimeMillis() - player.getLastPlayed())
				/ (1000L * 60L * 60L * 24L * 30L * 12L) > 0) {
			final String msg = "say Welcome back " + player.getName() + ","
				+ " we haven't seen you for " + time_since(player.getLastPlayed())
				+ ". You'll be glad to know the map hasn't been reset since then.";
			new BukkitRunnable() {
				@Override
				public void run() {
					Misc.instance.getServer().dispatchCommand(
							Misc.instance.getServer().getConsoleSender(), msg);

				}
			}.runTaskLater(Misc.instance, 5 * 20);
		}
	}

	/**
	 * Returns a string with how long (approximately) it's been since the player was last on
	 */
	private String time_since(long time) {
		long diff = (System.currentTimeMillis() - time) / 1000L;

		int seconds = (int) diff % 60;
		int minutes = (int) (diff % (60 * 60)) / 60;
		int hours = (int) (diff % (60 * 60 * 24)) / (60 * 60);
		int days = (int) (diff % (60 * 60 * 24 * 30)) / (60 * 60 * 24);
		int months = (int) (diff % (60 * 60 * 24 * 30 * 12)) / (60 * 60 * 24 * 30);
		long years = diff / (60 * 60 * 24 * 30 * 12);

		boolean display = false;

		String msg = "";
		if (years > 0) {
			msg += years + " year";
			if (years != 1)
				msg += "s";
			msg += ", ";
			display = true;
		}
		if (display || months > 0) {
			msg += months + " month";
			if (months != 1)
				msg += "s";
			msg += ", ";
			display = true;
		}
		if (display || days > 0) {
			msg += days + " day";
			if (days != 1)
				msg += "s";
			msg += ", ";
			display = true;
		}
		if (display || hours > 0) {
			msg += hours + " hour";
			if (hours != 1)
				msg += "s";
			msg += ", ";
			display = true;
		}
		if (display || minutes > 0) {
			msg += minutes + " minute";
			if (minutes != 1)
				msg += "s";
			msg += ", ";
		}
		msg += seconds + " second";
		if (seconds != 1)
			msg += "s";

		return msg;
	}
}

