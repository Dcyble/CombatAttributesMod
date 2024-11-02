package net.dcyble.combatsattributes.data;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

import static net.dcyble.combatsattributes.events.PlayerConnect.playerData;

public class PlayerDataSynchronization {

    public static final Identifier SERVER_TO_CLIENT = new Identifier("combatsattributes", "player_progress_packet");

    public static void synchronizePlayerData(List<UUID> playersList, MinecraftServer server) {

        playersList.forEach((playerUUID) -> {
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerUUID);

            if (player != null) {
                int[] playerProgress = playerData.get(playerUUID);

                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeIntArray(playerProgress);
                ServerPlayNetworking.send(player, SERVER_TO_CLIENT, buf);
            }
        });
    }
}
