package net.simpvp.Misc;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/* If the player logging out was an OP, it executes the Follow.unexemptPlayers method
*/
public class FollowLogoutListener implements Listener {

	@EventHandler(priority=EventPriority.NORMAL,ignoreCancelled=false)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {

		Player player = event.getPlayer();

		if (player.isOp())
			Follow.unexemptPlayers(player);

	}

}

