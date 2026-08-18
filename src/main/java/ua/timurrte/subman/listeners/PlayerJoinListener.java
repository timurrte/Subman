package ua.timurrte.subman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.kyori.adventure.audience.Audience;
import ua.timurrte.subman.SubmanPlugin;

public class PlayerJoinListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player player = e.getPlayer();
		player.sendMessage("Welcome to the server, " + player.getName());
		e.joinMessage(null);
	}
}
