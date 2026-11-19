package com.example.zombiesmod.screen;

import com.example.zombiesmod.MenuState;
import com.example.zombiesmod.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class LoadoutScreen extends Screen {
	private static final String[] STAT_NAMES = {"DAMAGE", "FIRE RATE", "RANGE", "MOBILITY"};
	private final Screen parent;
	private int margin;
	private int btnW;
	private int top;
	private int btnH;
	private boolean wide;

	public LoadoutScreen(Screen parent) {
		super(Component.literal("Loadout"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		margin = Theme.clamp(width / 30, 10, 32);
		wide = width >= 480;
		btnW = wide ? Theme.clamp(width * 35 / 100, 170, 240) : width - 2 * margin;
		top = height < 260 ? 44 : 80;
		btnH = height < 260 ? 20 : 24;
		addRenderableWidget(Button.builder(Component.literal("PRIMARY: " + MenuState.PRIMARY[MenuState.primary]), b -> {
			MenuState.primary = (MenuState.primary + 1) % MenuState.PRIMARY.length;
			b.setMessage(Component.literal("PRIMARY: " + MenuState.PRIMARY[MenuState.primary]));
		}).bounds(margin, top, btnW, btnH).build());
		addRenderableWidget(Button.builder(Component.literal("SECONDARY: " + MenuState.SECONDARY[MenuState.secondary]), b -> {
			MenuState.secondary = (MenuState.secondary + 1) % MenuState.SECONDARY.length;
			b.setMessage(Component.literal("SECONDARY: " + MenuState.SECONDARY[MenuState.secondary]));
		}).bounds(margin, top + btnH + 6, btnW, btnH).build());
		int sw = Theme.clamp(btnW, 90, 150);
		addRenderableWidget(Button.builder(Component.literal("SAVE LOADOUT"), b -> onClose())
				.bounds(margin, height - margin - btnH, sw, btnH).build());
	}

	@Override
	public void onClose() {
		Minecraft.getInstance().setScreen(parent);
	}

	@Override
	public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
	}

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		g.fill(0, 0, width, height, Theme.BG);
		g.drawString(font, "LOADOUT", margin, margin, Theme.ACCENT);
		if (height >= 260) {
			g.drawString(font, "SAMPLE STATS - CLICK A SLOT TO CYCLE", margin, margin + 14, Theme.MUTED);
		}

		int x1, y1;
		if (wide) {
			x1 = margin + btnW + 16;
			y1 = top;
		} else {
			x1 = margin;
			y1 = top + 2 * (btnH + 6) + 4;
		}
		int x2 = width - margin;
		int y2 = height - margin - btnH - 8;
		if (x2 - x1 > 80 && y2 - y1 > 30) {
			Theme.drawPanel(g, x1, y1, x2, y2);
			int y = y1 + 10;
			if (y2 - y1 > 130) {
				g.drawString(font, "WEAPON MODEL PREVIEW (COMING LATER)", x1 + 12, y, Theme.MUTED);
				y += 22;
			}
			int[] st = MenuState.STATS[MenuState.primary];
			int bw = Math.max(40, x2 - x1 - 24);
			for (int i = 0; i < 4; i++) {
				if (y + 20 > y2) break;
				g.drawString(font, STAT_NAMES[i], x1 + 12, y, Theme.MUTED);
				Theme.drawBar(g, x1 + 12, y + 11, bw, 5, st[i], Theme.ACCENT);
				y += 24;
			}
		}
		super.render(g, mouseX, mouseY, partialTick);
	}
}
