package tech.hadenw.icontrolu.timers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import tech.hadenw.icontrolu.plugin.Control;

public class Invisibility extends BukkitRunnable{
	private Control c;
	private BossBar timer;
	private double secs;
	private double i;
	public Invisibility(Control con, double str){
		c=con;
		secs=str;
		i=str;
		timer = Bukkit.createBossBar("Invisibility", BarColor.WHITE, BarStyle.SOLID);
		timer.setProgress(i/secs);
		timer.addPlayer(c.getController());
		
		for(Player op : Bukkit.getOnlinePlayers()){
			if(!op.equals(c.getController()))
				op.hidePlayer(c.getController());
		}
		
		c.getController().sendMessage(c.getPlugin().getMessages().controllerHidden());
	}
	
	public void run(){
		if(isPlayerControlling()) {
			timer.removeAll();
			this.cancel();
		}
		
		if(i > 0){
			i--;
			double x = i/secs;
			timer.setProgress(x);
			
			for(Player op : Bukkit.getOnlinePlayers()){
				if(!op.equals(c.getController())) {
					if(!op.canSee(c.getController()))
						op.hidePlayer(c.getController());
				}
			}
		}else{
			timer.removeAll();
			
			if(c.getController().isOnline())
				for(Player op : Bukkit.getOnlinePlayers()){
					op.showPlayer(c.getController());
				}
			
			c.getController().sendMessage(c.getPlugin().getMessages().controllerVisible());
			this.cancel();
		}
	}
	
	private boolean isPlayerControlling() {
		for(Control con : c.getPlugin().getControlSessions()) {
			if(con.getController().equals(c.getController()) || con.getTarget().equals(c.getController())){
				if(!con.equals(c))
					return true;
			}
		}
		
		return false;
	}
}
