package tech.hadenw.icontrolu.timers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import tech.hadenw.icontrolu.plugin.Control;

public class ControlTimer extends BukkitRunnable{
	private Control c;
	private BossBar timer;
	private double secs;
	private double i;
	public ControlTimer(Control con, double str){
		c=con;
		secs=str;
		i=str;
		timer = Bukkit.createBossBar("Control Timer", BarColor.YELLOW, BarStyle.SOLID);
		timer.setProgress(i/secs);
		timer.addPlayer(c.getController());
	}
	
	public void run(){
		if(c.isRunning()){
			if(i > 0){
				i--;
				double x = i/secs;
				timer.setProgress(x);
			}else{
				timer.removeAll();
				c.stopControl();
				c.getController().sendMessage(c.getPlugin().getMessages().stopControl(c.getTarget().getName()));
				
				if(c.getPlugin().getConfiguration().getShowMessages())
					c.getTarget().sendMessage(c.getPlugin().getMessages().stopControlVictim(c.getController().getName()));
				
				this.cancel();
			}
		}else{
			timer.removeAll();
			this.cancel();
		}
	}
}
