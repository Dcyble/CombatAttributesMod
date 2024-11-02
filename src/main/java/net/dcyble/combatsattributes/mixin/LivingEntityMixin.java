package net.dcyble.combatsattributes.mixin;

import net.dcyble.combatsattributes.playerprogress.serverlogic.Level;
import net.dcyble.combatsattributes.playerprogress.serverlogic.XP;
import net.dcyble.combatsattributes.data.PlayerDataSynchronization;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    private float healthBeforeDamage;

    @Unique
    private static Map<UUID, Map<UUID, ArrayList<Float>>> mobDamageMap = new HashMap<>();

    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamageHead(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity mob = (LivingEntity) (Object) this;

        if (source.getAttacker() instanceof PlayerEntity && mob instanceof Monster) {
            healthBeforeDamage = mob.getHealth();
        }
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void onDamageTail(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity mob = (LivingEntity) (Object) this;

        if (source.getAttacker() instanceof PlayerEntity && mob instanceof Monster) {
            UUID mobUUID = mob.getUuid();
            UUID playerUUID = source.getAttacker().getUuid();

            float healthAfterDamage = mob.getHealth();
            float damageDealt = healthBeforeDamage - healthAfterDamage;

            mobDamageMap.computeIfAbsent(mobUUID, k -> new HashMap<>())
                    .computeIfAbsent(playerUUID, k -> new ArrayList<>())
                    .add(damageDealt);
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onMobDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity mob = (LivingEntity) (Object) this;
        World world = mob.getWorld();

        if (mob instanceof Monster && !world.isClient) {
            UUID mobUUID = mob.getUuid();
            Map<UUID, ArrayList<Float>> playersDamageMap = mobDamageMap.get(mobUUID);

            if (playersDamageMap != null) {
                Map<UUID, Integer> playerDamageDealt = new HashMap<>();
                List<UUID> playersList = new ArrayList<>();

                MinecraftServer server = world.getServer();

                double xpMultiplier = 10.0d * (1.0d + (Math.sqrt(mob.getXpToDrop()) / 5.0d));

                playersDamageMap.forEach((playerUUID, damageList) -> {
                    double totalDamageDealt = damageList.stream().mapToDouble(Float::floatValue).sum();
                    playerDamageDealt.put(playerUUID, (int) (totalDamageDealt * xpMultiplier));
                    playersList.add(playerUUID);
                });

                XP.onMobKilled(playerDamageDealt);
                Level.updateLevel(playersList, server);
                PlayerDataSynchronization.synchronizePlayerData(playersList, server);
                mobDamageMap.remove(mobUUID);
            }
        }
    }
}