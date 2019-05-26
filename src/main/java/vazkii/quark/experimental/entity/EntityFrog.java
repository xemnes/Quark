package vazkii.quark.experimental.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import vazkii.quark.base.sounds.QuarkSounds;

public class EntityFrog extends EntityLiving {

	public int spawnCd;
	
	public EntityFrog(World worldIn) {
		super(worldIn);
		spawnCd = -1;
	}
	
	@Override
	public void onEntityUpdate() {
		if(spawnCd > 0) {
			spawnCd--;
			if(spawnCd == 0 && !world.isRemote) {
				float multiplier = 0.8F;
				EntityFrog newFrog = new EntityFrog(world);
				newFrog.setPosition(posX, posY, posZ);
				newFrog.motionX = (Math.random() - 0.5) * multiplier;
				newFrog.motionY = (Math.random() - 0.5) * multiplier;
				newFrog.motionZ = (Math.random() - 0.5) * multiplier;
				world.spawnEntity(newFrog);
				newFrog.spawnCd = 2;
			}
		}
		
		super.onEntityUpdate();
	}
	
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		spawnCd = 50;
		if(!world.isRemote)
			world.playSound(null, posX, posY, posZ, QuarkSounds.ENTITY_FROG_WEDNESDAY, SoundCategory.NEUTRAL, 1F, 1F);
		
		return true;
	}

}
