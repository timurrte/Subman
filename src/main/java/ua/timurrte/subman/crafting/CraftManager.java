package ua.timurrte.subman.crafting;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import ua.timurrte.subman.items.ItemRegistry;

public class CraftManager {
    private static final List<Craft> registeredCrafts = new ArrayList<>();

    private CraftManager() {
    }

    /** Loads custom 3x3 recipes from plugins/Subman/recipes.yml. */
    public static void init(Plugin plugin) {
        plugin.saveResource("recipes.yml", false);
        loadCrafts(YamlConfiguration.loadConfiguration(
            new java.io.File(plugin.getDataFolder(), "recipes.yml")), plugin);
    }

    public static void registerCraft(Craft craft) {
        registeredCrafts.add(craft);
    }

    public static void loadCrafts(FileConfiguration config, Plugin plugin) {
        registeredCrafts.clear();
        for (String id : config.getKeys(false)) {
            ConfigurationSection recipe = config.getConfigurationSection(id);
            if (recipe == null) {
                continue;
            }

            try {
                Craft craft = parseCraft(recipe, plugin);
                registeredCrafts.add(craft);
            } catch (IllegalArgumentException exception) {
                plugin.getLogger().warning("Skipping invalid recipe '" + id + "': " + exception.getMessage());
            }
        }
        plugin.getLogger().info("Loaded " + registeredCrafts.size() + " custom recipes.");
    }

    public static ItemStack findMatchingResult(List<ItemStack> currentGrid) {
        Craft craft = findMatchingCraft(currentGrid);
        return craft == null ? null : craft.getResult();
    }

    public static Craft findMatchingCraft(List<ItemStack> currentGrid) {
        return registeredCrafts.stream()
            .filter(craft -> craft.matches(currentGrid))
            .findFirst()
            .orElse(null);
    }

    private static Craft parseCraft(ConfigurationSection recipe, Plugin plugin) {
        List<String> shape = recipe.getStringList("shape");
        if (shape.size() != 3 || shape.stream().anyMatch(row -> row.length() != 3)) {
            throw new IllegalArgumentException("shape must contain three rows of exactly three characters");
        }

        ConfigurationSection ingredients = recipe.getConfigurationSection("ingredients");
        if (ingredients == null) {
            throw new IllegalArgumentException("missing ingredients section");
        }

        List<ItemStack> grid = new ArrayList<>(9);
        for (String row : shape) {
            for (char symbol : row.toCharArray()) {
                if (symbol == ' ') {
                    grid.add(null);
                    continue;
                }
                ConfigurationSection ingredient = ingredients.getConfigurationSection(String.valueOf(symbol));
                if (ingredient == null) {
                    throw new IllegalArgumentException("shape uses '" + symbol + "' without an ingredient definition");
                }
                grid.add(buildStack(ingredient, plugin));
            }
        }

        ConfigurationSection result = recipe.getConfigurationSection("result");
        if (result == null) {
            throw new IllegalArgumentException("missing result section");
        }
        return new Craft(grid, buildStack(result, plugin));
    }

    private static ItemStack buildStack(ConfigurationSection section, Plugin plugin) {
        String reference = section.getString("item");
        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException("every ingredient and result needs an item");
        }

        ItemStack stack = ItemRegistry.isExistsInRegistry(reference)
            ? ItemRegistry.buildItem(reference, plugin)
            : buildVanillaStack(reference);
        if (stack == null) {
            throw new IllegalArgumentException("unknown item '" + reference + "'");
        }

        int amount = section.getInt("amount", 1);
        if (amount < 1 || amount > stack.getMaxStackSize()) {
            throw new IllegalArgumentException("amount for '" + reference + "' must be between 1 and " + stack.getMaxStackSize());
        }
        stack.setAmount(amount);
        return stack;
    }

    private static ItemStack buildVanillaStack(String name) {
        Material material = Material.matchMaterial(name);
        return material == null || material.isAir() ? null : new ItemStack(material);
    }
}
