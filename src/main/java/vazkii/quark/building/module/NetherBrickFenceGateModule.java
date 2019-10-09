package vazkii.quark.building.module;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import vazkii.quark.base.block.QuarkFenceGateBlock;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

/**
 * @author WireSegal
 * Created at 10:51 AM on 10/9/19.
 */
@LoadModule(category = ModuleCategory.BUILDING)
public class NetherBrickFenceGateModule extends Module {
    @Override
    public void construct() {
        new QuarkFenceGateBlock("nether_brick_fence_gate", this, ItemGroup.REDSTONE,
                Block.Properties.create(Material.ROCK, MaterialColor.NETHERRACK).hardnessAndResistance(2.0F, 6.0F));
    }
}
