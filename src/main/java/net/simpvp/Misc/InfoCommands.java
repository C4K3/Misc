package net.simpvp.Misc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides configurable commands that just return a fixed message.
 */
public class InfoCommands implements Listener {
	private static Map<String, Command> commands = null;

	public InfoCommands(Misc plugin) {
		List<Map<?, ?>> config = plugin.getConfig().getMapList("infoCommands");
		if (config == null) {
			plugin.getLogger().warning("Unable to initialize infoCommands, null config");
			return;
		}

		commands = config
			.stream()
			.collect(
					Collectors.toMap(
						entry -> "/" + ((String) entry.get("command")).toLowerCase(),
						entry -> {
							String message = (String) entry.get("message");
							Boolean ignore_ops = (Boolean) entry.get("ignoreOps");
							if (ignore_ops == null) {
								ignore_ops = false;
							}
							return new Command(message, ignore_ops);
						}));

		if (commands.isEmpty()) {
			return;
		}

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	private static final Pattern cmd_regex = Pattern.compile("\\s.*$");

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();

		if (player == null) {
			// unreachable?
			return;
		}

		String cmd = event.getMessage().toLowerCase();
		cmd = cmd_regex.matcher(cmd).replaceFirst("");

		Command response = commands.get(cmd);
		if (response == null) {
			return;
		}

		if (response.ignore_ops && player.isOp()) {
			return;
		}

		player.sendMessage(response.message);
		event.setCancelled(true);
	}

	private class Command {
		public String message;
		public boolean ignore_ops;

		public Command(String message, boolean ignore_ops) {
			this.message = message;
			this.ignore_ops = ignore_ops;

		}
	}
}
