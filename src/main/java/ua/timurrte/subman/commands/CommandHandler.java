package ua.timurrte.subman.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface CommandHandler {
	public LiteralCommandNode<CommandSourceStack> getNode();
	public LiteralCommandNode<CommandSourceStack> setup(String rootCommand);
}
