package net.dcyble.combatsattributes;

import io.netty.buffer.Unpooled;
import net.dcyble.combatsattributes.GUI.RequirementsInTooltip;
import net.dcyble.combatsattributes.data.LoadOutfitData;
import net.dcyble.combatsattributes.data.LoadWeaponsData;
import net.dcyble.combatsattributes.data.CombiningItemsData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

import static net.dcyble.combatsattributes.data.PlayerDataSynchronization.SERVER_TO_CLIENT;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.Progress.Xp;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.Progress.Level;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.Progress.freePoints;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility.addedAgility;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence.addedIntelligence;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength.addedStrength;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Strength.strength;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Agility.agility;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Intelligence.intelligence;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality.addedVitality;
import static net.dcyble.combatsattributes.playerprogress.clientlogic.attributes.Vitality.vitality;

public class CombatsAttributesClient implements ClientModInitializer {

    public static boolean isPanelVisible = false;
    public static final Identifier CLIENT_TO_SERVER = new Identifier("combatsattributes", "player_connect_packet");
    public static final Identifier CLIENT_TO_SERVER_A = new Identifier("combatsattributes", "player_attributes_packet");

    @Override
    public void onInitializeClient() {

        ClientPlayNetworking.registerGlobalReceiver(SERVER_TO_CLIENT, (client, handler, buf, responseSender) -> {
            int[] playerData = buf.readIntArray();

            client.execute(() -> {
                Xp = playerData[0];
                Level = playerData[1];
                freePoints = playerData[2];
                strength = playerData[3];
                agility = playerData[4];
                intelligence = playerData[5];
                vitality = playerData[6];
                addedStrength = playerData[7];
                addedAgility = playerData [8];
                addedIntelligence = playerData[9];
                addedVitality = playerData[10];
            });
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {

            if (client.player != null) {
                UUID playerUUID = client.player.getUuid();

                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeUuid(playerUUID);
                ClientPlayNetworking.send(CLIENT_TO_SERVER, buf);
            }

            LoadWeaponsData.loadData();
            LoadOutfitData.loadData();
            CombiningItemsData.combineData();
        });

        ItemTooltipCallback.EVENT.register(new RequirementsInTooltip()::addCustomTooltip);
    }
}