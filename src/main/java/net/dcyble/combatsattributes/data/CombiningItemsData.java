package net.dcyble.combatsattributes.data;

import java.util.ArrayList;
import java.util.List;

public class CombiningItemsData {
    public static List<List<String>> items = new ArrayList<>();

    static final List<List<String>> weapons = LoadWeaponsData.getWeapons();
    static final List<List<String>> outfit = LoadOutfitData.getOutfit();

    public static void combineData() {
        items.addAll(weapons);
        items.addAll(outfit);
    }

    public static List<List<String>> getItems() {
        return items;
    }
}
