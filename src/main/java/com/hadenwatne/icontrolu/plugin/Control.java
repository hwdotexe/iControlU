package com.hadenwatne.icontrolu.plugin;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

import net.md_5.bungee.chat.ComponentSerializer;
import com.hadenwatne.icontrolu.timers.ControlTimer;
import com.hadenwatne.icontrolu.timers.Cooldown;
import com.hadenwatne.icontrolu.timers.Invisibility;
import com.hadenwatne.icontrolu.timers.ItemRefresh;
import com.hadenwatne.icontrolu.timers.PlayerDistanceCheck;

public class Control {
	private Player target;
	private Player controller;
	private iControlU plugin;
	private ItemStack[] controller_inventory;
	private ItemStack[] controller_armor;
	private Random r;
	private ICUPacketListener pl;
	private int entity_id;
	private boolean isRunning;
	private GameMode targetGameMode;
	private GameMode controllerGameMode;
	private boolean canTargetFly;
	private boolean isTargetFlying;
	private int controllerFood;
	private double controllerHealth;
	private boolean simpleMode;
	private Location cOrig;
	private PacketListener chat;
	private ItemRefresh refresh;
	
	public Control(iControlU c, Player tar, Player con){
		plugin = c;
		target = tar;
		controller = con;
		r = new Random();
		isRunning = false;
		simpleMode = controller.hasMetadata("iCU_SimpleMode");
		
		entity_id = this.getRandomInteger(3000) * -1;
		
		chat = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
	        @Override
	        public void onPacketSending(PacketEvent event) {
	            if (event.getPlayer().equals(target)) {
	            	if(controller.hasPermission("icu.readchat")) {
		            	String text = ChatColor.translateAlternateColorCodes('&',"&8&o[&c&o@"+target.getName()+"&8&o] &7&o")+ComponentSerializer.parse(event.getPacket().getChatComponents().read(0).getJson().toString())[0].toLegacyText();
		            	controller.sendMessage(text);
	            	}
	            }
	        }
		};
	}
	
	public void startControl(){
		boolean controlTarget = true;
		boolean wearingHelm = false;
		
		if(!simpleMode)
			if(target.getInventory().getHelmet() != null)
				if(target.getInventory().getHelmet().isSimilar(plugin.reductionHelmet())){
					if(r.nextInt(10) > 7)
						controlTarget = false;
				}
			
			if(controller.getInventory().getHelmet() != null)
				if(controller.getInventory().getHelmet().isSimilar(plugin.amplifyingHelmet())){
					wearingHelm = true;
				}
		
		if(controlTarget){
			this.saveControllerInventory();
			this.targetGameMode = target.getGameMode();
			this.canTargetFly = target.getAllowFlight();
			this.isTargetFlying = target.isFlying();
			
			this.controllerGameMode = controller.getGameMode();
			this.controllerFood = controller.getFoodLevel();
			this.controllerHealth = controller.getHealth();
			
			cOrig = controller.getLocation();
			controller.teleport(target.getLocation());
			
			// Send messages
			if(!simpleMode)
				for(Player op : Bukkit.getOnlinePlayers()){
					if(op.getWorld().getName()==controller.getWorld().getName())
						if(op.getLocation().distance(controller.getLocation()) <= 30)
							op.playSound(controller.getLocation(),  plugin.getConfiguration().getPlaySound(), 5, 1);
				}
			
			pl = new ICUPacketListener(this);
			pl.spawnFakePlayer();
			plugin.getServer().getPluginManager().registerEvents(pl, plugin);
			refresh = new ItemRefresh(pl);
			refresh.runTaskTimer(plugin, 100, 100);
			
			plugin.getProtocolManager().addPacketListener(chat);
			
			target.setAllowFlight(true);
			target.setFlying(true);
			
			controller.setGameMode(this.targetGameMode);
			controller.setFoodLevel(target.getFoodLevel());
			
			if(target.getHealth() <= controller.getMaxHealth())
				controller.setHealth(target.getHealth());
			else controller.setHealth(20d);
			
			if(!simpleMode)
				if(!controller.hasPermission("icu.notimelimit")){
					int secsToRun = plugin.getConfiguration().getMaxControlTime();
					if(secsToRun > 0){
						if(wearingHelm)
							secsToRun *= 2;
						
						new ControlTimer(this, secsToRun).runTaskTimer(plugin, 20, 20);
					}
				}
			
			// Also in PlayerDistanceCheck.java
			target.setGameMode(GameMode.ADVENTURE);
			
			if(plugin.getConfiguration().getAllowFreecam()) 
				new PlayerDistanceCheck(this).runTaskTimer(plugin, 20, 20);
			else new PlayerDistanceCheck(this).runTaskTimer(plugin, 10, 10);
			
			isRunning = true;
		}else{
			controller.sendMessage(plugin.getMessages().helmetBlocked());
			this.stopControl();
		}
	} 
	
	@SuppressWarnings("deprecation")
	public void stopControl(){
		if(isRunning){
			PlayerInteractEvent.getHandlerList().unregister(pl);
			PlayerMoveEvent.getHandlerList().unregister(pl);
			PlayerItemHeldEvent.getHandlerList().unregister(pl);
			InventoryClickEvent.getHandlerList().unregister(pl);
			PlayerSwapHandItemsEvent.getHandlerList().unregister(pl);
			PlayerQuitEvent.getHandlerList().unregister(pl);
			PlayerKickEvent.getHandlerList().unregister(pl);
			AsyncPlayerChatEvent.getHandlerList().unregister(pl);
			PlayerCommandPreprocessEvent.getHandlerList().unregister(pl);
			EntityDamageEvent.getHandlerList().unregister(pl);
			PlayerDropItemEvent.getHandlerList().unregister(pl);
			PlayerDeathEvent.getHandlerList().unregister(pl);
			PlayerJoinEvent.getHandlerList().unregister(pl);
			refresh.cancel();
			
			plugin.getProtocolManager().removePacketListener(chat);
			
			this.restoreControllerInventory();
			pl.destroyFakePlayer();
			
			for(Player op : Bukkit.getOnlinePlayers()){
				op.showPlayer(target);
			}
			
			target.setGameMode(this.targetGameMode);
			target.setFoodLevel(controller.getFoodLevel());
			target.setAllowFlight(this.canTargetFly);
			target.setFlying(this.isTargetFlying);
			
			if(controller.getHealth() <= target.getMaxHealth())
				target.setHealth(controller.getHealth());
			else target.setHealth(20d);
			
			controller.setFoodLevel(this.controllerFood);
			controller.setHealth(this.controllerHealth);
			controller.setGameMode(this.controllerGameMode);
			
			target.teleport(controller.getLocation());
			
			if(simpleMode)
				controller.teleport(cOrig);
			
			int invisTime = plugin.getConfiguration().getInvisibleTime();
			if(invisTime > 0)
				new Invisibility(this, invisTime).runTaskTimer(plugin, 20, 20);
		}
		
		if(!simpleMode)
			if(!controller.hasPermission("icu.nocooldown"))
				if(plugin.getConfiguration().getCooldown()>0)
					new Cooldown(plugin, controller).runTaskLater(plugin, plugin.getConfiguration().getCooldown() * 20);
		
		
		if(plugin.getControlSessions().contains(this))
			plugin.getControlSessions().remove(this);
		
		isRunning = false;
	}
	
	public void saveControllerInventory(){
		controller_inventory = controller.getInventory().getContents();
		controller_armor = controller.getInventory().getArmorContents();
		
		controller.getInventory().setContents(target.getInventory().getContents());
		controller.getInventory().setArmorContents(target.getInventory().getArmorContents());
	}
	
	public void restoreControllerInventory(){
		target.getInventory().setContents(controller.getInventory().getContents());
		target.getInventory().setArmorContents(controller.getInventory().getArmorContents());
		
		controller.getInventory().setContents(controller_inventory);
		controller.getInventory().setArmorContents(controller_armor);
	}
	
	public Player getTarget(){
		return target;
	}
	
	public Player getController(){
		return controller;
	}
	
	public int getRandomInteger(int limit){
		return r.nextInt(limit);
	}
	
	public int getFakeEntityID(){
		return entity_id;
	}
	
	public iControlU getPlugin(){
		return plugin;
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	public ICUPacketListener getPacketListener(){
		return pl;
	}
}
