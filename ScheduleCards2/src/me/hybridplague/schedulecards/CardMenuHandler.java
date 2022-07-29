package me.hybridplague.schedulecards;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CardMenuHandler implements Listener {
	
	ScheduleCards sc = ScheduleCards.getPlugin(ScheduleCards.class);

	private Inventory inv;
	
	public static double values = 0;
	
	public static List<UUID> holderList = new ArrayList<UUID>();
	public static List<Inventory> inventories = new ArrayList<Inventory>();
	
	public static Map<Player, String> type = new HashMap<Player, String>();
	public static Map<Player, Integer> totalPages = new HashMap<Player, Integer>();
	public static Map<Player, Integer> currentPage = new HashMap<Player, Integer>();
	public static Map<Player, Integer> position = new HashMap<Player, Integer>();
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (inventories.contains(e.getInventory())) {
			Player p = (Player) e.getWhoClicked();
			
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) return;
			if (!e.getCurrentItem().hasItemMeta()) return;
			if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;
			
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Next")) {
				int newValue = currentPage.get(p) + 1;
				
				currentPage.put(p, newValue);
				if (type.get(p).equals("dispensary")) {
					dispensaryHolders(p, newValue);
					return;
				}
				scheduleCardHolders(p, newValue);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Back")) {
				int newValue = currentPage.get(p) - 1;
				
				currentPage.put(p, newValue);
				if (type.get(p).equals("dispensary")) {
					dispensaryHolders(p, newValue);
					return;
				}
				scheduleCardHolders(p, newValue);
			}
		}
	}
	
	public void dispensaryHolders(Player p, int current) {
		
		if (sc.data.getConfig().getConfigurationSection("dispensary.holders").getKeys(false).isEmpty()) {
			p.sendMessage(CardUtils.format(CardUtils.prefix + "No one to display."));
			return;
		} else {
			sc.data.getConfig().getConfigurationSection("dispensary.holders").getKeys(false).forEach(key -> {
				holderList.add(UUID.fromString(key));
			});
		}
		
		int max = 45;
		double values = holderList.size();
		double pages = Math.ceil(values/max);
		
		totalPages.put(p,  (int) pages);
		currentPage.put(p, current);
		
		inv = Bukkit.createInventory(null, 54, CardUtils.format("&cDispensary Permit &7" + String.valueOf(current) + "/" + String.valueOf((int) pages) + " (" + (int) values + ")"));
		inventories.add(inv);
		p.openInventory(inv);
		
		for (int i = 45; i < 54; i++) {
			inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		}
		
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
	
		/*
		 * execute:
		 *   start position at 0 if page is 1
		 *   if page is 2, start array at max + 1 (max = 45 + 1 = 46)
		 *       page 1: 0     - max * (page-1) [max = 45, page = 0 (1-1)] max * 0 = 0
		 *       page 2: 45    - max * (page-1) [max = 45, page = 1 (2-1)] max * 1 = 45
		 *       page 3: 90    - max * (page-1) [max = 45, page = 2 (3-1)] max * 2 = 90
		 */
		try {
			position.put(p, max * (current - 1));
			
			for (int i = 0; i < max; i++) {
				UUID id = UUID.fromString(holderList.toArray()[position.get(p)].toString());
				
				lore.add("");
				
				try {
					lore.add(CardUtils.format("&bIssued Date: &7" + sc.data.getConfig().getString("dispensary.holders." + id + ".issuedDate")));
					lore.add(CardUtils.format("&bIssued By: &7" + Bukkit.getOfflinePlayer(UUID.fromString(sc.data.getConfig().getString("dispensary.holders." + id + ".issuedBy"))).getName()));
					lore.add(CardUtils.format("&bExpires On: &7" + sc.data.getConfig().getString("dispensary.holders." + id + ".expirationDate")));
					inv.addItem(CardUtils.createSkull(UUID.fromString(holderList.toArray()[position.get(p)].toString()), lore));
					lore.clear();
					position.put(p, position.get(p) + 1);
				} catch (NullPointerException ex) {
					// do nothing
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			// do nothing
		}
		
		if (totalPages.get(p) > currentPage.get(p)) {
			meta.setDisplayName(ChatColor.RED + "Next");
			item.setItemMeta(meta);
			inv.setItem(53, item);
		}
		
		if (current > 1) {
			meta.setDisplayName(ChatColor.RED + "Back");
			item.setItemMeta(meta);
			inv.setItem(45, item);
		}
		
		holderList.clear();
		
	}
	
	public void scheduleCardHolders(Player p, int current) {
		
		if (sc.data.getConfig().getConfigurationSection("schedule0.holders").getKeys(false).isEmpty()) {
			p.sendMessage(CardUtils.format(CardUtils.prefix + "No one to display."));
			return;
		} else {
			sc.data.getConfig().getConfigurationSection("schedule0.holders").getKeys(false).forEach(key -> {
				holderList.add(UUID.fromString(key));
			});
		}
		
		int max = 45;
		double values = holderList.size();
		double pages = Math.ceil(values/max);
		
		totalPages.put(p,  (int) pages);
		currentPage.put(p, current);
		
		inv = Bukkit.createInventory(null, 54, CardUtils.format("&cSchedule0 &7" + String.valueOf(current) + "/" + String.valueOf((int) pages) + " (" + (int) values + ")"));
		inventories.add(inv);
		p.openInventory(inv);
		
		for (int i = 45; i < 54; i++) {
			inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		}
		
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
	
		/*
		 * execute:
		 *   start position at 0 if page is 1
		 *   if page is 2, start array at max + 1 (max = 45 + 1 = 46)
		 *       page 1: 0     - max * (page-1) [max = 45, page = 0 (1-1)] max * 0 = 0
		 *       page 2: 45    - max * (page-1) [max = 45, page = 1 (2-1)] max * 1 = 45
		 *       page 3: 90    - max * (page-1) [max = 45, page = 2 (3-1)] max * 2 = 90
		 */
		try {
			position.put(p, max * (current - 1));
			
			for (int i = 0; i < max; i++) {
				UUID id = UUID.fromString(holderList.toArray()[position.get(p)].toString());
				
				lore.add("");
				
				try {
					
					String issued = sc.data.getConfig().getString("schedule0.holders." + id + ".issuedDate");
					String expires = sc.data.getConfig().getString("schedule0.holders." + id + ".expirationDate");
					
					SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
					Date d1 = new Date();
					Date d2 = sdf.parse(expires);
					
					lore.add(CardUtils.format("&bIssued Date: &7" + issued));
					lore.add(CardUtils.format("&bIssued By: &7" + Bukkit.getOfflinePlayer(UUID.fromString(sc.data.getConfig().getString("schedule0.holders." + id + ".issuedBy"))).getName()));
					lore.add(CardUtils.format("&bExpires On: &7" + expires + " &o(" + CardUtils.daysBetween(d1, d2) + " Days left)"));
					inv.addItem(CardUtils.createSkull(UUID.fromString(holderList.toArray()[position.get(p)].toString()), lore));
					lore.clear();
					position.put(p, position.get(p) + 1);
				} catch (NullPointerException | ParseException ex) {
					// do nothing
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			// do nothing
		}
		
		if (totalPages.get(p) > currentPage.get(p)) {
			meta.setDisplayName(ChatColor.RED + "Next");
			item.setItemMeta(meta);
			inv.setItem(53, item);
		}
		
		if (current > 1) {
			meta.setDisplayName(ChatColor.RED + "Back");
			item.setItemMeta(meta);
			inv.setItem(45, item);
		}
		
		holderList.clear();
	}
	
}
