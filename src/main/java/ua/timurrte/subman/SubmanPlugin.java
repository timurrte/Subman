package ua.timurrte.subman;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import ua.timurrte.subman.commands.CraftCommand;
import ua.timurrte.subman.commands.FactionCommand;
import ua.timurrte.subman.commands.GiveFactionItemCommand;
import ua.timurrte.subman.commands.ShopCommand;
import ua.timurrte.subman.crafting.Craft;
import ua.timurrte.subman.crafting.CraftManager;
import ua.timurrte.subman.items.CustomItems;
import ua.timurrte.subman.items.CustomRecipes;
import ua.timurrte.subman.items.ItemRegistry;
import ua.timurrte.subman.commands.CommandManager;
import ua.timurrte.subman.listeners.ChatListener;
import ua.timurrte.subman.listeners.FactionItemListener;
import ua.timurrte.subman.listeners.PlayerJoinListener;

public class SubmanPlugin extends JavaPlugin implements Listener {
	
  private static SubmanPlugin instance;
  private CustomItems customItems;
  private CustomRecipes customRecipes;
  
  @Override
  public void onEnable() {
	instance = this;
	
    ItemRegistry.init(this);
	
	this.customItems = new CustomItems(this);
	this.customRecipes = new CustomRecipes(this);
	
    Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    Bukkit.getPluginManager().registerEvents(new FactionItemListener(this), this);
    
    CommandManager.register(
            "shop", 
            ShopCommand::new, 
            "Opens the server shop menu", 
            List.of("sh", "sell", "ah")
        );
    
    CommandManager.register(
            "faction", 
            FactionCommand::new, 
            "Opens the factions menu"
        );
    
    CommandManager.register(
    		"givefactionitem", 
    		GiveFactionItemCommand::new, 
    		"Give a player faction item"
    	);
    
    CommandManager.register(
    		"craft", 
    		CraftCommand::new, 
    		"Open crafting menu"
    	);
  }
  
  public static SubmanPlugin getInstance() {
      return instance;
  }
}