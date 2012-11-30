package org.c4k3.Misc;

import org.bukkit.plugin.java.JavaPlugin;

public class Misc extends JavaPlugin {

	@Override
	public void onEnable(){
		new ConsoleDeathLog(this);
		new LogCmd(this);
		getCommand("coords").setExecutor(new CoordsForAll());
		getCommand("rules").setExecutor(new Rules());
		getCommand("follow").setExecutor(new Follow());
		getCommand("goback").setExecutor(new Follow());
	}
	
	@Override
	public void onDisable(){
		//onDisable
	}
	
}
