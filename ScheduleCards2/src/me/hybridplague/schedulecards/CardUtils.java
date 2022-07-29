package me.hybridplague.schedulecards;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.eclipse.jdt.annotation.Nullable;

public class CardUtils {
	
	
	public final static String prefix = "&8&lScheduleCards &b&l⼁ &r";
	
	public static ScheduleCards sc = ScheduleCards.getPlugin(ScheduleCards.class);
	
	public static String format(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static void helpMessage(Player p) {
		p.sendMessage(format("&8&l/scards..."));
		if (p.hasPermission("schedulecards.admin")) {
			p.sendMessage(format("    &cassign <type> <player>"));
			p.sendMessage(format("    &crevoke <player>"));
		}
		p.sendMessage(format("    &clist <type>"));
		p.sendMessage(format("    &csearch <player>"));
		p.sendMessage(format("    &ctypes"));
	}
	
	public static long daysBetween(Date d1, Date d2) {
		return (long) (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 *24);
	}
	
	public static boolean playerExists(OfflinePlayer op) {
		if (!op.hasPlayedBefore() && !op.isOnline()) return false;
		return true;
	}
	
	public static String errorNotHS() {
		return format(prefix + "Only DoH members can assign/revoke cards.");
	}
	
	public static String types() {
		return format(prefix + "&eSchedule0&7, &eDispensary");
	}
	
	public static String errorPlayerNotFound() {
		return format(prefix + "Player not found.");
	}
	
	
	public static String errorMissingArgs(String command) {
		return format(prefix + "&cMissing Arguments: &e/scards " + command + " &c<type> <player>");
	}
	
	public static String errorWIP() {
		return format(prefix + "This plugin is currently in re-development! There will be an announcement when it is completed.");
	}
	
	public static String errorInvalidType(String command) {
		return format(prefix + "&cInvalid Argument: &e/scards " + command + " &c<type> &e<player>");
	}
	
	public static String errorMissingType() {
		return format(prefix + "&cMissing Argument: &e/scards list <type>");
	}
	
	public static Date date = new Date();
	public static SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
	
	public static void assignDispensaryCard(Player issuedBy, OfflinePlayer player) {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 56);
		String newDate = sdf.format(c.getTime());
		
		sc.data.getConfig().set("dispensary.holders." + player.getUniqueId().toString() + ".issuedBy", issuedBy.getUniqueId().toString());
		sc.data.getConfig().set("dispensary.holders." + player.getUniqueId().toString() + ".issuedDate", sdf.format(date));
		sc.data.getConfig().set("dispensary.holders." + player.getUniqueId().toString() + ".expirationDate", newDate);
		sc.data.saveConfig();
		
		issuedBy.sendMessage(format("&e" + player.getName() + " &ahas been assigned the &eDispensary Permit&a. Expires on: &e" + newDate));
		
	}
	
	public static void assignScheduleCard(Player issuedBy, OfflinePlayer player) {
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 28);
		String newDate = sdf.format(c.getTime());
		
		sc.data.getConfig().set("schedule0.holders." + player.getUniqueId().toString() + ".issuedBy", issuedBy.getUniqueId().toString());
		sc.data.getConfig().set("schedule0.holders." + player.getUniqueId().toString() + ".issuedDate", sdf.format(date));
		sc.data.getConfig().set("schedule0.holders." + player.getUniqueId().toString() + ".expirationDate", newDate);
		sc.data.saveConfig();
		
		issuedBy.sendMessage(format("&e" + player.getName() + " &ahas been assigned the &eSchedule0 Card&a. Expires on: &e" + newDate));
		
	}
	
	public static void revokeCard(Player p, String type, OfflinePlayer player) {
		if (type.equalsIgnoreCase("schedule0")) {
			sc.data.getConfig().set("schedule0.holders." + player.getUniqueId().toString(), null);
			sc.data.saveConfig();
			p.sendMessage(format("&e" + player.getName() + " &chas been revoked the &eSchedule0 Card&a."));
			return;
		}
		sc.data.getConfig().set("dispensary.holders." + player.getUniqueId().toString(), null);
		sc.data.saveConfig();
		p.sendMessage(format("&e" + player.getName() + " &chas been revoked the &eDispensary Permit&a."));
		return;
	}
	
	public static void checkExpirations() {
		
		sc.data.getConfig().getConfigurationSection("schedule0.holders").getKeys(false).forEach(key -> {
			
			if (sc.data.getConfig().getString("schedule0.holders." + key + ".expirationDate").equals(sdf.format(date))) {
				sc.data.getConfig().set("schedule0.holders." + key + ".expirationDate", "Expired!");
			}
			
		});
		sc.data.getConfig().getConfigurationSection("dispensary.holders").getKeys(false).forEach(key -> {
			
			if (sc.data.getConfig().getString("dispensary.holders." + key + ".expirationDate").equals(sdf.format(date))) {
				sc.data.getConfig().set("dispensary.holders." + key + ".expirationDate", "Expired!");
			}
			
		});
		
		sc.data.saveConfig();
		
	}
	
	public static ItemStack createSkull(UUID uuid, @Nullable List<String> lore) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		List<String> itemLore = new ArrayList<String>();
		
		if (lore != null) {
			for (String l : lore) {
				itemLore.add(CardUtils.format(l));
			}
			meta.setLore(itemLore);
		}
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
		meta.setDisplayName(ChatColor.YELLOW + "" + Bukkit.getOfflinePlayer(uuid).getName());
		item.setItemMeta(meta);
		return item;
	}
	
}
