package net.simpvp.Misc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Statistic;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

/**
 * A command to let /everybody/ see when somebody first joined.
 */
public class AgeCommand implements Listener, CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		if (args.length < 1 && player == null) {
			Misc.instance.getLogger().info("You must specify a player to use this command.");
			return true;
		}

		Player target;
		if (args.length > 0) {
			@SuppressWarnings("deprecation") /* Only used for online players */
			Player tmp = Misc.instance.getServer().getPlayer(args[0]);
			target = tmp;
		} else {
			target = player;
		}

		final UUID uuid;
		if (player == null) {
			uuid = null;
		} else {
			uuid = player.getUniqueId();
		}

		if (target == null) {
			get_uuid(args[0], uuid);

		} else {
			send_result(uuid, target.getUniqueId(), target.getName());
		}

		return true;
	}

	private void get_uuid(final String name, final UUID caller_uuid) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
					URLConnection conn = url.openConnection();
					conn.connect();
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String resp = reader.readLine();
					if (resp == null)
						resp = "";

					Pattern p = Pattern.compile("\"id\":\"(\\S+?)\"");
					Matcher m = p.matcher(resp);
					m.find();
					String uuid_str = m.group(1);
					uuid_str = uuid_str.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");

					final UUID uuid = UUID.fromString(uuid_str);
					new BukkitRunnable() {
						@Override
						public void run() {
							send_result(caller_uuid, uuid, name);
						}
					}.runTask(Misc.instance);

				} catch (Exception e) {
					new BukkitRunnable() {
						@Override
						public void run() {
							send_result(caller_uuid, null, name);
						}
					}.runTask(Misc.instance);
				}


			}
		}.runTaskAsynchronously(Misc.instance);
	}

	private void send_result(UUID uuid, UUID target_uuid, String target_name) {
		Player player = Misc.instance.getServer().getPlayer(uuid);

		String msg;
		if (target_uuid == null) {
			msg = "Error retrieving data of " + target_name + ". No account with that name exists.";
		} else {
			OfflinePlayer off_player = Misc.instance.getServer().getOfflinePlayer(target_uuid);
			if (off_player.getFirstPlayed() == 0) {
				msg = target_name + " has never played on this server.";
			} else {

				long days = ((System.currentTimeMillis() - off_player.getFirstPlayed())
						/ (1000L * 60 * 60 * 24));
				SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");

				msg = off_player.getName() + " first joined on "
						+ ChatColor.AQUA + sdf.format(new Date(off_player.getFirstPlayed()))
						+ ChatColor.GREEN + ", " + days + " day";
				if (days != 1)
					msg += "s";
				msg += " ago.";

				int played_ticks = getTimePlayed(off_player);
				int played_minutes = played_ticks / (20 * 60);
				double played_hours = played_minutes / 60.0;
				double played_days = played_hours / 24.0;
				double played_years = played_days / 365.0;
				double avg_hours = played_hours / (double) (days + 1);
				msg += String.format(" They have played for %d minutes = %.1f hours = %.1f days = %.4f years. For an average of %.2f hours per day.",
						played_minutes, played_hours, played_days, played_years, avg_hours);

				Player on_player = Misc.instance.getServer().getPlayer(target_uuid);
				/* Only show time last played for offline players */
				if (on_player == null) {
					long last_played_seconds = (System.currentTimeMillis() - off_player.getLastPlayed()) / 1000L;
					if (last_played_seconds < 0) {
						throw new ArithmeticException();
					}
					long last_played_weeks = last_played_seconds / (60 * 60 * 24 * 7);
					long last_played_months = last_played_seconds / (60 * 60 * 24 * 30);
					long last_played_years = last_played_seconds / (60 * 60 * 24 * 365);

					/* We add 1 to the printed message so that the "less than" makes sense */
					msg += " They were last online within the past ";
					String unit;

					if (last_played_weeks <= 2) {
						if ((last_played_weeks + 1) == 1) {
							unit = "week";
						} else {
							msg += (last_played_weeks + 1) + " ";
							unit = "weeks";
						}
					} else if (last_played_months <= 10) {
						if ((last_played_months + 1) == 1) {
							unit = "month";
						} else {
							msg += (last_played_months + 1) + " ";
							unit = "months";
						}
					} else {
						if ((last_played_years + 1) == 1) {
							unit = "year";
						} else {
							msg += (last_played_years + 1) + " ";
							unit = "years";
						}
					}

					msg += unit + ".";


				}
			}
		}


		if (player != null) {
			player.sendMessage(ChatColor.GREEN + msg);
		}
		Misc.instance.getLogger().info(msg);

	}

	private static final HashMap<UUID, Integer> op_time_played = new HashMap<>();

	@EventHandler
	public void onVanishStatusChange(VanishStatusChangeEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		if (event.isVanishing()) {
			op_time_played.put(uuid, player.getStatistic(Statistic.PLAY_ONE_MINUTE));
		}
		else {
			op_time_played.remove(uuid);
		}
	}

	/**
	 * Get the time played for a player, or their last known playtime if currently vanished.
	 * @param off_player Player to get time for
	 * @return time
	 */
	public static int getTimePlayed(OfflinePlayer off_player) {
		return op_time_played.getOrDefault(
				off_player.getUniqueId(),
				off_player.getStatistic(Statistic.PLAY_ONE_MINUTE));
	}
}

