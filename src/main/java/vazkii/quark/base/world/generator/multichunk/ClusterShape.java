package vazkii.quark.base.world.generator.multichunk;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import vazkii.quark.base.world.config.ClusterSizeConfig;

public class ClusterShape {
	
	private final BlockPos src;
	private final Vec3d radius;
	private final PerlinNoiseGenerator noiseGenerator;
	
	public ClusterShape(BlockPos src, Vec3d radius, PerlinNoiseGenerator noiseGenerator) {
		this.src = src;
		this.radius = radius;
		this.noiseGenerator = noiseGenerator;
	}
	
	public boolean isInside(BlockPos pos) {
		// normalize distances by the radius 
		double dx = (double) (pos.getX() - src.getX()) / radius.x;
		double dy = (double) (pos.getY() - src.getY()) / radius.y;
		double dz = (double) (pos.getZ() - src.getZ()) / radius.z;
		
		// convert to spherical
		double r = Math.sqrt(dx * dx + dy * dy + dz * dz);
		if(r == 0)
			return true; // special case the center 
		
		double phi = Math.atan2(dz, dx);
		double theta = r == 0 ? 0 : Math.acos(dy / r);
		
		double xn = Math.sin((phi + Math.PI) / 2) * Math.PI + src.getX();
		double yn = theta + src.getZ();
		double maxR = (noiseGenerator.getValue(xn, yn) / 16.0) + 0.5;
		
		return r < maxR;
	}

	public int getUpperBound() {
		return (int) Math.ceil(src.getY() + radius.getY());
	}
	
	public int getLowerBound() {
		return (int) Math.floor(src.getY() - radius.getY());
	}
	
	public static class Provider {
		
		private final ClusterSizeConfig sizeProvider;
		private final PerlinNoiseGenerator noiseGenerator;
		
		public Provider(ClusterSizeConfig provider, long seed) {
			this.sizeProvider = provider;
			noiseGenerator = new PerlinNoiseGenerator(new Random(seed), 4);
		}
		
		public ClusterShape around(BlockPos src) {
			Random rand = randAroundBlockPos(src);
			
			int radiusX = sizeProvider.horizontalSize + rand.nextInt(sizeProvider.horizontalVariation);
			int radiusY = sizeProvider.verticalSize + rand.nextInt(sizeProvider.verticalVariation);
			int radiusZ = sizeProvider.horizontalSize + rand.nextInt(sizeProvider.horizontalVariation);
					
			return new ClusterShape(src, new Vec3d(radiusX, radiusY, radiusZ), noiseGenerator);
		}
		
		public int getRadius() {
			return sizeProvider.horizontalSize + sizeProvider.horizontalVariation;
		}
		
		public Random randAroundBlockPos(BlockPos pos) {
			return new Random(31 * (31 * (31 + pos.getX()) + pos.getY()) + pos.getZ()); 
		}
		
	}
	
}