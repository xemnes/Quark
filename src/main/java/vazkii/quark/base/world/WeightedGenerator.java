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
		int diff = o.weight - weight;
		if(diff != 0)
			return diff;
		
		return o.hashCode() - hashCode();
	}
	
	@Override
	public int hashCode() {
		return generator.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof WeightedGenerator && ((WeightedGenerator) obj).generator == generator); 
	}
	
}
