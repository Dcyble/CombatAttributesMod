package net.dcyble.combatsattributes.events;

import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PlayerConnect {

    public static Map<UUID, int[]> playerData = new HashMap<>();

    public static void getPlayerData(UUID playerUUID, World world) {
        Path worldPath = Objects.requireNonNull(world.getServer()).getSavePath(WorldSavePath.ROOT);
        Path playerDataFolderPath = worldPath.resolve("CombatsAttributes_playerData");
        Path playerDataFilePath = playerDataFolderPath.resolve(playerUUID + ".dat");

        if (Files.exists(playerDataFilePath)) {
            readPlayerData(playerDataFilePath, playerUUID);
        } else {
            try {
                Files.createDirectories(playerDataFolderPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            createPlayerData(playerDataFilePath);
            readPlayerData(playerDataFilePath, playerUUID);
        }
    }

    private static void readPlayerData(Path playerDataFilePath, UUID playerUUID) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(playerDataFilePath.toFile()))) {
            int length = dis.readInt();
            int[] data = new int[length];
            for (int i = 0; i < length; i++) {
                data[i] = dis.readInt();
            }
            playerData.put(playerUUID, data);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createPlayerData(Path playerDataFilePath) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(playerDataFilePath.toFile()))) {
            int[] defaultData = {0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0};
            dos.writeInt(defaultData.length);
            for (int value : defaultData) {
                dos.writeInt(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
