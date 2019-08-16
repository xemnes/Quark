package vazkii.quark.base.handler;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import vazkii.quark.base.Quark;

public class MiscUtil {

	public static final ResourceLocation GENERAL_ICONS = new ResourceLocation(Quark.MOD_ID, "textures/gui/general_icons.png");
	
	public static final Direction[] HORIZONTALS = new Direction[] {
			Direction.NORTH,
			Direction.SOUTH,
			Direction.WEST,
			Direction.EAST
	};
	
}
