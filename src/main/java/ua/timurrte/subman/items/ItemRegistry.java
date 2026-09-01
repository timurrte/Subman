package ua.timurrte.subman.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

        // Display Name
        meta.displayName(Component.text(config.getName()));

        // Unbreakable
        if (config.isUnbreakable()) {
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        // Attributes (like damage)
        if (config.getAttributes() != null) {
            for (Map.Entry<String, Double> entry : config.getAttributes().entrySet()) {
                try {
                    Attribute attr = Attribute.valueOf(entry.getKey());
                    NamespacedKey key = new NamespacedKey(plugin, "mod_" + entry.getKey().toLowerCase());
                    AttributeModifier modifier = new AttributeModifier(key, entry.getValue(), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND);
                    meta.addAttributeModifier(attr, modifier);
                } catch (IllegalArgumentException ignored) {}
            }
        }

        // Lore formatting (Adding Hypixel-style Rarity footer automatically)
        List<Component> formattedLore = config.getLore().stream()
            .map(Component::text)
            .collect(Collectors.toList());
        
        formattedLore.add(Component.text(""));
        formattedLore.add(Component.text(getRarityFormatted(config.getRarity()) + " " + config.getType()));
        meta.lore(formattedLore);

        // PDC Identifier tag so your plugin knows *what* custom item this is instantly
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
}