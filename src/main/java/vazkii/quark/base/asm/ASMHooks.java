package vazkii.quark.base.asm;

import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.automation.client.render.PistonTileEntityRenderer;
import vazkii.quark.automation.feature.PistonSpikes;
import vazkii.quark.automation.feature.PistonsMoveTEs;
import vazkii.quark.automation.feature.PistonsPushPullItems;
import vazkii.quark.client.feature.BetterFireEffect;
import vazkii.quark.client.feature.ItemsFlashBeforeExpiring;
import vazkii.quark.client.feature.ShowInvalidSlots;
import vazkii.quark.decoration.feature.MoreBannerLayers;
import vazkii.quark.experimental.features.CollateralPistonMovement;
import vazkii.quark.experimental.features.ColoredLights;
import vazkii.quark.management.feature.BetterCraftShifting;
import vazkii.quark.misc.feature.ColorRunes;
import vazkii.quark.tweaks.feature.ImprovedSleeping;
import vazkii.quark.tweaks.feature.SpringySlime;
import vazkii.quark.vanity.client.emotes.EmoteHandler;
import vazkii.quark.vanity.client.render.BoatBannerRenderer;
import vazkii.quark.vanity.feature.BoatSails;
import vazkii.quark.vanity.feature.WitchHat;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public final class ASMHooks {

	// ===== EMOTES ===== //
	
	public static void updateEmotes(Entity e) {
		EmoteHandler.updateEmotes(e);
	}

	// ===== COLOR RUNES ===== //
	
	public static void setColorRuneTargetStack(EntityLivingBase entity, EntityEquipmentSlot slot) {
		ColorRunes.setTargetStack(entity, slot);
	}

	public static void setColorRuneTargetStack(ItemStack stack) {
		ColorRunes.setTargetStack(stack);
	}
	
	public static int getRuneColor(int original) {
		return ColorRunes.getColor(original);
	}
	
	public static void applyRuneColor() {
		ColorRunes.applyColor();
	}
	
	// ===== BOAT SAILS ===== //
	
	public static void onBoatUpdate(EntityBoat boat) {
		BoatSails.onBoatUpdate(boat);
	}
	
	public static void dropBoatBanner(EntityBoat boat) {
		BoatSails.dropBoatBanner(boat);
	}
	
	@SideOnly(Side.CLIENT)
	public static void renderBannerOnBoat(EntityBoat boat, float partial) {
		BoatBannerRenderer.renderBanner(boat, partial);
	}

	// ===== PISTON BLOCK BREAKERS & PISTONS MOVE TES & COLLATERAL PISTON MOVEMENT ===== //
	
	public static void onPistonMove(World world, BlockPos sourcePos, BlockPistonStructureHelper helper, EnumFacing facing, boolean extending) {
		EnumFacing realFacing = extending ? facing : facing.getOpposite();
		
		PistonSpikes.breakStuffWithSpikes(world, sourcePos, helper, realFacing, extending);
		CollateralPistonMovement.applyCollateralMovements(world, helper, realFacing, extending);
		PistonsMoveTEs.detachTileEntities(world, helper, realFacing);
	}	
	
	// ===== BETTER CRAFT SHIFTING ===== //
	
	public static int getMaxInventoryBoundaryCrafting(int min, int max) {
		return BetterCraftShifting.getMaxInventoryBoundaryCrafting(min, max);
	}
	
	public static int getMaxInventoryBoundaryVillager(int min, int max) {
		return BetterCraftShifting.getMaxInventoryBoundaryVillager(min, max);
	}

	public static int getMinInventoryBoundaryCrafting(int min, int max) {
		return BetterCraftShifting.getMinInventoryBoundaryCrafting(min, max);
	}

	public static int getMinInventoryBoundaryVillager(int min, int max) {
		return BetterCraftShifting.getMinInventoryBoundaryVillager(min, max);
	}
	
	// ===== PISTONS MOVE TES ===== //
	
	public static boolean shouldPistonMoveTE(boolean te, IBlockState state) {
		return PistonsMoveTEs.shouldMoveTE(te, state);
	}
	
	public static boolean setPistonBlock(World world, BlockPos pos, IBlockState state, int flags) {
		return PistonsMoveTEs.setPistonBlock(world, pos, state, flags);
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean renderPistonBlock(BlockPos pos, IBlockState state, BufferBuilder buffer, World world, boolean checkSides) {
		return PistonTileEntityRenderer.renderPistonBlock(pos, state, buffer, world, checkSides);
	}
	
	// ===== PISTONS PUSH/PULL ITEMS ===== //
	
	public static void onPistonUpdate(TileEntityPiston piston) {
		PistonsPushPullItems.onPistonUpdate(piston);
	}
	
	// ===== IMPROVED SLEEPING ===== //

	public static int isEveryoneAsleep(World world) {
		return ImprovedSleeping.isEveryoneAsleep(world);
	}
	
	// ===== COLORED LIGHTS ===== //
	
	@SideOnly(Side.CLIENT)
	public static void putColorsFlat(IBlockAccess world, IBlockState state, BlockPos pos, BufferBuilder buffer, BakedQuad quad, int brightness) {
		ColoredLights.putColorsFlat(world, state, pos, buffer, quad, brightness);
	}
	
	// ===== MORE BANNER LAYERS ===== //
	public static int shiftLayerCount(int amount) {
		return amount + 6 - MoreBannerLayers.getLayerCount();
	}

	// ===== BETTER FIRE EFFECT ==== //
	public static boolean renderFire(Entity entity, double x, double y, double z) {
		return BetterFireEffect.renderFire(entity, x, y, z);
	}

	// ===== WITCH HATS ==== //
	public static boolean hasWitchHat(EntityLiving attacker, @Nullable EntityLivingBase target) {
		return WitchHat.hasWitchHat(attacker, target);
	}

	// ===== INVALID SLOTS ==== //

	@SideOnly(Side.CLIENT)
	public static void drawInvalidSlotOverlays(GuiContainer container) {
		ShowInvalidSlots.renderElements(container);
	}

	// ===== SPRINGY SLIME ==== //
	public static void applyCollisionLogic(Entity entity, double attemptedX, double attemptedY, double attemptedZ, double dX, double dY, double dZ) {
		SpringySlime.onEntityCollision(entity, attemptedX, attemptedY, attemptedZ, dX, dY, dZ);
	}

	public static void recordMotion(Entity entity) {
		SpringySlime.recordMotion(entity);
	}

	// ===== ITEMS FLASH BEFORE EXPIRING ==== //
	public static void ensureUpdatedItemAge(EntityItem entityItem) {
		ItemsFlashBeforeExpiring.updateItemInfo(entityItem);
	}
	
}
