package net.dcyble.combatsattributes.events;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

import static net.dcyble.combatsattributes.events.PlayerConnect.playerData;

public class PlayerDisconnect {

    public static void savePlayerData(ServerPlayerEntity player, World world) {
        UUID playerUUID = player.getUuid();

        Path worldPath = Objects.requireNonNull(world.getServer()).getSavePath(WorldSavePath.ROOT);
        Path playerDataFolderPath = worldPath.resolve("CombatsAttributes_playerData");
        Path playerDataFilePath = playerDataFolderPath.resolve(playerUUID + ".dat");

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(playerDataFilePath.toFile()))) {
            int[] playerProgress = playerData.get(playerUUID);
            dos.writeInt(playerProgress.length);
            for (int value : playerProgress) {
                dos.writeInt(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
