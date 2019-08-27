package vazkii.quark.decoration.module;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.decoration.block.GrateBlock;

/**
 * @author WireSegal
 * Created at 8:57 AM on 8/27/19.
 */
@LoadModule(category = ModuleCategory.DECORATION)
public class GrateModule extends Module {
    @Override
    public void start() {
        new GrateBlock("grate", this, ItemGroup.DECORATIONS, Block.Properties.create(Material.IRON)
                .hardnessAndResistance(5, 10).sound(SoundType.METAL));
    }
}
