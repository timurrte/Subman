package ua.timurrte.subman.mobs;

import java.util.List;
import java.util.Map;

public class CustomMobConfig {
    private final String id;
    private final String name;
    private final int level;
    private final Map<String, Double> attributes;
    private final String model;
    private final String weapon;
    private final ArmorPieces armor;
    private final List<String> biomes;
    private final int lightLevel;

    public CustomMobConfig(String id, String name, int level, Map<String, Double> attributes, 
                           String model, String weapon, ArmorPieces armor, List<String> biomes, int lightLevel) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.attributes = attributes;
        this.model = model;
        this.weapon = weapon;
        this.armor = armor;
        this.biomes = biomes;
        this.lightLevel = lightLevel;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getLevel() { return level; }
    public Map<String, Double> attributes() { return attributes; }
    public String getModel() { return model; }
    public String getWeapon() { return weapon; }
    public ArmorPieces getArmor() { return armor; }
    public List<String> getBiomes() { return biomes; }
    public int getLightLevel() { return lightLevel; }
}