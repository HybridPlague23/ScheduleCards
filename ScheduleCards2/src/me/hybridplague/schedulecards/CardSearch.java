package me.hybridplague.schedulecards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CardSearch {

	private static ScheduleCards sc = ScheduleCards.getPlugin(ScheduleCards.class);
	
	private static List<UUID> scheduleList= new ArrayList<UUID>();
	private static List<UUID> dispensaryList= new ArrayList<UUID>();
	
	public static void search(Player p, OfflinePlayer op) {
		
		UUID id = op.getUniqueId();
		
		if (sc.data.getConfig().getConfigurationSection("schedule0.holders").getKeys(false).isEmpty()) {
			
		} else {
			sc.data.getConfig().getConfigurationSection("schedule0.holders").getKeys(false).forEach(key -> {
				scheduleList.add(UUID.fromString(key));
			});
		}
		
		if (sc.data.getConfig().getConfigurationSection("dispensary.holders").getKeys(false).isEmpty()) {
		} else {
			sc.data.getConfig().getConfigurationSection("dispensary.holders").getKeys(false).forEach(key -> {
				dispensaryList.add(UUID.fromString(key));
			});
		}
		
		if (!scheduleList.contains(id) && !dispensaryList.contains(id)) {
			p.sendMessage(CardUtils.format(CardUtils.prefix + op.getName() + " does not have a Schedule0 Card or a Dispenary Permit"));
		}

		if (scheduleList.contains(id) && !dispensaryList.contains(id)) {
			
			p.sendMessage(CardUtils.format("&8--- &bSchedule0 &8---"));
			p.sendMessage("");
			p.sendMessage(CardUtils.format("&bIssued Date: &7" + sc.data.getConfig().getString("schedule0.holders." + id + ".issuedDate")));
			p.sendMessage(CardUtils.format("&bIssued By: &7" + Bukkit.getOfflinePlayer(UUID.fromString(sc.data.getConfig().getString("schedule0.holders." + id + ".issuedBy"))).getName()));
			p.sendMessage(CardUtils.format("&bExpires On: &7" + sc.data.getConfig().getString("schedule0.holders." + id + ".expirationDate")));
			p.sendMessage(CardUtils.format("&8----------------"));
			
		}
		
		if (!scheduleList.contains(id) && dispensaryList.contains(id)) {
			
			p.sendMessage(CardUtils.format("&8--- &bDispenary &8---"));
			p.sendMessage("");
			p.sendMessage(CardUtils.format("&bIssued Date: &7" + sc.data.getConfig().getString("dispensary.holders." + id + ".issuedDate")));
			p.sendMessage(CardUtils.format("&bIssued By: &7" + Bukkit.getOfflinePlayer(UUID.fromString(sc.data.getConfig().getString("dispensary.holders." + id + ".issuedBy"))).getName()));
			p.sendMessage(CardUtils.format("&bExpires On: &7" + sc.data.getConfig().getString("dispensary.holders." + id + ".expirationDate")));
			p.sendMessage(CardUtils.format("&8----------------"));
			
		}
		
		if (scheduleList.contains(id) && dispensaryList.contains(id)) {
			
			p.sendMessage(CardUtils.format("&8--- &bSchedule0 &8---"));
			p.sendMessage("");
			p.sendMessage(CardUtils.format("&bIssued Date: &7" + sc.data.getConfig().getString("dispensary.holders." + id + ".issuedDate")));
			p.sendMessage(CardUtils.format("&bIssued By: &7" + Bukkit.getOfflinePlayer(UUID.fromString(sc.data.getConfig().getString("dispensary.holders." + id + ".issuedBy"))).getName()));
			p.sendMessage(CardUtils.format("&bExpires On: &7" + sc.data.getConfig().getString("dispensary.holders." + id + ".expirationDate")));
			p.sendMessage(CardUtils.format("&8------------------"));
			p.sendMessage("");
			p.sendMessage(CardUtils.format("&8--- &bDispenary &8---"));
			p.sendMessage("");
			p.sendMessage(CardUtils.format("&bIssued Date: &7" + sc.data.getConfig().getString("schedule0.holders." + id + ".issuedDate")));
			p.sendMessage(CardUtils.format("&bIssued By: &7" + Bukkit.getOfflinePlayer(UUID.fromString(sc.data.getConfig().getString("schedule0.holders." + id + ".issuedBy"))).getName()));
			p.sendMessage(CardUtils.format("&bExpires On: &7" + sc.data.getConfig().getString("schedule0.holders." + id + ".expirationDate")));
			p.sendMessage(CardUtils.format("&8------------------"));
			
		}
		scheduleList.clear();
		dispensaryList.clear();
		
		
	}
	
	
}
