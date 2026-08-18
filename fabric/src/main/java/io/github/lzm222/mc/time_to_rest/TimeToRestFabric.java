package io.github.lzm222.mc.time_to_rest;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.resources.ResourceLocation;

public class TimeToRestFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		var min = 1;
		TimeToRestCommon.setPeriod(min*60*20);
		TimeToRestCommon.setEnabled(true);
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			TimeToRestCommon.setFlag(true);
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, sender) -> {
			TimeToRestCommon.resetTimer();
		});

		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			TimeToRestCommon.tickAction();
		});
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(TimeToRestCommon.MODID, path);
	}
}
