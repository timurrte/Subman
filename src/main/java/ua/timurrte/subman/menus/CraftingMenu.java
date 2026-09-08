package ua.timurrte.subman.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ua.timurrte.subman.crafting.Craft;
import ua.timurrte.subman.crafting.CraftManager;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.UpdateReason;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public abstract class CraftingMenu {
	public static int openMenu(Player player) {
					Item fillerItem = Item.builder()
					    .setItemProvider(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).hideTooltip(true))
					    .build();
					
					VirtualInventory craftingGrid    = new VirtualInventory(9);
					VirtualInventory outputInventory = new VirtualInventory(1);
					
					outputInventory.setGuiPriority(-1);
					
					craftingGrid.addPostUpdateHandler(_ -> {
					    List<ItemStack> items = new ArrayList<>();
					    for (int i = 0; i < craftingGrid.getSize(); i++) {
					        items.add(craftingGrid.getItem(i));
					    }
					    ItemStack result = CraftManager.findMatchingResult(items);
					    outputInventory.setItem(UpdateReason.SUPPRESSED , 0, result);
					});
					
					outputInventory.addPostUpdateHandler(_ -> {
					    List<ItemStack> currentGrid = getGridItems(craftingGrid);
					    Craft craft = CraftManager.findMatchingCraft(currentGrid);
					    if (craft == null) return;

					    List<ItemStack> requiredGrid = craft.getGrid();
					    for (int i = 0; i < craftingGrid.getSize(); i++) {
					        ItemStack required = requiredGrid.get(i);
					        ItemStack actual = currentGrid.get(i);
					        if (required == null || actual == null) continue;

					        int remaining = actual.getAmount() - required.getAmount();
					        if (remaining <= 0) {
					            craftingGrid.setItem(UpdateReason.SUPPRESSED, i, ItemStack.of(Material.AIR));
					        } else {
					            actual.setAmount(remaining);
					            craftingGrid.setItem(UpdateReason.SUPPRESSED, i, actual);
					        }
					    }

					    outputInventory.setItem(UpdateReason.SUPPRESSED, 0,
					        CraftManager.findMatchingResult(getGridItems(craftingGrid)));
                    });
					
					var gui = Gui.builder()
				            .setStructure(
				                "f f f f f f f f f",
				                "f a b c f f f f f",
				                "f d e j f f f r f",
				                "f g h i f f f f f",
				                "f f f f f f f f f"
				            )
				            .addIngredient('a', craftingGrid, 0)
				            .addIngredient('b', craftingGrid, 1)
				            .addIngredient('c', craftingGrid, 2)
				            .addIngredient('d', craftingGrid, 3)
				            .addIngredient('e', craftingGrid, 4)
				            .addIngredient('j', craftingGrid, 5)
				            .addIngredient('g', craftingGrid, 6)
				            .addIngredient('h', craftingGrid, 7)
				            .addIngredient('i', craftingGrid, 8)
				            .addIngredient('r', outputInventory, 0)
				            .addIngredient('f', fillerItem)
				            .build();
					Window window = Window.builder()
					    .setTitle("Crafting menu!")
					    .setUpperGui(gui)
					    .setViewer(player)
					    .build();

					window.open();
					return 1;
	}

	private static List<ItemStack> getGridItems(VirtualInventory craftingGrid) {
		List<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < craftingGrid.getSize(); i++) {
			items.add(craftingGrid.getItem(i));
		}
		return items;
	}
}
