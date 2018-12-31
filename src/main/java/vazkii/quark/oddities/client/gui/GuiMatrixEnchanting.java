package vazkii.quark.oddities.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GLSync;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import vazkii.arl.network.NetworkHandler;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.network.message.MessageMatrixEnchanterOperation;
import vazkii.quark.oddities.inventory.ContainerMatrixEnchanting;
import vazkii.quark.oddities.inventory.EnchantmentMatrix;
import vazkii.quark.oddities.inventory.EnchantmentMatrix.Piece;
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
	public void initGui() {
		super.initGui();
		
		addButton(new GuiButton(0, 20, 20, 40, 20, "Add"));
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
		
		EnchantmentMatrix matrix = enchanter.matrix;
		if(matrix != null)
			renderMatrixGrid(matrix);
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
	
	private void renderMatrixGrid(EnchantmentMatrix matrix) {
		int i = 0;
		
        mc.getTextureManager().bindTexture(BACKGROUND);
		GlStateManager.pushMatrix();
		GlStateManager.translate(86, 11, 0);
		for(Piece p : matrix.pieces.values()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(i * 30, 0, 0); // TODO
			renderPiece(p);
			GlStateManager.popMatrix();
			
			i++;
		}
		GlStateManager.popMatrix();
	}
	
	private void renderPiece(Piece piece) {
		float r = (float) ((piece.color >> 16) & 0xFF) / 255F;
		float g = (float) ((piece.color >> 8) & 0xFF) / 255F;
		float b = (float) (piece.color & 0xFF) / 255F;
		GlStateManager.color(r, g, b);
		
		for(int[] block : piece.blocks)
			renderBlock(block[0], block[1], piece.type);
		
		GlStateManager.color(1F, 1F, 1F);
	}
	
	private void renderBlock(int x, int y, int type) {
        drawTexturedModalRect(x * 10, y * 10, 11 + type * 10, ySize, 10, 10);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		add();
	}
	
	public void add() {
		send(TileMatrixEnchanter.OPER_ADD, 0, 0, 0);
	}
	
	public void place(int id, int x, int y) {
		send(TileMatrixEnchanter.OPER_PLACE, id, x, y);
	}
	
	public void remove(int id) {
		send(TileMatrixEnchanter.OPER_REMOVE, id, 0, 0);
	}
	
	public void rotate(int id) {
		send(TileMatrixEnchanter.OPER_REMOVE, id, 0, 0);
	}
	
	private void send(int operation, int arg0, int arg1, int arg2) {
		MessageMatrixEnchanterOperation message = new MessageMatrixEnchanterOperation(operation, arg0, arg1, arg2);
		NetworkHandler.INSTANCE.sendToServer(message);
	}
	
}
