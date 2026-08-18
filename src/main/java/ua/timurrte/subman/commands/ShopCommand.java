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
	private final LiteralCommandNode<CommandSourceStack> node;

	public ShopCommand(String rootCommand) {
		this.node = Commands.literal(rootCommand)
				.executes(ctx -> {
					var sender = ctx.getSource().getSender();
					if (!(sender instanceof Player)) {
						sender.sendMessage("This command can only be used by players");
						return 0;
					}
					Player player = (Player) sender;
					Item helloWorldItem = Item.builder()
					    .setItemProvider(new ItemBuilder(Material.DIAMOND))
					    .addClickHandler(click -> System.out.println("Hello World!"))
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
				})
				.build();
	}

	@Override
	public LiteralCommandNode<CommandSourceStack> getNode() {
		return node;
	}
}
