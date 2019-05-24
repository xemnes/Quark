package vazkii.quark.decoration.entity;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityLeashKnot2TheKnotting extends EntityLiving {

	public EntityLeashKnot2TheKnotting(World worldIn) {
		super(worldIn);
		setNoAI(true);
		width = 6F / 16F;
		height = 0.5F;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		IBlockState state = world.getBlockState(new BlockPos(posX, posY, posZ));
		if(!(state.getBlock() instanceof BlockFence)) {
			dismantle();
		} else {
			Entity holder = getLeashHolder();
			if(holder == null || holder.isDead)
				dismantle();
			else if(holder.posY < posY && holder instanceof EntityLeashKnot) {
				double targetX = holder.posX;
				double targetY = holder.posY;
				double targetZ = holder.posZ;
				holder.posX = posX;
				holder.posY = posY;
				holder.posZ = posZ;
				posX = targetX;
				posY = targetY;
				posZ = targetZ;
			}
		}
	}
	
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {
			Entity holder = getLeashHolder();
			holder.setDead();
			dismantle();
			if(!player.isCreative())
				dropItem(Items.LEAD, 1);
		}
		
		return true;
	}
	
	private void dismantle() {
		setDead();
		world.playSound(null, getPosition(), SoundEvents.ENTITY_LEASHKNOT_BREAK, SoundCategory.BLOCKS, 1F, 1F);
	}

}
