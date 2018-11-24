package vazkii.quark.oddities.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import vazkii.arl.item.ItemModArmor;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.base.lib.LibMisc;

public class ItemBackpack extends ItemModArmor implements IQuarkItem {

	private static final String WORN_TEXTURE = LibMisc.PREFIX_MOD + "textures/misc/backpack_worn.png";
	
	public ItemBackpack() {
		super("backpack", ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.CHEST);
		setCreativeTab(CreativeTabs.TOOLS);
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return WORN_TEXTURE;
	}

}
