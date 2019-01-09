package net.darr3n911.darchat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import me.x128.darUUID.DarUUID;

public class DarChat extends JavaPlugin implements Listener {
	
    public final Logger logger = Logger.getLogger("Minecraft");
    
    public boolean testingEnabled = false;
	
	public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH);
    
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
    	saveDefaultConfig();
    	getConfig().options().copyDefaults(true);    	
        this.logger.info("[" + this.getDescription().getName() + "] Plugin enabled."); 
        logToFile("##### SERVER REBOOTED OR PLUGIN STARTED #####");
	}
	
	public void onDisable() {
        this.logger.info("[" + this.getDescription().getName() + "] Plugin disabled.");
		saveConfig();
		logToFile("##### SERVER STOPPED OR PLUGIN UNLOADED #####");
	}
	
	public final int localRange = this.getConfig().getInt("local-range");
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("g")) {
			if (sender instanceof Player) {
	    		if (args.length > 0) {
        			if (sender.hasPermission("darchat.global")) {
        				if (!this.getConfig().getBoolean("global-mute." + DarUUID.getStoredUUID(sender.getName()))) {
        					Player playerSender = (Player)sender;
        					String message = createMessageContents(playerSender, args);
                            TextComponent finalMessage = createMessage(playerSender, message, "/g", "Global", ChatColor.DARK_GREEN, ChatColor.GRAY, true, false);
            		        for(Player p : Bukkit.getOnlinePlayers()){
            		            if(p.hasPermission("darchat.global")){
            		                p.spigot().sendMessage(finalMessage);
            		            }
            		        }
            		        Bukkit.getLogger().info("[G] " + playerSender.getName() + ": " + message);
            		        logToFile("[G] " + playerSender.getName() + ": " + message);
        				} else {
        					sender.sendMessage("§3 » §cYou are currently muted from the §2Global§c Channel.");
        				}
        			} else {
            			sender.sendMessage("§3 » §cYou don't have permission to chat in the §2Global §cChannel.");	
        			}
	    		} else {
	    			if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(sender.getName())).equalsIgnoreCase("G") == true) {
	    				
	    			} else {
	    				this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(sender.getName()), "G");
	    				sender.sendMessage("§3 » §eNow chatting in §2Global§e.");
	    			}
	    		}
			} else { // For console
				if (args.length > 0) {
					String message = createMessageContents(null, args);
					TextComponent consoleMessage = createConsoleMessage(message, "/g", "Global", ChatColor.DARK_GREEN, ChatColor.GRAY);
    		        for(Player p : Bukkit.getOnlinePlayers()){
    		            if(p.hasPermission("darchat.global")){
    		                p.spigot().sendMessage(consoleMessage);
    		            }
    		        }
    		        Bukkit.getLogger().info("[G] Console: " + message);
				}
			}
    	} else if (cmd.getName().equalsIgnoreCase("d")) {
    		if (sender instanceof Player) {
        		if (args.length > 0) {
        			if (sender.hasPermission("darchat.donator")) {
            			Player playerSender = (Player)sender;
            			String message = createMessageContents(playerSender, args);
                        TextComponent finalMessage = createMessage(playerSender, message, "/d", "Donator", ChatColor.DARK_AQUA, ChatColor.AQUA, false, false);
        		        for(Player p : Bukkit.getOnlinePlayers()){
        		            if(p.hasPermission("darchat.donator")){
        		                p.spigot().sendMessage(finalMessage);
        		            }
        		        }
        		        Bukkit.getLogger().info("[D] " + playerSender.getName() + ": " + message);
        		        logToFile("[D] " + playerSender.getName() + ": " + message);
        			} else {
            			sender.sendMessage("§3 » §cYou don't have permission to chat in the §bDonator§c Channel.");	
        			}
        		} else {
        			if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(sender.getName())).equalsIgnoreCase("D") == true) {
        				
        			} else {
        				if (sender.hasPermission("darchat.donator")) {
        					this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(sender.getName()), "D");
            				sender.sendMessage("§3 » §eNow chatting in §bDonator§e.");
        				} else {
        					sender.sendMessage("§3 » §cYou don't have permission to join the §bDonator§c Channel.");
        				}
        			}
        		}
    		}  else {
				if (args.length > 0) {
        			String message = createMessageContents(null, args);
					TextComponent consoleMessage = createConsoleMessage(message, "/d", "Donator", ChatColor.DARK_AQUA, ChatColor.AQUA);
    		        for(Player p : Bukkit.getOnlinePlayers()){
    		            if(p.hasPermission("darchat.donator")){
    		                p.spigot().sendMessage(consoleMessage);
    		            }
    		        }
    		        Bukkit.getLogger().info("[D] Console: " + message);
				}
			}
    	} else if (cmd.getName().equalsIgnoreCase("st")) {
    		if (sender instanceof Player) {
        		if (args.length > 0) {
        			if (sender.hasPermission("darchat.staff")) {
            			Player playerSender = (Player)sender;
            			String message = createMessageContents(playerSender, args);
                        TextComponent finalMessage = createMessage(playerSender, message, "/st", "Staff", ChatColor.DARK_RED, ChatColor.RED, false, true);
        		        for(Player p : Bukkit.getOnlinePlayers()){
        		            if(p.hasPermission("darchat.staff")){
        		                p.spigot().sendMessage(finalMessage);
        		            }
        		        }
        		        Bukkit.getLogger().info("[Staff] " + playerSender.getName() + ": " + message);
        			} else {
            			sender.sendMessage("§3 » §cYou don't have permission to chat in the §4Staff§c Channel.");	
        			}
        		} else {
        			if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(sender.getName())) == "S") {
        				
        			} else {
        				if (sender.hasPermission("darchat.staff")) {
        					this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(sender.getName()), "S");
            				sender.sendMessage("§3 » §eNow chatting in §4Staff§e.");
        				} else {
        					sender.sendMessage("§3 » §cYou don't have permission to join the §4Staff§c Channel.");
        				}
        			}
        		}
    		} else {
				if (args.length > 0) {
        			String message = createMessageContents(null, args);
					TextComponent consoleMessage = createConsoleMessage(message, "/st", "Staff", ChatColor.DARK_RED, ChatColor.RED);
    		        for(Player p : Bukkit.getOnlinePlayers()){
    		            if(p.hasPermission("darchat.staff")){
    		                p.spigot().sendMessage(consoleMessage);
    		            }
    		        }
    		        Bukkit.getLogger().info("[Staff] Console: " + message);
				}
			}
    	}  else if (cmd.getName().equalsIgnoreCase("l")) {
    		if (sender instanceof Player) {
        		if (args.length > 0) {
        			if (sender.hasPermission("darchat.local")) {
            			Player playerSender = (Player)sender;
            			if(playersAreNearby(playerSender)) {
            				String message = createMessageContents(playerSender, args);
                			
                			TextComponent staffMessage = createStaffMessage(playerSender, playerSender.getEyeLocation(), message);
                			for(Player p : Bukkit.getOnlinePlayers()){
            		            if(p.hasPermission("darchat.staff")){
            		                p.spigot().sendMessage(staffMessage);
            		            }
            		        }
                			
                            TextComponent finalMessage = createMessage(playerSender, message, "/l", "Local", ChatColor.GOLD, ChatColor.YELLOW, false, false);
                            for(Entity entity : playerSender.getNearbyEntities(localRange, localRange, localRange)) {
                            	if (entity instanceof Player) {
                            	Player p = (Player)entity;
                            	p.spigot().sendMessage(finalMessage);
                            	}
                            }
                            playerSender.spigot().sendMessage(finalMessage);
            		        Bukkit.getLogger().info("[L] " + playerSender.getName() + ": " + message);
            		        logToFile("[L] [" + playerSender.getLocation().getWorld().getName() + "|" + playerSender.getLocation().getBlockX() + "|" + playerSender.getLocation().getBlockY() + "|" + playerSender.getLocation().getBlockZ() + "] " + playerSender.getName() + ": " + message);
            			} else {
            				TextComponent msg = new TextComponent(TextComponent.fromLegacyText("§3 » §cNo players are nearby to hear you. Use §b/g §cto type to players in §2Global Chat§c."));
    						msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/g "));
    						playerSender.spigot().sendMessage(msg);
            			}
        			} else {
            			sender.sendMessage("§cYou don't have permission to chat in the §eLocal§c Channel.");	
        			}
        		} else {
        			if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(sender.getName())) == "L") {
        				
        			} else {
        				if (sender.hasPermission("darchat.local")) {
        					this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(sender.getName()), "L");
            				sender.sendMessage("§3 » §eNow chatting in Local.");
        				} else {
        					sender.sendMessage("§3 » §cYou don't have permission to join the §eLocal§c Channel.");
        				}
        			}
        		}
    		}
    	}   else if (cmd.getName().equalsIgnoreCase("rp")) {
    		if (sender instanceof Player) {
        		if (args.length > 0) {
        			if (sender.hasPermission("darchat.roleplay")) {
        				if (this.getConfig().getBoolean("roleplay-active." + DarUUID.getStoredUUID(sender.getName())) == true) {
                			Player playerSender = (Player)sender;
                			String message = createMessageContents(playerSender, args);
                            TextComponent finalMessage = createMessage(playerSender, message, "/rp", "RolePlay", ChatColor.DARK_PURPLE, ChatColor.GRAY, false, false);
            		        for(Player p : Bukkit.getOnlinePlayers()){
            		            if(this.getConfig().getBoolean("roleplay-active." + DarUUID.getStoredUUID(sender.getName())) == true ){
            		                p.spigot().sendMessage(finalMessage);
            		            }
            		        }
            		        Bukkit.getLogger().info("[RP] " + playerSender.getName() + ": " + message);
            		        logToFile("[RP] " + playerSender.getName() + ": " + message);
        				} else {
        					sender.sendMessage("§3 » §eYou must join the channel first. Type §b/rptoggle §eto join.");
        				}
        			} else {
            			sender.sendMessage("§3 » §cYou don't have permission to chat in the §5RolePlay§c Channel.");	
        			}
        		} else {
        			if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(sender.getName())) == "R") {
        				
        			} else {
        				if (sender.hasPermission("darchat.roleplay")) {
        					if (this.getConfig().getBoolean("roleplay-active." + DarUUID.getStoredUUID(sender.getName())) == true) {
            					this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(sender.getName()), "R");
                				sender.sendMessage("§3 » §eNow chatting in §5RolePlay§e.");
        					} else {
        						sender.sendMessage("§3 » §eYou must join the channel first. Type §b/rptoggle §eto join.");
        					}
        				} else {
        					sender.sendMessage("§3 » §cYou don't have permission to join the §5RolePlay§c Channel.");
        				}
        			}
        		}
    		}
    	} else if (cmd.getName().equalsIgnoreCase("saveconfig")) {
    		if (sender.hasPermission("darchat.staff")) {
        		saveConfig();
        		sender.sendMessage("§3[DarChat] §cConfig saved.");
    		} else {
    			sender.sendMessage("§3[DarChat] §cNo permission.");
    		}
    	} else if (cmd.getName().equalsIgnoreCase("rptoggle")) {
    		if (sender.hasPermission("darchat.roleplay")) {
    			if (this.getConfig().getBoolean("roleplay-active." + DarUUID.getStoredUUID(sender.getName())) == true) {
    				this.getConfig().set("roleplay-active." + DarUUID.getStoredUUID(sender.getName()), false);
    				sender.sendMessage("§3 » §eYou are no longer receiving messages from §5RolePlay§e.");
    				if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(sender.getName())) == "R") {
    					this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(sender.getName()), "G");
    					sender.sendMessage("§3 » §eNow chatting in §2Global§e.");
    				}
    			} else if (this.getConfig().getBoolean("roleplay-active." + DarUUID.getStoredUUID(sender.getName())) == false) {
    				this.getConfig().set("roleplay-active." + DarUUID.getStoredUUID(sender.getName()), true);
    				sender.sendMessage("§3 » §eYou are now receiving messages from §5RolePlay§e.");
    			} else {
    				this.getConfig().addDefault("roleplay-active." + DarUUID.getStoredUUID(sender.getName()), true);
    				sender.sendMessage("§3 » §eYou are now receiving messages from §5RolePlay§e.");
    			}
    		} else {
				sender.sendMessage("§3 » §cYou don't have permission to join the §5RolePlay§c Channel.");
    		}
    	} else if (cmd.getName().equalsIgnoreCase("ch")) {
    		sender.sendMessage("§3 » §eThat plugin is no longer in use on the server. Please use §b/darchat §efor chat channel assistance.");
    	} else if (cmd.getName().equalsIgnoreCase("darchat")) {
    		sender.sendMessage("§3 » §3DarChat Command Help: \n§3- §6Type §b/g §6to chat in Global Chat. \n§3- §6Type §b/l §6to chat in Local Chat. \n§3- §6Type §b/rp §6to chat in RolePlay chat. \n§3- §6Type §b/rptoggle §6to toggle viewing RolePlay messages. \n§3- §6Type §b/d §6to chat in Donator Chat. \n§9Hint: §7Type a message after a channel shortcut to quickly send a message to that channel! Example: §b/g Hello Everyone!");
    	} else if (cmd.getName().equalsIgnoreCase("gmute")) {
    		if (sender.hasPermission("darchat.mute")) {
        		if (args.length == 0) {
        			sender.sendMessage("§3 » §6Usage: §b/gmute <player>");
        		} else {
        			Player toMute = Bukkit.getPlayer(args[0]);
        			if (toMute != null) {
        				if (this.getConfig().getBoolean("global-mute." + DarUUID.getStoredUUID(toMute.getName()))) {
        					this.getConfig().set("global-mute." + DarUUID.getStoredUUID(toMute.getName()), false);
        					sender.sendMessage("§3 » §b" + toMute.getName() + " §ewas unmuted in §2Global§e.");
        				} else {
        					this.getConfig().set("global-mute." + DarUUID.getStoredUUID(toMute.getName()), true);
        					sender.sendMessage("§3 » §b" + toMute.getName() + " §ewas muted in §2Global§e.");
        				}
        			} else {
        				sender.sendMessage("§3 » §ePlayer Invalid/Offline.");
        			}
        		}
    		} else sender.sendMessage("§3 » §cNo permission.");
    	} else if (cmd.getName().equalsIgnoreCase("slm")) {
    		if (sender instanceof Player) {
    			if (sender.hasPermission("darchat.staff")) {
            		if (args.length == 0) {
            			sender.sendMessage("§3 » §6Send a §eLocal §6message to players near a location.\n§6Usage: §b/slm <worldname> <x> <y> <z> <message>");
            		} else if (args.length < 5) {
            			
            		} else {
            			
            			StringBuilder sb = new StringBuilder();
            	        for (int i = 4; i < args.length; ++i) {
            	            sb.append(args[i]);
            	            sb.append(' ');
            	        }
            	        String message = sb.toString();
            			
            	        TextComponent finalMessage = createMessage((Player)sender, message, "/l", "Local", ChatColor.GOLD, ChatColor.YELLOW, false, false);
            	        Location location = null;
            	        try {
            	        	location = new Location(Bukkit.getServer().getWorld(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            	        } catch (NumberFormatException e) {
            	        	
            	        }
            			if(location != null && location.getWorld() != null) {
            				
                			TextComponent staffMessage = createStaffMessage(((Player)sender).getPlayer(), location, message);
                			for(Player p : Bukkit.getOnlinePlayers()){
            		            if(p.hasPermission("darchat.staff")){
            		                p.spigot().sendMessage(staffMessage);
            		            }
            		        }
            				
                			for (Entity nearby : getNearbyEntities(location, localRange)) {
                            	if (nearby instanceof Player) {
                            	Player p = (Player)nearby;
                            	p.spigot().sendMessage(finalMessage);
                            	}
                			}
            			} else sender.sendMessage("§3 » §cThere was an issue with your world/coordinates. Please enter a valid world and integers for the coordinates.");
            		}
    			} else sender.sendMessage("§3 » §cNo permission.");
    		} else sender.sendMessage("§3 » §cOnly players can use this command!");
    	} else if (cmd.getName().equalsIgnoreCase("darchat-testing")) {
    		if (sender.isOp()) {
    			if (testingEnabled) {
    				testingEnabled = false;
    				sender.sendMessage("testing disabled");
    			}
    			else {
    				testingEnabled = true;
    				sender.sendMessage("testing enabled");
    			}
    		}
    	}
		return true;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName())) == null) {
			this.getConfig().addDefault("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName()), "G");
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void capsCheck(AsyncPlayerChatEvent e) {
		e.setMessage(capsCheck(e.getPlayer(), e.getMessage()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerChat(AsyncPlayerChatEvent e) {
		if (!e.isCancelled()) {
			if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName())).equalsIgnoreCase("G") == true) {
				if (!this.getConfig().getBoolean("global-mute." + DarUUID.getStoredUUID(e.getPlayer().getName()))) {
					Bukkit.getServer().spigot().broadcast(createMessage(e.getPlayer(), e.getMessage(), "/g", "Global", ChatColor.DARK_GREEN, ChatColor.GRAY, true, false));
					Bukkit.getLogger().info("[G] " + e.getPlayer().getName() + ": " + e.getMessage());
					logToFile("[G] " + e.getPlayer().getName() + ": " + e.getMessage());
				} else e.getPlayer().sendMessage("§3 » §cYou are currently muted from the §2Global§c Channel.");
				e.setCancelled(true);
			} else if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName())).equalsIgnoreCase("D") == true) {
				if (e.getPlayer().hasPermission("darchat.donator")) {
					TextComponent message = createMessage(e.getPlayer(), e.getMessage(), "/d", "Donator", ChatColor.DARK_AQUA, ChatColor.AQUA, false, false);
			        for(Player p : Bukkit.getOnlinePlayers()){
			            if(p.hasPermission("darchat.donator")){
			                p.spigot().sendMessage(message);
			            }
			        }
					Bukkit.getLogger().info("[D] " + e.getPlayer().getName() + ": " + e.getMessage());
					logToFile("[D] " + e.getPlayer().getName() + ": " + e.getMessage());
		        } else {
		        	e.getPlayer().sendMessage("§3 » §cYou no longer have permission to chat in this channel.\n§eNow chatting in §2Global§e.");
		        	this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName()), "G");
		        }
		        e.setCancelled(true);
		        
			} else if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName())).equalsIgnoreCase("S") == true) {
				if (e.getPlayer().hasPermission("darchat.staff")) {
					TextComponent message = createMessage(e.getPlayer(), e.getMessage(), "/st", "Staff", ChatColor.DARK_RED, ChatColor.RED, false, true);
			        for(Player p : Bukkit.getOnlinePlayers()){
			            if(p.hasPermission("darchat.staff")){
			                p.spigot().sendMessage(message);
			            }
			        }
					Bukkit.getLogger().info("[Staff] " + e.getPlayer().getName() + ": " + e.getMessage());
				} else {
		        	e.getPlayer().sendMessage("§3 » §cYou no longer have permission to chat in this channel.\n§eNow chatting in §2Global§e.");
		        	this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName()), "G");
				}
		        e.setCancelled(true);
			} else if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName())).equalsIgnoreCase("L") == true) {
				if (e.getPlayer().hasPermission("darchat.local")) {
					if (playersAreNearby(e.getPlayer())) {
						TextComponent message = createMessage(e.getPlayer(), e.getMessage(), "/l", "Local", ChatColor.GOLD, ChatColor.YELLOW, false, false);
						
	        			TextComponent staffMessage = createStaffMessage(e.getPlayer(), e.getPlayer().getEyeLocation(), e.getMessage());
	        			for(Player p : Bukkit.getOnlinePlayers()){
	    		            if(p.hasPermission("darchat.staff")){
	    		                p.spigot().sendMessage(staffMessage);
	    		            }
	    		        }
						
		                for(Entity entity : e.getPlayer().getNearbyEntities(localRange, localRange, localRange)) {
		                	if (entity instanceof Player) {
		                	Player p = (Player)entity;
		                	p.spigot().sendMessage(message);
		                	}
		                }
		                e.getPlayer().spigot().sendMessage(message);
						Bukkit.getLogger().info("[L] " + e.getPlayer().getName() + ": " + e.getMessage());
						logToFile("[L] [" + e.getPlayer().getLocation().getWorld().getName() + "|" + e.getPlayer().getLocation().getBlockX() + "|" + e.getPlayer().getLocation().getBlockY() + "|" + e.getPlayer().getLocation().getBlockZ() + "] " + e.getPlayer().getName() + ": " + e.getMessage());
					} else {
						TextComponent msg = new TextComponent(TextComponent.fromLegacyText("§3 » §cNo players are nearby to hear you. Click this message or type §b/g §cto switch to §2Global Chat§c."));
						msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/g"));
						e.getPlayer().spigot().sendMessage(msg);
					}
					
				} else {
		        	e.getPlayer().sendMessage("§3 » §cYou no longer have permission to chat in this channel.\n§eNow chatting in §2Global§e.");
		        	this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName()), "G");
				}
		        e.setCancelled(true);
			} else if (this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName())).equalsIgnoreCase("R") == true) {
				if (e.getPlayer().hasPermission("darchat.roleplay")) {
					TextComponent message = createMessage(e.getPlayer(), e.getMessage(), "/rp", "RolePlay", ChatColor.DARK_PURPLE, ChatColor.GRAY, false, false);
			        for(Player p : Bukkit.getOnlinePlayers()){
			            if(this.getConfig().getBoolean("roleplay-active." + DarUUID.getStoredUUID(e.getPlayer().getName())) == true ){
			                p.spigot().sendMessage(message);
			            }
			        }
					Bukkit.getLogger().info("[RP] " + e.getPlayer().getName() + ": " + e.getMessage());
					logToFile("[RP] " + e.getPlayer().getName() + ": " + e.getMessage());
				} else {
		        	e.getPlayer().sendMessage("§3 » §cYou no longer have permission to chat in this channel.\n§eNow chatting in §2Global§e.");
		        	this.getConfig().set("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName()), "G");
				}
		        e.setCancelled(true);
			} else {
				e.getPlayer().sendMessage("Config Value: " + this.getConfig().getString("focus-channel." + DarUUID.getStoredUUID(e.getPlayer().getName())));
				e.getPlayer().sendMessage("§3 » §cAn unexpected error has occured. Please contact DaRr3n911.");
			}
		}
	}
	
	private boolean playersAreNearby(Player sender) {
		
		int count = 0;
		for(Entity entity : sender.getNearbyEntities(localRange, localRange, localRange)) {
        	if (entity instanceof Player) {
        	count++;
        	}
        }
		
		if (count > 0) {
			return true;
		} else return false;
		
	}

	private String createMessageContents(Player player, String[] args) {
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; ++i) {
            if (i != 0) {
                sb.append(' ');
            }
            sb.append(args[i]);
        }
        if (player == null) {
        	return sb.toString();
        }
        return capsCheck(player, sb.toString());
	}
	
	@SuppressWarnings("deprecation")
	private TextComponent createMessage(Player sender, String chatMessage, String focusCommand, String channelName, ChatColor color, ChatColor messageColor, Boolean messagePrefix, Boolean isUsernamePlain) {
		if (testingEnabled) {
			if (sender.hasPermission("darchat.color")) {
				chatMessage = ChatColor.translateAlternateColorCodes('&', chatMessage);
				if (!sender.hasPermission("darchat.staff")) {
					chatMessage = chatMessage.replaceAll("§k", "");
				}
			} else {
				chatMessage = chatMessage.replaceAll("&0", "");
				chatMessage = chatMessage.replaceAll("&1", "");
				chatMessage = chatMessage.replaceAll("&2", "");
				chatMessage = chatMessage.replaceAll("&3", "");
				chatMessage = chatMessage.replaceAll("&4", "");
				chatMessage = chatMessage.replaceAll("&5", "");
				chatMessage = chatMessage.replaceAll("&6", "");
				chatMessage = chatMessage.replaceAll("&7", "");
				chatMessage = chatMessage.replaceAll("&8", "");
				chatMessage = chatMessage.replaceAll("&9", "");
				chatMessage = chatMessage.replaceAll("&a", "");
				chatMessage = chatMessage.replaceAll("&b", "");
				chatMessage = chatMessage.replaceAll("&c", "");
				chatMessage = chatMessage.replaceAll("&d", "");
				chatMessage = chatMessage.replaceAll("&e", "");
				chatMessage = chatMessage.replaceAll("&f", "");
				chatMessage = chatMessage.replaceAll("&m", "");
				chatMessage = chatMessage.replaceAll("&o", "");
				chatMessage = chatMessage.replaceAll("&l", "");
				chatMessage = chatMessage.replaceAll("&n", "");
			}
			TextComponent message = new TextComponent();
			if (messagePrefix) {
				if (isUsernamePlain) {
					TextComponent name = new TextComponent(ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(sender).getPrefix()) + sender.getName());
					name.setColor(ChatColor.GRAY);
					name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getPlayer().getName() + " "));
					String[] groups = PermissionsEx.getUser(sender).getGroupNames();
					name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Username: ").color(ChatColor.BLUE).append(sender.getName()).color(ChatColor.GRAY).append("\n").append("Rank: ").color(ChatColor.BLUE).append(groups[0]).color(ChatColor.GRAY).append("\n\n").append("Click to Send a Private Message!").color(ChatColor.YELLOW).create()));
					message.addExtra(name);
				} else {
					String[] groups = PermissionsEx.getUser(sender).getGroupNames();
//					if (groups[0].equalsIgnoreCase("supporter") || groups[1].equalsIgnoreCase("supporter")) {
					if (groups[0].equalsIgnoreCase("supporter")) {
						TextComponent donator = new TextComponent("* ");
						donator.setColor(ChatColor.GREEN);
						donator.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Supporter").color(ChatColor.GREEN).create()));
						message.addExtra(donator);
//					} else if (groups[0].equalsIgnoreCase("darcraftian") || groups[1].equalsIgnoreCase("darcraftian")) {
					} else if (groups[0].equalsIgnoreCase("darcraftian")) {
						TextComponent donator = new TextComponent("* ");
						donator.setColor(ChatColor.GOLD);
						donator.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Darcraftian").color(ChatColor.GOLD).create()));
						message.addExtra(donator);
					}
					TextComponent name = new TextComponent(ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(sender).getSuffix()) + sender.getDisplayName());
					name.setColor(ChatColor.GRAY);
					name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getPlayer().getName() + " "));
					name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Username: ").color(ChatColor.BLUE).append(sender.getName()).color(ChatColor.GRAY).append("\n").append("Rank: ").color(ChatColor.BLUE).append(groups[0]).color(ChatColor.GRAY).append("\n\n").append("Click to Send a Private Message!").color(ChatColor.YELLOW).create()));
					message.addExtra(name);
				}
			} else {
				if (isUsernamePlain) {
					TextComponent name = new TextComponent(sender.getName());
					name.setColor(ChatColor.GRAY);
					name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getPlayer().getName() + " "));
					String[] groups = PermissionsEx.getUser(sender).getGroupNames();
					name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Username: ").color(ChatColor.BLUE).append(sender.getName()).color(ChatColor.GRAY).append("\n").append("Rank: ").color(ChatColor.BLUE).append(groups[0]).color(ChatColor.GRAY).append("\n\n").append("Click to Send a Private Message!").color(ChatColor.YELLOW).create()));
					message.addExtra(name);
				} else {
					// TextComponent name = new TextComponent(sender.getDisplayName());
					TextComponent name = new TextComponent(ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(sender).getSuffix()) + sender.getDisplayName());
					name.setColor(ChatColor.GRAY);
					name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getPlayer().getName() + " "));
					String[] groups = PermissionsEx.getUser(sender).getGroupNames();
					name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Username: ").color(ChatColor.BLUE).append(sender.getName()).color(ChatColor.GRAY).append("\n").append("Rank: ").color(ChatColor.BLUE).append(groups[0]).color(ChatColor.GRAY).append("\n\n").append("Click to Send a Private Message!").color(ChatColor.YELLOW).create()));
					message.addExtra(name);
				}
			}
			TextComponent channel = new TextComponent(" » ");
			channel.setColor(color);
			channel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, focusCommand));
			channel.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(channelName + " Chat").color(color).create()));
			message.addExtra(channel);
			TextComponent text = new TextComponent(TextComponent.fromLegacyText(chatMessage));
			text.setColor(messageColor);
			message.addExtra(text);
			return message;
		} else {
			if (sender.hasPermission("darchat.color")) {
				chatMessage = ChatColor.translateAlternateColorCodes('&', chatMessage);
				if (!sender.hasPermission("darchat.staff")) {
					chatMessage = chatMessage.replaceAll("§k", "");
				}
			} else {
				chatMessage = chatMessage.replaceAll("&0", "");
				chatMessage = chatMessage.replaceAll("&1", "");
				chatMessage = chatMessage.replaceAll("&2", "");
				chatMessage = chatMessage.replaceAll("&3", "");
				chatMessage = chatMessage.replaceAll("&4", "");
				chatMessage = chatMessage.replaceAll("&5", "");
				chatMessage = chatMessage.replaceAll("&6", "");
				chatMessage = chatMessage.replaceAll("&7", "");
				chatMessage = chatMessage.replaceAll("&8", "");
				chatMessage = chatMessage.replaceAll("&9", "");
				chatMessage = chatMessage.replaceAll("&a", "");
				chatMessage = chatMessage.replaceAll("&b", "");
				chatMessage = chatMessage.replaceAll("&c", "");
				chatMessage = chatMessage.replaceAll("&d", "");
				chatMessage = chatMessage.replaceAll("&e", "");
				chatMessage = chatMessage.replaceAll("&f", "");
				chatMessage = chatMessage.replaceAll("&m", "");
				chatMessage = chatMessage.replaceAll("&o", "");
				chatMessage = chatMessage.replaceAll("&l", "");
				chatMessage = chatMessage.replaceAll("&n", "");
			}
			TextComponent message = new TextComponent();
			if (messagePrefix) {
				if (isUsernamePlain) {
					TextComponent name = new TextComponent(ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(sender).getPrefix()) + sender.getName());
					name.setColor(ChatColor.GRAY);
					name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getPlayer().getName() + " "));
					String[] groups = PermissionsEx.getUser(sender).getGroupNames();
					name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Username: ").color(ChatColor.BLUE).append(sender.getName()).color(ChatColor.GRAY).append("\n").append("Rank: ").color(ChatColor.BLUE).append(groups[0]).color(ChatColor.GRAY).append("\n\n").append("Click to Send a Private Message!").color(ChatColor.YELLOW).create()));
					message.addExtra(name);
				} else {
					TextComponent name = new TextComponent(ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(sender).getPrefix()) + sender.getDisplayName());
					name.setColor(ChatColor.GRAY);
					name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getPlayer().getName() + " "));
					String[] groups = PermissionsEx.getUser(sender).getGroupNames();
					name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Username: ").color(ChatColor.BLUE).append(sender.getName()).color(ChatColor.GRAY).append("\n").append("Rank: ").color(ChatColor.BLUE).append(groups[0]).color(ChatColor.GRAY).append("\n\n").append("Click to Send a Private Message!").color(ChatColor.YELLOW).create()));
					message.addExtra(name);
				}
			} else {
				if (isUsernamePlain) {
					TextComponent name = new TextComponent(sender.getName());
					name.setColor(ChatColor.GRAY);
					name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getPlayer().getName() + " "));
					String[] groups = PermissionsEx.getUser(sender).getGroupNames();
					name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Username: ").color(ChatColor.BLUE).append(sender.getName()).color(ChatColor.GRAY).append("\n").append("Rank: ").color(ChatColor.BLUE).append(groups[0]).color(ChatColor.GRAY).append("\n\n").append("Click to Send a Private Message!").color(ChatColor.YELLOW).create()));
					message.addExtra(name);
				} else {
					// TextComponent name = new TextComponent(sender.getDisplayName());
					TextComponent name = new TextComponent(ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(sender).getSuffix()) + sender.getDisplayName());
					name.setColor(ChatColor.GRAY);
					name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getPlayer().getName() + " "));
					String[] groups = PermissionsEx.getUser(sender).getGroupNames();
					name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Username: ").color(ChatColor.BLUE).append(sender.getName()).color(ChatColor.GRAY).append("\n").append("Rank: ").color(ChatColor.BLUE).append(groups[0]).color(ChatColor.GRAY).append("\n\n").append("Click to Send a Private Message!").color(ChatColor.YELLOW).create()));
					message.addExtra(name);
				}
			}
			TextComponent channel = new TextComponent(" » ");
			channel.setColor(color);
			channel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, focusCommand));
			channel.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(channelName + " Chat").color(color).create()));
			message.addExtra(channel);
			TextComponent text = new TextComponent(TextComponent.fromLegacyText(chatMessage));
			text.setColor(messageColor);
			message.addExtra(text);
			return message;
		}
	}
	
	private TextComponent createConsoleMessage(String chatMessage, String focusCommand, String channelName, ChatColor color, ChatColor txtColor) {
		chatMessage = ChatColor.translateAlternateColorCodes('&', chatMessage);
		TextComponent message = new TextComponent();
		TextComponent name = new TextComponent("* Console");
		name.setColor(ChatColor.LIGHT_PURPLE);
		name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + "Console "));
		name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Username: ").color(ChatColor.BLUE).append("Console").color(ChatColor.GRAY).append("\n").append("This was sent from Console.").color(ChatColor.LIGHT_PURPLE).append("\n\n").append("Click to Send a Private Message!").color(ChatColor.YELLOW).create()));
		message.addExtra(name);
		TextComponent channel = new TextComponent(" » ");
		channel.setColor(color);
		channel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, focusCommand));
		channel.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(channelName + " Chat").color(color).create()));
		message.addExtra(channel);
		TextComponent text = new TextComponent(TextComponent.fromLegacyText(chatMessage));
		text.setColor(txtColor);
		message.addExtra(text);
		return message;
	}
	
	private TextComponent createStaffMessage(Player playerSender, Location location, String chatMessage) {
		TextComponent message = new TextComponent();
		TextComponent log = new TextComponent("§9LOG »§f ");
		log.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/slm " + location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " "));
		log.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("World: ").color(ChatColor.BLUE).append(location.getWorld().getName()).color(ChatColor.GRAY).append("\n").append("Coordinates: ").color(ChatColor.BLUE).append("[" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "]").color(ChatColor.GRAY).append("\n\n").append("Click to send local message!").color(ChatColor.YELLOW).create()));
		message.addExtra(log);
		TextComponent name = new TextComponent("§f" + playerSender.getName());
		name.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpo " + playerSender.getPlayer().getName()));
		name.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to teleport to player!").color(ChatColor.YELLOW).create()));
		message.addExtra(name);
		message.addExtra(new TextComponent(TextComponent.fromLegacyText("§6 » §f"+ chatMessage)));
		return message;
	}
	
	public String capsCheck(Player player, String message) {
		if(!player.hasPermission("darchat.staff")) {
			int capsCount = 0;
			for(int i = 0; i < message.length(); i++) {
				if (Character.isUpperCase(message.charAt(i))) {
					capsCount++;
				}
			}
			if (capsCount > 8 && (message.length() / 2) < capsCount ) {
				message = message.toLowerCase();
				StringBuilder sb = new StringBuilder(message);
				sb.setCharAt(0, Character.toUpperCase(message.charAt(0)));
				player.sendMessage("§3 » §eYour message contained too many capital letters and was modified to follow the rules.");
		        for(Player p : Bukkit.getOnlinePlayers()){
		            if(p.hasPermission("darchat.staff")){
		                p.sendMessage("§9LOG »§b " + player.getName() + " §csent a message with caps.");
		            }
		        }
				return sb.toString();	
			} else return message;
		} else return message;
	}
	
	
	// Taken from https://bukkit.org/threads/getnearbyentities-of-a-location.101499/#post-1341141
	public static Entity[] getNearbyEntities(Location l, int radius) {
	    int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
	    HashSet <Entity> radiusEntities = new HashSet <Entity>();
	 
	    for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
	        for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
	            int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
	            for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
	                if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
	                    radiusEntities.add(e);
	            }
	        }
	    }
	 
	    return radiusEntities.toArray(new Entity[radiusEntities.size()]);
	}
	
	public void logToFile(String message) {
 
        try {
        	
            File dataFolder = getDataFolder();
            if(!dataFolder.exists()) {
                dataFolder.mkdir();
            }
 
            File saveTo = new File(getDataFolder(), "chatlog.log");
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }
 
            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println("[" + sdf.format(Calendar.getInstance().getTime()) + "] " + message);
            pw.flush();
            pw.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
