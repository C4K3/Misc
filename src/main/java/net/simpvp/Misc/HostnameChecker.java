package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.HashSet;
import java.util.UUID;

/**
 * Check if players are connecting to simpvp using a supported domain or if
 * they're using the IP address directly.
 *
 * It's disabled by default, has to be enabled using the simpvpHostnameChecker
 * config.
 *
 * The reasoning for this is to ensure we can switch IP addresses without any
 * impact on players. In the past, whenever we've changed IP address, there
 * have always been people who were connecting directly to the IP address and
 * thus got left behind. No matter how we try to notify people, even servers on
 * the old IP address trying to tell people to update their server lists, there
 * are always some people who miss or misunderstand the notification.
 *
 * Instead we have this check, so that we force people to connect to the domain
 * from the get-go, so there aren't any people left behind during IP address
 * changes. Some people might still misunderstand the message here, but in that
 * case we lose somebody with 0 playtime versus someone with existing playtime.
 *
 * Advanced players can bypass this check in a variety of ways.
 *
 * This is simpvp specific, we could maybe make it configurable?
 */
public class HostnameChecker implements Listener {
	final String message = ChatColor.LIGHT_PURPLE + "Welcome to Simplicity PvP\n\n"
		+ "Because our IP might change at any time, please edit your "
		+ "server list to connect to " + ChatColor.AQUA + "" + ChatColor.UNDERLINE
		+ "simpvp.net" + ChatColor.RESET + "\n\n" + ChatColor.LIGHT_PURPLE
		+ "Thanks.";

	final String message2 = "This is the server's website, to play on the server:\n\n"
		+ ChatColor.MAGIC + "===========================================================" + ChatColor.RESET + "\n\n"
		+ "Join " + ChatColor.AQUA + "" + ChatColor.UNDERLINE + "simpvp.net\n\n" + ChatColor.RESET
		+ ChatColor.MAGIC + "===========================================================";

	final String message3 = "Our ip changes often, to play on the server:\n\n"
		+ ChatColor.MAGIC + "===========================================================" + ChatColor.RESET + "\n\n"
		+ "Join " + ChatColor.AQUA + "" + ChatColor.UNDERLINE + "simpvp.net\n\n" + ChatColor.RESET
		+ ChatColor.MAGIC + "===========================================================";

	/* Players who have been shown the error message once */
	private static HashSet<UUID> failed_players = new HashSet<UUID>();

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=false)
	public void onPlayerLogin(PlayerLoginEvent event) {
		/* Replace all instances of binary \x00 with the
		 * string "\x00" */
		String hostname = event.getHostname()
			.replaceAll("\\x00", "\\\\x00");

		Misc.instance.getLogger().info(event.getPlayer().getName()
				+ " is trying to login to " + hostname);

		if (hostname.matches("(?i)(.*simpvp.net|play.simplicitypvp.net).*"))
			return;

		if (failed_players.contains(event.getPlayer().getUniqueId())) {
			failed_players.remove(event.getPlayer().getUniqueId());

			if (hostname.matches("(?i)simplicitypvp.net")) {
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
						message2);
			} else {
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
						message3);
			}
			return;
		}

		failed_players.add(event.getPlayer().getUniqueId());
		event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
	}
}
