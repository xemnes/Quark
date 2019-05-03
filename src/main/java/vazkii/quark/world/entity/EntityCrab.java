package vazkii.quark.world.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCrab extends EntityAnimal {

    private boolean raving;
    private BlockPos jukeboxPosition;
	
	public EntityCrab(World worldIn) {
		super(worldIn);
	}

	@Override
    public void onLivingUpdate() {
        if(jukeboxPosition == null || jukeboxPosition.distanceSq(posX, posY, posZ) > 12.0D || world.getBlockState(jukeboxPosition).getBlock() != Blocks.JUKEBOX) {
            raving = false;
            jukeboxPosition = null;
        }

        super.onLivingUpdate();
    }

	@Override
	public void setPartying(BlockPos pos, boolean isPartying) {
        jukeboxPosition = pos;
        raving = isPartying;
	}
	
    public boolean isRaving() {
        return raving;
    }

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityCrab(world);
	}

}
