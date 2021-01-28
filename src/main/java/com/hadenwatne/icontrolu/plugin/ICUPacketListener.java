package com.hadenwatne.icontrolu.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
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

import com.comphenix.packetwrapper.WrapperPlayServerAnimation;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.packetwrapper.WrapperPlayServerEntityHeadRotation;
import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMoveLook;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import com.hadenwatne.icontrolu.timers.Disguise;

public class ICUPacketListener implements Listener{
	
	private Control c;
	private boolean allowTargetChat;
	public ICUPacketListener(Control con){
		allowTargetChat = false;
		setControl(con);
	}
	
	public Control getControl() {
		return c;
	}

	public void setControl(Control c) {
		this.c = c;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getPlayer().equals(getControl().getController())){
			this.sendAnimationPacket(0);
		}else if(e.getPlayer().equals(getControl().getTarget())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player) {
			Player d = (Player)e.getDamager();
			
			if(d.equals(getControl().getController())){
				this.sendAnimationPacket(0);
			}else if(d.equals(getControl().getTarget())){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		if(e.getPlayer().equals(getControl().getTarget())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent e) {
		if(e.getDeathMessage().contains(getControl().getController().getName()))
			e.setDeathMessage(e.getDeathMessage().replaceAll(getControl().getController().getName(), getControl().getTarget().getName()));
		else if(e.getDeathMessage().contains(getControl().getController().getDisplayName()))
			e.setDeathMessage(e.getDeathMessage().replaceAll(getControl().getController().getDisplayName(), getControl().getTarget().getDisplayName()));
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getPlayer().equals(getControl().getController())){
			WrapperPlayServerEntityHeadRotation headpacket = new WrapperPlayServerEntityHeadRotation();
	        headpacket.setEntityID(getControl().getFakeEntityID());
	        headpacket.setHeadYaw(getCompressedAngle(e.getPlayer().getEyeLocation().getYaw()));
	        
	        WrapperPlayServerRelEntityMoveLook movelook = new WrapperPlayServerRelEntityMoveLook();
	        movelook.setEntityID(getControl().getFakeEntityID());
	        movelook.setDx(e.getTo().getX() - e.getFrom().getX());
	        movelook.setDy(e.getTo().getY() - e.getFrom().getY());
	        movelook.setDz(e.getTo().getZ() - e.getFrom().getZ());
	        movelook.setPitch(e.getPlayer().getEyeLocation().getPitch());
	        movelook.setYaw(e.getPlayer().getEyeLocation().getYaw());
	        
			for(Player op : Bukkit.getOnlinePlayers()){
				if(!op.equals(getControl().getController()))
					if(op.getWorld().getName() == getControl().getController().getWorld().getName()){
						movelook.sendPacket(op);
						headpacket.sendPacket(op);
					}
			}
		}
	}
	
	protected byte getCompressedAngle(float value) {
        return (byte)(int) (value * 256.0F / 360.0F);
    }
	
	@EventHandler
	public void onArmorChange(InventoryClickEvent e){
		if(e.getWhoClicked().getName().equals(getControl().getController().getName())){
			if(e.getSlotType()==SlotType.ARMOR){
				this.sendArmor();
			}
		}
	}
	
	@EventHandler
	public void onItemSwitch(PlayerItemHeldEvent e){
		if(e.getPlayer().equals(getControl().getController())){
			this.sendItems();
		}
	}
	
	@EventHandler
	public void onItemSwitch(PlayerSwapHandItemsEvent e){
		if(e.getPlayer().equals(getControl().getController())){
			this.sendItems();
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		this.stopControlPlayerLeft(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e){
		this.stopControlPlayerLeft(e.getPlayer());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(e.getPlayer().getWorld().getName() == getControl().getController().getWorld().getName()) {
			new Disguise(this, e.getPlayer()).runTaskLater(c.getPlugin(), 30);
		}
	}
	
	@EventHandler
	public void onCommandSend(PlayerCommandPreprocessEvent e){
		if(e.getPlayer().equals(getControl().getTarget())){
			e.setCancelled(true);
		}else if(e.getPlayer().equals(getControl().getController())){
			e.setCancelled(true);
			
			Bukkit.getServer().getPluginManager().registerEvents(new CommandGUI(e.getMessage().substring(1), getControl()),  getControl().getPlugin());
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		if(e.getPlayer().equals(getControl().getTarget())){
			if(!getControl().getTarget().hasPermission("icu.chat")){
				e.setCancelled(!this.allowTargetChat);
				this.allowTargetChat = false;
			}
		}else if(e.getPlayer().equals(getControl().getController())){
			e.setCancelled(true);
			this.allowTargetChat = true;
			getControl().getTarget().chat(e.getMessage());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onTakeDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(p.equals(getControl().getController())){
				double ch = getControl().getController().getHealth();
				double th = getControl().getTarget().getHealth();
				
				if(ch-e.getDamage() <= 0)
					getControl().getController().setHealth(e.getDamage() + 1d);
				
				if(th-e.getDamage() <= 0){
					getControl().stopControl();
					getControl().getController().sendMessage(getControl().getPlugin().getMessages().victimDied(getControl().getTarget().getName()));
					
					if(getControl().getPlugin().getConfiguration().getShowMessages())
						getControl().getController().sendMessage(getControl().getPlugin().getMessages().victimDiedVictim());
				}else{
					getControl().getController().sendMessage(getControl().getPlugin().getMessages().victimHealth(getControl().getTarget().getName(), th-e.getDamage()));
				}
				
				getControl().getTarget().damage(e.getDamage());
				getControl().getTarget().setLastDamageCause(e);
				this.sendAnimationPacket(1);
			}
		}
	}
	
	
	private void sendAnimationPacket(int aid){
		WrapperPlayServerAnimation packet = new WrapperPlayServerAnimation();
		packet.setEntityID(getControl().getFakeEntityID());
		packet.setAnimation(aid);
		
		for(Player op : Bukkit.getOnlinePlayers()){
			if(!op.equals(getControl().getController()))
				if(op.getWorld().getName() == getControl().getController().getWorld().getName())
					packet.sendPacket(op);
		}
	}
	
	private void stopControlPlayerLeft(Player p){
		if(p.equals(getControl().getTarget())){
			getControl().stopControl();
			getControl().getController().sendMessage(getControl().getPlugin().getMessages().stopControl(getControl().getTarget().getName()));
		}else if(p.equals(getControl().getController())){
			getControl().stopControl();
			if(getControl().getPlugin().getConfiguration().getShowMessages())
				getControl().getTarget().sendMessage(getControl().getPlugin().getMessages().stopControlVictim(getControl().getController().getName()));
		}
	}
	
	public void sendArmor(){
		WrapperPlayServerEntityEquipment head = null;
		if(getControl().getController().getInventory().getHelmet() != null){
			head = new WrapperPlayServerEntityEquipment();
			head.setEntityID(getControl().getFakeEntityID());
			head.setSlot(ItemSlot.HEAD);
			head.setItem(getControl().getController().getInventory().getHelmet());
		}
		
		WrapperPlayServerEntityEquipment chest = null;
		if(getControl().getController().getInventory().getChestplate() != null){
			chest = new WrapperPlayServerEntityEquipment();
			chest.setEntityID(getControl().getFakeEntityID());
			chest.setSlot(ItemSlot.CHEST);
			chest.setItem(getControl().getController().getInventory().getChestplate());
		}
		
		WrapperPlayServerEntityEquipment legs = null;
		if(getControl().getController().getInventory().getLeggings() != null){
			legs = new WrapperPlayServerEntityEquipment();
			legs.setEntityID(getControl().getFakeEntityID());
			legs.setSlot(ItemSlot.LEGS);
			legs.setItem(getControl().getController().getInventory().getLeggings());
		}
		
		WrapperPlayServerEntityEquipment boots = null;
		if(getControl().getController().getInventory().getBoots() != null){
			boots = new WrapperPlayServerEntityEquipment();
			boots.setEntityID(getControl().getFakeEntityID());
			boots.setSlot(ItemSlot.FEET);
			boots.setItem(getControl().getController().getInventory().getBoots());
		}
		
		for(Player op : Bukkit.getOnlinePlayers()){
			if(op.getEntityId() != getControl().getController().getEntityId())
				if(op.getWorld().getName() == getControl().getController().getWorld().getName())
					if(head != null)
						head.sendPacket(op);
					
					if(chest != null)
						chest.sendPacket(op);
					
					if(legs != null)
						legs.sendPacket(op);
					
					if(boots != null)
						boots.sendPacket(op);
		}
	}
	
	public void sendItems(){
		WrapperPlayServerEntityEquipment mainHand = null;
		ItemStack mainHandItem = getControl().getController().getInventory().getItemInMainHand();
		
		if(mainHandItem == null || mainHandItem.getType() == Material.AIR) {
			mainHandItem = new ItemStack(Material.AIR);
		}
		
		mainHand = new WrapperPlayServerEntityEquipment();
		mainHand.setEntityID(getControl().getFakeEntityID());
		mainHand.setSlot(ItemSlot.MAINHAND);
		mainHand.setItem(mainHandItem);
		
		
		WrapperPlayServerEntityEquipment offHand = null;
		ItemStack offHandItem = getControl().getController().getInventory().getItemInOffHand();
		
		if(offHandItem == null || offHandItem.getType() == Material.AIR) {
			offHandItem = new ItemStack(Material.AIR);
		}
		
		offHand = new WrapperPlayServerEntityEquipment();
		offHand.setEntityID(getControl().getFakeEntityID());
		offHand.setSlot(ItemSlot.OFFHAND);
		offHand.setItem(offHandItem);
		
		for(Player op : Bukkit.getOnlinePlayers()){
			if(op.getEntityId() != getControl().getController().getEntityId())
				if(op.getWorld().getName() == getControl().getController().getWorld().getName())
					if(mainHand != null)
						mainHand.sendPacket(op);
					
					if(offHand != null)
						offHand.sendPacket(op);
		}
	}
	
	public void destroyFakePlayer(){
		WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
		packet.setEntityIds(new int[]{getControl().getFakeEntityID()});
		
		for(Player op : Bukkit.getOnlinePlayers()){
			if(!op.equals(getControl().getController()))
				packet.sendPacket(op);
		}
	}
	
	public void spawnFakePlayer(){
		WrapperPlayServerNamedEntitySpawn packet = new WrapperPlayServerNamedEntitySpawn();
		packet.setPlayerUUID(getControl().getTarget().getUniqueId());
		packet.setEntityID(getControl().getFakeEntityID());
		packet.setPosition(getControl().getController().getLocation().toVector());
		
		// Add entity metadata so skin layers display properly
		packet.setMetadata(WrappedDataWatcher.getEntityWatcher(getControl().getTarget()));
		
		boolean useFreecam = getControl().getPlugin().getConfiguration().getAllowFreecam();
		
		for(Player op : Bukkit.getOnlinePlayers()){
			if(!op.equals(getControl().getController() )) {
				if(op.getWorld().getName() == getControl().getController().getWorld().getName()) {
					
					if(!(!useFreecam && op.equals(getControl().getTarget()))) {
						packet.sendPacket(op);
					}
					
					// Don't hide the controller / the target from themselves
					if(!op.equals(getControl().getTarget()))
						op.hidePlayer(getControl().getTarget());
					
					if(!op.equals(getControl().getController()))
						op.hidePlayer(getControl().getController());
				}
			}
		}
		
		getControl().getController().hidePlayer(getControl().getTarget());
		
		this.sendArmor();
		this.sendItems();
	}
}
