package net.simpvp.Misc;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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

		Block block = player.getLocation().getBlock();

		String msg = String.format("EntityResurrectEvent for player %s at '%d %d %d %s'",
				player.getName(),
				block.getX(),
				block.getY(),
				block.getZ(),
				block.getWorld().getName());

		Misc.instance.getLogger().info(msg);
	}
}
