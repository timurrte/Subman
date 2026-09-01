package ua.timurrte.subman.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import ua.timurrte.subman.SubmanPlugin;
import ua.timurrte.subman.items.CustomItems;
import ua.timurrte.subman.items.ItemRegistry;

public class GiveFactionItemCommand implements CommandHandler {
    private final LiteralCommandNode<CommandSourceStack> node;
    private final Plugin plugin;

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return node;
    }

    public GiveFactionItemCommand(String rootCommand) {
        this.plugin = SubmanPlugin.getInstance();
        this.node = Commands.literal(rootCommand)
            .executes(ctx -> {
                if (!(ctx.getSource().getSender() instanceof Player player)) return 0;  

                ItemStack valkyrie = ItemRegistry.buildItem("valkyrie_sword", this.plugin);
                
                if (valkyrie == null) {
                    player.sendMessage(Component.text("§cError: 'valkyrie_sword' could not be found in items.yml!"));
                    return 0;
                }

                player.getInventory().addItem(valkyrie);
                player.sendMessage(Component.text("§aYou have received the Valkyrie Sword!"));
                
                return 1;
            })
            .build();
    }
}