package ua.timurrte.subman.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public abstract class ShopMenu {
    
	public static int openMenu(Player player) {
			Item helloWorldItem = Item.builder()
			    .setItemProvider(new ItemBuilder(Material.DIAMOND))
			    .addClickHandler(_ -> System.out.println("Hello World!"))
			    .build();
			Gui gui = Gui.builder()
			    .setStructure("x x x x x x x x x")
			    .addIngredient('x', helloWorldItem)
			    .build();
			Window window = Window.builder()
			    .setTitle("Hello World!")
			    .setUpperGui(gui)
			    .setViewer(player)
			    .build();

			window.open();
			return 1;
	}
}
