package ua.timurrte.subman.commands;

import java.util.List;
import java.util.function.Function;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import ua.timurrte.subman.SubmanPlugin;


public class CommandManager<T extends CommandHandler> {
	/**
     * Registers any command class that implements CommandHandler.
     * 
     * @param rootCommand The primary name of the command (e.g., "shop")
     * @param commandFactory A reference to the constructor of the command handler that implements CommandHandler, e.g., ShopCommand::new
     * @param description Description of the command
     * @param aliases Optional command aliases
     */
    public static <T extends CommandHandler> void register(
    		String rootCommand,
    		Function<String, T> commandFactory,
    		String description,
    		List<String> aliases) 
    {
    	T commandInstance = commandFactory.apply(rootCommand);

	    SubmanPlugin.getInstance().getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
	    	event.registrar().register(
	    			commandInstance.getNode(),
	    			description,
	    			aliases
	    			);
	    });
	}
}
