package com.hadenwatne.icontrolu.plugin;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

import com.hadenwatne.icontrolu.files.Configuration;
import com.hadenwatne.icontrolu.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class iControlU extends JavaPlugin implements Listener{
	private List<Control> controls;
	private List<String> cooldowns_uuid;
	private Messages msg;
	private Configuration config;
	private ItemStack rH;
	private ItemStack aH;
	private boolean updateAvailable;
	private boolean isPLIB;
	private ProtocolManager pm;
	
	public void onEnable(){
		if(this.getServer().getPluginManager().isPluginEnabled("ProtocolLib")){
			pm = ProtocolLibrary.getProtocolManager();
			isPLIB = true;
			rH = null;
			aH = null;
			controls = new ArrayList<Control>();
			cooldowns_uuid = new ArrayList<String>();
			msg = new Messages(this);
			config = new Configuration(this);
			updateAvailable = this.checkUpdate();
			
			this.getCommand("icu").setExecutor(new Commander(this));
			this.getCommand("icu").setTabCompleter(new CommandTabComplete());
			
			if(config.getUseRecipes())
				this.registerrecipes();
		}else{
			this.getLogger().log(Level.SEVERE, "ProtocolLib is required to run iControlU! It can be downloaded from https://www.spigotmc.org/resources/protocollib.1997/");
			isPLIB = false;
		}
		
		if(this.isEnabled())
			this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if(e.getPlayer().isOp() || e.getPlayer().hasPermission("icu.admin")){
			if(!isPLIB){
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&c&liControlU requires &eProtocolLib &c&lto run, and it is not installed. Please install this plugin and restart the server."));
			}else{
				if(this.updateAvailable)
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&7&liControlU&8] &7An update is available! Download @ &cspigotmc.org&7!"));
			}
		}
	}
	
	public ProtocolManager getProtocolManager() {
		return pm;
	}
	
	public List<Control> getControlSessions(){
		return controls;
	}
	
	public List<String> getCooldowns(){
		return cooldowns_uuid;
	}
	
	public Configuration getConfiguration(){
		return config;
	}
	
	public Messages getMessages(){
		return msg;
	}
	private boolean checkUpdate() {
		if(this.getConfiguration().getCheckForUpdates()) {
			this.getLogger().log(Level.INFO, "Checking for updates...");
			
			try {
				URL url;
				List<String> str = new ArrayList<String>();
				url = new URL("https://api.spigotmc.org/legacy/update.php?resource=20034");
				
				Scanner s = new Scanner(url.openStream());
				while(true){
					if(s.hasNextLine())
						str.add(s.nextLine());
					else break;
				}
				
				s.close();
				
				int v = Integer.parseInt(this.getDescription().getVersion().replaceAll("\\.", ""));
				int nv = Integer.parseInt(str.get(0).replaceAll("\\.", "").substring(1));
				
				if(nv > v) return true;
			}catch(Exception e) {
			}
			
			this.getLogger().log(Level.INFO, "Finished checking for updates!");
		}
		
		return false;
	}
	
	private void registerrecipes(){
		{
			NamespacedKey key = new NamespacedKey(this, "iControlU_ReductionA");
			ShapedRecipe sr = new ShapedRecipe(key, this.reductionHelmet());
		    sr.shape("FFF","FHF","FFF");
		    sr.setIngredient('F', Material.FLINT);
		    sr.setIngredient('H', Material.DIAMOND_HELMET);
		    Bukkit.addRecipe(sr);
		}
		{
			NamespacedKey key = new NamespacedKey(this, "iControlU_ReductionB");
			ShapedRecipe sr = new ShapedRecipe(key, this.reductionHelmet());
		    sr.shape("FFF","FHF","FFF");
		    sr.setIngredient('F', Material.FLINT);
		    sr.setIngredient('H', Material.GOLDEN_HELMET);
		    Bukkit.addRecipe(sr);
		}
		{
			NamespacedKey key = new NamespacedKey(this, "iControlU_AmplifyingA");
			ShapedRecipe sr = new ShapedRecipe(key, this.amplifyingHelmet());
		    sr.shape("FFF","FHF","FFF");
		    sr.setIngredient('F', Material.DIAMOND);
		    sr.setIngredient('H', Material.GOLDEN_HELMET);
		    Bukkit.addRecipe(sr);
		}
		{
			NamespacedKey key = new NamespacedKey(this, "iControlU_AmplifyingB");
			ShapedRecipe sr = new ShapedRecipe(key, this.amplifyingHelmet());
		    sr.shape("FFF","FHF","FFF");
		    sr.setIngredient('F', Material.GOLD_INGOT);
		    sr.setIngredient('H', Material.DIAMOND_HELMET);
		    Bukkit.addRecipe(sr);
		}
	}
	
	public ItemStack reductionHelmet(){
		if(rH == null){
			ItemStack item = new ItemStack(Material.CHAINMAIL_HELMET);
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&6Reduction Helmet"));
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.translateAlternateColorCodes('&',"&7Reduces the chance of"));
			lore.add(ChatColor.translateAlternateColorCodes('&',"&7being controlled by 70%"));
			im.setLore(lore);
			item.setItemMeta(im);
			
			item=EnchantGlow.addGlow(item);
			
			return item;
		}else{
			return rH;
		}
	}
	
	public ItemStack amplifyingHelmet(){
		if(aH == null){
			ItemStack item = new ItemStack(Material.GOLDEN_HELMET);
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&bAmplifying Helmet"));
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.translateAlternateColorCodes('&',"&7Doubles the amount of time"));
			lore.add(ChatColor.translateAlternateColorCodes('&',"&7you can control others"));
			im.setLore(lore);
			item.setItemMeta(im);
			
			item=EnchantGlow.addGlow(item);
			
			return item;
		}else{
			return aH;
		}
	}
}