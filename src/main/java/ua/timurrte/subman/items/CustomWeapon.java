package ua.timurrte.subman.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import ua.timurrte.subman.factions.FactionManager.Faction;

public class CustomWeapon {
	private final Plugin plugin;
	
	public CustomWeapon(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public ItemStack createCustomWeapon(String name, Material material, Integer damage, String lore, Faction faction , TextColor colorForName, TextColor colorForLore) {
        ItemStack weapon = new ItemStack(material);
        ItemMeta meta = weapon.getItemMeta();
        String namespacedKey = name.toLowerCase().replaceAll(" ", "_");
        NamespacedKey damageKey = new NamespacedKey(plugin, namespacedKey);
        AttributeModifier damageModifier = new AttributeModifier(
        		damageKey, 
        		damage, 
        		AttributeModifier.Operation.ADD_NUMBER, 
        		EquipmentSlotGroup.MAINHAND
        );
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);
        
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        
        if (colorForName != null) {
        	meta.displayName(Component.text(name).color(colorForName));
        }
        else {
        	meta.displayName(Component.text(name).color(TextColor.color(0x3399FF)));
        }
        Component itemLore;
        if (colorForLore != null) {
        	itemLore = Component.text(lore).color(TextColor.color(colorForLore));
        }
        else {
        	itemLore = Component.text(lore).color(TextColor.color(0xE0E0E0));
        }
        
        final Component itemDamage = Component.text(damage + "⚔").color(TextColor.color(0xFF3333));
        List<Component> loreList = List.of(Component.text(""), itemLore, Component.text(""), itemDamage);
        meta.lore(loreList);
        
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "faction_item"), PersistentDataType.STRING, faction.toString());
        weapon.setItemMeta(meta);
        return weapon;
	}
	
}
