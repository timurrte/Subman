package ua.timurrte.subman;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import ua.timurrte.subman.commands.CommandManager;
import ua.timurrte.subman.commands.ShopCommand;
import ua.timurrte.subman.listeners.ChatListener;
import ua.timurrte.subman.listeners.PlayerJoinListener;

public class SubmanPlugin extends JavaPlugin implements Listener {
	
  private static SubmanPlugin instance;
  
  @Override
  public void onEnable() {
	instance = this;
	
    Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    
    new CommandManager<ShopCommand>("shop", new ShopCommand());
    this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
    	event.registrar().register(
    			ShopCommand.getNode(),
    			"Opens the server shop menu",
    			java.util.List.of("sh", "sell", "ah")
    			);
    });
  }
  
  public static SubmanPlugin getInstance() {
      return instance;
  }
}