package vazkii.quark.oddities.item;

import static vazkii.quark.oddities.feature.Backpacks.backpack;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.base.Predicates;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.render.IRenderBauble;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.item.ItemModArmor;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.base.handler.ProxiedItemStackHandler;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.oddities.client.model.ModelBackpack;
import vazkii.quark.oddities.feature.Backpacks;

@Optional.InterfaceList(value = {
	@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles", striprefs = true),
	@Optional.Interface(iface = "baubles.api.render.IRenderBauble", modid = "baubles", striprefs = true)
})
public class ItemBackpack extends ItemModArmor implements IBauble, IQuarkItem, IItemColorProvider, IRenderBauble {
	private static final String WORN_TEXTURE = LibMisc.PREFIX_MOD + "textures/misc/backpack_worn.png";
	private static final String WORN_OVERLAY_TEXTURE = LibMisc.PREFIX_MOD + "textures/misc/backpack_worn_overlay.png";
	
	private static final ResourceLocation WORN_TEXTURE_RL = new ResourceLocation(WORN_TEXTURE);
	private static final ResourceLocation WORN_OVERLAY_TEXTURE_RL = new ResourceLocation(WORN_OVERLAY_TEXTURE);

	public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {
		/**
		 * Dispense the specified stack, play the dispense sound and spawn particles.
		 */
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			ItemStack itemstack = ItemBackpack.dispenseBackpack(source, stack);
			return itemstack.isEmpty() ? super.dispenseStack(source, stack) : itemstack;
		}
	};
	
	public static ModelBackpack model;
	
	public ItemBackpack() {
		super("backpack", ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.CHEST);
		setCreativeTab(CreativeTabs.TOOLS);
		setMaxDamage(0);
		
		addPropertyOverride(new ResourceLocation("has_items"), (stack, world, entity) -> (!Backpacks.superOpMode && doesBackpackHaveItems(stack)) ? 1 : 0);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSER_BEHAVIOR);
	}

	public static ItemStack dispenseBackpack(IBlockSource blockSource, ItemStack stack) {
		BlockPos blockpos = blockSource.getBlockPos()
				.offset((EnumFacing) blockSource.getBlockState().getValue(BlockDispenser.FACING));
		List<EntityLivingBase> list = blockSource.getWorld().<EntityLivingBase>getEntitiesWithinAABB(
				EntityLivingBase.class, new AxisAlignedBB(blockpos),
				Predicates.and(EntitySelectors.NOT_SPECTATING, new EntitySelectors.ArmoredMob(stack)));

		if (list.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			EntityLivingBase entitylivingbase = list.get(0);

			if (entitylivingbase instanceof EntityPlayer && Loader.isModLoaded("baubles")) {
				EntityPlayer player = (EntityPlayer) entitylivingbase;
				IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
				for (int i = 0; i < baubles.getSlots(); i++) {
					if ((baubles.getStackInSlot(i) == null || baubles.getStackInSlot(i).isEmpty())
							&& baubles.isItemValidForSlot(i, stack, player)) {
						ItemStack splitstack = stack.splitStack(1);
						baubles.setStackInSlot(i, splitstack);
						if (splitstack.getItem() instanceof IBauble)
							((IBauble) splitstack.getItem()).onEquipped(splitstack, player);
						return stack;
					} else if (baubles.getStackInSlot(i) != null) {
						// If we are already wearing a backpack, don't put on another
						if (baubles.getStackInSlot(i).getItem() instanceof ItemBackpack) {
							return stack;
						}
					}
				}
			}

			EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(stack);
			ItemStack itemstack = stack.splitStack(1);
			entitylivingbase.setItemStackToSlot(entityequipmentslot, itemstack);

			if (entitylivingbase instanceof EntityLiving) {
				((EntityLiving) entitylivingbase).setDropChance(entityequipmentslot, 2.0F);
			}

			return stack;
		}
	}
	
	public static boolean doesBackpackHaveItems(ItemStack stack) {
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (handler == null) return false;
		for (int i = 0; i < handler.getSlots(); i++)
			if (!handler.getStackInSlot(i).isEmpty()) return true;
		
		return false;
	}
	
	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot) {
		return HashMultimap.create();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			if (Loader.isModLoaded("baubles")) {
				IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
				for (int i = 0; i < baubles.getSlots(); i++) {
					if ((baubles.getStackInSlot(i) == null || baubles.getStackInSlot(i).isEmpty())
							&& baubles.isItemValidForSlot(i, stack, player)) {
						baubles.setStackInSlot(i, stack.copy());
						if (!player.capabilities.isCreativeMode) {
							player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
						}
						onEquipped(stack, player);
						
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
					}
				}
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			} else {
				EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(stack);
				ItemStack stackInSlot = player.getItemStackFromSlot(entityequipmentslot);

				if (stackInSlot.isEmpty()) {
					player.setItemStackToSlot(entityequipmentslot, stack.copy());
					stack.setCount(stack.getCount()-1);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
				} else {
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (worldIn.isRemote) return;
		
		boolean hasItems = !Backpacks.superOpMode && doesBackpackHaveItems(stack);
		
		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
		boolean isCursed = enchants.containsKey(Enchantments.BINDING_CURSE);
		boolean changedEnchants = false;

		if (hasItems) {
			if (Backpacks.isEntityWearingBackpack(entityIn, stack)) {
				if (!isCursed) {
					enchants.put(Enchantments.BINDING_CURSE, 1);
					changedEnchants = true;
				}
			} else {
				ItemStack copy = stack.copy();
				stack.setCount(0);
				entityIn.entityDropItem(copy, 0);
			}
		} else if (isCursed) {
			enchants.remove(Enchantments.BINDING_CURSE);
			changedEnchants = true;
		}

		if (changedEnchants)
			EnchantmentHelper.setEnchantments(enchants, stack);
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
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return (type != null && type.equals("overlay")) ? WORN_OVERLAY_TEXTURE : WORN_TEXTURE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, 
			EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if (model == null)
			model = new ModelBackpack();

		return model;
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
	@Optional.Method(modid = "baubles")
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.BODY;
	}
	
	@Override
	@Optional.Method(modid = "baubles")
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return !Backpacks.isEntityWearingBackpack(player, itemstack);
	}
	
	@Override
	@Optional.Method(modid = "baubles")
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
	@Optional.Method(modid = "baubles")
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

	@Override
	public EntityEquipmentSlot getEquipmentSlot() {
		return Loader.isModLoaded("baubles") ? null : EntityEquipmentSlot.CHEST;
	}

	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
		if (stack.getItem() instanceof ItemBackpack && entity instanceof EntityPlayer) {
			if (Loader.isModLoaded("baubles"))
				return false;
		}
		return super.isValidArmor(stack, armorType, entity);
	}
}
