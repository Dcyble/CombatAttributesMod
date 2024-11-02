package net.dcyble.combatsattributes.playerprogress.serverlogic;

import java.util.Map;
import java.util.UUID;

import static net.dcyble.combatsattributes.events.PlayerConnect.playerData;

public class XP {

    public static void onMobKilled(Map<UUID, Integer> playerDamageDealt) {
        playerDamageDealt.forEach((playerUUID, XPReceived) -> {
            int[] playerProgress = playerData.get(playerUUID);
            playerProgress[0] += XPReceived;

            if (playerProgress[0] > 1000000) {
                playerProgress[0] = 1000000;
            }

            playerData.put(playerUUID, playerProgress);
        });
    }
}
