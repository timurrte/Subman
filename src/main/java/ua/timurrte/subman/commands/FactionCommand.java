package ua.timurrte.subman.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class FactionCommand implements CommandHandler {
	private final LiteralCommandNode<CommandSourceStack> node;

	@Override
	public LiteralCommandNode<CommandSourceStack> getNode() {
		
		// TODO Auto-generated method stub
		return node;
	}

	public FactionCommand(String rootCommand) {
		super();
		this.node = Commands.literal(rootCommand)
				.executes(ctx -> {
					var sender = ctx.getSource().getSender();
					if (!(sender instanceof Player)) {
						sender.sendMessage("This command can only be used by players");
						return 0;
					}
					Player player = (Player) sender;
					Item goldenSwordItem = Item.builder()
					    .setItemProvider(new ItemBuilder(Material.GOLDEN_SWORD))
					    .addClickHandler(click -> {
					    	player.sendMessage(Component.text("You chose Warriors faction"));
					  	}
					    )
					    .build();
					Item wizardsItem = Item.builder()
						    .setItemProvider(new ItemBuilder(Material.NETHER_STAR))
						    .addClickHandler(click -> {
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
					    .setTitle("Hello World!")
					    .setUpperGui(gui)
					    .setViewer(player)
					    .build();

					window.open();
					
					return 1;
				})
				.build();
	}

}
