package net.simpvp.Misc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides configurable commands that just return a fixed message.
 */
public class InfoCommands implements Listener {
	private static Map<String, String> commands = null;

	public InfoCommands(Misc plugin) {
		ConfigurationSection config = plugin.getConfig().getConfigurationSection("infoCommands");
		if (config == null) {
			plugin.getLogger().warning("Unable to initialize infoCommands, null config");
			return;
		}

		commands = config
			.getValues(false)
			.entrySet()
			.stream()
			.collect(
					Collectors.toMap(
						entry -> "/" + entry.getKey().toLowerCase(),
						entry -> (String) entry.getValue()));

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

		String response = commands.get(cmd);
		if (response == null) {
			return;
		}

		player.sendMessage(response);
		event.setCancelled(true);
	}
}
