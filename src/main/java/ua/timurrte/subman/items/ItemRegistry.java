package ua.timurrte.subman.items;

import org.bukkit.Material;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemRegistry {
    private static final Map<String, CustomItemConfig> itemConfigs = new HashMap<>();
    private static NamespacedKey itemIdKey;

    public static void init(Plugin plugin) {
        itemIdKey = new NamespacedKey(plugin, "custom_item_id");
        plugin.saveDefaultConfig();
        loadItems(plugin.getConfig());
    }

    public static void loadItems(FileConfiguration config) {
        System.out.println(config);
        itemConfigs.clear();
        ConfigurationSection section = config.getConfigurationSection("");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSec = section.getConfigurationSection(key);
            if (itemSec == null) continue;

            String material = itemSec.getString("material", "STONE");
            String name = itemSec.getString("name", "Custom Item");
            String rarity = itemSec.getString("rarity", "COMMON");
            String type = itemSec.getString("type", "ITEM");
            boolean unbreakable = itemSec.getBoolean("unbreakable", false);
            
            Map<String, Double> attributes = new HashMap<>();
            if (itemSec.isConfigurationSection("attributes")) {
                ConfigurationSection attrSec = itemSec.getConfigurationSection("attributes");
                for (String attrKey : attrSec.getKeys(false)) {
                    attributes.put(attrKey, attrSec.getDouble(attrKey));
                }
            }

            List<String> lore = itemSec.getStringList("lore");

            CustomItemConfig customItem = new CustomItemConfig(key, material, name, rarity, type, unbreakable, attributes, lore);
            itemConfigs.put(key, customItem);
        }
    }

    public static ItemStack buildItem(String id, Plugin plugin) {
        CustomItemConfig config = itemConfigs.get(id);
        if (config == null) return null;

        Material mat = Material.matchMaterial(config.getMaterialName());
        if (mat == null) mat = Material.STONE;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(config.getName()));

        if (config.isUnbreakable()) {
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        
        meta.setUseCooldown(config.getCooldown());

        // Attributes parsing
        if (config.getAttributes() != null) {
            for (Map.Entry<String, Double> entry : config.getAttributes().entrySet()) {
                try {
                    String rawKey = entry.getKey().toLowerCase();
                    NamespacedKey attrKey= NamespacedKey.fromString(rawKey.contains(":") ? rawKey : "minecraft:" + rawKey);
                    if (attrKey == null) continue;
                    Attribute attr = Registry.ATTRIBUTE.get(attrKey);
                    if (attr == null) continue;
                    
                    NamespacedKey modKey = new NamespacedKey(plugin, "mod_" + attrKey.getKey().replace('/', '_'));
                    AttributeModifier modifier = new AttributeModifier(
                            modKey, 
                            entry.getValue(), 
                            AttributeModifier.Operation.ADD_NUMBER, 
                            EquipmentSlotGroup.MAINHAND
                    );
                    
                    meta.addAttributeModifier(attr, modifier);
                } catch (IllegalArgumentException ignored) {}
            }
        }

        List<Component> formattedLore = config.getLore().stream()
            .map(Component::text)
            .collect(Collectors.toList());
        
        formattedLore.add(Component.text(""));
        formattedLore.add(Component.text(getRarityFormatted(config.getRarity()) + " " + config.getType()));
        meta.lore(formattedLore);

        // PDC Identifier tag
        meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, config.getId());

        item.setItemMeta(meta);
        return item;
    }

    private static String getRarityFormatted(String rarity) {
        return switch (rarity.toUpperCase()) {
            case "UNCOMMON" -> "§a§lUNCOMMON";
            case "RARE" -> "§9§lRARE";
            case "EPIC" -> "§5§lEPIC";
            case "LEGENDARY" -> "§6§lLEGENDARY";
            default -> "§f§lCOMMON";
        };
    }

    public static String getCustomId(ItemStack item, Plugin plugin) {
        if (item == null || !item.hasItemMeta()) return null;
        NamespacedKey key = new NamespacedKey(plugin, "custom_item_id");
        return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }
    
    public static Set<String> getRegisteredItemIds() {
        return itemConfigs.keySet();
    }
}