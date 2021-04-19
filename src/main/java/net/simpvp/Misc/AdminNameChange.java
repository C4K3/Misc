package net.simpvp.Misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Don't announce name changes for ops.
 */
public class AdminNameChange implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().isOp()) {
			return;
		}

		String jm = event.getJoinMessage();
		if (jm == null) {
			return;
		}

		if (!jm.matches("^.* \\(formerly known as .*$")) {
			return;
		}

		Misc.instance.getLogger().info("Removing name change info from: " + jm);
		event.setJoinMessage(jm.replaceFirst(" \\(formerly known as \\S+\\) ", " "));
	}
}
