/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [May 23, 2019, 16:18 AM (EST)]
 */
package vazkii.quark.world.entity;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import vazkii.quark.base.Quark;

import java.util.List;

import static vazkii.quark.world.module.NewStoneTypesModule.*;

public enum EnumStonelingVariant implements ILivingEntityData {
	STONE("stoneling", Blocks.COBBLESTONE, Blocks.STONE),
	ANDESITE("stoneling_andesite", Blocks.ANDESITE, Blocks.POLISHED_ANDESITE),
	DIORITE("stoneling_diorite", Blocks.DIORITE, Blocks.POLISHED_DIORITE),
	GRANITE("stoneling_granite", Blocks.GRANITE, Blocks.POLISHED_GRANITE),
	LIMESTONE("stoneling_limestone", limestoneBlock, polishedBlocks.get(limestoneBlock)),
	BASALT("stoneling_basalt", basaltBlock, polishedBlocks.get(basaltBlock)),
	MARBLE("stoneling_marble", marbleBlock, polishedBlocks.get(marbleBlock));

	private final ResourceLocation texture;
	private final List<Block> blocks;

	EnumStonelingVariant(String variantPath, Block... blocks) {
		this.texture = new ResourceLocation(Quark.MOD_ID, "textures/model/entity/" + variantPath + ".png");
		this.blocks = Lists.newArrayList(blocks);
	}

	public static EnumStonelingVariant byIndex(byte index) {
		EnumStonelingVariant[] values = values();
		return values[MathHelper.clamp(index, 0, values.length - 1)];
	}

	public byte getIndex() {
		return (byte) ordinal();
	}

	public ResourceLocation getTexture() {
		return texture;
	}

	public List<Block> getBlocks() {
		return blocks;
	}
}
