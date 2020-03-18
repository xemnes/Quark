package vazkii.quark.experimental.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import vazkii.quark.experimental.module.ToretoiseModule;
import vazkii.quark.mobs.entity.CrabEntity;

public class ToretoiseEntity extends AnimalEntity {

	public ToretoiseEntity(EntityType<? extends ToretoiseEntity> type, World world) {
		super(type, world);
	}
	
	@Override
	public AgeableEntity createChild(AgeableEntity arg0) {
		return new ToretoiseEntity(ToretoiseModule.toretoiseType, world); // TODO
	}

}
