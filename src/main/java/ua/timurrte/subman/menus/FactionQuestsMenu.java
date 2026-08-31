package ua.timurrte.subman.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import ua.timurrte.subman.factions.FactionManager;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public abstract class FactionQuestsMenu {
	public static int openMenu(Player player) {
        
        FactionManager.Faction faction = FactionManager.getFaction(player);
        Gui gui;

        if (faction == FactionManager.Faction.WARRIORS) {
            Item quest1 = Item.builder().setItemProvider(new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(Component.text("Task: Kill 10 Zombies"))).build();
            gui = Gui.builder().setStructure("x").addIngredient('x', quest1).build();
        } else if (faction == FactionManager.Faction.MAGES) {
            Item quest1 = Item.builder().setItemProvider(new ItemBuilder(Material.BREWING_STAND)
                .setName(Component.text("Task: Brew 3 Potions"))).build();
            gui = Gui.builder().setStructure("x").addIngredient('x', quest1).build();
        } else {
            player.sendMessage(Component.text("You must join a faction first using /faction!"));
            return 0;
        }

        Window.builder().setTitle("Your Faction Tasks").setUpperGui(gui).setViewer(player).build().open();
        return 1;
	}
}
