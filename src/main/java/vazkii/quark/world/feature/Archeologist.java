package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.quark.base.Quark;
import vazkii.quark.base.handler.DimensionConfig;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.client.render.RenderArcheologist;
import vazkii.quark.world.entity.EntityArcheologist;
import vazkii.quark.world.item.ItemArcheologistHat;
import vazkii.quark.world.world.ArcheologistHouseGenerator;

import java.util.List;

public class Archeologist extends Feature {

	public static final ResourceLocation HOUSE_STRUCTURE = new ResourceLocation("quark", "archeologist_house");

	public static int chance, maxY, minY;
	public static DimensionConfig dims;
	
	public static Item archeologist_hat;
	
	public static boolean enableHat, sellHat, dropHat, hatIncreasesOreYield;
	public static float increaseChance;

	@Override
	public void setupConfig() {
		chance = loadPropInt("Chance Per Chunk", "The chance (1/N) that the generator will attempt to place an Archeologist per chunk. More = less spawns", 5);
		maxY = loadPropInt("Max Y", "", 50);
		minY = loadPropInt("Min Y", "", 20);
		dims = new DimensionConfig(configCategory);
		
		enableHat = loadPropBool("Enable Hat", "", true);
		sellHat = loadPropBool("Sell Hat", "Set to false to make the archaeologist not sell the hat", true);
		dropHat = loadPropBool("Drop Hat", "Set to false to make the archaeologist not drop the hat", true);
		hatIncreasesOreYield = loadPropBool("Hat Increases Ore Yield" , "Set to false to make the hat not increase ore yield", true);
		increaseChance = (float) loadPropDouble("Yield Increase Chance", "The chance for the hat to increase ore yield, 0 is 0%, 1 is 100%", 0.25);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		if(enableHat)
			archeologist_hat = new ItemArcheologistHat();
		
		String archeologistName = "quark:archeologist";
		EntityRegistry.registerModEntity(new ResourceLocation(archeologistName), EntityArcheologist.class, archeologistName, LibEntityIDs.ARCHEOLOGIST, Quark.instance, 80, 3, true, 0xb5966e, 0xb37b62);

		GameRegistry.registerWorldGenerator(new ArcheologistHouseGenerator(), 3000);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void preInitClient(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityArcheologist.class, RenderArcheologist.FACTORY);
	}
	
	@SubscribeEvent
	public void onDrops(HarvestDropsEvent event) {
		if(enableHat && hatIncreasesOreYield) {
			EntityPlayer player = event.getHarvester();
			if(player == null)
				return;
			
			ItemStack hat = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(hat.getItem() ==  archeologist_hat) {
				List<ItemStack> drops = event.getDrops();
				if(drops.size() == 1) {
					ItemStack drop = drops.get(0);
					if(!drop.isEmpty() && !(drop.getItem() instanceof ItemBlock) && drop.getCount() < drop.getMaxStackSize()) {
						IBlockState state = event.getState();
						Block block = state.getBlock();
						ItemStack stack = new ItemStack(block);
						if (!stack.isEmpty()) {
							int[] ids = OreDictionary.getOreIDs(stack);

							for (int i : ids) {
								String name = OreDictionary.getOreName(i);
								if (name.matches("^ore[A-Z][a-zA-Z]+$")) {
									if (player.world.rand.nextFloat() < increaseChance) {
										drop.grow(1);
										System.out.println("GROWN " + drop);
									}

									break;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
