package net.simpvp.Misc;

import org.bukkit.Location;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlueSkulls implements Listener {
	private static final int ACTIVATION_MINIMUM = 32 * 32;

	/**
	 * Prevent explosion damage from blue wither skulls if no player is nearby.
	 */
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if (!(event.getEntity() instanceof WitherSkull))
			return;

		WitherSkull skull = (WitherSkull) event.getEntity();
		if (!skull.isCharged())
			return; // Not blue skull

		Location loc = event.getLocation();
		boolean hasNearby = loc.getWorld().getPlayers().stream()
				.anyMatch(p -> p.getLocation().distanceSquared(loc) < ACTIVATION_MINIMUM);

		if (!hasNearby)
			event.setCancelled(true);
	}
}
