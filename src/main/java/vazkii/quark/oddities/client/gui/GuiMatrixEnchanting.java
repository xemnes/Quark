package vazkii.quark.oddities.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.oddities.inventory.ContainerMatrixEnchanting;
import vazkii.quark.oddities.tile.TileMatrixEnchanter;

public class GuiMatrixEnchanting extends GuiContainer {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(LibMisc.MOD_ID, "textures/misc/matrix_enchanting.png");

	InventoryPlayer playerInv;
	TileMatrixEnchanter enchanter;
	
	public GuiMatrixEnchanting(InventoryPlayer playerInv, TileMatrixEnchanter enchanter) {
		super(new ContainerMatrixEnchanting(playerInv, enchanter));
		this.playerInv = playerInv;
		this.enchanter = enchanter;
	}

	@Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(BACKGROUND);
        int i = guiLeft;
        int j = guiTop;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(enchanter.getDisplayName().getUnformattedText(), 12, 5, 4210752);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
	
}
