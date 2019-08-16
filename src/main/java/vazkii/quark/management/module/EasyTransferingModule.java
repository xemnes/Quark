package vazkii.quark.management.module;

import net.minecraft.client.gui.screen.Screen;
import vazkii.quark.base.handler.InventoryButtonHandler;
import vazkii.quark.base.handler.InventoryButtonHandler.ButtonTargetType;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.network.QuarkNetwork;
import vazkii.quark.base.network.message.InventoryTransferMessage;
import vazkii.quark.management.client.gui.MiniInventoryButton;

@LoadModule(category = ModuleCategory.MANAGEMENT)
public class EasyTransferingModule extends Module {

	@Override
	public void clientSetup() {
		addButton(1, "insert", false);
		addButton(2, "extract", true);
	}

	private void addButton(int priority, String name, boolean restock) {
		InventoryButtonHandler.addButtonProvider(this, ButtonTargetType.CONTAINER_PLAYER_INVENTORY, priority, 
				(parent, x, y) -> new MiniInventoryButton(parent, priority, x, y, "quark.gui.button." + name,
						(b) -> QuarkNetwork.sendToServer(new InventoryTransferMessage(Screen.hasShiftDown(), restock)))
				.setTextureShift(() -> Screen.hasShiftDown()));
	}

}
