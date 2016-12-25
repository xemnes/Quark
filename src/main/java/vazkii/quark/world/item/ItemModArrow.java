/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/07/2016, 03:48:20 (GMT)]
 */
package vazkii.quark.world.item;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.arl.interf.IVariantHolder;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.base.lib.LibMisc;

public class ItemModArrow extends ItemArrow implements IVariantHolder, IQuarkItem {

	private final String bareName;
	private final ArrowProvider provider; 

	public ItemModArrow(String name, ArrowProvider provider) {
		setUnlocalizedName(name);
		
		bareName = name;
		this.provider = provider;
		ItemMod.variantHolders.add(this);
	}
	
	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		if(provider == null)
			return super.createArrow(worldIn, stack, shooter);
		
		return provider.provide(worldIn, stack, shooter);
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
		return false;
	}
	
	@Override
	public Item setUnlocalizedName(String name) {
		super.setUnlocalizedName(name);
		GameRegistry.register(this, new ResourceLocation(LibMisc.PREFIX_MOD + name));

		return this;
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int dmg = par1ItemStack.getItemDamage();
		String[] variants = getVariants();

		String name;
		if(dmg >= variants.length)
			name = bareName;
		else name = variants[dmg];

		return "item." + LibMisc.PREFIX_MOD + name;
	}
	
	@Override
	public String[] getVariants() {
		return new String[] { bareName };
	}

	@Override
	public ItemMeshDefinition getCustomMeshDefinition() {
		return null;
	}

	public static interface ArrowProvider {
		public EntityArrow provide(World worldIn, ItemStack stack, EntityLivingBase shooter);
	}
	
}
