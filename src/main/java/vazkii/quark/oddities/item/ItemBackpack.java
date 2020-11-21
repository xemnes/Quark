package vazkii.quark.oddities.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.handler.ProxiedItemStackHandler;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.oddities.client.model.ModelBackpack;
import vazkii.quark.oddities.feature.Backpacks;

import javax.annotation.Nonnull;

import static vazkii.quark.oddities.feature.Backpacks.backpack;

public class ItemBackpack extends Item implements IBauble, IQuarkItem, IItemColorProvider, IRenderBauble {
	private static final String WORN_TEXTURE = LibMisc.PREFIX_MOD + "textures/misc/backpack_worn.png";
	private static final String WORN_OVERLAY_TEXTURE = LibMisc.PREFIX_MOD + "textures/misc/backpack_worn_overlay.png";
	
	private static final ResourceLocation WORN_TEXTURE_RL = new ResourceLocation(WORN_TEXTURE);
	private static final ResourceLocation WORN_OVERLAY_TEXTURE_RL = new ResourceLocation(WORN_OVERLAY_TEXTURE);
	
	public static String bareName = "backpack";
	
	public static ModelBackpack model;
	
	public ItemBackpack() {
		setTranslationKey(bareName);
		setCreativeTab(CreativeTabs.TOOLS);
		setMaxDamage(0);
		
		addPropertyOverride(new ResourceLocation("has_items"), (stack, world, entity) -> (!Backpacks.superOpMode && doesBackpackHaveItems(stack)) ? 1 : 0);
	}
	
	public static boolean doesBackpackHaveItems(ItemStack stack) {
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (handler == null) return false;
		for (int i = 0; i < handler.getSlots(); i++)
			if (!handler.getStackInSlot(i).isEmpty()) return true;
		
		return false;
	}
	
//	@Nonnull
//	@Override
//	public Multimap<String, AttributeModifier> getItemAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot) {
//		return HashMultimap.create();
//	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (worldIn.isRemote) return;
		
		boolean hasItems = !Backpacks.superOpMode && doesBackpackHaveItems(stack);
		
		if (hasItems && !Backpacks.isEntityWearingBackpack(entityIn, stack)) {
			ItemStack copy = stack.copy();
			stack.setCount(0);
			entityIn.entityDropItem(copy, 0);
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		if (Backpacks.superOpMode || entityItem.world.isRemote) return false;
		
		ItemStack stack = entityItem.getItem();
		
		if (!ItemNBTHelper.detectNBT(stack)) return false;
		
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (handler == null) return false;
		
		for (int i = 0; i < handler.getSlots(); i++) {
			ItemStack stackAt = handler.getStackInSlot(i);
			if (!stackAt.isEmpty()) {
				ItemStack copy = stackAt.copy();
				InventoryHelper.spawnItemStack(entityItem.world, entityItem.posX, entityItem.posY, entityItem.posZ, copy);
			}
		}
		
		NBTTagCompound comp = ItemNBTHelper.getNBT(stack);
		comp.removeTag("Inventory");
		if (comp.getSize() == 0) stack.setTagCompound(null);
		
		return false;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return false;
	}
	
	@Nonnull
	@Override
	public IRarity getForgeRarity(@Nonnull ItemStack stack) {
		return EnumRarity.COMMON;
	}
	
	@Override
	public boolean isEnchantable(@Nonnull ItemStack stack) {
		return false;
	}
	
	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
		ProxiedItemStackHandler handler = new ProxiedItemStackHandler(stack, 27);
		
		if (oldCapNbt != null && oldCapNbt.hasKey("Parent")) {
			NBTTagCompound itemData = oldCapNbt.getCompoundTag("Parent");
			ItemStackHandler stacks = new ItemStackHandler();
			stacks.deserializeNBT(itemData);
			
			for (int i = 0; i < stacks.getSlots(); i++)
				handler.setStackInSlot(i, stacks.getStackInSlot(i));
			
			oldCapNbt.removeTag("Parent");
		}
		
		return handler;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IItemColor getItemColor() {
		return (stack, i) -> i == 1 ? ((ItemBackpack) stack.getItem()).getColor(stack) : -1;
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.BODY;
	}
	
	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return !Backpacks.isEntityWearingBackpack(player, itemstack);
	}
	
	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return Backpacks.superOpMode || !doesBackpackHaveItems(itemstack);
	}
	
	public boolean hasColor(@Nonnull ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		return (nbttagcompound != null)
				&& nbttagcompound.hasKey("display", 10)
				&& nbttagcompound.getCompoundTag("display").hasKey("color", 3);
	}
	
	public int getColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		
		if (nbttagcompound != null) {
			NBTTagCompound display = nbttagcompound.getCompoundTag("display");
			
			if (display.hasKey("color", 3)) {
				return display.getInteger("color");
			}
		}
		
		return 10511680;
	}
	
	public void setColor(ItemStack stack, int color) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		
		if (nbttagcompound == null) {
			nbttagcompound = new NBTTagCompound();
			stack.setTagCompound(nbttagcompound);
		}
		
		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
		
		if (!nbttagcompound.hasKey("display", 10)) {
			nbttagcompound.setTag("display", nbttagcompound1);
		}
		
		nbttagcompound1.setInteger("color", color);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack itemStack, EntityPlayer player, RenderType renderType, float v) {
		if (!player.world.isRemote) return;

		if (renderType != RenderType.BODY) return;

		if (model == null) model = new ModelBackpack();
		
		model.setModelAttributes(new ModelPlayer(0.0F, false));
		
		Minecraft.getMinecraft().renderEngine.bindTexture(WORN_TEXTURE_RL);
		
		int i = backpack.getColor(itemStack);
		float red = (float) (i >> 16 & 255) / 255.0F;
		float green = (float) (i >> 8 & 255) / 255.0F;
		float blue = (float) (i & 255) / 255.0F;
		
		GlStateManager.color(red, green, blue, 1);
		

		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		float f = this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
		float f1 = this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
		float f2 = f1 - f;
//		float f4 = renderer.prepareScale(player, Minecraft.getMinecraft().getRenderPartialTicks());
		float f4 = 0.0625F;
		float f8 = (float) player.ticksExisted + partialTicks;
		float f5 = player.prevLimbSwingAmount + (player.limbSwingAmount - player.prevLimbSwingAmount) * partialTicks;
		float f6 = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);
		float f7 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
		
		model.setVisible(false);
		model.bipedBody.showModel = true;
		
		model.render(player, f6, f5, f8, f2, f7, f4);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(WORN_OVERLAY_TEXTURE_RL);
		
		GlStateManager.color(1, 1, 1, 1);
		
		model.render(player, 0, 0, 1000, 0, 0, 0.0625F);
	}
	
	/**
	 * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
	 * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
	 * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
	 */
	protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f;
		
		for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
		}
		
		while (f >= 180.0F) {
			f -= 360.0F;
		}
		
		return prevYawOffset + partialTicks * f;
	}
	
	@Nonnull
	@Override
	public Item setTranslationKey(@Nonnull String name) {
		super.setTranslationKey(name);
		setRegistryName(new ResourceLocation(getPrefix() + name));
		ProxyRegistry.register(this);
		
		return this;
	}
	
	@Nonnull
	@Override
	public String getTranslationKey(ItemStack par1ItemStack) {
		par1ItemStack.getItemDamage();
		
		return "item." + getPrefix() + bareName;
	}
	
	@Override
	public String[] getVariants() {
		return new String[] {bareName};
	}
	
}
