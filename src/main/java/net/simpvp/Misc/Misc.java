package net.simpvp.Misc;

import org.bukkit.plugin.java.JavaPlugin;

public class Misc extends JavaPlugin {

	public static JavaPlugin instance;

	@Override
	public void onEnable(){
		instance = this;
		new ConsoleDeathLog(this);
		new LogCmd(this);
		getServer().getPluginManager().registerEvents(new DisableCmd(), this);
		//getServer().getPluginManager().registerEvents(new FollowLogoutListener(), this);
		getServer().getPluginManager().registerEvents(new ExactSpawn(), this);
		getServer().getPluginManager().registerEvents(new BedLog(), this);
		getServer().getPluginManager().registerEvents(new TimeSinceLastJoin(), this);
		getServer().getPluginManager().registerEvents(new DisableSelectors(), this);
		getServer().getPluginManager().registerEvents(new BedTrap(), this);
		getServer().getPluginManager().registerEvents(new EntityResurrect(), this);
		getServer().getPluginManager().registerEvents(new AdminNameChange(), this);
		getCommand("coords").setExecutor(new CoordsForAll());
		getCommand("f").setExecutor(new Follow());
		getCommand("b").setExecutor(new Follow());
		getCommand("a").setExecutor(new AdminChat());
		getCommand("tpo").setExecutor(new AdminTeleport());
		getCommand("tpc").setExecutor(new AdminTeleport());
		getCommand("tpw").setExecutor(new AdminTeleport());
		getCommand("dw").setExecutor(new AdminShortcuts());
		getCommand("dp").setExecutor(new AdminShortcuts());
		getCommand("nearby").setExecutor(new Nearby());
		getCommand("x").setExecutor(new AdminShortcuts());
		getCommand("age").setExecutor(new AgeCommand());
		getCommand("requestrestart").setExecutor(new Restart());
		getCommand("cancelrestart").setExecutor(new Restart());
		getCommand("say").setExecutor(new AnonymousSay());
		getCommand("date").setExecutor(new DateCommand());
		getCommand("uptime").setExecutor(new UptimeCommand());
		NoNewPlayers nnp_instance = new NoNewPlayers();
		getCommand("nonewplayers").setExecutor(nnp_instance);
		OnlineCheck oc_instance = new OnlineCheck();
		getCommand("checkonline").setExecutor(oc_instance);
		getCommand("on").setExecutor(oc_instance);
		getServer().getPluginManager().registerEvents(nnp_instance, this);
		getCommand("entitycounts").setExecutor(new EntityCounts());

		Thunder.add_protocol_listeners();
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

	@Override
	public void onDisable(){
		//onDisable
	}

}

