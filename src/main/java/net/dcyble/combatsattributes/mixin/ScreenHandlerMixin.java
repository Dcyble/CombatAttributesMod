package net.dcyble.combatsattributes.mixin;

import net.dcyble.combatsattributes.data.LoadOutfitData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility.agility;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence.intelligence;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength.strength;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality.vitality;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin{

    @Shadow public abstract ItemStack getCursorStack();

    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
    private void checkOutfit(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        ScreenHandler handler = (ScreenHandler) (Object) this;

        if (slotIndex >= 0 && slotIndex < handler.slots.size()) {
            Slot slot = (handler).getSlot(slotIndex);
            Set<Integer> armorSlotsIndexes = Set.of(5, 6, 7, 8);

            if (handler instanceof PlayerScreenHandler) {
                if (actionType == SlotActionType.PICKUP || actionType == SlotActionType.QUICK_CRAFT) {
                    if (armorSlotsIndexes.contains(slotIndex)) {
                        ItemStack cursorStack = getCursorStack();
                        checkAttributes(cursorStack, player, ci);
                    }
                } else if (actionType == SlotActionType.QUICK_MOVE) {
                    ItemStack slotStack = slot.getStack();
                    checkAttributes(slotStack, player, ci);
                } else if (actionType == SlotActionType.SWAP && armorSlotsIndexes.contains(slotIndex)) {
                    ItemStack swapStack = player.getInventory().getStack(button);
                    checkAttributes(swapStack, player, ci);
                }
            }
        }
    }

    @Unique
    private void checkAttributes(ItemStack stack, PlayerEntity player, CallbackInfo ci) {
        String outfitId = Registries.ITEM.getId(stack.getItem()).toString();

        final List<List<String>> outfit = LoadOutfitData.getOutfit();

        for (List<String> outfitData : outfit) {
            if (outfitData.get(0).equals(outfitId)) {
                if (strength < Integer.parseInt(outfitData.get(1)) ||
                        agility < Integer.parseInt(outfitData.get(2)) ||
                        intelligence < Integer.parseInt(outfitData.get(3)) ||
                        vitality < Integer.parseInt(outfitData.get(4))) {

                    if (player.getWorld().isClient()) {
                        player.sendMessage(Text.translatable("not_enough_attributes").append(" ").append(Text.translatable("to_wear")).formatted(Formatting.RED), true);
                    }

                    ci.cancel();
                    return;
                }
            }
        }
    }
}
