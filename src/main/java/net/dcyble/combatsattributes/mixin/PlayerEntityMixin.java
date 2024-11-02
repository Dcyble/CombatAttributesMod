package net.dcyble.combatsattributes.mixin;

import net.dcyble.combatsattributes.data.LoadWeaponsData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility.agility;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence.intelligence;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength.strength;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality.vitality;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void checkAttackUse(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack stack = player.getMainHandStack();
        Identifier weaponId = Registries.ITEM.getId(stack.getItem());

        final List<List<String>> weapons = LoadWeaponsData.getWeapons();

        for (List<String> weaponData : weapons) {
            if (weaponData.get(0).equals(weaponId.toString())) {
                if (strength < Integer.parseInt(weaponData.get(1)) ||
                        agility < Integer.parseInt(weaponData.get(2)) ||
                        intelligence < Integer.parseInt(weaponData.get(3)) ||
                        vitality < Integer.parseInt(weaponData.get(4))) {
                    if (player.getWorld().isClient()) {
                        player.sendMessage(Text.translatable("not_enough_attributes").append(" ").append(Text.translatable("to_attack")).formatted(Formatting.RED), true);
                    }

                    ci.cancel();
                    return;
                }
            }
        }
    }
}
