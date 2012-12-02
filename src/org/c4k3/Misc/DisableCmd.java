package org.c4k3.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class DisableCmd implements Listener {
	/* Disables all commands in the disabled.txt file
	 * Adapted from PlgDisableCmd by Plague
	 */
	
	private static List<String> disabledCmds;
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String[] split = event.getMessage().split(" ");
		if (split.length < 1) return;
		
		String cmd = split[0].trim().substring(1).toLowerCase();
		
		if (Collections.binarySearch(disabledCmds, cmd) >= 0) {
			
			event.setCancelled(true);
			event.setMessage("command_has_been_disabled");
			
		}
	}
	
	public static void loadDisabledCmds() {
		
		try {
        	disabledCmds = new ArrayList<String>();
        	File f = new File(Misc.getInstance().getDataFolder(), "disabledCmds.txt");
			if (!f.exists()) {
				System.out.println("No commands to disable " + Misc.getInstance().getDataFolder());
			} else {
				BufferedReader rdr = new BufferedReader(new FileReader(f));
				String line;
				while ((line = rdr.readLine()) != null) {
					line = line.trim();
					if (line.length() < 1) continue;
					disabledCmds.add(line.trim().toLowerCase());
    			}
    			rdr.close();
    			Collections.sort(disabledCmds);
    			Bukkit.getLogger().info("Disabling " + disabledCmds.size() + " commands");
			}
        } catch (Exception e) {
        	Bukkit.getLogger().info("Unexpected error: " + e.getMessage());
        }
		
	}
	
	

}
