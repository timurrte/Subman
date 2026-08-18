package ua.timurrte.subman;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import ua.timurrte.subman.commands.CommandManager;
import ua.timurrte.subman.commands.FactionCommand;
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
    
    CommandManager.register(
            "shop", 
            ShopCommand::new, 
            "Opens the server shop menu", 
            List.of("sh", "sell", "ah")
        );
    
    CommandManager.register(
            "faction", 
            FactionCommand::new, 
            "Opens the factions menu", 
            List.of()
        );
  }
  
  public static SubmanPlugin getInstance() {
      return instance;
  }
}