package ua.timurrte.subman.commands;

import org.bukkit.entity.Player;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import ua.timurrte.subman.menus.ShopMenu;

public class ShopCommand implements CommandHandler {
	private final LiteralCommandNode<CommandSourceStack> node;

	public ShopCommand(String rootCommand) {
		this.node = Commands.literal(rootCommand)
				.executes(ctx -> {
					if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
					return ShopMenu.openMenu(player);
				})
				.build();
	}

	@Override
	public LiteralCommandNode<CommandSourceStack> getNode() {
		return node;
	}
}
