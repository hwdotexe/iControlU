package com.hadenwatne.icontrolu.timers;

import com.hadenwatne.icontrolu.plugin.iControlU;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Cooldown extends BukkitRunnable{
	private Player p;
	private String uuid;
	private iControlU plugin;
	
	public Cooldown(iControlU c, Player pl){
		plugin=c;
		p=pl;
		uuid=p.getUniqueId().toString();
		
		plugin.getCooldowns().add(uuid);
	}
	
	public void run(){
		plugin.getCooldowns().remove(uuid);
		
		if(p.isOnline()){
			p.sendMessage(plugin.getMessages().cooldownEnded());
		}
		
		this.cancel();
	}
}
