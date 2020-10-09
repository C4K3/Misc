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

		if (!event.getJoinMessage().matches("^.* \\(formerly known as .*$")) {
			return;
		}

		Misc.instance.getLogger().info("Removing name change info from: " + event.getJoinMessage());
		event.setJoinMessage(event.getJoinMessage().replaceFirst(" \\(formerly known as \\S+\\) ", " "));
	}
}
