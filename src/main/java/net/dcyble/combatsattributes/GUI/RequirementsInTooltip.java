package net.dcyble.combatsattributes.GUI;

import net.dcyble.combatsattributes.data.LoadOutfitData;
import net.dcyble.combatsattributes.data.LoadWeaponsData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility.agility;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence.intelligence;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength.strength;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality.vitality;

public class RequirementsInTooltip {
    private final List<List<String>> weapons = LoadWeaponsData.getWeapons();
    private final List<List<String>> armor = LoadOutfitData.getOutfit();

    public void addCustomTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip) {
        Identifier itemId = Registries.ITEM.getId(stack.getItem());

        checkAttributes(itemId, weapons, tooltip);
        checkAttributes(itemId, armor, tooltip);
    }

    private void checkAttributes(Identifier itemId, List<List<String>> items, List<Text> tooltip) {
        for (List<String> itemData : items) {
            if (itemData.get(0).equals(itemId.toString())) {
                tooltip.add(Text.translatable("requires").formatted(Formatting.GRAY));

                int itemStrength = Integer.parseInt(itemData.get(1));
                if (itemStrength > 0) {
                    if (strength < itemStrength) {
                        tooltip.add(Text.translatable("strength").append(" ").append(String.valueOf(itemStrength)).formatted(Formatting.RED));
                    } else {
                        tooltip.add(Text.translatable("strength").append(" ").append(String.valueOf(itemStrength)).formatted(Formatting.GOLD));
                    }
                }

                int itemAgility = Integer.parseInt(itemData.get(2));
                if (itemAgility > 0) {
                    if (agility < itemAgility) {
                        tooltip.add(Text.translatable("agility").append(" ").append(String.valueOf(itemStrength)).formatted(Formatting.RED));
                    } else {
                        tooltip.add(Text.translatable("agility").append(" ").append(String.valueOf(itemStrength)).formatted(Formatting.GREEN));
                    }
                }

                int itemIntelligence = Integer.parseInt(itemData.get(3));
                if (itemIntelligence > 0) {
                    if (intelligence < itemIntelligence) {
                        tooltip.add(Text.translatable("intelligence").append(" ").append(String.valueOf(itemStrength)).formatted(Formatting.RED));
                    } else {
                        tooltip.add(Text.translatable("intelligence").append(" ").append(String.valueOf(itemStrength)).formatted(Formatting.DARK_PURPLE));
                    }
                }

                int itemVitality = Integer.parseInt(itemData.get(4));
                if (itemVitality > 0) {
                    if (vitality < itemVitality) {
                        tooltip.add(Text.translatable("vitality").append(" ").append(String.valueOf(itemStrength)).formatted(Formatting.RED));
                    } else {
                        tooltip.add(Text.translatable("vitality").append(" ").append(String.valueOf(itemStrength)).formatted(Formatting.DARK_RED));
                    }
                }
            }
        }
    }
}
