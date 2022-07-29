package me.hybridplague.schedulecards;

import org.bukkit.plugin.java.JavaPlugin;

public class ScheduleCards extends JavaPlugin {

	public CardMenuHandler cmh;
	DataManager data;
	
	@Override
	public void onEnable() {
		
		this.data = new DataManager(this);
		
		this.getCommand("schedulecards").setTabCompleter(new CardTabCompleter());
		this.getCommand("scards").setTabCompleter(new CardTabCompleter());
		
		this.getServer().getPluginManager().registerEvents(new CardMenuHandler(), this);
		this.getCommand("schedulecards").setExecutor(new CardCommandHandler());
		this.getCommand("scards").setExecutor(new CardCommandHandler());
		
		this.cmh = new CardMenuHandler();
		
		CardUtils.checkExpirations();
		
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}
	
	
}
