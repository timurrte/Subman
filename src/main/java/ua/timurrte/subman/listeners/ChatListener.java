package ua.timurrte.subman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class ChatListener implements Listener, ChatRenderer {
	@EventHandler
	public void onPlayerMessage(AsyncChatEvent event) {
		if(event.isAsynchronous()) {
			event.renderer(this);
		}
	}

	@Override
	public Component render(Player source, Component sourceDisplayName, Component message, Audience viewer) {
		return sourceDisplayName.append(Component.text(": ")).append(message);
	}
}
