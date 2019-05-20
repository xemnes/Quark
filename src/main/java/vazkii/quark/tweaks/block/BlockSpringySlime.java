/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [May 20, 2019, 10:26 AM (EST)]
 */
package vazkii.quark.tweaks.block;

import net.minecraft.block.BlockSlime;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;

public class BlockSpringySlime extends BlockSlime {
	public BlockSpringySlime() {
		setSoundType(SoundType.SLIME);
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
		if (entity instanceof EntityArrow) {
			EnumFacing sideHit = EnumFacing.getFacingFromVector(
					(float) (entity.posX + entity.motionX) - (pos.getX() + 0.5f),
					(float) (entity.posY + entity.motionY) - (pos.getY() + 0.5f),
					(float) (entity.posZ + entity.motionZ) - (pos.getZ() + 0.5f));

			switch (sideHit.getAxis()) {
				case X:
					if (Math.abs(entity.motionX) < 0.1)
						return;
					entity.motionX = 0.8 * Math.min(Math.abs(entity.motionX), 0.25) * sideHit.getXOffset();
					break;
				case Y:
					if (Math.abs(entity.motionY) < 0.1)
						return;
					entity.motionY = 0.8 * Math.min(Math.abs(entity.motionY), 0.25) * sideHit.getYOffset();
					break;
				case Z:
					if (Math.abs(entity.motionZ) < 0.1)
						return;
					entity.motionZ = 0.8 * Math.min(Math.abs(entity.motionZ), 0.25) * sideHit.getZOffset();
					break;
			}

			// inGround
			ObfuscationReflectionHelper.setPrivateValue(EntityArrow.class, (EntityArrow) entity, false, "field_70254_i");
		}
	}

	@Override
	public void onLanded(@Nonnull World worldIn, Entity entityIn) {
		// Override slime block behavior, as it's handled in SpringySlime
		entityIn.motionY = 0.0;
	}
}
