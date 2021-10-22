package ruleta;

import java.util.Random;

public class Ruleta {
	private Random rng;
	private Rollo rollo1, rollo2, rollo3;
	private int giros1, giros2, giros3;

	public Ruleta() {
		rng = new Random();
		this.rollo1 = new Rollo(rng);
		this.rollo2 = new Rollo(rng);
		this.rollo3 = new Rollo(rng);
	}

	public void reset(int minGiros, int maxGiros) {
		double x = Math.random() * (maxGiros - minGiros) + minGiros;
		double y = Math.random() * (maxGiros - minGiros) + minGiros;
		double z = Math.random() * (maxGiros - minGiros) + minGiros;
		double max = Math.max(x, Math.max(y, z));
		double min = Math.min(x, Math.min(y, z));
		double mid = x + y + z - max - min;
		giros1 = (int) min;
		giros2 = (int) mid;
		giros3 = (int) max;
	}

	// TODO: En lugar de avanzar aleatoriamente, avanzan a la par pero con 3
	// cantidades de veces diferente
	public ItemRuleta[][] girar() {
		ItemRuleta[] i1 = rollo1.actual();
		ItemRuleta[] i2 = rollo2.actual();
		ItemRuleta[] i3 = rollo3.actual();
		if (giros1-- > 0)
			i1 = rollo1.siguiente();
		if (giros2-- > 0)
			i2 = rollo2.siguiente();
		if (giros3-- > 0)
			i3 = rollo3.siguiente();
		return new ItemRuleta[][] { new ItemRuleta[] { i1[0], i2[0], i3[0] }, new ItemRuleta[] { i1[1], i2[1], i3[1] },
				new ItemRuleta[] { i1[2], i2[2], i3[2] } };
	}

	public boolean isFinished() {
		return giros1 == giros2 && giros2 == giros3 && giros3 == 0;
	}

	public ItemRuleta[][] actual() {
		ItemRuleta[] i1 = rollo1.actual();
		ItemRuleta[] i2 = rollo2.actual();
		ItemRuleta[] i3 = rollo3.actual();
		return new ItemRuleta[][] { new ItemRuleta[] { i1[0], i2[0], i3[0] }, new ItemRuleta[] { i1[1], i2[1], i3[1] },
				new ItemRuleta[] { i1[2], i2[2], i3[2] } };
	}

	public Puntuacion getPuntuacion() {
		ItemRuleta[][] m = actual();
		if (m[1][0].equals(m[1][1]) && m[1][1].equals(m[1][2])) {
			return new Puntuacion("línea central" + (m[1][0].getValor() > 1 ? " de " + m[1][0].toString() : ""),
					6 * m[1][0].getValor());
		}
		if (m[0][0].equals(m[0][1]) && m[0][1].equals(m[0][2])) {
			return new Puntuacion("línea superior" + (m[0][0].getValor() > 1 ? " de " + m[0][0].toString() : ""),
					2.5 * m[0][0].getValor());
		}
		if (m[2][0].equals(m[2][1]) && m[2][1].equals(m[2][2])) {
			return new Puntuacion("línea inferior" + (m[2][0].getValor() > 1 ? " de " + m[2][0].toString() : ""),
					2.5 * m[2][0].getValor());
		}
		if (m[0][0].equals(m[1][1]) && m[1][1].equals(m[2][2])) {
			return new Puntuacion(
					"diagonal descendente [\\]" + (m[0][0].getValor() > 1 ? " de " + m[0][0].toString() : ""),
					2.5 * m[0][0].getValor());
		}
		if (m[2][0].equals(m[1][1]) && m[1][1].equals(m[0][2])) {
			return new Puntuacion(
					"diagonal ascendente [/]" + (m[2][0].getValor() > 1 ? " de " + m[2][0].toString() : ""),
					2.5 * m[2][0].getValor());
		}
		return new Puntuacion("mala suerte", 0);
	}

//	public static void main(String[] args) {
//		ItemRuleta[][] m = new ItemRuleta[][] {
//			new ItemRuleta[] {new ItemRuleta('A'), new ItemRuleta('A'), new ItemRuleta('A')},
//			new ItemRuleta[] {new ItemRuleta('B'), new ItemRuleta('A'), new ItemRuleta('A')},
//			new ItemRuleta[] {new ItemRuleta('A'), new ItemRuleta('B'), new ItemRuleta('A')},
//		};
//		if (m[0][0].equals(m[0][1]) && m[0][1].equals(m[0][2])) {
//			System.out.println("LS");
////			return new Puntuacion("L�nea superior", 3 * m[1][0].getValor());
//		}
//		if (m[1][0].equals(m[1][1]) && m[1][1].equals(m[1][2])) {
//			System.out.println("LM");
////			return new Puntuacion("L�nea central", 6 * m[1][0].getValor());
//		}
//		if (m[2][0].equals(m[2][1]) && m[2][1].equals(m[2][2])) {
//			System.out.println("LI");
////			return new Puntuacion("L�nea inferior", 3 * m[1][0].getValor());
//		}
//		if (m[0][0].equals(m[1][1]) && m[1][1].equals(m[2][2])) {
//			System.out.println("\\");
////			return new Puntuacion("Diagonal \\ ", 3 * m[0][0].getValor());
//		}
//		if (m[2][0].equals(m[1][1]) && m[1][1].equals(m[0][2])) {
//			System.out.println("/");
////			return new Puntuacion("Diagonal /", 3 * m[2][0].getValor());
//		}
//		System.out.println("Ning�n patr�n");
//	}
}
