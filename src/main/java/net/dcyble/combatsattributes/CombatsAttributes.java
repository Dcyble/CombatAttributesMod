package net.dcyble.combatsattributes;

import net.dcyble.combatsattributes.data.AddXPCommand;
import net.dcyble.combatsattributes.data.PlayerDataSynchronization;
import net.dcyble.combatsattributes.events.PlayerConnect;
import net.dcyble.combatsattributes.events.PlayerDisconnect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.dcyble.combatsattributes.CombatsAttributesClient.CLIENT_TO_SERVER;
import static net.dcyble.combatsattributes.CombatsAttributesClient.CLIENT_TO_SERVER_A;
import static net.dcyble.combatsattributes.events.PlayerConnect.playerData;

public class CombatsAttributes implements ModInitializer {

	@Override
	public void onInitialize() {

		ServerPlayNetworking.registerGlobalReceiver(CLIENT_TO_SERVER, (server, player, handler, buf, responseSender) -> {
			UUID playerUUID = buf.readUuid();

			List<UUID> playersList = new ArrayList<>();
			playersList.add(playerUUID);

			PlayerDataSynchronization.synchronizePlayerData(playersList, server);
		});

		ServerPlayNetworking.registerGlobalReceiver(CLIENT_TO_SERVER_A, (server, player, handler, buf, responseSender) -> {
			UUID playerUUID = player.getUuid();

			int[] data = buf.readIntArray();

			playerData.put(playerUUID, data);

			List<UUID> playersList = new ArrayList<>();
			playersList.add(playerUUID);

			PlayerDataSynchronization.synchronizePlayerData(playersList, server);
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;
			World world = player.getWorld();
			UUID playerUUID = player.getUuid();

			PlayerConnect.getPlayerData(playerUUID, world);
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity player = handler.player;
			World world = player.getWorld();

			PlayerDisconnect.savePlayerData(player, world);
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				AddXPCommand.register(dispatcher));
	}
}
