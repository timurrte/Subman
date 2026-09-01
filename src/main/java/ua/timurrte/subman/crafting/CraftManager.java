package ua.timurrte.subman.crafting;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class CraftManager {
    private static final List<Craft> registeredCrafts = new ArrayList<>();

    public static void registerCraft(Craft craft) {
        registeredCrafts.add(craft);
    }

    public static ItemStack findMatchingResult(List<ItemStack> currentGrid) {
        for (Craft craft : registeredCrafts) {
            if (craft.matches(currentGrid)) {
                return craft.getResult().clone();
            }
        }
        return null;
    }
}