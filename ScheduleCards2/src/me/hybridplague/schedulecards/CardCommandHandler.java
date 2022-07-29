package me.hybridplague.schedulecards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CardCommandHandler implements CommandExecutor {

	private String basePerm = "schedulecards.use";
	private String adminPerm = "schedulecards.admin";
	private String noPerm = CardUtils.format("&cInsufficient Permission");
	
	public static ScheduleCards sc = ScheduleCards.getPlugin(ScheduleCards.class);
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You much be a player to run this command.");
			return true;
		}

		Player p = (Player) sender;
		if (!p.hasPermission(basePerm)) {
			p.sendMessage(noPerm);
			return true;
		}
		
		/*if (!p.hasPermission(adminPerm) ) {
			p.sendMessage(CardUtils.errorWIP());
			return true;
		}*/
		
		/*          0      1       2
		 * scards revoke <type> <player>
		 * scards assign <type> <player>
		 * scards list <type>
		 * scards type
		 */
		
		switch (args.length) {
		case 0:
			CardUtils.helpMessage(p);
			break;
		case 1, 2:
			switch (args[0].toLowerCase()) {
			case "revoke", "assign":
				if (!p.hasPermission(adminPerm)) {
					p.sendMessage(CardUtils.errorNotHS());
					break;
				}
				p.sendMessage(CardUtils.errorMissingArgs(args[0].toLowerCase()));
				break;
			case "search":
				if (args.length < 2) {
					p.sendMessage(CardUtils.format(CardUtils.prefix + "&cMissing Argument: &e/scards search <player>"));
					break;
				}
				OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
				if (!CardUtils.playerExists(op)) { 
					p.sendMessage(CardUtils.errorPlayerNotFound());
					break;
				}
				CardSearch.search(p, op);
				break;
			case "list":
				if (args.length < 2) {
					p.sendMessage(CardUtils.errorMissingType());
					break;
				}
				// >= 2
				switch(args[1].toLowerCase()) {
				case "dispensary":
					sc.cmh.dispensaryHolders(p, 1);
					break;
				case "schedule0":
					sc.cmh.scheduleCardHolders(p, 1);
					break;
				default:
					p.sendMessage(CardUtils.errorMissingType());
				}
				break;
			case "types":
				p.sendMessage(CardUtils.types());
				break;
			default:
				CardUtils.helpMessage(p);
				break;
			}
			break;
		case 3:
			OfflinePlayer op = Bukkit.getOfflinePlayer(args[2]);
			if (!CardUtils.playerExists(op)) { 
				p.sendMessage(CardUtils.errorPlayerNotFound());
				break;
			}
			
			switch (args[0].toLowerCase()) {
			case "search":
				CardSearch.search(p, Bukkit.getOfflinePlayer(args[1]));
				break;
			case "revoke":
				if (!p.hasPermission(adminPerm)) {
					p.sendMessage(CardUtils.errorNotHS());
					break;
				}
				switch(args[1].toLowerCase()) {
				case "schedule0":
					CardUtils.revokeCard(p, "schedule0", op);
					break;
				case "dispensary":
					CardUtils.revokeCard(p, "dispensary", op);
					break;
				default:
					p.sendMessage(CardUtils.errorInvalidType("revoke"));
					break;
				}
				break;
			case "assign":
				if (!p.hasPermission(adminPerm)) {
					p.sendMessage(CardUtils.errorNotHS());
					break;
				}
				switch(args[1].toLowerCase()) {
				case "schedule0":
					CardUtils.assignScheduleCard(p, op);
					break;
				case "dispensary":
					CardUtils.assignDispensaryCard(p, op);
					break;
				default:
					p.sendMessage(CardUtils.errorInvalidType("revoke"));
					break;
				}
				break;
			case "list":
				switch(args[1].toLowerCase()) {
				case "dispensary":
					sc.cmh.dispensaryHolders(p, 1);
					break;
				case "schedule0":
					sc.cmh.scheduleCardHolders(p, 1);
					break;
				default:
					p.sendMessage(CardUtils.errorMissingType());
				}
				break;
			case "types":
				p.sendMessage(CardUtils.types());
				break;
			default:
				CardUtils.helpMessage(p);
				break;
			}
			break;
		}
		return true;
	}
	
}
