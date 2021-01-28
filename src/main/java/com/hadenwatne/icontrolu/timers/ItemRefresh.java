package com.hadenwatne.icontrolu.timers;

import com.hadenwatne.icontrolu.plugin.ICUPacketListener;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemRefresh extends BukkitRunnable {
	private ICUPacketListener pl;
	
	public ItemRefresh(ICUPacketListener packet) {
		pl=packet;
	}
	
	public void run() {
		if(pl.getControl().isRunning()) {
			pl.sendArmor();
			pl.sendItems();
		}else {
			this.cancel();
		}
	}
}
