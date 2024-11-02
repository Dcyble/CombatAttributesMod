package net.dcyble.combatsattributes.mixin;

import net.dcyble.combatsattributes.data.LoadOutfitData;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility.agility;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence.intelligence;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength.strength;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality.vitality;

@Mixin(ArmorItem.class)
public class ArmorItemMixin {

    @Inject(method = "dispenseArmor", at = @At("HEAD"), cancellable = true)
    private static void checkDispenserArmor(BlockPointer pointer, ItemStack armor, CallbackInfoReturnable<Boolean> cir) {
        String outfitId = Registries.ITEM.getId(armor.getItem()).toString();

        final List<List<String>> outfit = LoadOutfitData.getOutfit();

        for (List<String> outfitData : outfit) {
            if (outfitData.get(0).equals(outfitId)) {
                if (strength < Integer.parseInt(outfitData.get(1)) ||
                        agility < Integer.parseInt(outfitData.get(2)) ||
                        intelligence < Integer.parseInt(outfitData.get(3)) ||
                        vitality < Integer.parseInt(outfitData.get(4))) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }
}