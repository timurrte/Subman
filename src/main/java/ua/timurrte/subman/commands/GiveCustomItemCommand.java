package ua.timurrte.subman.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import ua.timurrte.subman.SubmanPlugin;
import ua.timurrte.subman.items.ItemRegistry;

public class GiveCustomItemCommand implements CommandHandler {
    
    private final LiteralCommandNode<CommandSourceStack> node;
    private final Plugin plugin;
    
    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return node;
    }
    
    public GiveCustomItemCommand(String rootCommand) {
        this.plugin = SubmanPlugin.getInstance();
        this.node = Commands.literal(rootCommand)
                .then(Commands.argument("itemId", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            for (String id : ItemRegistry.getRegisteredItemIds()) {
                                builder.suggest(id);
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
                            
                            String itemId = ctx.getArgument("itemId", String.class);
                            ItemStack item = ItemRegistry.buildItem(itemId, this.plugin);
                            
                            if (item == null) {
                                player.sendMessage(Component.text("§cUnknown custom item ID: " + itemId));
                                return 0;
                            }
                            player.getInventory().addItem(item);
                            player.sendMessage(Component.text("§aSuccessfully given custom item: " + itemId));
                            return 1;
                        })
                    )
                .build();
    }
}
