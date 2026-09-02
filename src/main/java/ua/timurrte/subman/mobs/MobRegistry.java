package ua.timurrte.subman.mobs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ua.timurrte.subman.SubmanPlugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MobRegistry {
    private static final Map<String, CustomMobConfig> mobConfigs = new HashMap<>();
    private static NamespacedKey mobIdKey;

    public static void init(Plugin plugin) {
        mobIdKey = new NamespacedKey(plugin, "custom_mob_id");

        File file = new File(plugin.getDataFolder(), "mobs.yml");
        if (!file.exists()) {
            plugin.saveResource("mobs.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadMobs(config);
    }

    public static void loadMobs(FileConfiguration config) {
        mobConfigs.clear();
        ConfigurationSection section = config.getConfigurationSection("");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            ConfigurationSection sec = section.getConfigurationSection(key);
            if (sec == null) continue;

            String name = sec.getString("name", "Custom Mob");
            int level = sec.getInt("level", 1);
            String model = sec.getString("model", "zombie");
            String weapon = sec.getString("weapon", null);
            
            ArmorPieces armorPieces = null;
            List<String> armorList = sec.getStringList("armor");
            
            if (!armorList.isEmpty()) {
                Material helmet = armorList.size() > 0 ? Material.matchMaterial(armorList.get(0)) : null;
                Material chestplate = armorList.size() > 1 ? Material.matchMaterial(armorList.get(1)) : null;
                Material leggings = armorList.size() > 2 ? Material.matchMaterial(armorList.get(2)) : null;
                Material boots = armorList.size() > 3 ? Material.matchMaterial(armorList.get(3)) : null;
                
                armorPieces = new ArmorPieces(helmet, chestplate, leggings, boots);
            }
            
            List<String> biomes = sec.getStringList("biomes");
            int lightLevel = sec.getInt("light_level", 0);

            Map<String, Double> attributes = new HashMap<>();
            if (sec.isConfigurationSection("attributes")) {
                ConfigurationSection attrSec = sec.getConfigurationSection("attributes");
                for (String attrKey : attrSec.getKeys(false)) {
                    attributes.put(attrKey, attrSec.getDouble(attrKey));
                }
            }

            CustomMobConfig mobConfig = new CustomMobConfig(key, name, level, attributes, model, weapon, armorPieces, biomes, lightLevel);
            mobConfigs.put(key, mobConfig);
        }
    }

    public static LivingEntity spawnMob(String id, Location location, Plugin plugin) {
        CustomMobConfig config = mobConfigs.get(id);
        
        if (config == null || location.getWorld() == null) return null;

        // Determine EntityType from model string
        EntityType entityType = EntityType.valueOf(config.getModel().toUpperCase());
        
        // Spawn entity
        org.bukkit.entity.Entity spawned = location.getWorld().spawnEntity(location, entityType);
        if (!(spawned instanceof LivingEntity living)) {
            spawned.remove();
            return null;
        }

        // Apply display name (Level + Name formatted with MiniMessage)
        String formattedName = "<gray>[Lvl " + config.getLevel() + "] <reset>" + config.getName();
        living.customName(MiniMessage.miniMessage().deserialize(formattedName));
        living.setCustomNameVisible(true);

        // Apply attributes
        for (Map.Entry<String, Double> entry : config.attributes().entrySet()) {
            try {
                NamespacedKey attrKey = NamespacedKey.minecraft(entry.getKey().toLowerCase());
                Attribute attr = Registry.ATTRIBUTE.get(attrKey);
                if (attr != null) {
                    AttributeInstance instance = living.getAttribute(attr);
                    if (instance != null) {
                        instance.setBaseValue(entry.getValue());
                        // If it's max health, heal them to full value matching it
                        if (attr == Attribute.MAX_HEALTH) {
                            living.setHealth(entry.getValue());
                        }
                    }
                }
            } catch (Exception ignored) {}
        }

        // Apply weapon in main hand if specified
        if (config.getWeapon() != null && !config.getWeapon().isEmpty()) {
            Material weaponMat = Material.matchMaterial(config.getWeapon());
            if (weaponMat != null && living.getEquipment() != null) {
                living.getEquipment().setItemInMainHand(new ItemStack(weaponMat));
                living.getEquipment().setItemInMainHandDropChance(0.08f); // 8% drop chance
            }
        }
        
        if (config.getArmor() != null) {
            living.getEquipment().setArmorContents(config.getArmor().getFullSet());
        }

        // Tag entity in PersistentDataContainer to recognize it later
        living.getPersistentDataContainer().set(mobIdKey, PersistentDataType.STRING, config.getId());

        return living;
    }

    public static Set<String> getRegisteredMobIds() {
        return mobConfigs.keySet();
    }
}