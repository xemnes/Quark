package vazkii.quark.base.world;

public class WeightedGenerator implements Comparable<WeightedGenerator> {

	public final Generator generator;
	public final int weight;
	
	public WeightedGenerator(Generator generator, int weight) {
		this.generator = generator;
		this.weight = weight;
	}

	@Override
	public int compareTo(WeightedGenerator o) {
		return o.weight - weight;
	}
	
}
