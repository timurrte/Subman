package ua.timurrte.subman.crafting;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public final class Craft {
    private final List<ItemStack> grid;
    private final ItemStack result;
    
    public Craft(List<ItemStack> grid, ItemStack result) {
        if (grid.size() != 9) {
            throw new IllegalArgumentException("A custom craft must contain exactly 9 slots");
        }
        this.grid = grid.stream().map(Craft::copyOrNull).toList();
        this.result = result.clone();
    }
    
    public List<ItemStack> getGrid() {
        return grid.stream().map(Craft::copyOrNull).toList();
    }
    public ItemStack getResult() {
        return result.clone();
    }
    
    public boolean matches(List<ItemStack> otherGrid) {
        if(grid.size() != otherGrid.size()) return false;
        for (int i = 0; i < grid.size(); i++) {
            ItemStack expected = grid.get(i);
            ItemStack actual = otherGrid.get(i);
            
            boolean expectedAir = (expected == null || expected.getType().isAir());
            boolean actualAir = (actual == null || actual.getType().isAir());
            
            if (expectedAir && actualAir) continue;
            if (expectedAir != actualAir) return false;
            
            if (!expected.isSimilar(actual) || actual.getAmount() < expected.getAmount()) {
                return false;
            }
        }
        return true;
    }

    private static ItemStack copyOrNull(ItemStack item) {
        return item == null || item.getType().isAir() ? null : item.clone();
    }
}
