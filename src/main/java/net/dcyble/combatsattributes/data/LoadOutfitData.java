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

public class LoadOutfitData {

    public static List<List<String>> outfit = new ArrayList<>();

    public static void loadData() {
        loadOutfit("boots/vanilla_boots.json");
        loadOutfit("boots/custom_boots.json");
        loadOutfit("chestplates/vanilla_chestplates.json");
        loadOutfit("chestplates/custom_chestplates.json");
        loadOutfit("helmets/vanilla_helmets.json");
        loadOutfit("helmets/custom_helmets.json");
        loadOutfit("leggings/vanilla_leggings.json");
        loadOutfit("leggings/custom_leggings.json");
        loadOutfit("other/vanilla_other.json");
        loadOutfit("other/custom_other.json");
    }

    private static void loadOutfit(String fileName) {
        try {
            Resource resource = MinecraftClient.getInstance()
                    .getResourceManager()
                    .getResource(new Identifier("combats_attributes", "data/attribute_thresholds/outfit/" + fileName))
                    .orElseThrow(() -> new RuntimeException("Resource not found: " + fileName));

            InputStreamReader reader = new InputStreamReader(resource.getInputStream());
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonArray outfitData = jsonArray.get(i).getAsJsonArray();
                List<String> outfitEntry = new ArrayList<>();

                for (int j = 0; j < outfitData.size(); j++) {
                    outfitEntry.add(outfitData.get(j).getAsString());
                }

                outfit.add(outfitEntry);
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> getOutfit() {
        return outfit;
    }
}