package net.simpvp.Misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MOTD implements Listener, CommandExecutor {
	private static String motd = "";

	public MOTD(Misc plugin) {
		String m = plugin.getConfig().getString("motd");
		if (m == null || m.isEmpty()) {
			return;
		}

		motd = m;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getCommand("motd").setExecutor(this);
	}

	@EventHandler(ignoreCancelled=true,priority=EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(motd);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(motd);
		return true;
	}
}
