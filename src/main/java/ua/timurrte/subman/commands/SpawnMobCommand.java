package ua.timurrte.subman.commands;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import ua.timurrte.subman.SubmanPlugin;
import ua.timurrte.subman.mobs.MobRegistry;

public class SpawnMobCommand implements CommandHandler {
    LiteralCommandNode<CommandSourceStack> node;

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return node;
    }

    public SpawnMobCommand(String rootCommand) {
        super();
        this.node = Commands.literal(rootCommand)
            .executes(ctx -> {
                if (!(ctx.getSource().getSender() instanceof Player player)) return 0;      
                LivingEntity mob = MobRegistry.spawnMob("cavalier", player.getLocation(), SubmanPlugin.getInstance());
                return 1;
            })
            .build();
    }
}
