package vazkii.quark.world.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemModArmor;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.world.client.model.ModelArcheologistHat;

import javax.annotation.Nonnull;

public class ItemArcheologistHat extends ItemModArmor implements IQuarkItem {

	public static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/entity/archeologist_hat.png");
	
	@SideOnly(Side.CLIENT)
	public static ModelBiped headModel;

	public ItemArcheologistHat() {
		super("archeologist_hat", ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if(headModel == null)
			headModel = new ModelArcheologistHat();

		return headModel;
	}

	@Override
	public boolean hasColor(@Nonnull ItemStack stack) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return TEXTURE.toString();
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

}
