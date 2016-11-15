package net.simpvp.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class DisableCmd implements Listener {
	/* Disables all commands in the disabled.txt file
	 * Adapted from PlgDisableCmd by Plague
	 */

	private static List<String> disabledCmds;

	private static List<String> opOnlyCmds;

	private Player player;

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String[] split = event.getMessage().split(" ");
		if (split.length < 1)
			return;

		String cmd = split[0].trim().substring(1).toLowerCase();

		player = event.getPlayer();

		if (Collections.binarySearch(disabledCmds, cmd) >= 0 ) {
			disable_event(event, "Blocked command found in disabledCmds.txt");

		} else if (Collections.binarySearch(opOnlyCmds, cmd) >= 0 && !player.isOp()) {
			disable_event(event, "Blocked command found in opOnlyCmds.txt");
		} else if (cmd.contains(":")) {
			disable_event(event, "Blocked command that contained :");
		}

	}

	private void disable_event(PlayerCommandPreprocessEvent event, String blocked_message) {
		event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
		Misc.instance.getLogger().info(blocked_message);
		event.setCancelled(true);
	}

	public static void loadDisabledCmds() {

		/* Adds disabled cmds to disabledCmds array 
		 * Reads from plugins/Misc/disabledCmds.txt file */
		try {
			disabledCmds = new ArrayList<String>();
			File f = new File(Misc.instance.getDataFolder(), "disabledCmds.txt");

			if (!f.exists()) {
				Misc.instance.getLogger().info("No items found in disabledCmds.txt");
			} else {

				BufferedReader rdr = new BufferedReader(new FileReader(f));
				String line;
				while ((line = rdr.readLine()) != null) {

					line = line.trim();
					if (line.length() < 1) continue;
					disabledCmds.add(line.trim().toLowerCase());
				}

				rdr.close();
				Collections.sort(disabledCmds);
				Misc.instance.getLogger().info("Disabling " + disabledCmds.size() + " commands from disabledCmds.txt");

			}

		} catch (Exception e) {
			Misc.instance.getLogger().info("Unexpected error: " + e.getMessage());
		}

		/* Adding commands that are disabled for everybody but OPs to the opOnlyCmds array
		 * Reads from plugins/Misc/opOnlyCmds.txt */
		try {
			opOnlyCmds = new ArrayList<String>();
			File f = new File(Misc.instance.getDataFolder(), "opOnlyCmds.txt");

			if (!f.exists()) {
				Bukkit.getLogger().info("No items found in opOnlyCmds.txt");
			} else {

				BufferedReader rdr = new BufferedReader(new FileReader(f));
				String line;

				while ((line = rdr.readLine()) != null) {
					line = line.trim();
					if (line.length() < 1) continue;
					opOnlyCmds.add(line.trim().toLowerCase());
				}

				rdr.close();
				Collections.sort(opOnlyCmds);
				Misc.instance.getLogger().info("Disabling " + opOnlyCmds.size() + " commands from opOnlyCmds.txt");

			}

		} catch (Exception e) {
			Misc.instance.getLogger().info("Unexpected error: " + e.getMessage());
		}

	}

}

