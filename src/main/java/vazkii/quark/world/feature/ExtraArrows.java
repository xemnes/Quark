/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/07/2016, 03:45:23 (GMT)]
 */
package vazkii.quark.world.feature;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.arl.util.RecipeHandler;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.client.render.RenderExtraArrow;
import vazkii.quark.world.entity.arrow.EntityArrowEnder;
import vazkii.quark.world.entity.arrow.EntityArrowExplosive;
import vazkii.quark.world.entity.arrow.EntityArrowTorch;
import vazkii.quark.world.item.ItemModArrow;

public class ExtraArrows extends Feature {

	public static Item arrow_ender;
	public static Item arrow_explosive;
	public static Item arrow_torch;

	boolean enableEnder, enableExplosive, enableTorch;
	
	public static double explosiveArrowPower;
	public static boolean explosiveArrowDestroysBlocks;
	
	@Override
	public void setupConfig() {
		enableEnder = loadPropBool("Enable Ender Arrow", "", true);
		enableExplosive = loadPropBool("Enable Explosive Arrow", "", true);
		enableTorch = loadPropBool("Enable Torch Arrow", "", true);
		
		explosiveArrowPower = loadPropDouble("Explosive Arrow Power", "", 2.0);
		explosiveArrowDestroysBlocks = loadPropBool("Explosive Arrow Destroys Blocks", "", true);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		if(enableEnder) {
			arrow_ender = new ItemModArrow("arrow_ender", (World worldIn, ItemStack stack, EntityLivingBase shooter) -> new EntityArrowEnder(worldIn, shooter));
			EntityRegistry.registerModEntity(EntityArrowEnder.class, "quark:arrow_ender", LibEntityIDs.ARROW_ENDER, Quark.instance, 64, 10, true);
			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(arrow_ender, new ArrowBehaviour((World world, IPosition pos) -> new EntityArrowEnder(world, pos)));
			RecipeHandler.addShapelessOreDictRecipe(new ItemStack(arrow_ender), new ItemStack(Items.ARROW), new ItemStack(Items.ENDER_PEARL));
		}
		
		if(enableExplosive) {
			arrow_explosive = new ItemModArrow("arrow_explosive", (World worldIn, ItemStack stack, EntityLivingBase shooter) -> new EntityArrowExplosive(worldIn, shooter));
			EntityRegistry.registerModEntity(EntityArrowExplosive.class, "quark:arrow_explosive", LibEntityIDs.ARROW_EXPLOSIVE, Quark.instance, 64, 10, true);
			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(arrow_explosive, new ArrowBehaviour((World world, IPosition pos) -> new EntityArrowExplosive(world, pos)));
			RecipeHandler.addShapelessOreDictRecipe(new ItemStack(arrow_explosive), new ItemStack(Items.ARROW), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.GUNPOWDER));
		}
		
		if(enableTorch) {
			arrow_torch = new ItemModArrow("arrow_torch", (World worldIn, ItemStack stack, EntityLivingBase shooter) -> new EntityArrowTorch(worldIn, shooter));
			EntityRegistry.registerModEntity(EntityArrowTorch.class, "quark:arrow_torch", LibEntityIDs.ARROW_TORCH, Quark.instance, 64, 10, true);
			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(arrow_torch, new ArrowBehaviour((World world, IPosition pos) -> new EntityArrowTorch(world, pos)));
			RecipeHandler.addShapelessOreDictRecipe(new ItemStack(arrow_torch), new ItemStack(Items.ARROW), new ItemStack(Blocks.TORCH));
		}
	}
	
	@Override
	public void preInitClient(FMLPreInitializationEvent event) {
		if(enableEnder)
			RenderingRegistry.registerEntityRenderingHandler(EntityArrowEnder.class, RenderExtraArrow.FACTORY_ENDER);
		
		if(enableExplosive)
			RenderingRegistry.registerEntityRenderingHandler(EntityArrowExplosive.class, RenderExtraArrow.FACTORY_EXPLOSIVE);
		
		if(enableTorch)
			RenderingRegistry.registerEntityRenderingHandler(EntityArrowTorch.class, RenderExtraArrow.FACTORY_TORCH);
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

	public static class ArrowBehaviour extends BehaviorProjectileDispense {

		ArrowProvider provider;
		
		public ArrowBehaviour(ArrowProvider provider) {
			this.provider = provider;
		}
		
		@Override
		protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
			EntityArrow arrow = provider.provide(worldIn, position);
			arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
			return arrow;
		}
		
		public static interface ArrowProvider {
			public EntityArrow provide(World world, IPosition pos);
		}

	}

}

