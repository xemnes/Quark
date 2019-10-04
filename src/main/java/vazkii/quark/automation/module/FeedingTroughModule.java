package vazkii.quark.automation.module;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.*;
import net.minecraftforge.common.util.FakePlayer;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.automation.block.FeedingTroughBlock;
import vazkii.quark.automation.tile.FeedingTroughTileEntity;
import vazkii.quark.base.module.*;

/**
 * @author WireSegal
 * Created at 9:48 AM on 9/20/19.
 */
@LoadModule(category = ModuleCategory.AUTOMATION)
public class FeedingTroughModule extends Module {
    public static TileEntityType<FeedingTroughTileEntity> tileEntityType;

    @Config
    public static int cooldown = 30;

    private static final double RANGE = 10;

    public static PlayerEntity temptWithTroughs(TemptGoal goal, PlayerEntity found) {
        if (!ModuleLoader.INSTANCE.isModuleEnabled(FeedingTroughModule.class) ||
                (found != null && (goal.isTempting(found.getHeldItemMainhand()) || goal.isTempting(found.getHeldItemOffhand()))))
            return found;

        if (!(goal.creature instanceof AnimalEntity) ||
                !((AnimalEntity) goal.creature).canBreed() ||
                ((AnimalEntity) goal.creature).getGrowingAge() != 0)
            return found;

        BlockPos rangeMin = new BlockPos(
                Math.floor(goal.creature.posX - RANGE),
                Math.floor(goal.creature.posY - RANGE),
                Math.floor(goal.creature.posZ - RANGE));
        BlockPos rangeMax = new BlockPos(
                Math.ceil(goal.creature.posX + RANGE),
                Math.ceil(goal.creature.posY + RANGE),
                Math.ceil(goal.creature.posZ + RANGE));

        double shortestDistanceSq = Double.MAX_VALUE;
        BlockPos location = null;
        FakePlayer target = null;

        for (BlockPos pos : BlockPos.getAllInBoxMutable(rangeMin, rangeMax)) {
            double distanceSq = pos.distanceSq(goal.creature.getPositionVector(), true);
            if (distanceSq <= RANGE * RANGE && distanceSq < shortestDistanceSq) {
                TileEntity tile = goal.creature.world.getTileEntity(pos);
                if (tile instanceof FeedingTroughTileEntity) {
                    FakePlayer foodHolder = ((FeedingTroughTileEntity) tile).getFoodHolder(goal);
                    if (foodHolder != null) {
                        shortestDistanceSq = distanceSq;
                        target = foodHolder;
                        location = pos.toImmutable();
                    }
                }
            }
        }

        if (target != null) {
            Vec3d eyesPos = new Vec3d(goal.creature.posX, goal.creature.posY + goal.creature.getEyeHeight(), goal.creature.posZ);
            Vec3d targetPos = new Vec3d(location).add(0.5, 0.0625, 0.5);
            BlockRayTraceResult ray = goal.creature.world.rayTraceBlocks(new RayTraceContext(eyesPos, targetPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, goal.creature));

            if (ray.getType() == RayTraceResult.Type.BLOCK && ray.getPos().equals(location))
                return target;
        }

        return found;
    }

    @Override
    public void construct() {
        Block feedingTrough = new FeedingTroughBlock("feeding_trough", this, ItemGroup.DECORATIONS,
                Block.Properties.create(Material.WOOD).hardnessAndResistance(0.6F).sound(SoundType.WOOD));
        tileEntityType = TileEntityType.Builder.create(FeedingTroughTileEntity::new, feedingTrough).build(null);
        RegistryHelper.register(tileEntityType, "feeding_trough");
    }
}
