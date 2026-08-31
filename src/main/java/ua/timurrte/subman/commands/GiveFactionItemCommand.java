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

public class GiveFactionItemCommand implements CommandHandler {
    private final LiteralCommandNode<CommandSourceStack> node;
    private final Plugin plugin;
    private final CustomItems customItems;

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return node;
    }

    public GiveFactionItemCommand(String rootCommand) {
    	this.plugin = SubmanPlugin.getInstance();
    	this.customItems = new CustomItems(plugin);
        this.node = Commands.literal(rootCommand)
            .executes(ctx -> {
            	if (!(ctx.getSource().getSender() instanceof Player player)) return 0;  

                // Giving both custom items for testing purposes
                ItemStack valkyrie = customItems.createValkyrieSword(plugin);
                ItemStack staff = customItems.createStaffOfDirt(plugin);

                player.getInventory().addItem(valkyrie, staff);
                player.sendMessage(Component.text("§aYou have received the faction items!"));
                
                return 1;
            })
            .build();
    }
}