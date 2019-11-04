package vazkii.quark.vanity.item;

import java.awt.Color;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.base.module.Module;

public class RainbowRuneItem extends RuneItem implements IItemColorProvider {

	public RainbowRuneItem(Module module) {
		super("rainbow_rune", module, 0);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public int getRuneColor(ItemStack stack) {
		return Color.HSBtoRGB(ClientTicker.total * 0.005F, 1F, 0.6F);
	}

	@Override
	public IItemColor getItemColor() {
		return (stack, i) -> i == 1 ? getRuneColor(stack) : 0xFFFFFF;
	}

}
