package com.example.zombiesmod.ui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class Theme {
	public static final int BG = 0xFF121416;
	public static final int WALL = 0xFF171A1C;
	public static final int FLOOR = 0xFF2A2E30;
	public static final int PANEL = 0xFF1C1F22;
	public static final int BORDER = 0xFF33383C;
	public static final int TEXT = 0xFFE8E6DF;
	public static final int MUTED = 0xFF9A9A90;
	public static final int ACCENT = 0xFFBF1919;
	public static final int GREEN = 0xFF8FBF6A;

	/** Set to false to always use the flat blocky figures instead of 3D player models. */
	public static final boolean USE_3D = true;

	private Theme() {}

	public static void drawNameTag(GuiGraphics g, Font font, String name, int color, int cx, int y) {
		int tw = font.width(name);
		g.fill(cx - tw / 2 - 3, y - 2, cx + tw / 2 + 3, y + 10, 0xAA000000);
		g.drawString(font, name, cx - tw / 2, y, color);
	}

	public static int clamp(int v, int lo, int hi) {
		return Math.max(lo, Math.min(hi, v));
	}

	/** Four L-shaped corner brackets around a rectangle. */
	public static void drawBrackets(GuiGraphics g, int x1, int y1, int x2, int y2, int arm, int color) {
		g.fill(x1, y1, x1 + arm, y1 + 2, color); g.fill(x1, y1, x1 + 2, y1 + arm, color);
		g.fill(x2 - arm, y1, x2, y1 + 2, color); g.fill(x2 - 2, y1, x2, y1 + arm, color);
		g.fill(x1, y2 - 2, x1 + arm, y2, color); g.fill(x1, y2 - arm, x1 + 2, y2, color);
		g.fill(x2 - arm, y2 - 2, x2, y2, color); g.fill(x2 - 2, y2 - arm, x2, y2, color);
	}

	private static String[] glyph(char c) {
		switch (c) {
			case 'Z': return new String[]{"11111", "00010", "00100", "01000", "11111"};
			case 'O': return new String[]{"01110", "10001", "10001", "10001", "01110"};
			case 'M': return new String[]{"10001", "11011", "10101", "10001", "10001"};
			case 'B': return new String[]{"11110", "10001", "11110", "10001", "11110"};
			case 'I': return new String[]{"11111", "00100", "00100", "00100", "11111"};
			case 'E': return new String[]{"11111", "10000", "11110", "10000", "11111"};
			case 'C': return new String[]{"01111", "10000", "10000", "10000", "01111"};
			case 'R': return new String[]{"11110", "10001", "11110", "10010", "10001"};
			case 'T': return new String[]{"11111", "00100", "00100", "00100", "00100"};
			case 'A': return new String[]{"01110", "10001", "11111", "10001", "10001"};
			case 'L': return new String[]{"10000", "10000", "10000", "10000", "11111"};
			case 'S': return new String[]{"01111", "10000", "01110", "00001", "11110"};
			default: return null;
		}
	}

	/** Blocky title text drawn with plain rectangles (no scaling API needed). */
	public static void drawPixelText(GuiGraphics g, String text, int x, int y, int px, int color) {
		for (int i = 0; i < text.length(); i++) {
			String[] rows = glyph(text.charAt(i));
			if (rows != null) {
				for (int r = 0; r < 5; r++) {
					for (int c = 0; c < 5; c++) {
						if (rows[r].charAt(c) == '1') {
							g.fill(x + c * px, y + r * px, x + (c + 1) * px, y + (r + 1) * px, color);
						}
					}
				}
			}
			x += 6 * px;
		}
	}

	public static void drawPanel(GuiGraphics g, int x1, int y1, int x2, int y2) {
		g.fill(x1, y1, x2, y2, BORDER);
		g.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, PANEL);
	}

	public static void drawBar(GuiGraphics g, int x, int y, int w, int h, int percent, int color) {
		g.fill(x, y, x + w, y + h, BORDER);
		g.fill(x, y, x + (w * percent / 100), y + h, color);
	}

	/** A blocky walking character. Legs and arms swing with time. */
	public static void drawFigure(GuiGraphics g, Font font, String name, int tagColor,
			int cx, int floorY, int s, int shirt, int skin, int seed) {
		double wave = Math.sin(System.currentTimeMillis() / 140.0 + seed);
		int liftL = (int) Math.round(Math.max(0, wave) * 3 * s);
		int liftR = (int) Math.round(Math.max(0, -wave) * 3 * s);
		int armDy = (int) Math.round(wave * 3 * s);

		int legsTop = floorY - 12 * s;
		int torsoTop = legsTop - 12 * s;
		int headTop = torsoTop - 8 * s;

		g.fill(cx - 8 * s, floorY, cx + 8 * s, floorY + s, 0x66000000);
		// legs
		g.fill(cx - 4 * s, legsTop - liftL, cx, floorY - liftL, 0xFF2B2F33);
		g.fill(cx, legsTop - liftR, cx + 4 * s, floorY - liftR, 0xFF2B2F33);
		// arms
		g.fill(cx - 7 * s, torsoTop + armDy, cx - 4 * s, legsTop + armDy, shirt);
		g.fill(cx + 4 * s, torsoTop - armDy, cx + 7 * s, legsTop - armDy, shirt);
		// torso + head
		g.fill(cx - 4 * s, torsoTop, cx + 4 * s, legsTop, shirt);
		g.fill(cx - 4 * s, headTop, cx + 4 * s, torsoTop, skin);
		g.fill(cx - 4 * s, headTop, cx + 4 * s, headTop + 3 * s, 0xFF3A2A1C);
		g.fill(cx - 3 * s, headTop + 4 * s, cx - 2 * s, headTop + 5 * s, 0xFF222222);
		g.fill(cx + 2 * s, headTop + 4 * s, cx + 3 * s, headTop + 5 * s, 0xFF222222);
		// name tag
		int tw = font.width(name);
		int ty = headTop - 16;
		g.fill(cx - tw / 2 - 3, ty - 2, cx + tw / 2 + 3, ty + 10, 0xAA000000);
		g.drawString(font, name, cx - tw / 2, ty, tagColor);
	}
}
