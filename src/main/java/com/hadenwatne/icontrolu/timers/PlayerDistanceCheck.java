package com.hadenwatne.icontrolu.timers;

import com.hadenwatne.icontrolu.plugin.Control;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDistanceCheck extends BukkitRunnable{
	private Control c;
	public PlayerDistanceCheck(Control co){
		c=co;
	}
	
	public void run(){
		if(c.isRunning()){
			if(c.getController().isOnline() && c.getTarget().isOnline()){
				
				// Update Gamemode
				if(c.getTarget().getGameMode()!=c.getController().getGameMode()) {
					c.getTarget().setGameMode(GameMode.ADVENTURE);
					c.getTarget().setAllowFlight(true);
					c.getTarget().setFlying(true);
				}
				
				//World changed, send fake player packets
				if(!c.getController().getWorld().getName().equals(c.getTarget().getWorld().getName())){
					c.getTarget().teleport(c.getController());
						
					c.getPacketListener().destroyFakePlayer();
					c.getPacketListener().spawnFakePlayer();
				}
				
				// Teleport real target based on controller's location
				if(c.getPlugin().getConfiguration().getAllowFreecam()){
					if(c.getController().getLocation().distance(c.getTarget().getLocation()) > c.getPlugin().getConfiguration().getMaxDistance()){
						c.getTarget().teleport(c.getController());
					}
				}else{
					c.getTarget().teleport(c.getController());
				}
				
				// Update target's inventory to reflect the controller's
				c.getTarget().getInventory().setContents(c.getController().getInventory().getContents());
				c.getTarget().getInventory().setArmorContents(c.getController().getInventory().getArmorContents());
			}else{
				this.cancel();
			}
		}else{
			this.cancel();
		}
	}
}
