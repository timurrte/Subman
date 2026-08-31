package ua.timurrte.subman.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public abstract class CraftingMenu {
	public static int openMenu(Player player) {
					Item fillerItem = Item.builder()
					    .setItemProvider(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).hideTooltip(true))
					    .build();
					var inv = new VirtualInventory(3 * 3);
					var gui = Gui.builder()
					    .setStructure(
					        "f f f f f f f f f",
					        "f x x x f f f f f",
					        "f x x x f f f f f",
					        "f x x x f f f f f",
					        "f f f f f f f f f"
					    )
					    .addIngredient('x', inv)
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
}
