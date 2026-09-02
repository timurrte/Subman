package ua.timurrte.subman.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ua.timurrte.subman.SubmanPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EquipmentRegistry {
    public static String EQUIPMENT_FILE = "equipment.yml";
    private static final Map<String, CustomEquipmentConfig> equipmentConfigs = new HashMap<>();
    private static NamespacedKey pieceIdKey;

    public static void init(Plugin plugin) {
        pieceIdKey = new NamespacedKey(plugin, "custom_equipment_id");
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        File equipmentFile = new File(plugin.getDataFolder(), EQUIPMENT_FILE);
        if (!equipmentFile.exists()) {
            plugin.saveResource(EQUIPMENT_FILE, false);
        }
        FileConfiguration equipmentConfig = YamlConfiguration.loadConfiguration(equipmentFile);
        loadEquipment(equipmentConfig);
    }

    public static void loadEquipment(FileConfiguration config) {
        equipmentConfigs.clear();
        ConfigurationSection section = config.getConfigurationSection("");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSec = section.getConfigurationSection(key);
            if (itemSec == null) continue;

            String name = itemSec.getString("name", "Custom Item");
            String material = itemSec.getString("material", "LEATHER_HELMET");
            String rarity = itemSec.getString("rarity", "COMMON");
            String type = itemSec.getString("type", "ARMOR");

            float cooldownSeconds = (float) itemSec.getDouble("cooldown", 0.0);
            String cooldownGroup = itemSec.getString("cooldownGroup", "subman:default_cooldown");

            boolean unbreakable = itemSec.getBoolean("unbreakable", false);
            List<Integer> colorRgb = itemSec.getIntegerList("color");
            
            Map<String, Double> attributes = new HashMap<>();
            if (itemSec.isConfigurationSection("attributes")) {
                ConfigurationSection attrSec = itemSec.getConfigurationSection("attributes");
                for (String attrKey : attrSec.getKeys(false)) {
                    attributes.put(attrKey, attrSec.getDouble(attrKey));
                }
            }

            List<String> lore = itemSec.getStringList("lore");

            CustomEquipmentConfig customItem = new CustomEquipmentConfig(
                key, name, material, rarity, type, cooldownSeconds, cooldownGroup, 
                unbreakable, colorRgb, attributes, lore
            );
            equipmentConfigs.put(key, customItem);
        }
    }

    public static ItemStack buildItem(String id, Plugin plugin) {
        CustomEquipmentConfig config = equipmentConfigs.get(id);
        MiniMessage mm = MiniMessage.miniMessage();
        
        if (config == null) return null;

        Material mat = Material.matchMaterial(config.getMaterialName());
        if (mat == null) {
            SubmanPlugin.getInstance().getComponentLogger().error(Component.text("Material cannot be null for equipment: " + id));
            mat = Material.LEATHER_HELMET;
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(mm.deserialize(config.getName()));

        if (config.isUnbreakable()) {
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        // Apply Dye Color for Leather Armor
        if (meta instanceof LeatherArmorMeta leatherMeta && config.getColorRgb() != null && config.getColorRgb().size() >= 3) {
            leatherMeta.setColor(Color.fromRGB(config.getColorRgb().get(0), config.getColorRgb().get(1), config.getColorRgb().get(2)));
        }

        // Attributes parsing (Applied generally to any armor slot)
        if (config.getAttributes() != null) {
            for (Map.Entry<String, Double> entry : config.getAttributes().entrySet()) {
                try {
                    String rawKey = entry.getKey().toLowerCase();
                    NamespacedKey attrKey = NamespacedKey.fromString(rawKey.contains(":") ? rawKey : "minecraft:" + rawKey);
                    if (attrKey == null) continue;
                    Attribute attr = Registry.ATTRIBUTE.get(attrKey);
                    if (attr == null) continue;
                    
                    NamespacedKey modKey = new NamespacedKey(plugin, "mod_" + attrKey.getKey().replace('/', '_'));
                    AttributeModifier modifier = new AttributeModifier(
                            modKey, 
                            entry.getValue(), 
                            AttributeModifier.Operation.ADD_NUMBER, 
                            EquipmentSlotGroup.ANY
                    );
                    
                    meta.addAttributeModifier(attr, modifier);
                } catch (IllegalArgumentException ignored) {}
            }
        }

        List<Component> formattedLore = config.getLore().stream()
            .map(mm::deserialize)
            .collect(Collectors.toList());
        
        formattedLore.add(Component.text(""));
        formattedLore.add(mm.deserialize(getRarityFormatted(config.getRarity()) + " " + config.getType()));
        meta.lore(formattedLore);

        meta.getPersistentDataContainer().set(pieceIdKey, PersistentDataType.STRING, config.getId());

        item.setItemMeta(meta);
        return item;
    }

    private static String getRarityFormatted(String rarity) {
        return switch (rarity.toUpperCase()) {
            case "UNCOMMON" -> "<green><bold>UNCOMMON</bold></green>";
            case "RARE" -> "<blue><bold>RARE</bold></blue>";
            case "EPIC" -> "<dark_purple><bold>EPIC</bold></dark_purple>";
            case "LEGENDARY" -> "<gold><bold>LEGENDARY</bold></gold>";
            default -> "<white><bold>COMMON</bold></white>";
        };
    }

    public static String getCustomId(ItemStack item, Plugin plugin) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(pieceIdKey, PersistentDataType.STRING);
    }
    
    public static boolean isExistsInRegistry(String itemId) {
        if (equipmentConfigs.containsKey(itemId)) {
            return true;
        }
        return false;
    }
    
    public static Set<String> getRegisteredItemIds() {
        return equipmentConfigs.keySet();
    }
}