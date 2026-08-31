package ua.timurrte.subman.listeners;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import net.kyori.adventure.text.Component;
import ua.timurrte.subman.factions.FactionManager;

public class FactionItemListener implements Listener {
    private final NamespacedKey itemKey;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final long COOLDOWN_MS = 1000; // 1 second cooldown

    public FactionItemListener(Plugin plugin) {
        this.itemKey = new NamespacedKey(plugin, "faction_item");
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (!item.hasItemMeta()) return;
        String requiredFaction = item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
        
        if ("WARRIORS".equals(requiredFaction)) {
            if (FactionManager.getFaction(player) != FactionManager.Faction.WARRIORS) {
                event.setCancelled(true);
                player.sendMessage(Component.text("§cOnly Warriors can use the Valkyrie Sword!"));
                return;
            }
            event.setDamage(event.getDamage() * 2.5);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta()) return;
        String requiredFaction = item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);

        if ("MAGES".equals(requiredFaction)) {
            if (FactionManager.getFaction(player) != FactionManager.Faction.MAGES) {
                event.setCancelled(true);
                player.sendMessage(Component.text("§cOnly Mages can use the Staff of Dirt!"));
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                UUID uuid = player.getUniqueId();
                long now = System.currentTimeMillis();
                
                if (cooldowns.containsKey(uuid) && (now - cooldowns.get(uuid)) < COOLDOWN_MS) {
                    player.sendMessage(Component.text("§cStaff is on cooldown!"));
                    return;
                }
                
                cooldowns.put(uuid, now);
                
                // Staff of Dirt AOE Effect (Damage entities in a 5-block radius)
                Location loc = player.getLocation();
                loc.getWorld().spawnParticle(Particle.BLOCK, loc, 30, 2, 0.5, 2);
                
                for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                    if (entity instanceof LivingEntity living && entity != player) {
                        living.damage(6.0, player);
                    }
                }
                player.sendMessage(Component.text("§2You unleashed a wave of earth!"));
            }
        }
    }
}