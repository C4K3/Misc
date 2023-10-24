package net.simpvp.Misc;

import java.util.Arrays;
import java.util.Comparator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ping implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			int ping = player.getPing();

			if (ping == 0) {
				player.sendMessage(ChatColor.RED + "Ping not yet calculated");
				return true;
			}

			ChatColor color;
			if (ping < 100) {
				color = ChatColor.GREEN;
			} else if (ping < 500) {
				color = ChatColor.YELLOW;
			} else {
				color = ChatColor.RED;
			}

			String msg = String.format("%s%dms", color, ping);
			player.sendMessage(msg);
		} else {
			StringBuffer msg = new StringBuffer();

			Player[] players = Misc.instance.getServer().getOnlinePlayers().toArray(new Player[0]);
			Arrays.sort(players, Comparator.comparingInt(Player::getPing));

			for (Player p : players) {
				int ping = p.getPing();
				if (ping == 0) {
					msg.append(String.format("%s\tN/A\n", p.getName()));
				} else {
					msg.append(String.format("%s\t%dms\n", p.getName(), ping));
				}
			}
			// Delete last newline
			if (msg.length() > 0) {
				msg.deleteCharAt(msg.length() - 1);
			}

			sender.sendMessage(msg.toString());
		}

		return true;
	}
}
