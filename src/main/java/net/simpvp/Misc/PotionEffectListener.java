package net.simpvp.Misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

//This file disables elder guardians from giving mining fatigue within 100 blocks of spawn
public class PotionEffectListener implements Listener {
	
	List<?> listOfWorlds = Misc.instance.getConfig().getList("disableElderGuardians");
	
	@EventHandler
	public void potionEffectEvent(EntityPotionEffectEvent event) {
		
		//Check if world name is in the config
		if (!listOfWorlds.contains(event.getEntity().getWorld().getName())) {
			return;
		}
		
		//If entity is less than 100 blocks from spawn
		if (event.getEntity().getLocation().distanceSquared(event.getEntity().getWorld().getSpawnLocation()) <= 100*100) {
			//If potion equals mining fatigue and the event is from the "attack" cause
			if (event.getModifiedType().equals(PotionEffectType.SLOW_DIGGING) && event.getCause().equals(EntityPotionEffectEvent.Cause.ATTACK)) {
				//Cancel event
				event.setCancelled(true);
			}
		}
	}
}
