package fr.globack.skit;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Kit {
	
	private String name;
	private boolean free;
	private boolean grade;
	private boolean boutique;
	private String price;
	private String gradeName;
	private int delay;
	private String boutiqueLink;
	private List<ItemStack> items;
	private Material material;
	private ConfigurationSection section;
	private Inventory inv;
	private byte data;
	
	public Kit(String name, Material material, byte data, boolean free, boolean grade, boolean boutique, String price, String gradeName, int delay, String boutiqueLink, List<ItemStack> items, ConfigurationSection section) {
		this.name = name;
		this.free = free;
		this.grade = grade;
		this.boutique = boutique;
		this.price = price;
		this.gradeName = gradeName;
		this.delay = delay;
		this.boutiqueLink = boutiqueLink;
		this.items = items;
		this.material = material;
		this.section = section;
		this.data = data;
		this.inv = Bukkit.createInventory(null, 9 * 5, name.replace('&', '§'));
		
		for(ItemStack it : items) {
			inv.addItem(it);
		}
		
		ItemStack it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)7);
		for(int i = 27; i <= 35; i++) {
			this.inv.setItem(i, it);
		}
		
		ItemStack r = new ItemStack(Material.BARRIER, 1);
		ItemMeta im = r.getItemMeta();
		im.setDisplayName("§cRetour au menu");
		im.setLore(Arrays.asList("§eRetourner au menu des kits."));
		r.setItemMeta(im);
		
		ItemStack d = new ItemStack(Material.WATCH, 1);
		ItemMeta dm = d.getItemMeta();
		dm.setDisplayName("§bDélai d'utilisation : §a" + delay + " jour(s)");
		d.setItemMeta(dm);
		
		ItemStack b = new ItemStack(Material.SIGN, 1);
		ItemMeta bm = b.getItemMeta();
		bm.setDisplayName("§6§lBoutique : §e" + boutiqueLink.replace('&', '§'));
		b.setItemMeta(bm);
		
		ItemStack g = new ItemStack(Material.ANVIL, 1);
		ItemMeta gm = g.getItemMeta();
		gm.setDisplayName("§e§lAvantage du §b" + gradeName.replace('&', '§'));
		g.setItemMeta(gm);
		
		ItemStack p = new ItemStack(Material.GOLD_INGOT, 1);
		ItemMeta pm = p.getItemMeta();
		pm.setDisplayName("§bPrix : §a" + price + "€");
		p.setItemMeta(pm);
		
		inv.setItem(36, r);
		inv.setItem(40, d);
		inv.setItem(44, getKitItem());
		
		if(free) {
			inv.setItem(37, it);
			inv.setItem(38, it);
			inv.setItem(39, it);
			inv.setItem(41, it);
			inv.setItem(42, it);
			inv.setItem(43, it);
		} else if(grade) {
			inv.setItem(37, it);
			inv.setItem(38, it);
			inv.setItem(39, g);
			inv.setItem(41, b);
			inv.setItem(42, it);
			inv.setItem(43, it);
		} else if(boutique) {
			inv.setItem(37, it);
			inv.setItem(38, it);
			inv.setItem(39, p);
			inv.setItem(41, b);
			inv.setItem(42, it);
			inv.setItem(43, it);
		}
		
	}
	
	public ConfigurationSection getSection() {
		return section;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public String getBoutiqueLink() {
		return boutiqueLink.replace('&', '§');
	}
	
	public int getDelay() {
		return delay;
	}
	
	public String getGradeName() {
		return gradeName.replace('&', '§');
	}
	
	public List<ItemStack> getItems() {
		return items;
	}
	
	public String getName() {
		return name.replace('&', '§');
	}
	
	public String getPrice() {
		return price;
	}
	
	public boolean isBoutique() {
		return boutique;
	}
	
	public byte getData() {
		return data;
	}
	
	public boolean isFree() {
		return free;
	}
	
	public boolean isGrade() {
		return grade;
	}
	
	public ItemStack getKitItem() {
		ItemStack it = new ItemStack(material, 1, data);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName(name.replace('&', '§'));
		it.setItemMeta(im);
		return it;
	}
	
	public Inventory getInventory() {
		return inv;
	}

}
