package vazkii.quark.automation.module;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.automation.block.FeedingTroughBlock;
import vazkii.quark.automation.tile.FeedingTroughTileEntity;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.ModuleLoader;

/**
 * @author WireSegal
 * Created at 9:48 AM on 9/20/19.
 */
@LoadModule(category = ModuleCategory.AUTOMATION)
public class FeedingTroughModule extends Module {
    public static TileEntityType<FeedingTroughTileEntity> tileEntityType;

    private static final double RANGE = 10;

    public static PlayerEntity temptWithTroughs(TemptGoal goal, PlayerEntity found) {
        if (!ModuleLoader.INSTANCE.isModuleEnabled(FeedingTroughModule.class) ||
                (found != null && (goal.isTempting(found.getHeldItemMainhand()) || goal.isTempting(found.getHeldItemOffhand()))))
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
                    }
                }
            }
        }

        return target == null ? found : target;
    }

    @Override
    public void start() {
        Block feedingTrough = new FeedingTroughBlock("feeding_trough", this, ItemGroup.DECORATIONS,
                Block.Properties.create(Material.WOOD).hardnessAndResistance(0.6F).sound(SoundType.WOOD));
        tileEntityType = TileEntityType.Builder.create(FeedingTroughTileEntity::new, feedingTrough).build(null);
        RegistryHelper.register(tileEntityType, "feeding_trough");
    }
}
