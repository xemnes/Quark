/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [18/03/2016, 22:34:52 (GMT)]
 */
package vazkii.quark.world;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.quark.base.module.Module;
import vazkii.quark.world.feature.*;

public class QuarkWorld extends Module {

	@Override
	public void addFeatures() {
		registerFeature(new Basalt());
		registerFeature(new ClayGeneration(), "Generate clay underground like dirt");
		registerFeature(new OceanGuardians(), "Guardians spawn in oceans");
		registerFeature(new NaturalBlazesInNether(), "Blazes spawn naturally in the nether");
		registerFeature(new MushroomsInSwamps(), "Big mushrooms generate in swamps");
		registerFeature(new Biotite());
		registerFeature(new BuriedTreasure());
		registerFeature(new DepthMobs());
		registerFeature(new PirateShips());
		registerFeature(new Wraiths());
		registerFeature(new RevampStoneGen());
		registerFeature(new CrystalCaves());
		registerFeature(new VariedDungeons());
		registerFeature(new UndergroundBiomes());
		registerFeature(new PathfinderMaps());
		registerFeature(new NetherSmoker());
		registerFeature(new NetherFossils());
		registerFeature(new Speleothems());
		registerFeature(new FairyRings());
		registerFeature(new CaveRoots());
		registerFeature(new Archaeologist());
		registerFeature(new MonsterBoxes());
		registerFeature(new Stonelings());
		registerFeature(new Frogs());
		registerFeature(new Foxhounds());

		registerFeature(new RealisticWorldType());
		registerFeature(new DefaultWorldOptions());
	}
	
	@Override
	public ItemStack getIconStack() {
		return new ItemStack(Blocks.GRASS);
	}

}
