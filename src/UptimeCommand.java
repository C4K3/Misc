package net.simpvp.Misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.time.Duration;

public class UptimeCommand implements CommandExecutor {

	Instant server_start = Instant.now();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


		Duration uptime = Duration.between(server_start, Instant.now());

		long seconds = uptime.getSeconds();
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		hours %= 24;
		minutes %= 60;

		/* This output roughly approximates that of procps uptime */
		String d;
		if (days > 1) {
			d = String.format("%d days, ", days);
		} else if (days == 1) {
			d = "1 day, ";
		} else {
			d = "";
		}
		String h = hours > 0
			? String.format("%d:", hours)
			: "";
		String m = hours > 0
			? String.format("%02d", minutes)
			: String.format("%d min", minutes);

		sender.sendMessage(String.format("up %s%s%s", d, h, m));

		return true;
	}
}
