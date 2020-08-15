package ruleta;

import java.util.ArrayList;
import java.util.Random;

public class Rollo extends ArrayList<ItemRuleta> {
	private static final long serialVersionUID = 2465039072523553802L;
	
	private Random rng;
	private int indice = 0;

	private Rollo(ItemRuleta[] array, Random rng) {
		super(array.length);
		for (ItemRuleta itemRuleta : array) {
			add(itemRuleta);
		}
		indice = rng.nextInt(array.length);
		this.rng = rng;
	}
	
	public Rollo(Random rng) {
		this(ItemRuleta.values(), rng);
	}
	
	public ItemRuleta[] siguiente() {
		if (rng.nextDouble() < 0.7) {
			indice++;
		}
		return new ItemRuleta[] {get(indice+2), get(indice+1), get(indice)};
	}
	
	public ItemRuleta[] actual() {
		return new ItemRuleta[] {get(indice+2), get(indice+1), get(indice)};
	}
	
	@Override
	public ItemRuleta get(int index) {
		return super.get(index % size());
	}
}
