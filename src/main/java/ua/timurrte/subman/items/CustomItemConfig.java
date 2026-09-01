package ua.timurrte.subman.items;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.meta.components.UseCooldownComponent;

public class CustomItemConfig {
    private final String id;
    private final String materialName;
    private final String name;
    private final String rarity;
    private final String type;
    private final float cooldownSeconds;
    private final String cooldownGroup;
    private final boolean unbreakable;
    private final Map<String, Double> attributes;
    private final List<String> lore;


    public CustomItemConfig(String id, String materialName, String name, String rarity, String type, 
                            float cooldownSeconds, String cooldownGroup, boolean unbreakable, Map<String, Double> attributes, List<String> lore) {
        this.id = id;
        this.materialName = materialName;
        this.name = name;
        this.rarity = rarity;
        this.type = type;
        this.cooldownSeconds = cooldownSeconds;
        this.cooldownGroup = cooldownGroup;
        this.unbreakable = unbreakable;
        this.attributes = attributes;
        this.lore = lore;
    }

    public String getId() { return id; }
    public String getMaterialName() { return materialName; }
    public String getName() { return name; }
    public String getRarity() { return rarity; }
    public float getCooldownSeconds() { return cooldownSeconds; }
    public String getCooldownGroup() { return cooldownGroup; }
    public String getType() { return type; }
    public boolean isUnbreakable() { return unbreakable; }
    public Map<String, Double> getAttributes() { return attributes; }
    public List<String> getLore() { return lore; }
}