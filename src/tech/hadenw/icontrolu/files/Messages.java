package tech.hadenw.icontrolu.files;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.hadenw.icontrolu.plugin.iControlU;

public class Messages {
	iControlU plugin;
	private FileConfiguration messages;
	private File messagesFile;
	private HashMap<String, String> vals;
	public Messages(iControlU c){
		plugin=c;
		this.messages = null;
	    this.messagesFile = null;
	    
	    vals = new HashMap<String, String>();
	    this.reloadMessages();
	    
	    if(this.getMessages().contains("plugin-title")){
	    	this.loadValues();
	    }else{
	    	this.saveDefaultValues();
	    }
	    
	    plugin.getServer().getLogger().log(Level.INFO, "Loaded Custom Messages");
	}
	
	public void saveDefaultValues(){
		this.getMessages().set("plugin-title", "&8[&7iControlU&8]");
		this.getMessages().set("wrong-command", "&cWrong command or usage!");
		this.getMessages().set("no-permission", "&cYou don't have permission!");
		this.getMessages().set("must-be-player", "&cYou must be a player to run that command!");
		this.getMessages().set("must-cooldown", "&cYou must cool down first!");
		this.getMessages().set("cooldown-ended", "&aYou have cooled down!");
		this.getMessages().set("player-is-immune", "&cThat player is immune!");
		this.getMessages().set("player-is-offline", "&cThat player isn't online!");
		this.getMessages().set("player-is-busy", "&cYou can't do that right now!");
		this.getMessages().set("player-is-not-controlling", "&cThat player isn't controlling anyone!");
		this.getMessages().set("cannot-control-self", "&cThat player can already control themself!");
		this.getMessages().set("not-controlling", "&cYou aren't controlling anyone!");
		this.getMessages().set("troll-ended-other", "&c%CONTROLLER%'s control ended.");
		this.getMessages().set("player-not-found", "&cThat player can't be found!");
		this.getMessages().set("plugin-reloaded", "&aPlugin was reloaded!");
		this.getMessages().set("you-died", "&cYou died whilst being controlled!");
		this.getMessages().set("helmet-blocked-control", "&cThe victim was immune to control!");
		this.getMessages().set("now-hidden", "&aYou are &enow hidden &afrom other players!");
		this.getMessages().set("now-visible", "&aYou are &eno longer hidden &afrom other players!");
		this.getMessages().set("no-nearby-players", "&cThere are no nearby players!");
		this.getMessages().set("custom-control-started", "&aYou &eactivated Control Mode with &a%VICTIM%");
		this.getMessages().set("custom-control-started-victim", "&a%CONTROLLER% &eactivated Control Mode with &aYou");
		this.getMessages().set("custom-control-started-other", "&a%CONTROLLER% &eactivated Control Mode with &a%VICTIM%");
		this.getMessages().set("custom-control-ended", "&aYou &edeactivated Control Mode with &a%VICTIM%");
		this.getMessages().set("custom-control-ended-victim", "&a%CONTROLLER% &edeactivated Control Mode with &aYou");
		this.getMessages().set("custom-control-ended-other", "&a%CONTROLLER% &edeactivated Control Mode with &a%VICTIM%");
		this.getMessages().set("custom-victim-died", "&a%VICTIM% &cdied whilst being controlled!");
		this.getMessages().set("custom-victim-health", "&a%VICTIM% &ehas &c%HEALTH% &eHP remaining!");
		this.getMessages().set("toggle-simple-mode", "&aToggled &7Simple Mode %STATUS%");

		this.saveMessages();
		this.loadValues();
	}
	
	public void loadValues(){
		vals.clear();
		
		// Introduced v2.3.9
		if(!getMessages().contains("toggle-simple-mode")) {
			this.getMessages().set("toggle-simple-mode", "&aToggled &7Simple Mode &a%STATUS%");
			this.saveMessages();
		}
		
		vals.put("plugin-title", getMessages().getString("plugin-title").replaceAll("&", "§")+" ");
		vals.put("wrong-command", getMessages().getString("wrong-command").replaceAll("&", "§"));
		vals.put("no-permission", getMessages().getString("no-permission").replaceAll("&", "§"));
		vals.put("must-be-player", getMessages().getString("must-be-player").replaceAll("&", "§"));
		vals.put("must-cooldown", getMessages().getString("must-cooldown").replaceAll("&", "§"));
		vals.put("cooldown-ended", getMessages().getString("cooldown-ended").replaceAll("&", "§"));
		vals.put("player-is-immune", getMessages().getString("player-is-immune").replaceAll("&", "§"));
		vals.put("player-is-offline", getMessages().getString("player-is-offline").replaceAll("&", "§"));
		vals.put("player-is-busy", getMessages().getString("player-is-busy").replaceAll("&", "§"));
		vals.put("player-is-not-controlling", getMessages().getString("player-is-not-controlling").replaceAll("&", "§"));
		vals.put("cannot-control-self", getMessages().getString("cannot-control-self").replaceAll("&", "§"));
		vals.put("not-controlling", getMessages().getString("not-controlling").replaceAll("&", "§"));
		vals.put("troll-ended-other", getMessages().getString("troll-ended-other").replaceAll("&", "§"));
		vals.put("player-not-found", getMessages().getString("player-not-found").replaceAll("&", "§"));
		vals.put("plugin-reloaded", getMessages().getString("plugin-reloaded").replaceAll("&", "§"));
		vals.put("you-died", getMessages().getString("you-died").replaceAll("&", "§"));
		vals.put("helmet-blocked-control", getMessages().getString("helmet-blocked-control").replaceAll("&", "§"));
		vals.put("now-hidden", getMessages().getString("now-hidden").replaceAll("&", "§"));
		vals.put("now-visible", getMessages().getString("now-visible").replaceAll("&", "§"));
		vals.put("no-nearby-players", getMessages().getString("no-nearby-players").replaceAll("&", "§"));
		vals.put("custom-control-started", getMessages().getString("custom-control-started").replaceAll("&", "§"));
		vals.put("custom-control-started-victim", getMessages().getString("custom-control-started-victim").replaceAll("&", "§"));
		vals.put("custom-control-started-other", getMessages().getString("custom-control-started-other").replaceAll("&", "§"));
		vals.put("custom-control-ended", getMessages().getString("custom-control-ended").replaceAll("&", "§"));
		vals.put("custom-control-ended-victim", getMessages().getString("custom-control-ended-victim").replaceAll("&", "§"));
		vals.put("custom-control-ended-other", getMessages().getString("custom-control-ended-other").replaceAll("&", "§"));
		vals.put("custom-victim-died", getMessages().getString("custom-victim-died").replaceAll("&", "§"));
		vals.put("custom-victim-health", getMessages().getString("custom-victim-health").replaceAll("&", "§"));
		vals.put("toggle-simple-mode", getMessages().getString("toggle-simple-mode").replaceAll("&", "§"));
	}
	
	public String getTitle(){
		return vals.get("plugin-title");
	}
	
	public String wrongCommand(){
		return vals.get("plugin-title")+vals.get("wrong-command");
	}
	public String noPermission(){
		return vals.get("plugin-title")+vals.get("no-permission");
	}
	public String mustBePlayer(){
		return vals.get("plugin-title")+vals.get("must-be-player");
	}
	public String mustCooldown(){
		return vals.get("plugin-title")+vals.get("must-cooldown");
	}
	public String playerIsImmune(){
		return vals.get("plugin-title")+vals.get("player-is-immune");
	}
	public String playerIsOffline(){
		return vals.get("plugin-title")+vals.get("player-is-offline");
	}
	public String playerIsBusy(){
		return vals.get("plugin-title")+vals.get("player-is-busy");
	}
	public String playerNotControlling(){
		return vals.get("plugin-title")+vals.get("player-is-not-controlling");
	}
	public String cantControlSelf(){
		return vals.get("plugin-title")+vals.get("cannot-control-self");
	}
	public String playerIsNotControlling(){
		return vals.get("plugin-title")+vals.get("not-controlling");
	}
	public String playerNotFound(){
		return vals.get("plugin-title")+vals.get("player-not-found");
	}
	public String startControl(String vname){
		return vals.get("plugin-title")+vals.get("custom-control-started").replaceAll("%VICTIM%", vname);
	}
	public String startControlVictim(String cname){
		return vals.get("plugin-title")+vals.get("custom-control-started-victim").replaceAll("%CONTROLLER%", cname);
	}
	public String startControlAdmin(String cname, String vname){
		return vals.get("plugin-title")+vals.get("custom-control-started-other").replaceAll("%CONTROLLER%", cname).replaceAll("%VICTIM%", vname);
	}
	public String stopControl(String vname){
		return vals.get("plugin-title")+vals.get("custom-control-ended").replaceAll("%VICTIM%", vname);
	}
	public String stopControlVictim(String cname){
		return vals.get("plugin-title")+vals.get("custom-control-ended-victim").replaceAll("%CONTROLLER%", cname);
	}
	public String stopControlAdmin(String cname, String vname){
		return vals.get("plugin-title")+vals.get("custom-control-ended-other").replaceAll("%CONTROLLER%", cname).replaceAll("%VICTIM%", vname);
	}
	public String pluginReloaded(){
		return vals.get("plugin-title")+vals.get("plugin-reloaded");
	}
	public String victimDied(String vname){
		return vals.get("plugin-title")+vals.get("custom-victim-died").replaceAll("%VICTIM%", vname);
	}
	public String victimDiedVictim(){
		return vals.get("plugin-title")+vals.get("you-died");
	}
	public String helmetBlocked(){
		return vals.get("plugin-title")+vals.get("helmet-blocked-control");
	}
	public String cooldownEnded(){
		return vals.get("plugin-title")+vals.get("cooldown-ended");
	}
	public String victimHealth(String vname, double h){
		return vals.get("plugin-title")+vals.get("custom-victim-health").replaceAll("%VICTIM%", vname).replaceAll("%HEALTH%", Long.toString(Math.round(h)));
	}
	public String controllerHidden(){
		return vals.get("plugin-title")+vals.get("now-hidden");
	}
	public String controllerVisible(){
		return vals.get("plugin-title")+vals.get("now-visible");
	}
	public String noNearbyPlayers(){
		return vals.get("plugin-title")+vals.get("no-nearby-players");
	}
	public String trollEndedOther(String name){
		return vals.get("plugin-title")+vals.get("troll-ended-other").replaceAll("%CONTROLLER%", name);
	}
	public String toggleSimpleMode(boolean on){
		String s = "";
		
		if(on) s = "§aON";
		else s = "§cOFF";
		
		return vals.get("plugin-title")+vals.get("toggle-simple-mode").replaceAll("%STATUS%", s);
	}
	
	public void reloadMessages(){
		if (this.messagesFile == null){
			this.messagesFile = new File(plugin.getDataFolder()+"/messages.yml");
			this.messages = YamlConfiguration.loadConfiguration(this.messagesFile);
		}
	}
	 
	public FileConfiguration getMessages(){
		if (this.messages == null) {
			reloadMessages();
		}
		return this.messages;
	}
	 
	public void saveMessages(){
		if ((this.messages == null) || (this.messagesFile == null)) {
			return;
		}
		
		try{
			getMessages().save(this.messagesFile);
		} catch (Exception ex){
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.messagesFile, ex);
		}
	}
}
