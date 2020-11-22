package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class NoNewPlayers implements Listener,CommandExecutor {

	/* How many hours a player must have played to be allowed to log in,
	 * a negative value means anybody may log in, 0 means somebody who has
	 * logged in at least once for any amount of time may log in. */
	private int hours_required;

	private String kick_msg = ChatColor.RED + "You have reached SimplicityPvP\n\n"
		+ "We're sorry, the server is currently being swarmed by alts, and therefore new players are temporarily restricted from joining the server.\n\n"
		+ "Please try again later.";

	public NoNewPlayers() {
		hours_required = Misc.instance.getConfig().getInt("NoNewPlayers");
		Misc.instance.getLogger().info("NoNewPlayers set to " + hours_required);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled=false)
	public void onPlayerLogin(PlayerLoginEvent event) {

		if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
			return;
		}

		if (hours_required < 0) {
			return;
		}

		/* Read
		 * https://www.spigotmc.org/threads/how-to-use-hasplayedbefore-in-a-playerloginevent.355496/
		 * before making changes to this. */
		Player player = event.getPlayer();
		OfflinePlayer offplayer = Misc.instance.getServer().getOfflinePlayer(player.getUniqueId());

		/* OPs can join unconditionally */
		if (player.isOp()) {
			return;
		}

		/* Whitelisted players can join unconditionally */
		if (player.isWhitelisted()) {
			return;
		}

		int hours = -1;
		if (offplayer.hasPlayedBefore()) {
			int played_ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
			hours = played_ticks / (20 * 60 * 60);
		}

		if (hours < hours_required) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, kick_msg);
			String m = String.format("NoNewPlayers blocking %s. %d < %d.", player.getName(), hours, hours_required);
			Misc.instance.getLogger().info(m);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (!player.isOp()) {
				String msg = (hours_required < 0) ?
						"Not currently blocking players from joining" :
						"Currently blocking players with less than " + hours_required + " hours played from joining";
				player.sendMessage(msg);
				return true;
			}
		}

		if (args.length == 0) {
			sender.sendMessage("/nonewplayers will prevent players who have less than the desired hours played from being able to join the server. This is used if the server is being raided by someone with many alts, by preventing non-regular players from being able to join. To enable nonewplayers, use /nonewplayers <hours> where hours is the minimum hours somebody must have played to be able to join. Set it to a negative number to allow anybody to join. It is currently set to " + hours_required);
			return true;
		}

		try {
			hours_required = Integer.parseInt(args[0]);
		} catch (Exception e) {
			sender.sendMessage("Invalid integer " + args[0]);
			return true;
		}

		Misc.instance.reloadConfig();
		Misc.instance.getConfig().set("NoNewPlayers", hours_required);
		Misc.instance.saveConfig();

		String m = "/nonewplayers hours required set to " + hours_required;
		sender.sendMessage(m);
		Misc.instance.getLogger().info(m);

		return true;
	}

}

