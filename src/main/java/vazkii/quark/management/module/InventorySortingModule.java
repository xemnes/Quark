package vazkii.quark.management.module;

import com.google.common.base.Supplier;

import vazkii.quark.base.handler.InventoryButtonHandler;
import vazkii.quark.base.handler.InventoryButtonHandler.ButtonProvider;
import vazkii.quark.base.handler.InventoryButtonHandler.ButtonTargetType;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.network.QuarkNetwork;
import vazkii.quark.base.network.message.SortInventoryMessage;
import vazkii.quark.management.client.gui.MiniInventoryButton;

@LoadModule(category = ModuleCategory.MANAGEMENT)
public class InventorySortingModule extends Module {

	@Config public static boolean enablePlayerInventory = true;
	@Config public static boolean enablePlayerInventoryInChests = true;
	@Config public static boolean enableChests = true;
	
	@Override
	public void clientSetup() {
		InventoryButtonHandler.addButtonProvider(this, ButtonTargetType.PLAYER_INVENTORY, 0, provider("sort", true, () -> enablePlayerInventory));
		InventoryButtonHandler.addButtonProvider(this, ButtonTargetType.CONTAINER_PLAYER_INVENTORY, 0, provider("sort_inventory", true, () -> enablePlayerInventoryInChests));
		InventoryButtonHandler.addButtonProvider(this, ButtonTargetType.CONTAINER_INVENTORY, 0, provider("sort_container", false, () -> enableChests));
	}
	
	private ButtonProvider provider(String tooltip, boolean forcePlayer, Supplier<Boolean> condition) {
		return (parent, x, y) -> !condition.get() ? null :
			new MiniInventoryButton(parent, 0, x, y, "quark.gui.button." + tooltip, (b) -> QuarkNetwork.sendToServer(new SortInventoryMessage(forcePlayer)));
	}
	
}
