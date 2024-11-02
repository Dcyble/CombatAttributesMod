package net.dcyble.combatsattributes.playerprogress.serverlogic;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.UUID;

import static net.dcyble.combatsattributes.events.PlayerConnect.playerData;

public class Level {

    public static void updateLevel(List<UUID> playersList, MinecraftServer server) {
        int[][] levels = {
                {0, 0}, {1, 100}, {2, 400}, {3, 900}, {4, 1600}, {5, 2500}, {6, 3600}, {7, 4900}, {8, 6400}, {9, 8100}, {10, 10000},
                {11, 12100}, {12, 14400}, {13, 16900}, {14, 19600}, {15, 22500}, {16, 25600}, {17, 28900}, {18, 32400}, {19, 36100}, {20, 40000},
                {21, 44100}, {22, 48400}, {23, 52900}, {24, 57600}, {25, 62500}, {26, 67600}, {27, 72900}, {28, 78400}, {29, 84100}, {30, 90000},
                {31, 96100}, {32, 102400}, {33, 108900}, {34, 115600}, {35, 122500}, {36, 129600}, {37, 136900}, {38, 144400}, {39, 152100}, {40, 160000},
                {41, 168100}, {42, 176400}, {43, 184900}, {44, 193600}, {45, 202500}, {46, 211600}, {47, 220900}, {48, 230400}, {49, 240100}, {50, 250000},
                {51, 260100}, {52, 270400}, {53, 280900}, {54, 291600}, {55, 302500}, {56, 313600}, {57, 324900}, {58, 336400}, {59, 348100}, {60, 360000},
                {61, 372100}, {62, 384400}, {63, 396900}, {64, 409600}, {65, 422500}, {66, 435600}, {67, 448900}, {68, 462400}, {69, 476100}, {70, 490000},
                {71, 504100}, {72, 518400}, {73, 532900}, {74, 547600}, {75, 562500}, {76, 577600}, {77, 592900}, {78, 608400}, {79, 624100}, {80, 640000},
                {81, 656100}, {82, 672400}, {83, 688900}, {84, 705600}, {85, 722500}, {86, 739600}, {87, 756900}, {88, 774400}, {89, 792100}, {90, 810000},
                {91, 828100}, {92, 846400}, {93, 864900}, {94, 883600}, {95, 902500}, {96, 921600}, {97, 940900}, {98, 960400}, {99, 980100}, {100, 1000000},
                {101, 1000001}
        };

        playersList.forEach((playerUUID) -> {
            int[] playerProgress = playerData.get(playerUUID);

            for (int i = 0; i < levels.length - 1; i++) {
                int lowLimit = levels[i][1];
                int upLimit = levels[i + 1][1];

                if (playerProgress[0] >= lowLimit && playerProgress[0] < upLimit) {
                    int currentLevel = levels[i][0];

                    if (currentLevel > playerProgress[1]) {
                        FreePoints.checkLevel = playerProgress[1];
                        for (int j = playerProgress[1]; j < currentLevel; j++) {
                            FreePoints.accrualPoints(playerProgress);
                        }
                        FreePoints.checkLevel = 0;
                        ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerUUID);

                        if (player != null) {
                            player.sendMessage(Text.translatable("getting_level", currentLevel).formatted(Formatting.YELLOW), true);
                            player.sendMessage(Text.translatable("amount_free_points", playerProgress[2]));
                        }
                    }
                    playerProgress[1] = currentLevel;
                    break;
                }
            }
            playerData.put(playerUUID, playerProgress);
        });
    }
}
