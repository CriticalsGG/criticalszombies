package com.example.zombiesmod.screen;

import com.example.zombiesmod.MenuState;
import com.example.zombiesmod.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MapSelectScreen extends Screen {
	private final Screen parent;
	private int margin;
	private int listW;
	private int top;
	private int btnH;
	private boolean wide;

	public MapSelectScreen(Screen parent) {
		super(Component.literal("Select Map"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		margin = Theme.clamp(width / 30, 10, 32);
		wide = width >= 480;
		listW = wide ? Theme.clamp(width * 35 / 100, 150, 230) : width - 2 * margin;
		top = height < 260 ? 44 : 80;
		btnH = height < 260 ? 20 : 24;
		int n = MenuState.MAPS.length;
		int step = Theme.clamp((height - margin - btnH - 8 - top) / n, btnH + 2, btnH + 6);
		for (int i = 0; i < n; i++) {
			final int idx = i;
			String label = MenuState.MAPS[i] + (MenuState.LOCKED[i] ? "  [LOCKED]" : "");
			Button b = Button.builder(Component.literal(label), btn -> MenuState.selectedMap = idx)
					.bounds(margin, top + i * step, listW, btnH).build();
			b.active = !MenuState.LOCKED[i];
			addRenderableWidget(b);
		}
		int bw = Theme.clamp((width - 2 * margin - 8) / 2, 70, 130);
		int by = height - margin - btnH;
		addRenderableWidget(Button.builder(Component.literal("BACK"), b -> onClose())
				.bounds(margin, by, bw, btnH).build());
		addRenderableWidget(Button.builder(Component.literal("SELECT MAP"), b -> onClose())
				.bounds(margin + bw + 8, by, bw, btnH).build());
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
		g.drawString(font, "SELECT MAP", margin, margin, Theme.ACCENT);
		int sel = MenuState.selectedMap;
		if (height >= 260) {
			String info = wide ? "PARTY 3 / 4 (SAMPLE)"
					: MenuState.MAPS[sel] + " · " + MenuState.DIFFICULTY[sel];
			g.drawString(font, info, margin, margin + 14, Theme.MUTED);
		}

		if (wide) {
			int x1 = margin + listW + 16;
			int x2 = width - margin;
			int y1 = top;
			int y2 = height - margin - btnH - 8;
			if (x2 - x1 > 80 && y2 - y1 > 40) {
				Theme.drawPanel(g, x1, y1, x2, y2);
				int ph = Theme.clamp((y2 - y1) / 2, 20, 120);
				g.fillGradient(x1 + 8, y1 + 8, x2 - 8, y1 + 8 + ph, 0xFF3A4A3A, 0xFF1D241D);
				int ty = y1 + 16 + ph;
				if (ty + 10 < y2) g.drawString(font, MenuState.MAPS[sel], x1 + 12, ty, Theme.TEXT);
				if (ty + 24 < y2) g.drawString(font, "[MAP DESCRIPTION]", x1 + 12, ty + 14, Theme.MUTED);
				if (ty + 38 < y2) g.drawString(font, "DIFFICULTY: " + MenuState.DIFFICULTY[sel], x1 + 12, ty + 28, Theme.ACCENT);
			}
		}
		super.render(g, mouseX, mouseY, partialTick);
	}
}
