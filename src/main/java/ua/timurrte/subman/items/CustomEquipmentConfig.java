package ua.timurrte.subman.items;

import java.util.List;
import java.util.Map;

public class CustomEquipmentConfig {
    private final String id;
    private final String name;
    private final String material;
    private final String rarity;
    private final String type;
    private final float cooldownSeconds;
    private final String cooldownGroup;
    private final boolean unbreakable;
    private final List<Integer> colorRgb;
    private final Map<String, Double> attributes;
    private final List<String> lore;


    public CustomEquipmentConfig(String id, String name, String material, String rarity, String type,
                            float cooldownSeconds, String cooldownGroup, boolean unbreakable, List<Integer> colorRgb, Map<String, Double> attributes, List<String> lore) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.type = type;
        this.cooldownSeconds = cooldownSeconds;
        this.cooldownGroup = cooldownGroup;
        this.unbreakable = unbreakable;
        this.attributes = attributes;
        this.colorRgb = colorRgb;
        this.lore = lore;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRarity() { return rarity; }
    public float getCooldownSeconds() { return cooldownSeconds; }
    public String getCooldownGroup() { return cooldownGroup; }
    public String getType() { return type; }
    public boolean isUnbreakable() { return unbreakable; }
    public String getMaterialName() { return material; }
    public List<Integer> getColorRgb() { return colorRgb; }
    public Map<String, Double> getAttributes() { return attributes; }
    public List<String> getLore() { return lore; }
}
