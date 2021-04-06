package net.simpvp.Misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectListener implements Listener {
	@EventHandler
	public void potionEffectEvent(EntityPotionEffectEvent event) {

		if (event.getEntity().getLocation().distance(event.getEntity().getWorld().getSpawnLocation()) <= 200) {
			if (event.getModifiedType().equals(PotionEffectType.SLOW_DIGGING) && event.getCause().equals(EntityPotionEffectEvent.Cause.ATTACK)) {
				
				event.setCancelled(true);
			}
		}
	}
}
