package net.dcyble.combatsattributes.mixin;

import net.dcyble.combatsattributes.CombatsAttributesClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractInventoryScreen.class)
public abstract class StatusEffectHudMixin {

    @Inject(method = "drawStatusEffects", at = @At("HEAD"), cancellable = true)
    private void drawStatusEffects(CallbackInfo ci) {
        if (CombatsAttributesClient.isPanelVisible) {
            ci.cancel();
        }
    }
}
