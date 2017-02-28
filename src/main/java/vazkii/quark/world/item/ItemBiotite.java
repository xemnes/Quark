/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [18/04/2016, 17:37:41 (GMT)]
 */
package vazkii.quark.world.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.world.feature.UndergroundBiomes;
import vazkii.quark.world.feature.UndergroundBiomes.UndergroundBiomeInfo;
import vazkii.quark.world.world.underground.UndergroundBiome;

public class ItemBiotite extends ItemMod implements IQuarkItem {

	public ItemBiotite() {
		super("biotite");
		OreDictionary.registerOre("gemEnderBiotite", this);
		setCreativeTab(CreativeTabs.MATERIALS);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) { // TODO DEBUG
			UndergroundBiomeInfo biomeInfo = UndergroundBiomes.biomes.get(4);
			int radiusX = biomeInfo.minXSize + worldIn.rand.nextInt(biomeInfo.xVariation);
			int radiusY = biomeInfo.minYSize + worldIn.rand.nextInt(biomeInfo.yVariation);
			int radiusZ = biomeInfo.minZSize + worldIn.rand.nextInt(biomeInfo.zVariation);
			biomeInfo.biome.apply(worldIn, pos, radiusX, radiusY, radiusZ);
			
//			biomeInfo.biome.spawnDungeon((WorldServer) worldIn, pos, facing);
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

}
