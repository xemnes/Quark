package vazkii.quark.oddities.client.screen;

import java.util.LinkedList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.base.Quark;
import vazkii.quark.base.network.QuarkNetwork;
import vazkii.quark.base.network.message.MatrixEnchanterOperationMessage;
import vazkii.quark.oddities.container.EnchantmentMatrix;
import vazkii.quark.oddities.container.EnchantmentMatrix.Piece;
import vazkii.quark.oddities.container.MatrixEnchantingContainer;
import vazkii.quark.oddities.module.MatrixEnchantingModule;
import vazkii.quark.oddities.tile.MatrixEnchantingTableTileEntity;

public class MatrixEnchantingScreen extends ContainerScreen<MatrixEnchantingContainer> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation(Quark.MOD_ID, "textures/misc/matrix_enchanting.png");

	protected final PlayerInventory playerInv;
	protected final MatrixEnchantingTableTileEntity enchanter;

	protected Button plusButton;
	protected MatrixEnchantingPieceList pieceList;
	protected Piece hoveredPiece;

	protected int selectedPiece = -1;
	protected int gridHoverX, gridHoverY;
	protected List<Integer> listPieces = null;

	public MatrixEnchantingScreen(MatrixEnchantingContainer container, PlayerInventory inventory, ITextComponent component) {
		super(container, inventory, component);
		this.playerInv = inventory;
		this.enchanter = container.enchanter;
	}

	@Override
	public void init(Minecraft mc, int x, int y) {
		super.init(mc, x, y);

		selectedPiece = -1;
		addButton(plusButton = new MatrixEnchantingPlusButton(guiLeft + 86, guiTop + 63, this::add));
		pieceList = new MatrixEnchantingPieceList(this, 28, 64, guiTop + 11, guiTop + 75, 22);
		pieceList.setLeftPos(guiLeft + 139);
		children.add(pieceList);
		updateButtonStatus();
		
		pieceList.refresh();
	}

	@Override
	public void tick() {
		super.tick();
		updateButtonStatus();

		if(enchanter.matrix == null) {
			selectedPiece = -1;
			pieceList.refresh();
		}
		
		if(enchanter.clientMatrixDirty) {
			pieceList.refresh();
			enchanter.clientMatrixDirty = false;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = getMinecraft();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(BACKGROUND);
		int i = guiLeft;
		int j = guiTop;
		blit(i, j, 0, 0, xSize, ySize);

		if(enchanter.charge > 0 && MatrixEnchantingModule.chargePerLapis > 0) {
			int maxHeight = 18;
			int barHeight = (int) (((float) enchanter.charge / MatrixEnchantingModule.chargePerLapis) * maxHeight);
			blit(i + 7, j + 32 + maxHeight - barHeight, 50, 176 + maxHeight - barHeight, 4, barHeight);
		}
		
		pieceList.render(mouseX, mouseY, partialTicks);

		if(enchanter.matrix != null && enchanter.matrix.canGeneratePiece(enchanter.bookshelfPower, enchanter.enchantability) && !mc.player.isCreative()) {
			int x = i + 74;
			int y = j + 58;
			int xpCost = enchanter.matrix.getNewPiecePrice();
			int xpMin = enchanter.matrix.getMinXpLevel(enchanter.bookshelfPower);
			boolean has = enchanter.matrix.validateXp(mc.player, enchanter.bookshelfPower);
			blit(x, y, 0, ySize, 10, 10);
			String text = String.valueOf(xpCost);

			if(!has && mc.player.experienceLevel < xpMin) {
				font.drawStringWithShadow("!", x + 6, y + 3, 0xFF0000);
				text = I18n.format("quark.gui.enchanting.min", xpMin);
			}

			x -= (font.getStringWidth(text) - 5);
			y += 3;
			font.drawString(text, x - 1, y, 0);
			font.drawString(text, x + 1, y, 0);
			font.drawString(text, x, y + 1, 0);
			font.drawString(text, x, y - 1, 0);
			font.drawString(text, x, y, has ? 0xc8ff8f : 0xff8f8f);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		font.drawString(enchanter.getDisplayName().getUnformattedComponentText(), 12, 5, 4210752);
		font.drawString(playerInv.getDisplayName().getUnformattedComponentText(), 8, ySize - 96 + 2, 4210752);

		if(enchanter.matrix != null) {
			boolean needsRefresh = listPieces == null;
			listPieces = enchanter.matrix.benchedPieces;
			if(needsRefresh)
				pieceList.refresh();
			renderMatrixGrid(enchanter.matrix);
		}
	}
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);

		if(enchanter.matrix != null)
			RenderHelper.disableStandardItemLighting();

		if(hoveredPiece != null) {
			List<String> tooltip = new LinkedList<>();
			tooltip.add(TextFormatting.AQUA + hoveredPiece.enchant.getDisplayName(hoveredPiece.level).getFormattedText().replaceAll("\\u00A7.", ""));

			if(hoveredPiece.influence > 0)
				tooltip.add(TextFormatting.GRAY + I18n.format("quark.gui.enchanting.influence", (int) (hoveredPiece.influence * MatrixEnchantingModule.influencePower * 100)));

			int max = hoveredPiece.getMaxXP();
			if(max > 0)
				tooltip.add(TextFormatting.GRAY + I18n.format("quark.gui.enchanting.upgrade", hoveredPiece.xp, max));

			if(gridHoverX == -1) {
				tooltip.add("");
				tooltip.add(TextFormatting.GRAY + I18n.format("quark.gui.enchanting.left_click"));
				tooltip.add(TextFormatting.GRAY + I18n.format("quark.gui.enchanting.right_click"));
			} else if(selectedPiece != -1) {
				Piece p = getPiece(selectedPiece);
				if(p != null && p.enchant == hoveredPiece.enchant && hoveredPiece.level < hoveredPiece.enchant.getMaxLevel()) {
					tooltip.add("");
					tooltip.add(TextFormatting.GRAY + I18n.format("quark.gui.enchanting.merge"));
				}
			}

			renderTooltip(tooltip, mouseX, mouseY);
		} else renderHoveredToolTip(mouseX, mouseY);
	}


	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		int gridMouseX = (int) (mouseX - guiLeft - 86);
		int gridMouseY = (int) (mouseY - guiTop - 11);

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

		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(enchanter.matrix == null)
			return true;

		if(mouseButton == 0 && gridHoverX != -1) { // left click
			int hover = enchanter.matrix.matrix[gridHoverX][gridHoverY];

			if(selectedPiece != -1) {
				if(hover == -1)
					place(selectedPiece, gridHoverX, gridHoverY);
				else merge(selectedPiece);
			} else {
				remove(hover);
				if(!hasShiftDown())
					selectedPiece = hover;
			}
		} else if(mouseButton == 1 && selectedPiece != -1) {
			rotate(selectedPiece);
		}
		
		return true;
	}
	
	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);


	}

	private void renderMatrixGrid(EnchantmentMatrix matrix) {
		Minecraft mc = getMinecraft();
		mc.getTextureManager().bindTexture(BACKGROUND);
		RenderSystem.pushMatrix();
		RenderSystem.translatef(86, 11, 0);

		for(int i : matrix.placedPieces) {
			Piece piece = getPiece(i);
			if (piece != null) {
				RenderSystem.pushMatrix();
				RenderSystem.translatef(piece.x * 10, piece.y * 10, 0);
				renderPiece(piece, 1F);
				RenderSystem.popMatrix();
			}
		}

		if(selectedPiece != -1 && gridHoverX != -1) {
			Piece piece = getPiece(selectedPiece);
			if(piece != null && !(hoveredPiece != null && piece.enchant == hoveredPiece.enchant && hoveredPiece.level < hoveredPiece.enchant.getMaxLevel())) {
				RenderSystem.pushMatrix();
				RenderSystem.translatef(gridHoverX * 10, gridHoverY * 10, 0);

				float a = 0.2F;
				if(matrix.canPlace(piece, gridHoverX, gridHoverY))
					a = (float) ((Math.sin(ClientTicker.total * 0.2) + 1) * 0.4 + 0.4);

				renderPiece(piece, a);
				RenderSystem.popMatrix();
			}
		}

		if(hoveredPiece == null && gridHoverX != -1)
			renderHover(gridHoverX, gridHoverY);

		RenderSystem.popMatrix();
	}

	protected void renderPiece(Piece piece, float a) {
		float r = ((piece.color >> 16) & 0xFF) / 255F;
		float g = ((piece.color >> 8) & 0xFF) / 255F;
		float b = (piece.color & 0xFF) / 255F;

		boolean hovered = hoveredPiece == piece;

		for(int[] block : piece.blocks)
			renderBlock(block[0], block[1], piece.type, r, g, b, a, hovered);

		RenderSystem.color3f(1F, 1F, 1F);
	}

	private void renderBlock(int x, int y, int type, float r, float g, float b, float a, boolean hovered) {
		RenderSystem.color4f(r, g, b, a);
		blit(x * 10, y * 10, 11 + type * 10, ySize, 10, 10);
		if(hovered)
			renderHover(x, y);
	}

	private void renderHover(int x, int y) {
		fill(x * 10, y * 10, x * 10 + 10, y * 10 + 10, 0x66FFFFFF);
	}

	public void add(Button button) {
		send(MatrixEnchantingTableTileEntity.OPER_ADD, 0, 0, 0);
	}

	public void place(int id, int x, int y) {
		send(MatrixEnchantingTableTileEntity.OPER_PLACE, id, x, y);
		selectedPiece = -1;
		click();
	}

	public void remove(int id) {
		send(MatrixEnchantingTableTileEntity.OPER_REMOVE, id, 0, 0);
	}

	public void rotate(int id) {
		send(MatrixEnchantingTableTileEntity.OPER_ROTATE, id, 0, 0);
	}

	public void merge(int id) {
		int hover = enchanter.matrix.matrix[gridHoverX][gridHoverY];
		Piece p = getPiece(hover);
		Piece p1 = getPiece(selectedPiece);
		if(p != null && p1 != null && p.enchant == p1.enchant && p.level < p.enchant.getMaxLevel()) {
			send(MatrixEnchantingTableTileEntity.OPER_MERGE, hover, id, 0);
			selectedPiece = -1;
			click();
		}
	}	

	private void send(int operation, int arg0, int arg1, int arg2) {
		MatrixEnchanterOperationMessage message = new MatrixEnchanterOperationMessage(operation, arg0, arg1, arg2);
		QuarkNetwork.sendToServer(message);
	}

	private void click() {
		getMinecraft().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	private void updateButtonStatus() {
		plusButton.active = (enchanter.matrix != null 
				&& (getMinecraft().player.isCreative() || enchanter.charge > 0)
				&& enchanter.matrix.validateXp(getMinecraft().player, enchanter.bookshelfPower)
				&& enchanter.matrix.canGeneratePiece(enchanter.bookshelfPower, enchanter.enchantability));
	}

	protected Piece getPiece(int id) {
		EnchantmentMatrix matrix = enchanter.matrix;
		if(matrix != null)
			return matrix.pieces.get(id);

		return null;
	}

}