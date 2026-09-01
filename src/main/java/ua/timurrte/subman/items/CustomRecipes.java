package ua.timurrte.subman.items;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import ua.timurrte.subman.crafting.Craft;
import ua.timurrte.subman.crafting.CraftManager;

public class CustomRecipes {
    private Plugin plugin;
    private CustomItems customItems;
    
    public CustomRecipes(Plugin plugin) {
        this.plugin = plugin;
        this.customItems = new CustomItems(plugin);
        registerCustomRecipes();
    }
    
    private void registerCustomRecipes() {
        ItemStack dirt = new ItemStack(Material.DIRT);
        Craft staffCraft = new Craft(
            Arrays.asList(
                dirt, dirt, null,
                dirt, dirt, null,
                null, null, null
            ),
            this.customItems.createStaffOfDirt(this.plugin)
        );
        CraftManager.registerCraft(staffCraft);

        Craft valkyrieCraft = new Craft(
            Arrays.asList(
                null, new ItemStack(Material.DIAMOND), null,
                null, new ItemStack(Material.GOLDEN_SWORD), null,
                null, new ItemStack(Material.STICK), null
            ),
            this.customItems.createValkyrieSword(this.plugin)
        );
        CraftManager.registerCraft(valkyrieCraft);
    }
}
