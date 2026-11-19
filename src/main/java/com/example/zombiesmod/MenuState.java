package com.example.zombiesmod;

/** Sample data for milestone 1. Real data comes with the game systems. */
public final class MenuState {
	public static final String[] MAPS = {"BUNKER 17", "OUTPOST NORD", "DOCKYARD", "COLD HARBOR"};
	public static final String[] DIFFICULTY = {"NORMAL", "HARD", "NORMAL", "LOCKED"};
	public static final boolean[] LOCKED = {false, false, false, true};
	public static final String[] PRIMARY = {"AK-74", "M16A1", "MP5", "SPAS-12"};
	// damage, fire rate, range, mobility (sample values)
	public static final int[][] STATS = {{62, 70, 55, 48}, {58, 62, 70, 52}, {42, 85, 35, 70}, {88, 25, 30, 40}};
	public static final String[] SECONDARY = {"M1911", "MAKAROV", "COLT PYTHON"};

	public static int selectedMap = 0;
	public static int primary = 0;
	public static int secondary = 0;

	private MenuState() {}
}
