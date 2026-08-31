package ua.timurrte.subman.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import ua.timurrte.subman.factions.FactionManager;
import ua.timurrte.subman.factions.FactionManager.Faction;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public abstract class FactionChooseMenu {
	public static int openMenu(Player player) {
		Item goldenSwordItem = Item.builder()
			    .setItemProvider(new ItemBuilder(Material.GOLDEN_SWORD))
			    .addClickHandler(_ -> {
			    	FactionManager.setFaction(player, Faction.WARRIORS);
			    	player.sendMessage(Component.text("You chose Warriors faction"));	
			  	}
			    )
			    .build();
			Item wizardsItem = Item.builder()
				 .setItemProvider(new ItemBuilder(Material.NETHER_STAR))
				 .addClickHandler(_ -> {
				    FactionManager.setFaction(player, Faction.MAGES);
				    player.sendMessage(Component.text("You chose Mages faction"));
				 }
				 )
				 .build();
			Item divisionItem = Item.builder()
				    .setItemProvider(new ItemBuilder(Material.GLASS_PANE))
				 .build();
			Gui gui = Gui.builder()
			    .setStructure("x x x x d o o o o")
			    .addIngredient('x', goldenSwordItem)
			    .addIngredient('o', wizardsItem)
			    .addIngredient('d', divisionItem)
			    .build();
			Window window = Window.builder()
			    .setTitle("Choose your faction!")
			    .setUpperGui(gui)
			    .setViewer(player)
			    .build();

			window.open();
			return 1;
	}
}
