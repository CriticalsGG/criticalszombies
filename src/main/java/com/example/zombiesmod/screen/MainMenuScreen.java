package com.example.zombiesmod.screen;

import com.example.zombiesmod.MenuState;
import com.example.zombiesmod.ui.Player3D;
import com.example.zombiesmod.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;

/**
 * Title screen replacement: full-width lobby scene with the button bar along the bottom (left to right).
 * Every position is computed from the window size, so it fits any resolution.
 */
public class MainMenuScreen extends Screen {
	private static final String[] LABELS = {"PLAY", "MULTIPLAYER", "MAPS", "LOADOUT", "SETTINGS", "QUIT"};
	private static final String[] NAMES = {"Steve_77", "Alex_Rook", "Mina", "[PLAYER 4]"};
	private static final int[] SHIRTS = {0xFF4A5D3A, 0xFF7A3B2E, 0xFF3B5A7A, 0xFF5A5A5A};
	private static final int[] SKINS = {0xFFC8A07C, 0xFF8D5D3F, 0xFFE0B894, 0xFF6B4A33};
	private static final boolean[] READY = {true, false, true, false};

	private static final float TURN = 55.0F;

	private int margin;
	private int panelTop;
	private int pxTitle;

	public MainMenuScreen() {
		super(Component.literal("Zombies"));
	}

	@Override
	protected void init() {
		margin = Theme.clamp(width / 30, 10, 32);
		pxTitle = Theme.clamp((width * 40 / 100) / 53, 2, 6);

		int cols = width >= 560 ? 6 : (width >= 300 ? 3 : 2);
		int rows = (LABELS.length + cols - 1) / cols;
		int gap = width >= 560 ? 8 : 4;
		int btnH = Theme.clamp(height / 18, 18, 26);
		int panelH = 8 + 12 + 6 + rows * btnH + (rows - 1) * gap + 10;
		panelTop = height - panelH;

		Minecraft mc = Minecraft.getInstance();
		Button.OnPress[] actions = {
			b -> mc.setScreen(new SelectWorldScreen(this)),
			b -> mc.setScreen(new JoinMultiplayerScreen(this)),
			b -> mc.setScreen(new MapSelectScreen(this)),
			b -> mc.setScreen(new LoadoutScreen(this)),
			b -> mc.setScreen(new OptionsScreen(this, mc.options)),
			b -> mc.stop()
		};
		int colW = (width - 2 * margin - (cols - 1) * gap) / cols;
		int by = panelTop + 8 + 12 + 6;
		for (int i = 0; i < LABELS.length; i++) {
			int x = margin + (i % cols) * (colW + gap);
			int y = by + (i / cols) * (btnH + gap);
			addRenderableWidget(Button.builder(Component.literal(LABELS[i]), actions[i])
					.bounds(x, y, colW, btnH).build());
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		// Intentionally empty: we draw our own background in render().
	}

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		int floorTop = (int) (panelTop * 0.62);
		g.fill(0, 0, width, height, Theme.BG);
		g.fill(0, 0, width, floorTop, Theme.WALL);
		g.fill(0, floorTop, width, panelTop, Theme.FLOOR);
		Theme.drawBrackets(g, margin, margin, width - margin, panelTop - 6, 10, Theme.ACCENT);

		// Title block, top left
		int tx = margin + 14;
		int ty = margin + 12;
		Theme.drawPixelText(g, "CRITICALS", tx, ty, pxTitle, Theme.ACCENT);
		Theme.drawPixelText(g, "ZOMBIES", tx, ty + 7 * pxTitle, pxTitle, Theme.ACCENT);
		String sub = "ROUND-BASED SURVIVAL";
		if (font.width(sub) > width * 45 / 100) {
			sub = "SURVIVAL";
		}
		int subY = ty + 12 * pxTitle + 8;
		g.drawString(font, sub, tx, subY, Theme.MUTED);
		if (width >= 520) {
			String lobbyText = "BRIEFING ROOM / LOBBY 1/4";
			g.drawString(font, lobbyText, width - margin - 14 - font.width(lobbyText), ty, Theme.MUTED);
		}

		// Walking players spread across the full width
		long t = System.currentTimeMillis();
		int count = width < 420 ? 2 : (width < 640 ? 3 : 4);
		int titleBottom = subY + 9 + 6;
		int s = Theme.clamp((panelTop - 22 - titleBottom - 22) / 32, 1, 3);
		for (int i = 0; i < count; i++) {
			int sway = (int) (Math.sin(t / 2000.0 + i * 1.7) * Math.min(30, width / (count * 5)));
			int cx = width * (i + 1) / (count + 1) + sway;
			int floorY = Math.max(floorTop + 10, panelTop - 12 - (i % 2) * 10);
			int tagColor = READY[i] ? Theme.GREEN : Theme.MUTED;
			String name = i == 0 ? Minecraft.getInstance().getUser().getName() : NAMES[i];
			int figH = 32 * s;
			// Turn toward the direction of travel. If they walk backwards, change TURN to -TURN.
			float facing = (float) (TURN * Math.cos(t / 2000.0 + i * 1.7));
			boolean drew3d = Theme.USE_3D && Player3D.draw(g, i, cx, floorY, figH, facing);
			if (drew3d) {
				Theme.drawNameTag(g, font, name, tagColor, cx, floorY - figH - 18);
			} else {
				Theme.drawFigure(g, font, name, tagColor, cx, floorY, s, SHIRTS[i], SKINS[i], i * 2);
			}
		}

		// Bottom panel: info strip, then the buttons (added in init)
		g.fill(0, panelTop, width, height, 0xFF0C0D0E);
		g.fill(0, panelTop, width, panelTop + 1, Theme.BORDER);
		int iy = panelTop + 8;
		g.drawString(font, "MAP  " + MenuState.MAPS[MenuState.selectedMap], margin, iy, Theme.TEXT);
		int barW = Theme.clamp(width / 6, 60, 180);
		int bx = width - margin - barW;
		String lv = width >= 420 ? "CAREER / LV 42" : "LV 42";
		g.drawString(font, lv, bx - font.width(lv) - 8, iy, Theme.MUTED);
		Theme.drawBar(g, bx, iy + 2, barW, 6, 64, Theme.ACCENT);

		super.render(g, mouseX, mouseY, partialTick);
	}
}
