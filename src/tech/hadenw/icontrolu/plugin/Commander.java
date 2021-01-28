package tech.hadenw.icontrolu.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class Commander implements CommandExecutor{
	private iControlU plugin;
	public Commander(iControlU c){
		plugin=c;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("icu")){
			if(sender.hasPermission("icu.use")) {
				if(args.length == 0){
					String version = plugin.getDescription().getVersion().toString(); 
					sender.sendMessage("§6----------( §c§ki§6§l iControlU §7Help §cv"+version+" §ki§6 )----------");
					sender.sendMessage("§7/icu control (§ec§7) §o[controller] <player> §8- Enter Control Mode");
					sender.sendMessage("§7/icu controlnearest (§ecn§7) §8- Control the nearest player");
					sender.sendMessage("§7/icu stop (§es§7) §o[controller] §8- Exit Control Mode");
					sender.sendMessage("§7/icu forcechat (§efc§7) §o<player> <message> §8- Force a chat message");
					sender.sendMessage("§7/icu reload (§er§7) §8- Reload the plugin");
					sender.sendMessage("§7/icu simple (§esim§7) §8- Toggle Simple Mode");
					sender.sendMessage("\n§5Created by §lFireBreath15\n§5§ohttp://hadenw.tech");
					sender.sendMessage("§6----------( §c§ki§6§l iControlU §7Help §cv"+version+" §ki§6 )----------");
				}else{
					if(args.length==1){
						if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")){
							if(sender.hasPermission("icu.reload")){
								plugin.getConfiguration().loadValues();;
								sender.sendMessage(plugin.getMessages().pluginReloaded());
							}else{
								sender.sendMessage(plugin.getMessages().noPermission());
							}
						}else if(args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("s")){
							if(sender instanceof Player){
								Player p = (Player)sender;
								if(p.hasPermission("icu.control")){
									if(this.isPlayerBusy(p)){
										for(Control cs : plugin.getControlSessions()){
											if(cs.getController().equals(p)){
												cs.stopControl();
												plugin.getControlSessions().remove(cs);
												p.sendMessage(plugin.getMessages().stopControl(cs.getTarget().getName()));
												
												if(plugin.getConfiguration().getShowMessages()){
													cs.getTarget().sendMessage(plugin.getMessages().stopControlVictim(p.getName()));
												}
												
												break;
											}
										}
									}else{
										sender.sendMessage(plugin.getMessages().playerIsNotControlling());
									}
								}else{
									sender.sendMessage(plugin.getMessages().noPermission());
								}
							}else{
								sender.sendMessage(plugin.getMessages().mustBePlayer());
							}
						}else if(args[0].equalsIgnoreCase("controlnearest") || args[0].equalsIgnoreCase("cn")){
							if(sender instanceof Player){
								Player p = (Player)sender;
								
								if(p.hasPermission("icu.control.nearest")){
									if(!this.isPlayerBusy(p)){
										Player t = null;
										Location nearest = null;
										
										for(Player op : Bukkit.getOnlinePlayers()){
											if(op.getName() != p.getName()){
												if(op.getWorld().getName()==p.getWorld().getName()){
													if(p.getLocation().distance(op.getLocation())<=plugin.getConfiguration().getMaxRadius()){
														if(!op.hasPermission("icu.exempt")){
															if(!this.isPlayerBusy(op)){
																if(nearest == null){
																	nearest = op.getLocation();
																	t = op;
																}else{
																	if(op.getLocation().distanceSquared(p.getLocation()) < nearest.distanceSquared(p.getLocation())){
																		nearest = op.getLocation();
																		t = op;
																	}
																}
															}
														}
													}
												}
											}
										}
										
										if(t != null){
											this.startControlMode(p, t);
										}else{
											sender.sendMessage(plugin.getMessages().noNearbyPlayers());
										}
									}else{
										sender.sendMessage(plugin.getMessages().playerIsBusy());
									}
								}else{
									sender.sendMessage(plugin.getMessages().noPermission());
								}
							}else{
								sender.sendMessage(plugin.getMessages().mustBePlayer());
							}
						}else if(args[0].equalsIgnoreCase("simple") || args[0].equalsIgnoreCase("sim")){
							if(sender instanceof Player) {
								if(sender.hasPermission("icu.simple")) {
									Player p = (Player)sender;
									
									if(p.hasMetadata("iCU_SimpleMode")) {
										p.removeMetadata("iCU_SimpleMode", plugin);
										p.sendMessage(plugin.getMessages().toggleSimpleMode(false));
									}else {
										p.setMetadata("iCU_SimpleMode", new FixedMetadataValue(plugin, true));
										p.sendMessage(plugin.getMessages().toggleSimpleMode(true));
									}
								}else {
									sender.sendMessage(plugin.getMessages().noPermission());
								}
							}else {
								sender.sendMessage(plugin.getMessages().mustBePlayer());
							}
						}else {
							sender.sendMessage(plugin.getMessages().wrongCommand());
						}
					}else if(args.length == 2){
						if(args[0].equalsIgnoreCase("control") || args[0].equalsIgnoreCase("c")){
							if(sender instanceof Player){
								Player p = (Player)sender;
								if(p.hasPermission("icu.control")){
									Player t = Bukkit.getPlayer(args[1]);
									
									if(t != null){
										if(!p.equals(t)){
											if(!t.hasPermission("icu.exempt")){
												if(!this.isPlayerBusy(p) && !this.isPlayerBusy(t)){
													if(t.isOnline()){
														this.startControlMode(p, t);
													}else{
														sender.sendMessage(plugin.getMessages().playerIsOffline());
													}
												}else{
													sender.sendMessage(plugin.getMessages().playerIsBusy());
												}
											}else{
												sender.sendMessage(plugin.getMessages().playerIsImmune());
											}
										}else{
											sender.sendMessage(plugin.getMessages().cantControlSelf());
										}
									}else{
										sender.sendMessage(plugin.getMessages().playerNotFound());
									}
								}else{
									sender.sendMessage(plugin.getMessages().noPermission());
								}
							}else{
								sender.sendMessage(plugin.getMessages().mustBePlayer());
							}
						}else if(args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("s")){
							if(sender.hasPermission("icu.control.other")){
								Player p = Bukkit.getPlayer(args[1]);
								
								if(p != null){
									if(this.isPlayerBusy(p)){
										Control cs = null;
										for(Control c : plugin.getControlSessions()){
											if(c.getController().equals(p)){
												c.stopControl();
												cs = c;
												break;
											}
										}
										
										plugin.getControlSessions().remove(cs);
										sender.sendMessage(plugin.getMessages().stopControlAdmin(p.getName(), cs.getTarget().getName()));
										
										if(plugin.getConfiguration().getShowMessages()){
											if(plugin.getConfiguration().getShowMessages()){
												p.sendMessage(plugin.getMessages().stopControl(cs.getTarget().getName()));
												cs.getTarget().sendMessage(plugin.getMessages().stopControlVictim(p.getName()));
											}
										}
									}else{
										sender.sendMessage(plugin.getMessages().playerNotControlling());
									}
								}else{
									sender.sendMessage(plugin.getMessages().playerNotFound());
								}
							}else{
								sender.sendMessage(plugin.getMessages().noPermission());
							}
						}else{
							sender.sendMessage(plugin.getMessages().wrongCommand());
						}
					}else if(args.length >= 3){
						if(args[0].equalsIgnoreCase("control") || args[0].equalsIgnoreCase("c")){
							Player p = Bukkit.getPlayer(args[1]);
							Player t = Bukkit.getPlayer(args[2]);
							
							if(sender.hasPermission("icu.control.other")){
								if(p != null && t != null){
									if(!p.equals(t)){
										if(!t.hasPermission("icu.exempt")){
											if(!this.isPlayerBusy(p) && !this.isPlayerBusy(t)){
												if(p.isOnline() && t.isOnline()){
													this.startControlMode(p, t, sender);
												}else{
													sender.sendMessage(plugin.getMessages().playerIsOffline());
												}
											}else{
												sender.sendMessage(plugin.getMessages().playerIsBusy());
											}
										}else{
											sender.sendMessage(plugin.getMessages().playerIsImmune());
										}
									}else{
										sender.sendMessage(plugin.getMessages().cantControlSelf());
									}
								}else{
									sender.sendMessage(plugin.getMessages().playerNotFound());
								}
							}else{
								sender.sendMessage(plugin.getMessages().noPermission());
							}
						}else if(args[0].equalsIgnoreCase("forcechat") || args[0].equalsIgnoreCase("fc")){
							if(sender.hasPermission("icu.forcechat")){
								Player t = Bukkit.getPlayer(args[1]);
								
								if(t != null){
									if(t.isOnline()){
										if(!t.hasPermission("icu.exempt")){
											String msg = "";
											for(int i=2; i<args.length; i++){
												msg += args[i];
												msg += " ";
											}
											
											t.chat(msg);
										}else{
											sender.sendMessage(plugin.getMessages().playerIsImmune());
										}
									}else{
										sender.sendMessage(plugin.getMessages().playerIsOffline());
									}
								}else{
									sender.sendMessage(plugin.getMessages().playerNotFound());
								}
							}else{
								sender.sendMessage(plugin.getMessages().noPermission());
							}
						}else{
							sender.sendMessage(plugin.getMessages().wrongCommand());
						}
					}
				}
				return true;
			} else {
				sender.sendMessage("Unknown command. Type \"/help\" for help.");
			}
		}
		return false;
	}
	
	private boolean isPlayerBusy(Player p){
		for(Control c : plugin.getControlSessions()){
			if(c.getController().equals(p) || c.getTarget().equals(p)){
				return true;
			}
		}
		
		return false;
	}
	
	private void startControlMode(Player c, Player t){
		if(!plugin.getCooldowns().contains(c.getUniqueId().toString())){
			Control cs = new Control(plugin, t, c);
			cs.startControl();
			plugin.getControlSessions().add(cs);
			c.sendMessage(plugin.getMessages().startControl(t.getName()));
			
			if(plugin.getConfiguration().getShowMessages()){
				t.sendMessage(plugin.getMessages().startControlVictim(c.getName()));
			}
		}else{
			c.sendMessage(plugin.getMessages().mustCooldown());
		}
	}
	
	private void startControlMode(Player c, Player t, CommandSender admin){
		if(!plugin.getCooldowns().contains(c.getUniqueId().toString())){
			Control cs = new Control(plugin, t, c);
			cs.startControl();
			plugin.getControlSessions().add(cs);
			admin.sendMessage(plugin.getMessages().startControlAdmin(c.getName(), t.getName()));
			
			if(plugin.getConfiguration().getShowMessages()){
				c.sendMessage(plugin.getMessages().startControl(t.getName()));
				t.sendMessage(plugin.getMessages().startControlVictim(c.getName()));
			}
		}else{
			admin.sendMessage(plugin.getMessages().playerIsBusy());
		}
	}
}
