package vazkii.quark.base.asm;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.automation.client.render.PistonTileEntityRenderer;
import vazkii.quark.automation.feature.ChainLinkage;
import vazkii.quark.automation.feature.PistonSpikes;
import vazkii.quark.automation.feature.PistonsMoveTEs;
import vazkii.quark.automation.feature.PistonsPushPullItems;
import vazkii.quark.client.feature.BetterFireEffect;
import vazkii.quark.client.feature.ItemsFlashBeforeExpiring;
import vazkii.quark.client.feature.RenderItemsInChat;
import vazkii.quark.client.feature.ShowInvalidSlots;
import vazkii.quark.decoration.feature.IronLadders;
import vazkii.quark.decoration.feature.MoreBannerLayers;
import vazkii.quark.experimental.features.BetterNausea;
import vazkii.quark.experimental.features.CollateralPistonMovement;
import vazkii.quark.experimental.features.ColoredLights;
import vazkii.quark.management.feature.BetterCraftShifting;
import vazkii.quark.misc.feature.*;
import vazkii.quark.tweaks.feature.HoeSickle;
import vazkii.quark.tweaks.feature.ImprovedSleeping;
import vazkii.quark.tweaks.feature.SpringySlime;
import vazkii.quark.vanity.client.emotes.EmoteHandler;
import vazkii.quark.vanity.client.render.BoatBannerRenderer;
import vazkii.quark.vanity.feature.BoatSails;
import vazkii.quark.vanity.feature.WitchHat;
import vazkii.quark.world.client.render.ChainRenderer;

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
	
	// ===== BOAT SAILS & CHAIN LINKAGE ===== //

	@SideOnly(Side.CLIENT)
	public static void renderChain(Render render, double x, double y, double z, Entity entity, float partTicks) {
		ChainRenderer.renderChain(render, x, y, z, entity, partTicks);
	}
	
	public static void onBoatUpdate(EntityBoat boat) {
		BoatSails.onBoatUpdate(boat);
		ChainLinkage.onBoatUpdate(boat);
	}
	
	public static void boatDrops(EntityBoat boat) {
		BoatSails.dropBoatBanner(boat);
		ChainLinkage.drop(boat);
	}

	public static void minecartDrops(EntityMinecart minecart) {
		ChainLinkage.drop(minecart);
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
	public static void renderPistonBlock(BlockPos pos, IBlockState state, BufferBuilder buffer, World world) {
		PistonTileEntityRenderer.renderPistonBlock(pos, state, buffer, world);
	}
	
	// ===== PISTONS PUSH/PULL ITEMS ===== //
	
	public static void onPistonUpdate(TileEntityPiston piston) {
		PistonsPushPullItems.onPistonUpdate(piston);
	}
	
	// ===== IMPROVED SLEEPING ===== //

	public static int isEveryoneAsleep(World world) {
		return ImprovedSleeping.isEveryoneAsleep(world);
	}

	public static void whenPlayersWake(WorldServer world) {
		ImprovedSleeping.whenNightPasses(world);
	}
	
	// ===== COLORED LIGHTS ===== //
	
	@SideOnly(Side.CLIENT)
	public static void putColorsFlat(IBlockAccess world, IBlockState state, BlockPos pos, BufferBuilder buffer, BakedQuad quad, int brightness) {
		ColoredLights.putColorsFlat(world, state, pos, buffer, quad, brightness);
	}
	
	// ===== MORE BANNER LAYERS ===== //
	public static int shiftLayerCountDown(int amount) {
		return amount + 6 - MoreBannerLayers.getLayerCount();
	}

	public static int shiftLayerCountUp(int amount) {
		return amount - 6 + MoreBannerLayers.getLayerCount();
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

	// ===== EXTRA POTIONS ==== //
	@SideOnly(Side.CLIENT)
	public static boolean renderBeaconButton(GuiButton button, GuiBeacon beacon, Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
		return ExtraPotions.renderPotion(button, beacon, minecraft, mouseX, mouseY, partialTicks);
	}

	// ===== BETTER NAUSEA ==== //
	@SideOnly(Side.CLIENT)
	public static void renderNausea(EntityRenderer renderer, float partialTicks) {
		BetterNausea.renderNausea(renderer, partialTicks);
	}

	// ===== PICKARANG ==== //
	public static boolean canSharpnessApply(ItemStack stack) {
		return Pickarang.canSharpnessApply(stack);
	}

	// ===== RENDER ITEMS IN CHAT ==== //
	public static ITextComponent createStackComponent(ITextComponent component) {
		return RenderItemsInChat.createStackComponent(component);
	}

	// ===== HOE SICKLES ==== //
	public static boolean canFortuneApply(Enchantment enchantment, ItemStack stack) {
		return HoeSickle.canFortuneApply(enchantment, stack);
	}

	// ===== IRON LADDERS ==== //
	public static boolean isBlockNotBrokenByWater(Block block) {
		return IronLadders.isBlockNotBrokenByWater(block);
	}

	// ===== ANCIENT TOMES ==== //
	public static NBTTagList getEnchantmentsForStack(NBTTagList previous, ItemStack stack) {
		return AncientTomes.getEnchantmentsForStack(previous, stack);
	}

	// ===== PARROT EGGS ==== //
	public static ResourceLocation replaceParrotTexture(ResourceLocation previous, NBTTagCompound parrot) {
		return ParrotEggs.getTextureForParrot(previous, parrot);
	}
	
}
