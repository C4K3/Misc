package net.simpvp.Misc;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Modifies damage source data in various ways
 * to prevent it from being used to track players positions
 *
 * @author cats
 */
public class DamageSource implements Listener {

    private final int maxDistanceSq;

    public DamageSource(int maxDistance) {
        this.maxDistanceSq = maxDistance * maxDistance;
    }

    /**
     * This handler removes the TNT explosion source
     * By doing this, we prevent the tnt knockback from an improper damage source
     */
    @EventHandler
    public void onExplosionPrimed(ExplosionPrimeEvent event) {
        if (event.getEntityType() == EntityType.PRIMED_TNT) {
            final TNTPrimed tnt = (TNTPrimed) event.getEntity();

            tnt.setSource(null);
        }
    }

    /**
     * Reset the velocity 1 tick after the player takes damage
     *
     * Possible todo: maybe make this run async to avoid tps lag playing a factor
     * I don't think it does because bukkit scheduler, but who knows
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            // This SHOULD always be a successful cast
            final Projectile projectile = (Projectile) event.getDamager();

            if (projectile.getShooter() instanceof Player) {
                final Player shooter = (Player) projectile.getShooter();


                // I'm wincing but it shouldn't be too much math
                if (projectile.getLocation().distanceSquared(shooter.getLocation()) > this.maxDistanceSq) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            event.getEntity().setVelocity(new Vector(0, event.getEntity().getVelocity().getY(), 0));

                        }
                        // The velocity should be sent all at once, so we don't send 2 conflicting ones
                        // Just 1 all at once
                    }.runTaskLater(Misc.instance, 1L);
                }
            }
        }
    }
}
