package fr.globack.skit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import fr.globack.skit.command.SkitCommand;

public class Skit extends JavaPlugin implements Listener {
	
	private static Skit instance;
	public static List<Kit> kit = new ArrayList<>();
	public static Inventory global;
	
	public static Skit getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		
		global = Bukkit.createInventory(null, getConfig().getInt("inventory-size"), "Skit");
		
		saveDefaultConfig();
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("skit").setExecutor(new SkitCommand());
		
		ConfigurationSection section = getConfig().getConfigurationSection("kit");
		for(String s : section.getKeys(false)) {
			
			String name = section.getString(s + ".name").replace('&', '§');
			Material mat = Material.WEB;
			String m = section.getString(s + ".item");
			if(Material.matchMaterial(m) != null) mat = Material.matchMaterial(m);
			boolean free = section.getBoolean(s + ".free");
			boolean grade = section.getBoolean(s + ".grade");
			boolean boutique = section.getBoolean(s + ".boutique");
			String price = section.getString(s + ".price");
			String gradeAvantage = section.getString(s + ".grade-avantage").replace('&', '§');
			int delay = section.getInt(s + ".delay");
			int dat = section.getInt(s + ".data");
			String boutiqueLink = section.getString(s + ".boutique-link").replace('&', '§');
			List<ItemStack> items = new ArrayList<>();
			ConfigurationSection sect = section;
			
			if((free && grade) || (grade && boutique) || (boutique && free)) {
				getLogger().warning("Erreur dans le config.yml");
				setEnabled(false);
			}
			
			ConfigurationSection sec = getConfig().getConfigurationSection("kit." + s + ".items");
			for(String se : sec.getKeys(false)) {
				
				String names = sec.getString(se + ".name");
				Material mats = Material.WEB;
				String ms = sec.getString(se + ".material");
				if(Material.matchMaterial(ms) != null) mats = Material.matchMaterial(ms);
				int amount = sec.getInt(se + ".amount");
				int data = sec.getInt(se + ".data");
				
				ItemStack it = new ItemStack(mats, amount, (byte)data);
				ItemMeta i = it.getItemMeta();
				i.setDisplayName(names.replace('&', '§'));
				it.setItemMeta(i);
				
				items.add(it);
				
			}
			
			Kit kit = new Kit(name.replace('&', '§'), mat, (byte)dat, free, grade, boutique, price, gradeAvantage.replace('&', '§'), delay, boutiqueLink.replace('&', '§'), items, sect);
			Skit.kit.add(kit);
			
		}
		//Bukkit.createInventory(null, 9 * 5, name.replace('&', '§'));
		ConfigurationSection inv = getConfig().getConfigurationSection("inventory");
		for(String s : inv.getKeys(false)) {
			if(inv.getString(s + ".kit") != null) {
				for(Kit kit : Skit.kit) {
					if(inv.getString(s + ".kit").replace('&', '§').equalsIgnoreCase(kit.getName())) {
						int slot = inv.getInt(s + ".slot");
						global.setItem(slot, kit.getKitItem());
					}
				}
			} else {
				
				String name = inv.getString(s + ".name").replace('&', '§');
				Material mat = Material.WEB;
				String m = inv.getString(s + ".item");
				if(Material.matchMaterial(m) != null) mat = Material.matchMaterial(m);
				int slot = inv.getInt(s + ".slot");
				int data = inv.getInt(s + ".data");
				
				ItemStack it = new ItemStack(mat, 1, (byte)data);
				ItemMeta i = it.getItemMeta();
				i.setDisplayName(name);
				it.setItemMeta(i);
				
				global.setItem(slot, it);
				
			}
		}
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		Inventory inv = event.getInventory();
		
		if(inv == null || it == null) return;
		
		if(inv.getName().equalsIgnoreCase(global.getName().replace('&', '§'))) {
			
			event.setCancelled(true);
			
			if(!it.hasItemMeta()) return;
			
			for(Kit kit : kit) {
				if(kit.getName().replace('&', '§') == it.getItemMeta().getDisplayName().replace('&', '§')) {
					player.openInventory(kit.getInventory());
				}
			}
			
		}
			
			for(Kit kit : kit) {
				if(inv.getName().equalsIgnoreCase(kit.getInventory().getName().replace('&', '§'))) {
					event.setCancelled(true);
					if(it.getType() == Material.BARRIER) {
						player.openInventory(global);
					}
				}
			}
		
	}

}
