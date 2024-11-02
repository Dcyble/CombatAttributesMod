package net.dcyble.combatsattributes.data;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.dcyble.combatsattributes.playerprogress.serverlogic.Level;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.dcyble.combatsattributes.events.PlayerConnect.playerData;

public class AddXPCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("addXP")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", StringArgumentType.word())
                        .suggests((context, builder) -> CommandSource.suggestMatching(
                                context.getSource().getPlayerNames(), builder))
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");

                                    ServerPlayerEntity targetPlayer = context.getSource().getServer()
                                            .getPlayerManager().getPlayer(playerName);

                                    if (targetPlayer != null) {
                                        UUID playerUUID = targetPlayer.getUuid();

                                        if (playerData.containsKey(playerUUID)) {
                                            MinecraftServer server = targetPlayer.getWorld().getServer();

                                            List<UUID> players = new ArrayList<>();
                                            players.add(playerUUID);

                                            int[] data = playerData.get(playerUUID);
                                            if (data[0] < 1000000) {
                                                data[0] += amount;

                                                if (data[0] > 1000000) {
                                                    data[0] = 1000000;
                                                }
                                            }
                                            playerData.put(playerUUID, data);
                                            targetPlayer.sendMessage(Text.translatable("xp_received", amount), false);
                                            Level.updateLevel(players, server);
                                            PlayerDataSynchronization.synchronizePlayerData(players, server);
                                        }
                                        return 1;
                                    } else {
                                        context.getSource().sendError(Text.literal(playerName + " not found"));
                                        return 0;
                                    }
                                })
                        )
                )
        );
    }
}
