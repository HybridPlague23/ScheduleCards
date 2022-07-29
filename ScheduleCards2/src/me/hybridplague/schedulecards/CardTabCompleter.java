package me.hybridplague.schedulecards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CardTabCompleter implements TabCompleter {

	List<String> firstArg = new ArrayList<String>();
	List<String> types = new ArrayList<String>();
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (firstArg.isEmpty()) {
			if (sender.hasPermission("schedulecards.admin")) {
				firstArg.add("assign");
				firstArg.add("revoke");
			}
			firstArg.add("list");
			firstArg.add("types");
			firstArg.add("search");
		}
		
		if (types.isEmpty()) {
			types.add("schedule0");
			types.add("dispensary");
		}
		
		List<String> result = new ArrayList<String>();
		
		if (args.length == 1) {
			for (String a : firstArg) {
				if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("search") || args[0].equalsIgnoreCase("types")) return null;
			for (String a : types) {
				if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		}
		return null;
	}
	
}
