package vazkii.quark.base.capability;

import java.util.concurrent.Callable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.quark.api.ICustomSorting;
import vazkii.quark.api.IRuneColorProvider;
import vazkii.quark.api.ITransferManager;
import vazkii.quark.api.QuarkCapabilities;
import vazkii.quark.base.Quark;

@Mod.EventBusSubscriber(modid = Quark.MOD_ID)
public class CapabilityHandler {

	public static void setup() {
		registerLambda(IRuneColorProvider.class, (stack) -> -1);
		registerLambda(ITransferManager.class, (player) -> false);

		register(ICustomSorting.class, DummySorting::new);
	}

	private static <T> void registerLambda(Class<T> clazz, T provider) {
		register(clazz, () -> provider);
	}

	private static <T> void register(Class<T> clazz, Callable<T> provider) {
		CapabilityManager.INSTANCE.register(clazz, new CapabilityFactory<>(), provider);
	}

	private static class CapabilityFactory<T> implements Capability.IStorage<T> {

		@Override
		public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
			if (instance instanceof INBTSerializable)
				return ((INBTSerializable<?>) instance).serializeNBT();
			return null;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
			if (nbt instanceof CompoundNBT)
				((INBTSerializable<INBT>) instance).deserializeNBT(nbt);
		}

	}

	private static final ResourceLocation DROPOFF_MANAGER = new ResourceLocation(Quark.MOD_ID, "dropoff");
	private static final ResourceLocation SORTING_HANDLER = new ResourceLocation(Quark.MOD_ID, "sort");

	@SubscribeEvent
	public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
		Item item = event.getObject().getItem();

		if(item instanceof ICustomSorting)
			SelfProvider.attachItem(SORTING_HANDLER, QuarkCapabilities.SORTING, event);
	}

	@SubscribeEvent
	public static void attachTileCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof ITransferManager)
			SelfProvider.attach(DROPOFF_MANAGER, QuarkCapabilities.TRANSFER, event);
	}
}
