package vazkii.quark.decoration.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityLeashKnot2TheKnotting extends EntityLiving {

	public EntityLeashKnot2TheKnotting(World worldIn) {
		super(worldIn);
		setNoAI(true);
		width = 6F / 16F;
		height = 0.5F;
	}
	
}
