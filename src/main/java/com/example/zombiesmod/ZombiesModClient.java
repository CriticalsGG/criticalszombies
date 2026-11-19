package com.example.zombiesmod;

import com.example.zombiesmod.screen.MainMenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screens.TitleScreen;

public class ZombiesModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Replace the vanilla title screen with our own main menu.
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.screen instanceof TitleScreen) {
				client.setScreen(new MainMenuScreen());
			}
		});
	}
}
