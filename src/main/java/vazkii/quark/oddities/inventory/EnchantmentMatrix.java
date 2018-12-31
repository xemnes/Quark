package vazkii.quark.oddities.inventory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class EnchantmentMatrix {
	
	public static final int MATRIX_WIDTH = 5;
	public static final int MATRIX_HEIGHT = 5;
	
	private static final int PIECE_VARIANTS = 8;
	
	private static final String TAG_PIECES = "pieces";
	private static final String TAG_PIECE_ID = "id";
	private static final String TAG_BENCHED_PIECES = "benchedPieces";
	private static final String TAG_PLACED_PIECES = "placedPieces";
	private static final String TAG_COUNT = "count";
	
	public Map<Integer, Piece> pieces = new HashMap();
	public List<Integer> benchedPieces = new ArrayList();
	public List<Integer> placedPieces = new ArrayList();
	
	public Piece[][] matrix = new Piece[MATRIX_WIDTH][MATRIX_HEIGHT];
	
	public int count = 0;
	
	public EnchantmentMatrix() {
		generatePiece(); // TODO test
	}
	
	public boolean generatePiece() {
		Enchantment enchant = Enchantments.UNBREAKING; // TODO test
		int level = 1;
		
		Piece piece = new Piece(enchant, level, count % PIECE_VARIANTS);
		piece.generateBlocks();
		pieces.put(count, piece);
		benchedPieces.add(count);
		count++;
		
		return true;
	}
	
	public boolean place(int id, int x, int y) {
		Piece p = pieces.get(id);
		if(p != null && benchedPieces.contains(id) && canPlace(p, x, y)) {
			p.x = x;
			p.y = y;
			
			benchedPieces.remove(id);
			placedPieces.add(id);
			
			computeMatrix();
			return true;
		}
		
		return false;
	}
	
	public boolean remove(int id) {
		Piece p = pieces.get(id);
		if(p != null && placedPieces.contains(id)) {
			placedPieces.remove(id);
			benchedPieces.add(id);
			
			computeMatrix();
			return true;
		}
		
		return false;
	}
	
	public boolean rotate(int id) {
		Piece p = pieces.get(id);
		if(p != null && benchedPieces.contains(id)) {
			p.rotate();
			return true;
		}
		
		return false;
	}
	
	public void writeToNBT(NBTTagCompound cmp) {
		NBTTagList list = new NBTTagList();
		for(Integer i : pieces.keySet()) {
			NBTTagCompound pcmp = new NBTTagCompound();
			
			pcmp.setInteger(TAG_PIECE_ID, i);
			pieces.get(i).writeToNBT(pcmp);
			
			list.appendTag(pcmp);
		}
		
		cmp.setTag(TAG_PIECES, list);
		cmp.setIntArray(TAG_BENCHED_PIECES, packList(benchedPieces));
		cmp.setIntArray(TAG_PLACED_PIECES, packList(placedPieces));
		cmp.setInteger(TAG_COUNT, count);
	}
	
	public void readFromNBT(NBTTagCompound cmp) {
		pieces.clear();
		NBTTagList plist = cmp.getTagList(TAG_PIECES, cmp.getId());
		for(int i = 0; i < plist.tagCount(); i++) {
			NBTTagCompound pcmp = plist.getCompoundTagAt(i);
			
			int id = pcmp.getInteger(TAG_PIECE_ID);
			Piece piece = new Piece();
			piece.readFromNBT(pcmp);
			pieces.put(id, piece);
		}
		
		benchedPieces = unpackList(cmp.getIntArray(TAG_BENCHED_PIECES));
		placedPieces = unpackList(cmp.getIntArray(TAG_PLACED_PIECES));
		count = cmp.getInteger(TAG_COUNT);
		
		computeMatrix();
	}
	
	private void computeMatrix() {
		matrix = new Piece[MATRIX_WIDTH][MATRIX_HEIGHT];
		
		for(Integer i : placedPieces) {
			Piece p = pieces.get(i);
			for(int[] b : p.blocks)
				matrix[p.x + b[0]][p.y + b[1]] = p;
		}
	}
	
	private boolean canPlace(Piece p, int x, int y) {
		for(Integer i : placedPieces) {
			Piece pp = pieces.get(i);
			if(p.collides(pp, x, y))
				return false;
		}
		
		return true;
	}
	
	private int[] packList(List<Integer> list) {
		int[] arr = new int[list.size()];
		for(int i = 0; i < arr.length; i++)
			arr[i] = list.get(i);
		return arr;
	}
	
	private List<Integer> unpackList(int[] arr) {
		List<Integer> list = new ArrayList(arr.length);
		for(int i = 0; i < arr.length; i++)
			list.set(i, arr[i]);
		
		return list;
	}
	
	public static class Piece {

		private static final String TAG_COLOR = "color";
		private static final String TAG_TYPE = "type";
		private static final String TAG_ENCHANTMENT = "enchant";
		private static final String TAG_LEVEL = "level";
		private static final String TAG_BLOCK_COUNT = "blockCount";
		private static final String TAG_BLOCK = "block";
		private static final String TAG_X = "x";
		private static final String TAG_Y = "y";

		public Enchantment enchant;
		public int level, color, type, x, y;
		public int[][] blocks;
		
		Piece() { }
		
		Piece(Enchantment enchant, int level, int type) {
			this.enchant = enchant;
			this.level = level;
			this.color = Color.HSBtoRGB(new Random(enchant.getRegistryName().toString().hashCode()).nextFloat(), 0.9F, 1.0F);
			this.type = type;
		}
		
		public void generateBlocks() {
			int count = 5;
			blocks = new int[count][2];
			blocks[0] = new int[] { 0, 0 };
			blocks[1] = new int[] { 0, 1 };
			blocks[2] = new int[] { 1, 0 };
			blocks[3] = new int[] { 0, -1 };
			blocks[4] = new int[] { -1, 0 };
		}
		
		public boolean collides(Piece o, int x, int y) {
			int ox = x - o.x;
			int oy = y - o.y;

			for(int[] b : blocks) {
				int bx = b[0] + ox;
				int by = b[1] + bx;
				
				for(int[] ob : o.blocks)
					if(bx == ob[0] && by == ob[1])
						return true;
			}
			
			return false;
		}
		
		public void rotate() {
			// TODO
		}
		
		public void writeToNBT(NBTTagCompound cmp) {
			cmp.setInteger(TAG_COLOR, color);
			cmp.setInteger(TAG_TYPE, type);
			cmp.setString(TAG_ENCHANTMENT, enchant.getRegistryName().toString());
			cmp.setInteger(TAG_LEVEL, level);
			cmp.setInteger(TAG_X, x);
			cmp.setInteger(TAG_Y, y);

			cmp.setInteger(TAG_BLOCK_COUNT, blocks.length);
			for(int i = 0; i < blocks.length; i++)
				cmp.setIntArray(TAG_BLOCK + i, blocks[i]);
		}
		
		public void readFromNBT(NBTTagCompound cmp) {
			color = cmp.getInteger(TAG_COLOR);
			type = cmp.getInteger(TAG_TYPE);
			enchant = Enchantment.getEnchantmentByLocation(cmp.getString(TAG_ENCHANTMENT));
			level = cmp.getInteger(TAG_LEVEL);
			x = cmp.getInteger(TAG_X);
			y = cmp.getInteger(TAG_Y);
			
			blocks = new int[2][cmp.getInteger(TAG_BLOCK_COUNT)];
			for(int i = 0; i < blocks.length; i++)
				blocks[i] = cmp.getIntArray(TAG_BLOCK + i);
		}
		
	}	
	
}
