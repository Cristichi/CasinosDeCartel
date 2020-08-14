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

	public String[] girar() {
		return new String[] { rollo1.toStringSiguiente(), rollo2.toStringSiguiente(), rollo3.toStringSiguiente() };
	}

	public String[] actual() {
		return new String[] { rollo1.toStringActual(), rollo2.toStringActual(), rollo3.toStringActual() };
	}
}
