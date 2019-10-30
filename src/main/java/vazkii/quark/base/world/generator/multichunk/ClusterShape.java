package vazkii.quark.base.world.generator.multichunk;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import vazkii.quark.base.world.config.ClusterSizeConfig;

public class ClusterShape {
	
	private final BlockPos src;
	private final Vec3d radius;
	private final Vec3d radiusSq;
	
	public ClusterShape(BlockPos src, Vec3d radius) {
		this.src = src;
		this.radius = radius;
		this.radiusSq = new Vec3d(radius.x * radius.x, radius.y * radius.y, radius.z * radius.z);
	}
	
	public boolean isInside(BlockPos pos) {
		int x = pos.getX() - src.getX();
		int y = pos.getY() - src.getY();
		int z = pos.getZ() - src.getZ();

		double distX = x * x;
		double distY = y * y;
		double distZ = z * z;
		double dist = distX / radiusSq.x + distY / radiusSq.y + distZ / radiusSq.z;
		boolean inside = dist <= 1;
		
		return inside;
	}

	public int getUpperBound() {
		return (int) Math.ceil(src.getY() + radius.getY());
	}
	
	public int getLowerBound() {
		return (int) Math.floor(src.getY() - radius.getY());
	}
	
	public static class Provider {
		
		private final ClusterSizeConfig sizeProvider;
		private final OctavesNoiseGenerator noiseGenerator;
		
		public Provider(ClusterSizeConfig provider, long seed) {
			this.sizeProvider = provider;
			noiseGenerator = new OctavesNoiseGenerator(new Random(seed), 12);
		}
		
		public ClusterShape around(BlockPos src) {
			Random rand = randAroundBlockPos(src);
			
			int radiusX = sizeProvider.horizontalSize + rand.nextInt(sizeProvider.horizontalVariation);
			int radiusY = sizeProvider.verticalSize + rand.nextInt(sizeProvider.verticalVariation);
			int radiusZ = sizeProvider.horizontalSize + rand.nextInt(sizeProvider.horizontalVariation);
					
			return new ClusterShape(src, new Vec3d(radiusX, radiusY, radiusZ));
		}
		
		public int getRadius() {
			return sizeProvider.horizontalSize + sizeProvider.horizontalVariation;
		}
		
		public Random randAroundBlockPos(BlockPos pos) {
			return new Random(31 * (31 * (31 + pos.getX()) + pos.getY()) + pos.getZ()); 
		}
		
	}
	
}