package net.simpvp.Misc;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import net.md_5.bungee.api.ChatColor;
import net.simpvp.Events.Event;

public class ServerListMessage implements Listener {
	
	List<?> listOfMOTDS = Misc.instance.getConfig().getList("serverListMessage");
	String motd = "Welcome to simpvp";
	
	
	@EventHandler
	public void serverListPingEvent(ServerListPingEvent event) {

		
		if (Event.getIsActive() && Event.getEventName() != null) {
			event.setMotd(ChatColor.LIGHT_PURPLE + "Currently playing:" + Event.getEventName());
			return;
		}
		
		if (!listOfMOTDS.isEmpty()) {
			// Get a random motd from the config
			motd = (String) listOfMOTDS.get(ThreadLocalRandom.current().nextInt(listOfMOTDS.size()));
			event.setMotd(motd);
		}
	}
}

