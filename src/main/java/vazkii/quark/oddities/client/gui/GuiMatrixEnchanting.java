package vazkii.quark.oddities.client.gui;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.network.message.MessageMatrixEnchanterOperation;
import vazkii.quark.oddities.client.gui.button.GuiButtonMatrixEnchantingPlus;
import vazkii.quark.oddities.inventory.ContainerMatrixEnchanting;
import vazkii.quark.oddities.inventory.EnchantmentMatrix;
import vazkii.quark.oddities.inventory.EnchantmentMatrix.Piece;
import vazkii.quark.oddities.tile.TileMatrixEnchanter;

public class GuiMatrixEnchanting extends GuiContainer {

	public static final ResourceLocation BACKGROUND = new ResourceLocation(LibMisc.MOD_ID, "textures/misc/matrix_enchanting.png");

	InventoryPlayer playerInv;
	TileMatrixEnchanter enchanter;
	
	GuiButton plusButton;
	PieceList pieceList;
	Piece hoveredPiece;
	
	int selectedPiece = -1;
	int gridHoverX, gridHoverY;
	List<Integer> listPieces = null;
	
	public GuiMatrixEnchanting(InventoryPlayer playerInv, TileMatrixEnchanter enchanter) {
		super(new ContainerMatrixEnchanting(playerInv, enchanter));
		this.playerInv = playerInv;
		this.enchanter = enchanter;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		selectedPiece = -1;
		addButton(plusButton = new GuiButtonMatrixEnchantingPlus(guiLeft + 86, guiTop + 63));
		pieceList = new PieceList(this, 29, 64, guiTop + 11, guiLeft + 139, 22);
		updateButtonStatus();
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		updateButtonStatus();
		
		if(enchanter.matrix == null)
			selectedPiece = -1;
	}

	@Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(BACKGROUND);
        int i = guiLeft;
        int j = guiTop;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
        
        if(enchanter.matrix != null && enchanter.matrix.canGeneratePiece(enchanter.bookshelfPower, enchanter.enchantability)) {
        	int x = i + 74;
        	int y = j + 64;
        	int xpCost = enchanter.matrix.getNewPiecePrice();
        	boolean has = mc.player.experienceLevel <= xpCost || mc.player.isCreative();
            drawTexturedModalRect(x, y, 0, ySize, 10, 10);
            String text = String.valueOf(xpCost);
            fontRenderer.drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) - 2, y, has ? 0xc8ff8f : 0xff8f8f);
        }
        
        // TODO test
        fontRenderer.drawStringWithShadow("Bookshelves: " + enchanter.bookshelfPower, i, j - 32, 0xFFFFFF);
        fontRenderer.drawStringWithShadow("Enchantability: " + enchanter.enchantability, i, j - 22, 0xFFFFFF);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(enchanter.getDisplayName().getUnformattedText(), 12, 5, 4210752);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
		
		if(enchanter.matrix != null) {
			listPieces = enchanter.matrix.benchedPieces;
			renderMatrixGrid(enchanter.matrix);
		}
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        if(enchanter.matrix != null) {
        	RenderHelper.enableGUIStandardItemLighting();
            pieceList.drawScreen(mouseX, mouseY, ClientTicker.partialTicks);
        }
        
        if(hoveredPiece != null) {
        	List<String> tooltip = new LinkedList();
        	tooltip.add(hoveredPiece.enchant.getTranslatedName(hoveredPiece.level));
        	if(gridHoverX == -1) {
        		tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("quarkmisc.matrixLeftClick"));
        		tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("quarkmisc.matrixRightClick"));
        	} else if(selectedPiece != -1) {
        		Piece p = getPiece(selectedPiece);
        		if(p.enchant == hoveredPiece.enchant && hoveredPiece.level < hoveredPiece.enchant.getMaxLevel())
        			tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("quarkmisc.matrixMerge"));
        	}
        	drawHoveringText(tooltip, mouseX, mouseY);
        } else renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
	public void handleMouseInput() throws IOException {
		int mouseX = Mouse.getEventX() * width / mc.displayWidth;
		int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
		
		if(enchanter.matrix != null)
			pieceList.handleMouseInput(mouseX, mouseY);
		
		int gridMouseX = mouseX - guiLeft - 86;
		int gridMouseY = mouseY - guiTop - 11;
		
		gridHoverX = gridMouseX < 0 ? -1 : gridMouseX / 10;
		gridHoverY = gridMouseY < 0 ? -1 : gridMouseY / 10;
		if(gridHoverX < 0 || gridHoverX > 4 || gridHoverY < 0 || gridHoverY > 4) {
			gridHoverX = -1;
			gridHoverY = -1;
			hoveredPiece = null;
		} else if(enchanter.matrix != null) {
			int hover = enchanter.matrix.matrix[gridHoverX][gridHoverY];
			hoveredPiece = getPiece(hover);
		}
		
		super.handleMouseInput();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(mouseButton == 0 && gridHoverX != -1) { // left click
			int hover = enchanter.matrix.matrix[gridHoverX][gridHoverY];

			if(selectedPiece != -1) {
				if(hover == -1)
					place(selectedPiece, gridHoverX, gridHoverY);
				else merge(selectedPiece, gridHoverX, gridHoverY);
			} else {
				remove(hover);
				if(!isShiftKeyDown())
					selectedPiece = hover;
			}
		} else if(mouseButton == 1 && selectedPiece != -1)
			rotate(selectedPiece);
	}
	
	private void renderMatrixGrid(EnchantmentMatrix matrix) {
        mc.getTextureManager().bindTexture(BACKGROUND);
		GlStateManager.pushMatrix();
		GlStateManager.translate(86, 11, 0);
		
		for(int i : matrix.placedPieces) {
			Piece piece = getPiece(i);
			GlStateManager.pushMatrix();
			GlStateManager.translate(piece.x * 10, piece.y * 10, 0);
			renderPiece(piece, 1F);
			GlStateManager.popMatrix();
		}
		
		if(selectedPiece != -1 && gridHoverX != -1) {
			Piece piece = getPiece(selectedPiece);
			if(piece != null) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(gridHoverX * 10, gridHoverY * 10, 0);
				
				float a = 0.2F;
				if(matrix.canPlace(piece, gridHoverX, gridHoverY))
					a = (float) ((Math.sin(ClientTicker.total * 0.2) + 1) * 0.4 + 0.4);
				
				renderPiece(piece, a);
				GlStateManager.popMatrix();
			}
		}
		
		GlStateManager.popMatrix();
	}
	
	private void renderPiece(Piece piece, float a) {
		float r = (float) ((piece.color >> 16) & 0xFF) / 255F;
		float g = (float) ((piece.color >> 8) & 0xFF) / 255F;
		float b = (float) (piece.color & 0xFF) / 255F;
		GlStateManager.color(r, g, b, a);
		
		for(int[] block : piece.blocks)
			renderBlock(block[0], block[1], piece.type);
		
		GlStateManager.color(1F, 1F, 1F);
	}
	
	private void renderBlock(int x, int y, int type) {
        drawTexturedModalRect(x * 10, y * 10, 11 + type * 10, ySize, 10, 10);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button == plusButton)
			add();
	}
	
	public void add() {
		send(TileMatrixEnchanter.OPER_ADD, 0, 0, 0);
	}
	
	public void place(int id, int x, int y) {
		send(TileMatrixEnchanter.OPER_PLACE, id, x, y);
		selectedPiece = -1;
	}
	
	public void remove(int id) {
		send(TileMatrixEnchanter.OPER_REMOVE, id, 0, 0);
	}
	
	public void rotate(int id) {
		send(TileMatrixEnchanter.OPER_ROTATE, id, 0, 0);
	}
	
	public void merge(int id, int x, int y) {
		int hover = enchanter.matrix.matrix[gridHoverX][gridHoverY];
		Piece p = getPiece(hover);
		Piece p1 = getPiece(selectedPiece);
		if(p != null && p1 != null && p.enchant == p1.enchant && p.level < p.enchant.getMaxLevel()) {
			send(TileMatrixEnchanter.OPER_MERGE, hover, id, 0);
			selectedPiece = -1;
		}
	}	
	
	private void send(int operation, int arg0, int arg1, int arg2) {
		MessageMatrixEnchanterOperation message = new MessageMatrixEnchanterOperation(operation, arg0, arg1, arg2);
		NetworkHandler.INSTANCE.sendToServer(message);
	}
	
	private void updateButtonStatus() {
		plusButton.enabled = (enchanter.matrix != null 
				&& !enchanter.getStackInSlot(1).isEmpty() 
				&& enchanter.matrix.canGeneratePiece(enchanter.bookshelfPower, enchanter.enchantability)
				&& (enchanter.matrix.getNewPiecePrice() < mc.player.experienceLevel || mc.player.isCreative()));
	}
	
	private Piece getPiece(int id) {
		EnchantmentMatrix matrix = enchanter.matrix;
		if(matrix != null)
			return matrix.pieces.get(id);
		
		return null;
	}
	
	public static class PieceList extends GuiScrollingList {

		private GuiMatrixEnchanting parent;
		private int mouseX, mouseY;
		
		public PieceList(GuiMatrixEnchanting parent, int width, int height, int top, int left, int entryHeight) {
			super(parent.mc, width, height, top, top + height, left, entryHeight, parent.width, parent.height);
			this.parent = parent;
		}
		
		@Override
		protected int getSize() {
			return parent.listPieces == null ? 0 : parent.listPieces.size();
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			int id = parent.listPieces.get(index);
			if(parent.selectedPiece == id)
				parent.selectedPiece = -1;
			else parent.selectedPiece = id;
		}

		@Override
		protected boolean isSelected(int index) {
			int id = parent.listPieces.get(index);
			return parent.selectedPiece == id;
		}

		@Override
		protected void drawBackground() {
			// NO-OP
		}
		
		@Override
		public void handleMouseInput(int mouseX, int mouseY) throws IOException {
			super.handleMouseInput(mouseX, mouseY);
			this.mouseX = mouseX;
			this.mouseY = mouseY;
		}

		@Override
		protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
			int id = parent.listPieces.get(slotIdx);
			
			Piece piece = parent.getPiece(id);
			if(piece != null) {
		        parent.mc.getTextureManager().bindTexture(BACKGROUND);
				GlStateManager.pushMatrix();
				GlStateManager.translate(left + (listWidth - 7) / 2, slotTop + slotHeight / 2, 0);
				GlStateManager.scale(0.5, 0.5, 0.5);
				GlStateManager.translate(-4, -8, 0);
				parent.renderPiece(piece, 1F);
				GlStateManager.popMatrix();
				
				if(mouseX >= left && mouseX < left + listWidth - 7 && mouseY >= slotTop && mouseY <= slotTop + slotHeight && mouseY < bottom)
					parent.hoveredPiece = piece;
			}
		}
		
	}
	
}
