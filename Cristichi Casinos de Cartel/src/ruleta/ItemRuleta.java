package ruleta;

import org.bukkit.ChatColor;

public class ItemRuleta {
	private static ItemRuleta[] items;
	static {
		ItemRuleta.items = new ItemRuleta[] {
				new ItemRuleta('A'),
				new ItemRuleta('B'),
				new ItemRuleta('C'),
				new ItemRuleta('X', 1.3),
				new ItemRuleta('7', 1.7),
		};
	}
	
	public static ItemRuleta[] values() {
		return items;
	}
	
	private char simbolo;
	private double valor;

	public ItemRuleta(char simbolo) {
		this.simbolo = simbolo;
		valor = 1;
	}

	public ItemRuleta(char simbolo, double valor) {
		this.simbolo = simbolo;
		this.valor = valor;
	}

	public char getSimbolo() {
		return simbolo;
	}
	
	public double getValor() {
		return valor;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Character) {
			return (Character) obj == simbolo;
		}
		if (obj instanceof ItemRuleta) {
			return ((ItemRuleta) obj).simbolo == simbolo;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return ChatColor.GOLD+Character.toString(simbolo)+ChatColor.RESET;
	}
}
