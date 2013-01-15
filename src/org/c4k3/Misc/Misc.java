package org.c4k3.Misc;

import org.bukkit.plugin.java.JavaPlugin;

public class Misc extends JavaPlugin {
	
	private static Misc plugin;
	
	@Override
	public void onEnable(){
		plugin = this;
		new ConsoleDeathLog(this);
		new LogCmd(this);
		getServer().getPluginManager().registerEvents(new DisableCmd(), this);
		DisableCmd.loadDisabledCmds();
		getCommand("coords").setExecutor(new CoordsForAll());
		getCommand("follow").setExecutor(new Follow());
		getCommand("goback").setExecutor(new Follow());
		getCommand("a").setExecutor(new AdminChat());
		getCommand("tp").setExecutor(new AdminTeleport());
		getCommand("tps").setExecutor(new AdminTeleport());
		getCommand("tpc").setExecutor(new AdminTeleport());
		getCommand("tpw").setExecutor(new AdminTeleport());
	}
	
	@Override
	public void onDisable(){
		//onDisable
	}
	
	public static Misc getInstance() {
		return plugin;
	}
	
}
