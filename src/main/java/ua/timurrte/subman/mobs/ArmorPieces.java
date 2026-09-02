package ua.timurrte.subman.mobs;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import ua.timurrte.subman.SubmanPlugin;

public class ArmorPieces {
    private Material helmet;
    private Material chestplate;
    private Material leggings;
    private Material boots;
    
    public ArmorPieces(Material helmet, Material chestplate, Material leggings, Material boots) {
        SubmanPlugin.getInstance().getComponentLogger().info(helmet.toString());
        SubmanPlugin.getInstance().getComponentLogger().info(chestplate.toString());
        SubmanPlugin.getInstance().getComponentLogger().info(leggings.toString());
        SubmanPlugin.getInstance().getComponentLogger().info(boots.toString());
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }
    
    public ItemStack[] getFullSet() {
        return new ItemStack[] {
                ItemStack.of(boots),
                ItemStack.of(leggings),
                ItemStack.of(chestplate),
                ItemStack.of(helmet)        
        };
    }
    
    public Material getHelmet() {
        return helmet;
    }
    public void setHelmet(Material helmet) {
        this.helmet = helmet;
    }
    public Material getChestplate() {
        return chestplate;
    }
    public void setChestplate(Material chestplate) {
        this.chestplate = chestplate;
    }
    public Material getLeggings() {
        return leggings;
    }
    public void setLeggings(Material leggings) {
        this.leggings = leggings;
    }
    public Material getBoots() {
        return boots;
    }
    public void setBoots(Material boots) {
        this.boots = boots;
    }
    
    

}
