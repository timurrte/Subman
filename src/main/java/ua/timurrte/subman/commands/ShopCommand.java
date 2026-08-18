package ua.timurrte.subman.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class ShopCommand implements CommandHandler {
	private LiteralCommandNode<CommandSourceStack> node;

	@Override
	public LiteralCommandNode<CommandSourceStack> setup(String rootCommand) {
		this.node = Commands.literal(rootCommand)
				.executes(ctx -> {
					var sender = ctx.getSource().getSender();
					if (!(sender instanceof Player)) {
						sender.sendMessage("This command can only be used by players");
						return 0;
					}
					Player player = (Player) sender;
					// item: a (clickable) ui element
					Item helloWorldItem = Item.builder()
					    .setItemProvider(new ItemBuilder(Material.DIAMOND)) // the item is represented by a diamond (ItemBuilder acts as ItemProvider)
					    .addClickHandler(click -> System.out.println("Hello World!")) // "Hello World" is printed to the console on click
					    .build();

					// gui: a rectangular arrangement of ui elements
					Gui gui = Gui.builder()
					    .setStructure("x x x x x x x x x") // the gui is of dimensions 9x1 and uses the item 'x' everywhere
					    .addIngredient('x', helloWorldItem) // by item 'x', we mean helloWorldItem
					    .build();

					// window: the menu that is shown to the player, containing the gui(s), which contain the item(s)
					Window window = Window.builder()
					    .setTitle("Hello World!")
					    .setUpperGui(gui)
					    .setViewer(player)
					    .build();

					window.open();
					
					return 1;
				})
				.build();
		return node;
	}

	@Override
	public LiteralCommandNode<CommandSourceStack> getNode() {
		return node;
	}
}
