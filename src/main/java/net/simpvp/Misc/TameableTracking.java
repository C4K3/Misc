package net.simpvp.Misc;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Provides mitigation against using wolves to find bases.
 *
 * By default standing wolves will look at their owner, no matter how far away they are.
 * This means that if a player leaves behind a single wolf, and somebody else then finds
 * that wolf, then that player is able to track the wolf owner indefinitely.
 *
 * This mitigates this by disabling wolf AI when their owner is far away.
 */
public class TameableTracking implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerInteract(PlayerInteractEntityEvent event) {
		check_tameable(event.getRightClicked());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	public void onChunkLoad(ChunkLoadEvent event) {
		if (event.isNewChunk()) {
			return;
		}

		for (Entity e : event.getChunk().getEntities()) {
			check_tameable(e);
		}
	}

	private void check_tameable(Entity e) {
		Tameable entity;
		if (e instanceof Tameable) {
			entity = (Tameable) e;
		} else {
			return;
		}
		if (!entity.isTamed() || entity.getOwner() == null) {
			return;
		}

		if (owner_in_range(entity)) {
			entity.setAware(true);
		} else {
			entity.setAware(false);

			if (entity instanceof Sittable) {
				((Sittable) entity).setSitting(true);
			}
		}
	}

	private boolean owner_in_range(Tameable entity) {
		AnimalTamer owner = entity.getOwner();
		if (!(owner instanceof Player)) {
			return false;
		}
		Player player = (Player) owner;

		try {
			double distance = entity.getLocation().distanceSquared(player.getLocation());
			return distance < (200 * 200);
		} catch (IllegalArgumentException e) {
			// Throws IllegalArgumentException if locations are in
			// different worlds
			return false;
		}
	}
}
