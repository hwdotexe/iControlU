package com.hadenwatne.icontrolu.timers;

import com.hadenwatne.icontrolu.plugin.ICUPacketListener;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;

public class Disguise extends BukkitRunnable{
	
	private ICUPacketListener pl;
	private Player p;
	public Disguise(ICUPacketListener packet, Player pp) {
		pl=packet;
		p=pp;
	}
	
	public void run() {
		WrapperPlayServerNamedEntitySpawn packet = new WrapperPlayServerNamedEntitySpawn();
		packet.setPlayerUUID(pl.getControl().getTarget().getUniqueId());
		packet.setEntityID(pl.getControl().getFakeEntityID());
		packet.setPosition(pl.getControl().getController().getLocation().toVector());
		
		packet.sendPacket(p);
		p.hidePlayer(pl.getControl().getTarget());
		p.hidePlayer(pl.getControl().getController());
		
		pl.sendArmor();
		pl.sendItems();
		
		this.cancel();
	}
}
