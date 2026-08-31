package ua.timurrte.subman.factions;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;

public class FactionManager {
    private static final HashMap<UUID, Faction> playerFactions = new HashMap<>();

    public enum Faction {
        WARRIORS, MAGES, NONE
    }

    public static Faction getFaction(Player player) {
        return playerFactions.getOrDefault(player.getUniqueId(), Faction.NONE);
    }

    public static void setFaction(Player player, Faction faction) {
        playerFactions.put(player.getUniqueId(), faction);
    }
}