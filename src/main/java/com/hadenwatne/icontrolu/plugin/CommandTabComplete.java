package com.hadenwatne.icontrolu.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class CommandTabComplete implements TabCompleter{
	private static final List<String> commands = Arrays.asList(new String[] {"control", "controlnearest", "stop", "forcechat", "reload", "simple"});
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> matches = new ArrayList<String>();
		
		if(args.length > 0) {
			if(args.length == 1) {
				StringUtil.copyPartialMatches(args[0], commands, matches);
				
			} else if(args[0].equalsIgnoreCase("control") && args.length <= 2) {
				matches.add("<player>");
			} else if(args[0].equalsIgnoreCase("control") && args.length <= 3) {
				matches.add("[player]");
			} else if(args[0].equalsIgnoreCase("stop") && args.length <= 2) {
				matches.add("[player]");
			} else if(args[0].equalsIgnoreCase("forcechat") && args.length <= 2) {
				matches.add("<player>");
			} else if(args[0].equalsIgnoreCase("forcechat") && args.length <= 3) {
				matches.add("<message>");
			}
		}
		
		return matches;
	}
}
