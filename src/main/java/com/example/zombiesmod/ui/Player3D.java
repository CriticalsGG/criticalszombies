package com.example.zombiesmod.ui;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.PlayerSkin;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.UUID;

/**
 * Draws a real 3D Minecraft player model (with a skin) inside a screen, using the 1.21.11 GUI entity renderer.
 * There is no world on the title screen, so we build the render state by hand instead of using an entity.
 *
 * If anything goes wrong at runtime this class turns itself off and the menu falls back to the flat figures.
 */
public final class Player3D {
	private static boolean failed = false;

	// Random-looking ids: with no skin data, Minecraft gives each one a default skin (Steve, Alex, etc.).
	private static final UUID[] SAMPLE_IDS = {
		UUID.fromString("00000000-0000-0000-0000-000000000001"),
		UUID.fromString("5f1c0a0e-0d6a-4f2b-9c3e-1a2b3c4d5e01"),
		UUID.fromString("a7e2b9c4-31d8-4c0f-8b6a-2c3d4e5f6a02"),
		UUID.fromString("c3d4e5f6-7a8b-49c0-b1d2-3e4f5a6b7c03")
	};

	private Player3D() {}

	/**
	 * @param slot     which character (0 = you, using your real skin)
	 * @param cx       horizontal centre in GUI pixels
	 * @param floorY   y of the feet in GUI pixels
	 * @param heightPx how tall the player should be on screen
	 * @param facing   extra turn in degrees (0 = facing the camera)
	 * @return true if it was drawn, false if the caller should draw the flat fallback
	 */
	public static boolean draw(GuiGraphics g, int slot, int cx, int floorY, int heightPx, float facing) {
		if (failed) {
			return false;
		}
		try {
			Minecraft mc = Minecraft.getInstance();
			GameProfile profile = slot == 0
					? mc.getGameProfile()
					: new GameProfile(SAMPLE_IDS[slot % SAMPLE_IDS.length], "Player" + slot);
			PlayerSkin skin = mc.getSkinManager().getInsecureSkin(profile);

			AvatarRenderState state = new AvatarRenderState();
			state.skin = skin;
			state.showHat = true;
			state.showJacket = true;
			state.showLeftPants = true;
			state.showRightPants = true;
			state.showLeftSleeve = true;
			state.showRightSleeve = true;
			state.bodyRot = 180.0F + facing;
			state.yRot = 0.0F;
			state.xRot = 0.0F;
			state.walkAnimationPos = System.currentTimeMillis() / 1000.0F * 6.0F + slot * 2.3F;
			state.walkAnimationSpeed = 0.8F;

			float scale = heightPx / 1.8F;
			int centerY = floorY - Math.round(0.9F * scale);
			int halfW = Math.round(scale);
			int halfH = Math.round(1.2F * scale);
			g.submitEntityRenderState(state, scale,
					new Vector3f(0.0F, 0.9F, 0.0F),
					new Quaternionf().rotateZ((float) Math.PI),
					null,
					cx - halfW, centerY - halfH, cx + halfW, centerY + halfH);
			return true;
		} catch (Throwable t) {
			failed = true;
			System.err.println("[zombiesmod] 3D player render failed, using flat figures: " + t);
			return false;
		}
	}
}
