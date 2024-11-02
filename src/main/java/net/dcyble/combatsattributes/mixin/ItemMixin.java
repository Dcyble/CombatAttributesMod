package net.dcyble.combatsattributes.mixin;

import net.dcyble.combatsattributes.data.CombiningItemsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility.agility;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence.intelligence;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength.strength;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality.vitality;

@Mixin(ItemStack.class)
public abstract class ItemMixin{

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void checkPermissionUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        Identifier stackId = Registries.ITEM.getId(stack.getItem());

        final List<List<String>> items = CombiningItemsData.getItems();

        for (List<String> itemsData : items) {
            if (itemsData.get(0).equals(stackId.toString())) {
                if (strength < Integer.parseInt(itemsData.get(1)) ||
                        agility < Integer.parseInt(itemsData.get(2)) ||
                        intelligence < Integer.parseInt(itemsData.get(3)) ||
                        vitality < Integer.parseInt(itemsData.get(4))) {
                    if (world.isClient) {
                        user.sendMessage(Text.translatable("not_enough_attributes").append(" ").append(Text.translatable("to_use")).formatted(Formatting.RED), true);
                    }

                    cir.setReturnValue(TypedActionResult.fail(stack));
                    cir.cancel();
                    return;
                }
            }
        }
    }
}
