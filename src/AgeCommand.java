package net.simpvp.Misc;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Statistic;

/**
 * A command to let /everybody/ see when somebody first joined.
 */
public class AgeCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		if (cmd.getName().equals("firstjoined")) {
			if (player == null) {
				Misc.instance.getLogger().info("firstjoined has been renamed to age.");
			} else {
				player.sendMessage(ChatColor.RED +
						"/firstjoined has been renamed to /age.");
			}
			return true;
		}

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
		String msg;
		if (target_uuid == null) {
			msg = "Error retrieving data of " + target_name + ".";
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

				/* FIXME, currently this for some reason is only possible for online players, but it should be possible at all times */
				Player on_player = Misc.instance.getServer().getPlayer(target_uuid);
				if (on_player != null) {
					int played_ticks = on_player.getStatistic(Statistic.PLAY_ONE_TICK);
					double played_minutes = played_ticks / (20 * 60);
					double played_hours = played_minutes / 60;
					double played_days = played_hours / 24;
					double played_years = played_days / 365;
					double avg_hours = played_hours / (days + 1);
					msg += String.format("\n They have played for %.0f minutes = %.1f hours = %.1f days = %.4f years. For an average of %.2f hours per day.",
							played_minutes, played_hours, played_days, played_years, avg_hours);
				}
			}
		}

		Player player = Misc.instance.getServer().getPlayer(uuid);

		if (player == null) {
			Misc.instance.getLogger().info(msg);
		} else {
			player.sendMessage(ChatColor.GREEN + msg);
		}

	}

}

