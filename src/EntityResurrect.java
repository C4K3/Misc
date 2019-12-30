package net.simpvp.Misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityResurrectEvent;

/**
 * Prints out every time there's an EntityResurrect event for a player
 */
public class EntityResurrect implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void onEntityResurrect(EntityResurrectEvent event) {
		Player player = null;
		if (event.getEntity() instanceof Player) {
			player = (Player) event.getEntity();
		} else {
			return;
		}

		Misc.instance.getLogger().info("EntityResurrectEvent for player " + player.getName());
	}
}
