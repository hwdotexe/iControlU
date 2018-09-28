package tech.hadenw.icontrolu.timers;

import org.bukkit.scheduler.BukkitRunnable;

import tech.hadenw.icontrolu.plugin.ICUPacketListener;

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
