package net.dcyble.combatsattributes.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoadWeaponsData {

    public static List<List<String>> weapons = new ArrayList<>();

    public static void loadData() {
        loadWeapons("melee/vanilla_melee.json");
        loadWeapons("melee/custom_melee.json");
        loadWeapons("range/vanilla_range.json");
        loadWeapons("range/custom_range.json");
    }

    private static void loadWeapons(String fileName) {
        try {
            Resource resource = MinecraftClient.getInstance()
                    .getResourceManager()
                    .getResource(new Identifier("combats_attributes", "data/attribute_thresholds/weapons/" + fileName))
                    .orElseThrow(() -> new RuntimeException("Resource not found: " + fileName));

            InputStreamReader reader = new InputStreamReader(resource.getInputStream());
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonArray weaponData = jsonArray.get(i).getAsJsonArray();
                List<String> weaponEntry = new ArrayList<>();

                for (int j = 0; j < weaponData.size(); j++) {
                    weaponEntry.add(weaponData.get(j).getAsString());
                }

                weapons.add(weaponEntry);
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getWeapons() {
        return weapons;
    }
}