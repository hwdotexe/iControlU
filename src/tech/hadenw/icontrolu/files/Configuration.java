package tech.hadenw.icontrolu.files;

import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Sound;

import tech.hadenw.icontrolu.plugin.iControlU;

public class Configuration {
	private int time_controlLimit;
	private int time_cooldown;
	private int time_invisible;
	private boolean showMessages;
	private boolean useRecipes;
	private boolean allowFreecam;
	private int maxRadius;
	private int maxDistance;
	private Sound playSound;
	private boolean checkUpdates;
	private Random r;
	
	iControlU pl;
	public Configuration(iControlU plugin){
		pl=plugin;
		r = new Random();
		pl.saveDefaultConfig();
		this.loadValues();
		plugin.getServer().getLogger().log(Level.INFO, "Loaded Configuration");
	}
	
	public void loadValues(){
		pl.reloadConfig();
		
		// Perform a type check, load values, and correct any errors
		String value = "";
		{
			value = pl.getConfig().getString("time_controlLimit");
			if(isNumber(value)){
				this.time_controlLimit = Integer.parseInt(value);
				if(this.time_controlLimit <= 0) this.time_controlLimit = -1;
			}else{
				this.time_controlLimit = 45;
				pl.getConfig().set("time_controlLimit", this.time_controlLimit);
				pl.saveConfig();
			}
		}
		{
			value = pl.getConfig().getString("time_cooldown");
			if(isNumber(value)){
				this.time_cooldown = Integer.parseInt(value);
				if(this.time_cooldown <= 0) this.time_cooldown = -1;
			}else{
				this.time_cooldown = 60;
				pl.getConfig().set("time_cooldown", this.time_cooldown);
				pl.saveConfig();
			}
		}
		{
			value = pl.getConfig().getString("time_invisible");
			if(isNumber(value)){
				this.time_invisible = Integer.parseInt(value);
				if(this.time_invisible <= 0) this.time_invisible = -1;
			}else{
				this.time_invisible = 15;
				pl.getConfig().set("time_invisible", this.time_invisible);
				pl.saveConfig();
			}
		}
		{
			value = pl.getConfig().getString("showMessages");
			if(isBoolean(value)){
				this.showMessages = Boolean.parseBoolean(value);
			}else{
				this.showMessages = false;
				pl.getConfig().set("showMessages", this.showMessages);
				pl.saveConfig();
			}
		}
		{
			value = pl.getConfig().getString("useRecipes");
			if(isBoolean(value)){
				this.useRecipes = Boolean.parseBoolean(value);
			}else{
				this.useRecipes = true;
				pl.getConfig().set("useRecipes", this.useRecipes);
				pl.saveConfig();
			}
		}
		{
			value = pl.getConfig().getString("allowFreecam");
			if(isBoolean(value)){
				this.allowFreecam = Boolean.parseBoolean(value);
			}else{
				this.allowFreecam = true;
				pl.getConfig().set("allowFreecam", this.allowFreecam);
				pl.saveConfig();
			}
		}
		{
			value = pl.getConfig().getString("maxRadius");
			if(isNumber(value)){
				this.maxRadius = Integer.parseInt(value);
				if(this.maxRadius <= 0) this.maxRadius = 5;
			}else{
				this.maxRadius = 50;
				pl.getConfig().set("maxRadius", this.maxRadius);
				pl.saveConfig();
			}
		}
		{
			value = pl.getConfig().getString("maxDistance");
			if(isNumber(value)){
				this.maxDistance = Integer.parseInt(value);
				if(this.maxDistance <= 0) this.maxDistance = 5;
			}else{
				this.maxDistance = 15;
				pl.getConfig().set("maxDistance", this.maxDistance);
				pl.saveConfig();
			}
		}
		{
			value = pl.getConfig().getString("playSound");
			if(isSound(value)){
				this.playSound = Sound.valueOf(value);
			}else{
				this.playSound = Sound.values()[r.nextInt(Sound.values().length)];
				pl.getConfig().set("playSound", this.playSound.toString());
				pl.saveConfig();
			}
		}
		{
			value = pl.getConfig().getString("checkForUpdates");
			if(isBoolean(value)){
				this.checkUpdates = Boolean.parseBoolean(value);
			}else{
				this.checkUpdates = true;
				pl.getConfig().set("checkForUpdates", true);
				pl.saveConfig();
			}
		}
	}
	
	private boolean isSound(String sound){
		try{
			Sound.valueOf(sound);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	private boolean isNumber(String number){
		try{
			Integer.parseInt(number);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	private boolean isBoolean(String bool){
		try{
			Boolean.parseBoolean(bool);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public int getMaxControlTime(){
		return this.time_controlLimit;
	}
	
	public int getCooldown(){
		return this.time_cooldown;
	}
	
	public boolean getShowMessages(){
		return this.showMessages;
	}
	
	public int getMaxDistance(){
		return this.maxDistance;
	}
	
	public boolean getUseRecipes(){
		return this.useRecipes;
	}
	
	public boolean getAllowFreecam(){
		return this.allowFreecam;
	}
	
	public int getMaxRadius(){
		return this.maxRadius;
	}
	
	public int getInvisibleTime(){
		return this.time_invisible;
	}
	
	public Sound getPlaySound(){
		return this.playSound;
	}
	
	public boolean getCheckForUpdates(){
		return this.checkUpdates;
	}
}
