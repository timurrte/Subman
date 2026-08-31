package ua.timurrte.subman.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ua.timurrte.subman.SubmanPlugin;
import ua.timurrte.subman.factions.FactionManager.Faction;

public class CustomItems {
	private final CustomWeapon weaponFabric = new CustomWeapon(SubmanPlugin.getInstance());
	
	public CustomItems(Plugin plugin) {
		super();
	}
	
    public ItemStack createValkyrieSword(Plugin plugin) {
    	return weaponFabric.createCustomWeapon("Valkyrie Sword", Material.GOLDEN_SWORD, 14, "Deadliest Valkyrian weapon in the war against demon hordes", Faction.MAGES, null, null);
    }

    public ItemStack createStaffOfDirt(Plugin plugin) {
    	return weaponFabric.createCustomWeapon("Staff of Dirt", Material.STICK, 8, "Staff used by apprentices of the mage faction", Faction.MAGES, null, null);
    }
}