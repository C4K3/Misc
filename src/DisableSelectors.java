package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This plugin cancels all player commands that may contain selectors
 */
public class DisableSelectors implements Listener {
	
	@EventHandler(priority=EventPriority.NORMAL,ignoreCancelled=true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String message = event.getMessage();
				
		if (!message.contains("@a")
				&& !message.contains("@p")
				&& !message.contains("@r")
				&& !message.contains("@e"))
			return;
				
		event.setCancelled(true);
		event.getPlayer().sendMessage(ChatColor.RED
				+ "You are not allowed to use selectors in your commands "
				+ "\n (such as @a, @p, et.c.)");
		
	}

}
