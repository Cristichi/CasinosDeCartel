package ruleta;

import java.util.Random;

public class Ruleta {
	private Random rng;
	private Rollo rollo1, rollo2, rollo3;

	public Ruleta() {
		rng = new Random();
		this.rollo1 = new Rollo(rng);
		this.rollo2 = new Rollo(rng);
		this.rollo3 = new Rollo(rng);
	}
	
	public ItemRuleta[][] girar() {
		ItemRuleta[] i1 = rollo1.siguiente();
		ItemRuleta[] i2 = rollo2.siguiente();
		ItemRuleta[] i3 = rollo3.siguiente();
		return new ItemRuleta[][] {
			new ItemRuleta[] {i1[0], i2[0], i3[0]},
			new ItemRuleta[] {i1[1], i2[1], i3[1]},
			new ItemRuleta[] {i1[2], i2[2], i3[2]}
		};
	}
	
	public ItemRuleta[][] actual() {
		ItemRuleta[] i1 = rollo1.actual();
		ItemRuleta[] i2 = rollo2.actual();
		ItemRuleta[] i3 = rollo3.actual();
		return new ItemRuleta[][] {
			new ItemRuleta[] {i1[0], i2[0], i3[0]},
			new ItemRuleta[] {i1[1], i2[1], i3[1]},
			new ItemRuleta[] {i1[2], i2[2], i3[2]}
		};
	}
	
	public Puntuacion getPuntuacion() {
		ItemRuleta[][] m = actual();
		if (m[1][0].equals(m[1][1]) && m[1][1].equals(m[1][2])) {
			return new Puntuacion("Línea central", 6 * m[1][0].getValor());
		}
		if (m[0][0].equals(m[1][1]) && m[1][1].equals(m[2][2])) {
			return new Puntuacion("Diagonal \\ ", 3 * m[0][0].getValor());
		}
		if (m[2][0].equals(m[1][1]) && m[1][1].equals(m[0][2])) {
			return new Puntuacion("Diagonal /", 3 * m[2][0].getValor());
		}
		return new Puntuacion("Sin premio", 0);
	}
	
//	public static void main(String[] args) {
//		ItemRuleta[][] m = new ItemRuleta[][] {
//			new ItemRuleta[] {new ItemRuleta('A'), new ItemRuleta('A'), new ItemRuleta('A')},
//			new ItemRuleta[] {new ItemRuleta('B'), new ItemRuleta('A'), new ItemRuleta('A')},
//			new ItemRuleta[] {new ItemRuleta('A'), new ItemRuleta('B'), new ItemRuleta('A')},
//		};
//		if (m[1][0].equals(m[1][1]) && m[1][1].equals(m[1][2])) {
////			return new Puntuacion("Línea central", 2.5);
//			System.out.println("Línea Central");
//		}
//		if (m[0][0].equals(m[1][1]) && m[1][1].equals(m[2][2])) {
////			return new Puntuacion("Diagonal", 1.5);
//			System.out.println("Diagonal \\");
//		}
//		if (m[2][0].equals(m[1][1]) && m[1][1].equals(m[0][2])) {
////			return new Puntuacion("Diagonal", 1.5);
//			System.out.println("Diagonal /");
//		}
//		System.out.println("Ningún patrón");
//	}
}
