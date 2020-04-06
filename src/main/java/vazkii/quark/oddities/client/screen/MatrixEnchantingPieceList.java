package vazkii.quark.oddities.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.list.ExtendedList;
import vazkii.quark.oddities.container.EnchantmentMatrix.Piece;

public class MatrixEnchantingPieceList extends ExtendedList<MatrixEnchantingPieceList.PieceEntry> {

	private final MatrixEnchantingScreen parent;
	private final int listWidth;

	public MatrixEnchantingPieceList(MatrixEnchantingScreen parent, int listWidth, int listHeight, int top, int bottom, int entryHeight) {
		super(parent.getMinecraft(), listWidth, listHeight, top, bottom, entryHeight);
		this.listWidth = listWidth;
		this.parent = parent;
		refresh();
	}

	@Override
	protected int getScrollbarPosition() {
		return this.listWidth;
	}

	@Override
	public int getRowWidth() {
		return this.listWidth;
	}

	public void refresh() {
		clearEntries();

		for(int i : parent.listPieces) {
			Piece piece = parent.getPiece(i);
			if(piece != null)
				addEntry(new PieceEntry(piece, i));
		}
	}

	@Override
	protected void renderBackground() {
		// NO-OP
	}

	protected class PieceEntry extends ExtendedList.AbstractListEntry<PieceEntry> {

		final Piece piece;
		final int index;

		PieceEntry(Piece piece, int index) {
			this.piece = piece;
			this.index = index;
		}

		@Override
		public void render(int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
			if(mouseX >= left && mouseX < left + entryWidth && mouseY >= top && mouseY <= top + entryHeight)
				parent.hoveredPiece = piece;

			parent.getMinecraft().getTextureManager().bindTexture(MatrixEnchantingScreen.BACKGROUND);
			RenderSystem.pushMatrix();
			RenderSystem.translatef(left + (listWidth - 7) / 2f, top + entryHeight / 2f, 0);
			RenderSystem.scaled(0.5, 0.5, 0.5);
			RenderSystem.translatef(-4, -8, 0);
			parent.renderPiece(piece, 1F);
			RenderSystem.popMatrix();
		}

		@Override
		public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
			parent.selectedPiece = index;
			setSelected(this);
			return false;
		}

	}

}