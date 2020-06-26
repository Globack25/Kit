package fr.globack.skit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import fr.globack.skit.Skit;
import fr.globack.skit.Kit;

public class SkitCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			if(args.length == 0) {
				((Player) sender).openInventory(Skit.global);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("help")) {
				sendHelp((Player)sender);
			}
			
			Player player = (Player)sender;
			
			if(player.isOp()) {
				
				/*
				 * 	player.sendMessage("§aAjout : §b/skit add §6[name] [item] [isFree] [isGrade] [isBoutique] [price] [gradeName] [delay] [boutiqueLink] >"
					+ "[name] -> nom du kit, [item] -> item représentatif du kit"
					+ "[isFree] -> gratuit ? (true or false), [isGrade] -> vient d'un grade ? (true or false), [isBoutique] -> payant ? (true or false) "
					+ "-- pour les trois derniers un seul en > true < --"
					+ "[price] -> prix, [gradeName] -> nom du grade, [delay] -> délai du kit, [boutiqueLink] -> lien boutique qui mène au kit");
				 */
				
				String name = null;
				Material item = null;
				boolean free = false;
				boolean grade = false;
				boolean boutique = false;
				String price = null;
				String gradeName = null;
				int delay = 0;
				String boutiqueLink = null;
				List<ItemStack> items = new ArrayList<>();
				
				if(args[0].equalsIgnoreCase("add")) {
					
					if(args.length != 10) {
						player.sendMessage("§aAjout : §b/skit add §6[name] [item] [isFree] [isGrade] [isBoutique] [price] [gradeName] [delay] [boutiqueLink] >"
								+ "[name] -> nom du kit, [item] -> item représentatif du kit"
								+ "[isFree] -> gratuit ? (true or false), [isGrade] -> vient d'un grade ? (true or false), [isBoutique] -> payant ? (true or false) "
								+ "-- pour les trois derniers un seul en > true < --"
								+ "[price] -> prix, [gradeName] -> nom du grade, [delay] -> délai du kit, [boutiqueLink] -> lien boutique qui mène au kit");
						return true;
					}
					
					if(args[1] != null && String.valueOf(args[1]) != null) {
						String kitName = args[1].replace('&', '§');
						if(exist(kitName)) {
							player.sendMessage("§cErreur : le kit " + kitName + " existe déjà");
							return true;
						} else {
							name = kitName;
						}
					} else {
						player.sendMessage("§cErreur : veuillez indiquer un nom valide");
						return true;
					}
					
					if(args[2] != null && String.valueOf(args[2]) != null) {
						String mat = args[2];
						if(Material.matchMaterial(mat) == null) {
							player.sendMessage("§cErreur : veuillez indiquer un nom d'item valide");
							return true;
						} else {
							item = Material.matchMaterial(mat);
						}
					} else {
						player.sendMessage("§cErreur : veuillez indiquer un nom d'item valide");
						return true;
					}
					
					if((args[3] != null && Boolean.valueOf(args[3]) != null) && (args[4] != null && Boolean.valueOf(args[4]) != null) && (args[5] != null && Boolean.valueOf(args[5]) != null)) {
						free = Boolean.valueOf(args[3]);
						grade = Boolean.valueOf(args[4]);
						boutique = Boolean.valueOf(args[5]);
					} else {
						player.sendMessage("§cErreur : veuillez indiquer une valeur valide (true ou false)");
						return true;
					}
					
					if((free && grade) || (grade && boutique) || (boutique && free)) {
						player.sendMessage("§cErreur : -- pour les trois derniers un seul en > true < --");
						return true;
					}
					
					if(!free && !grade && !boutique) {
						player.sendMessage("§cErreur : -- pour les trois derniers au moin un en > true < --");
						return true;
					}
					
					if(args[6] != null && String.valueOf(args[6]) != null) {
						price = String.valueOf(args[6]);
					} else {
						player.sendMessage("§cErreur : veuillez indiquer un prix valide");
						return true;
					}
					
					if(args[7] != null && String.valueOf(args[7]) != null) {
						gradeName = args[7].replace('&', '§');
					} else {
						player.sendMessage("§cErreur : veuillez indiquer un nom de grade valide");
						return true;
					}
					
					if(args[8] != null && Integer.valueOf(args[8]) != null) {
						delay = Integer.valueOf(args[8]);
					} else {
						player.sendMessage("§cErreur : veuillez indiquer un delai valide");
						return true;
					}
					
					if(args[9] != null && String.valueOf(args[9]) != null) {
						boutiqueLink = args[9].replace('&', '§');
					} else {
						player.sendMessage("§cErreur : veuillez indiquer un lien vers la boutique valide");
						return true;
					}
					
					for(int i = 0; i < player.getInventory().getSize(); i++) {
						if(player.getInventory().getItem(i) != null) {
							items.add(player.getInventory().getItem(i));
						}
					}
					
					Kit kit = new Kit(name, item, (byte)0, free, grade, boutique, price, gradeName, delay, boutiqueLink, items, Skit.getInstance().getConfig().createSection("kit." + name));
					ConfigurationSection sec = kit.getSection();
					sec.set("name", name);
					sec.set("item", item.name());
					sec.set("free", free);
					sec.set("grade", grade);
					sec.set("boutique", boutique);
					sec.set("price", price);
					sec.set("grade-avantage", gradeName);
					sec.set("delay", delay);
					sec.set("boutique-link", boutiqueLink);
					ConfigurationSection se = Skit.getInstance().getConfig().createSection("kit." + name + ".items");
					
					for(int i = 0; i < items.size(); i ++) {
						
						Random r = new Random();
						int alea = r.nextInt(400);
						
						while(se.contains(String.valueOf(alea))) {
							alea = r.nextInt(400);
						}
						
						Skit.getInstance().getConfig().set("kit." + name + ".items." + alea + ".name", items.get(i).getType().name());
						Skit.getInstance().getConfig().set("kit." + name + ".items." + alea + ".material", items.get(i).getType().name());
						Skit.getInstance().getConfig().set("kit." + name + ".items." + alea + ".amount", items.get(i).getMaxStackSize());
						Skit.getInstance().getConfig().set("kit." + name + ".items." + alea + ".data", (int)items.get(i).getData().getData());
						
					}
					
					Skit.kit.add(kit);
					
				}
				
				Skit.getInstance().saveConfig();
				
			}
			
			if(args[0].equalsIgnoreCase("check")) {
				if(args[1] != null && String.valueOf(args[1]) != null) {
					String kitName = args[1].replace('&', '§');
					if(exist(kitName)) {
						player.openInventory(getKitByName(kitName).getInventory());
					} else {
						player.sendMessage("§cErreur : le kit " + kitName + " est introuvable, /skit list");
					}
				} else {
					player.sendMessage("§cErreur : veuillez indiquer un nom valide");
				}
			}
			
			if(args[0].equalsIgnoreCase("list")) {
				player.sendMessage("§aList des kits : ");
				for(Kit k : Skit.kit) {
					player.sendMessage("§a" + k.getName());
				}
			}
			
		}
		return false;
	}
	
	public void sendHelp(Player player) {
		if(player.isOp()) {
			player.sendMessage("§7- - - - - - - - /skit - - - - - - - -");
			player.sendMessage("§aAjout : §b/skit add §6[name] [item] [isFree] [isGrade] [isBoutique] [price] [gradeName] [delay] [boutiqueLink] >"
					+ "[name] -> nom du kit, [item] -> item représentatif du kit"
					+ "[isFree] -> gratuit ? (true or false), [isGrade] -> vient d'un grade ? (true or false), [isBoutique] -> payant ? (true or false) "
					+ "-- pour les trois derniers un seul en > true < --"
					+ "[price] -> prix, [gradeName] -> nom du grade, [delay] -> délai du kit, [boutiqueLink] -> lien boutique qui mène au kit");
			player.sendMessage("§aModif : §b/skit modif §6[name]");
			player.sendMessage("§aCheck : §b/skit check §6[name]");
			player.sendMessage("§aList : §b/skit list");
			player.sendMessage("§7- - - - - - - - /skit - - - - - - - -");
		} else {
			player.sendMessage("§7- - - - - - - - /skit - - - - - - - -");
			player.sendMessage("§b/skit check §6[nom du kit]");
			player.sendMessage("§b/skit list");
			player.sendMessage("§7- - - - - - - - /skit - - - - - - - -");
		}
	}
	
	public boolean exist(String kitName) {
		for(Kit k : Skit.kit) {
			if(k.getName().equalsIgnoreCase(kitName.toString())) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}
	
	public Kit getKitByName(String kitName) {
		for(Kit k : Skit.kit) {
			if(k.getName().equalsIgnoreCase(kitName.toString())) {
				return k;
			} else {
				continue;
			}
		}
		return null;
	}

}
