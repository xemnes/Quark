package vazkii.quark.base.asm;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.automation.feature.PistonSpikes;
import vazkii.quark.vanity.client.emotes.base.EmoteHandler;
import vazkii.quark.vanity.client.render.BoatBannerRenderer;
import vazkii.quark.vanity.feature.BoatSails;
import vazkii.quark.world.feature.ColorRunes;

public final class ASMHooks {

	// ===== EMOTES ===== //
	
	public static void updateEmotes(Entity e) {
		EmoteHandler.updateEmotes(e);
	}

	// ===== COLOR RUNES ===== //
	
	public static void setColorRuneTargetStack(ItemStack stack) {
		ColorRunes.setTargetStack(stack);
	}
	
	public static int getRuneColor() {
		return ColorRunes.getColor();
	}
	
	public static void applyRuneColor(float f1, float f2, float f3, float f4) {
		ColorRunes.applyColor(f1, f2, f3, f4);
	}
	
	// ===== BOAT SAILS ===== //
	
	public static void onBoatUpdate(EntityBoat boat) {
		BoatSails.onBoatUpdate(boat);
	}
	
	public static void dropBoatBanner(EntityBoat boat) {
		BoatSails.dropBoatBanner(boat);
	}
	
	@SideOnly(Side.CLIENT)
	public static void renderBannerOnBoat(EntityBoat boat, float pticks) {
		BoatBannerRenderer.renderBanner(boat, pticks);
	}

	// ===== PISTON BLOCK BREAKERS ===== //
	
	public static boolean breakStuffWithSpikes(World world, List<BlockPos> moveList, List<BlockPos> destroyList, EnumFacing facing, boolean extending) {
		return PistonSpikes.breakStuffWithSpikes(world, moveList, destroyList, facing, extending);
	}	
	
}

	