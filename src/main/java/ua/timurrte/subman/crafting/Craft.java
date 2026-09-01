package ua.timurrte.subman.crafting;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Craft {
    List<ItemStack> grid;
    ItemStack result;
    
    public Craft(List<ItemStack> grid, ItemStack result) {
        this.grid = grid;
        this.result = result;
    }
    
    public List<ItemStack> getGrid() {
        return grid;
    }
    public ItemStack getResult() {
        return result;
    }
    
    public boolean matches(List<ItemStack> otherGrid) {
        if(grid.size() != otherGrid.size()) return false;
        for (int i = 0; i < grid.size(); i++) {
            ItemStack expected = grid.get(i);
            ItemStack actual = otherGrid.get(i);
            
            boolean expectedAir = (expected == null || expected.getType().isAir());
            boolean actualAir = (actual == null || expected.getType().isAir());
            
            if (expectedAir && actualAir) continue;
            if (expectedAir != actualAir) return false;
            
            if (!expected.isSimilar(actual) || actual.getAmount() < actual.getAmount()) {
                return false;
            }
        }
        return true;
    }
}
