package net.simpvp.Misc;

import org.bukkit.plugin.java.JavaPlugin;


public class Misc extends JavaPlugin {

	public static JavaPlugin instance;

	@Override
	public void onEnable(){
		instance = this;
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		PluginLoadFailsafe.schedule_check();

		new ConsoleDeathLog(this);
		new LogCmd(this);
		new BlockLog(this);
		getServer().getPluginManager().registerEvents(new DisableCmd(), this);
		//getServer().getPluginManager().registerEvents(new FollowLogoutListener(), this);
		getServer().getPluginManager().registerEvents(new ExactSpawn(), this);
		getServer().getPluginManager().registerEvents(new BedLog(), this);
		getServer().getPluginManager().registerEvents(new TimeSinceLastJoin(), this);
		if (this.getConfig().getBoolean("disableSelectors")) {
			getServer().getPluginManager().registerEvents(new DisableSelectors(), this);
		}
		if (this.getConfig().getBoolean("simpvpHostnameChecker")) {
			getServer().getPluginManager().registerEvents(new HostnameChecker(), this);
		}
		getServer().getPluginManager().registerEvents(new BedTrap(), this);
		getServer().getPluginManager().registerEvents(new EntityResurrect(), this);
		new InventoryLog(this);
		getServer().getPluginManager().registerEvents(new AdminNameChange(), this);
		getServer().getPluginManager().registerEvents(new TameableTracking(), this);
		getServer().getPluginManager().registerEvents(new DamageSource(this.getConfig().getInt("projectileOwnerMaxDistance")), this);
		getServer().getPluginManager().registerEvents(new Announcement(this), this);
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
		new AgeCommand(this);
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
		EntityCounts ec_instance = new EntityCounts();
		getCommand("entitycounts").setExecutor(ec_instance);
		getCommand("entitycounts").setTabCompleter(ec_instance);
		RelativeCoords rc_instance = new RelativeCoords();
		getServer().getPluginManager().registerEvents(rc_instance, this);
		getCommand("relativecoords").setExecutor(rc_instance);
		getServer().getPluginManager().registerEvents(new PotionEffectListener(), this);
		getServer().getPluginManager().registerEvents(new ServerListMessage(this), this);
		getCommand("chunkdebug").setExecutor(new ChunkDebug());
		new InfoCommands(this);
		new MOTD(this);


		if (getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
			Thunder.add_protocol_listeners();
		} else {
			getLogger().warning("ProtocolLib not found, not enabling thunder modification");
			return;
		}
	}

	@Override
	public void onDisable(){
		//onDisable
	}

}

