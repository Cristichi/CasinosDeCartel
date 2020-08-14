package ruleta;

import java.util.ArrayList;
import java.util.Random;

public class Rollo extends ArrayList<ItemRuleta> {
	private static final long serialVersionUID = 2465039072523553802L;
	
	private int indice = 0;

	private Rollo(ItemRuleta[] array, Random rng) {
		super(array.length);
		for (ItemRuleta itemRuleta : array) {
			add(itemRuleta);
		}
		indice = rng.nextInt(array.length);
	}
	
	public Rollo(Random rng) {
		this(new ItemRuleta[] {
				new ItemRuleta('A'),
				new ItemRuleta('B'),
				new ItemRuleta('C'),
				new ItemRuleta('7'),
		}, rng);
//		this(ItemRuleta.values(), rng);
	}
	
	public ItemRuleta[] siguiente() {
		return new ItemRuleta[] {get(indice++), get(indice+1), get(indice+2)};
	}
	
	public ItemRuleta[] actual() {
		return new ItemRuleta[] {get(indice), get(indice+1), get(indice+2)};
	}
	
	@Override
	public ItemRuleta get(int index) {
		return super.get(index % size());
	}

	public String toStringSiguiente() {
		return "- [" + get(indice++) + "][" + get(indice+1) + "][" + get(indice+2) + "] -";
	}

	public String toStringActual() {
		return "- [" + get(indice) + "][" + get(indice+1) + "][" + get(indice+2) + "] -";
	}
}
