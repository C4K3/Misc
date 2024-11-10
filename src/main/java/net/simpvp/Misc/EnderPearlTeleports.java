package net.simpvp.Misc;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EnderPearlTeleports implements Listener {
    private final Set<String> allowedWorlds = new HashSet<>(Arrays.asList("world", "world_nether", "world_the_end"));

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == TeleportCause.ENDER_PEARL) {
            World fromWorld = event.getFrom().getWorld();
            World toWorld = event.getTo().getWorld();

            if (!fromWorld.equals(toWorld)) {
                boolean fromAllowed = allowedWorlds.contains(fromWorld.getName());
                boolean toAllowed = allowedWorlds.contains(toWorld.getName());

                if (!(fromAllowed && toAllowed)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
