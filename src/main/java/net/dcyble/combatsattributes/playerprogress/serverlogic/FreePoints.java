package net.dcyble.combatsattributes.playerprogress.serverlogic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FreePoints {

    public static int checkLevel;

    public static void accrualPoints(int[] playerProgress) {
        checkLevel += 1;
        playerProgress[2] += 1;

        Set<Integer> keyLevels = new HashSet<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100));

        if (keyLevels.contains(checkLevel)) {
            playerProgress[2] += 2;
        }
    }
}
