package ua.timurrte.subman.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import ua.timurrte.subman.SubmanPlugin;


public class CommandManager<T extends CommandHandler> {
    public CommandManager(String rootCommand, T commandClassInstance) {
		super();
		commandClassInstance.setup(rootCommand);
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(rootCommand);
	    SubmanPlugin.getInstance().getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
	    	event.registrar().register(
	    			root,
	    			commandClassInstance.getNode(),
	    			"Opens the server shop menu",
	    			java.util.List.of("sh", "sell", "ah")
	    			);
	    });
	}
}
