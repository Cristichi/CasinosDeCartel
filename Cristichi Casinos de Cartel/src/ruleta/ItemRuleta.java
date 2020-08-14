package ruleta;

import java.util.ArrayList;

import org.bukkit.ChatColor;

public class ItemRuleta {
	private static ItemRuleta[] items;
	static {
		ItemRuleta.items = new ItemRuleta[100];
		ArrayList<ItemRuleta> items = new ArrayList<>(200);
		for (int i = 128; i < 255; i++) {
			items.add(new ItemRuleta((char) i));
		}
		ItemRuleta.items = items.toArray(ItemRuleta.items);
	}
	
	public static ItemRuleta[] values() {
		return items;
	}
	
	private char simbolo;

	public ItemRuleta(char simbolo) {
		this.simbolo = simbolo;
	}

	public char getSimbolo() {
		return simbolo;
	}
	
	@Override
	public String toString() {
		return ChatColor.GOLD+Character.toString(simbolo)+ChatColor.RESET;
	}
}
