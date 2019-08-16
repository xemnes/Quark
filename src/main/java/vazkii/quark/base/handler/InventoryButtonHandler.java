package vazkii.quark.base.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.function.Predicate;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.Module;

@EventBusSubscriber(modid = Quark.MOD_ID, value = Dist.CLIENT)
public final class InventoryButtonHandler {

	private static final Multimap<ButtonTargetType, ButtonProviderHolder> providers = Multimaps.newSetMultimap(new HashMap<>(), TreeSet::new);

	@SubscribeEvent
	public static void initGui(GuiScreenEvent.InitGuiEvent.Post event) {
		if(event.getGui() instanceof ContainerScreen) {
			Minecraft mc = Minecraft.getInstance();
			ContainerScreen<?> screen = (ContainerScreen<?>) event.getGui();

			if(screen instanceof InventoryScreen)
				applyProviders(event, ButtonTargetType.PLAYER_INVENTORY, screen, s -> s.inventory == mc.player.inventory && s.getSlotIndex() == 17);
			else {
				if(InventoryTransferHandler.accepts(screen.getContainer(), mc.player)) { 
					applyProviders(event, ButtonTargetType.CONTAINER_INVENTORY, screen, s -> s.inventory != mc.player.inventory && s.getSlotIndex() == 8);
					applyProviders(event, ButtonTargetType.CONTAINER_PLAYER_INVENTORY, screen, s -> s.inventory == mc.player.inventory && s.getSlotIndex() == 17);
				}
			}
		}
	}

	private static void applyProviders(GuiScreenEvent.InitGuiEvent.Post event, ButtonTargetType type, ContainerScreen<?> screen, Predicate<Slot> slotPred) {
		Collection<ButtonProviderHolder> holders = providers.get(type);
		if(!holders.isEmpty()) {
			for(Slot slot : screen.getContainer().inventorySlots)
				if(slotPred.test(slot)) {
					int x = slot.xPos + 6;
					int y = slot.yPos - 13;

					for(ButtonProviderHolder holder : holders) {
						Button button = holder.getButton(screen, x, y);
						if(button != null) {
							event.addWidget(button);
							x -= 12;
						}
					}

					return;
				}
		}
	}

	public static void addButtonProvider(Module module, ButtonTargetType type, int priority, ButtonProvider provider) {
		providers.put(type, new ButtonProviderHolder(module, priority, provider));
	}

	public static enum ButtonTargetType {
		PLAYER_INVENTORY,
		CONTAINER_INVENTORY,
		CONTAINER_PLAYER_INVENTORY
	}

	public static interface ButtonProvider {
		Button provide(ContainerScreen<?> parent, int x, int y);
	}

	private static class ButtonProviderHolder implements Comparable<ButtonProviderHolder> {

		final int priority;
		final Module module;
		final ButtonProvider provider;

		public ButtonProviderHolder(Module module, int priority, ButtonProvider provider) {
			this.module = module;
			this.priority = priority;
			this.provider = provider;
		}

		@Override
		public int compareTo(ButtonProviderHolder o) {
			return priority - o.priority;
		}

		public Button getButton(ContainerScreen<?> parent, int x, int y) {
			return module.enabled ? provider.provide(parent, x, y) : null;
		}

	}

}
