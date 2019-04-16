package vazkii.quark.oddities.inventory;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.WeightedRandom;
import vazkii.quark.oddities.feature.MatrixEnchanting;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantmentMatrix {
	
	public static final int MATRIX_WIDTH = 5;
	public static final int MATRIX_HEIGHT = 5;
	
	private static final int PIECE_VARIANTS = 8;
	
	private static final String TAG_PIECES = "pieces";
	private static final String TAG_PIECE_ID = "id";
	private static final String TAG_BENCHED_PIECES = "benchedPieces";
	private static final String TAG_PLACED_PIECES = "placedPieces";
	private static final String TAG_COUNT = "count";
	private static final String TAG_TYPE_COUNT = "typeCount";

	public Map<Integer, Piece> pieces = new HashMap<>();
	public List<Integer> benchedPieces = new ArrayList<>();
	public List<Integer> placedPieces = new ArrayList<>();
	
	public int[][] matrix;
	public int count, typeCount;

	public final boolean book;
	public final ItemStack target;
	public final Random rng;
	
	public EnchantmentMatrix(ItemStack target, Random rng) {
		this.target = target;
		this.rng = rng;
		book = target.getItem() == Items.BOOK;
		computeMatrix();
	}

	public boolean canGeneratePiece(int bookshelfPower, int enchantability) {
		if(enchantability == 0)
			return false;
		
		if(book) {
			if(!MatrixEnchanting.allowBooks)
				return false;
			
			int bookshelfCount = Math.max(0, Math.min(bookshelfPower - 1, MatrixEnchanting.maxBookshelves)) / 7;
			int maxCount = MatrixEnchanting.baseMaxPieceCountBook + bookshelfCount;
			return count < maxCount;
		} else {
			int bookshelfCount = (Math.min(bookshelfPower, MatrixEnchanting.maxBookshelves) + 1) / 2;
			int enchantabilityCount = (Math.min(bookshelfPower, enchantability)) / 2;
			int maxCount = MatrixEnchanting.baseMaxPieceCount + bookshelfCount + enchantabilityCount;
			return count < maxCount;
		}
	}
	
	public boolean validateXp(EntityPlayer player, int bookshelfPower, int enchantability) {
		return player.isCreative() || (player.experienceLevel >= getMinXpLevel(bookshelfPower, enchantability) && player.experienceLevel >= getNewPiecePrice());
	}
	
	public int getMinXpLevel(int bookshelfPower, int enchantability) {
		float scale = MatrixEnchanting.minLevelScaleFactor;
		int cutoff = MatrixEnchanting.minLevelCutoff;
		
		if(book)
			return (int) (Math.min(bookshelfPower, MatrixEnchanting.maxBookshelves) * MatrixEnchanting.minLevelScaleFactorBook);
		else 
			return count > cutoff ? ((int) (cutoff * scale) - cutoff + count) : (int) (count * scale);
	}
	
	public int getNewPiecePrice() {
		return 1 + (MatrixEnchanting.piecePriceScale == 0 ? 0 : count / MatrixEnchanting.piecePriceScale); 
	}
	
	public boolean generatePiece(int bookshelfPower, int enchantability) {
		EnchantmentDataWrapper data = generateRandomEnchantment(bookshelfPower, enchantability);
		if (data == null)
			return false;
		
		int type = -1;
		for(Piece p : pieces.values())
			if(p.enchant == data.enchantment)
				type = p.type;
		
		if(type == -1) {
			type = typeCount % PIECE_VARIANTS;
			typeCount++;
		}

		Piece piece = new Piece(data.enchantment, data.enchantmentLevel, type, data.marked);
		piece.generateBlocks();
		
		pieces.put(count, piece);
		benchedPieces.add(count);
		count++;
		
		if(book && count == 1) {
			for (int i = 0; i < 2; i++)
				if (rng.nextBoolean())
					count++;
		}

		return true;
	}
	
	private EnchantmentDataWrapper generateRandomEnchantment(int bookshelfPower, int enchantability) {
		int level = book ? (MatrixEnchanting.bookEnchantability + rng.nextInt(Math.max(1, bookshelfPower) * 2)) : 0;
		
		List<Piece> marked = pieces.values().stream().filter(p -> p.marked).collect(Collectors.toList());
		
		List<EnchantmentDataWrapper> validEnchants = new ArrayList<>();
		for(Enchantment enchantment : Enchantment.REGISTRY) {
			if ((!enchantment.isTreasureEnchantment() || MatrixEnchanting.allowTreasures)
					&& !MatrixEnchanting.disallowedEnchantments.contains(enchantment.getRegistryName().toString())
					&& (enchantment.canApplyAtEnchantingTable(target) || (book && enchantment.isAllowedOnBooks()))) {
				int enchantLevel = 1;
				if (book) {
					for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
						if (level >= enchantment.getMinEnchantability(i) && level <= enchantment.getMaxEnchantability(i)) {
							enchantLevel = i;
							break;
						}
					}
				}

				EnchantmentDataWrapper wrapper = new EnchantmentDataWrapper(enchantment, enchantLevel);
				wrapper.normalizeRarity(marked);
				validEnchants.add(wrapper);
			}
		}

		if (validEnchants.isEmpty())
			return null;

		return WeightedRandom.getRandomItem(rng, validEnchants);
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
	
	public boolean merge(int placed, int hover) {
		Piece placedPiece = pieces.get(placed);
		Piece hoveredPiece = pieces.get(hover);
		if(placedPiece != null && hoveredPiece != null && placedPieces.contains(placed) && benchedPieces.contains(hover)) {
			Enchantment enchant = placedPiece.enchant;
			if(hoveredPiece.enchant == enchant && placedPiece.level < enchant.getMaxLevel()) {
				placedPiece.xp += hoveredPiece.level;
				int max = placedPiece.getMaxXP();
				while(placedPiece.xp >= max) {
					if(placedPiece.level >= enchant.getMaxLevel())
						break;
					
					placedPiece.level++;
					placedPiece.xp -= max;
					max = placedPiece.getMaxXP();
				}

				if(hoveredPiece.marked)
					placedPiece.marked = true;
				
				benchedPieces.remove(Integer.valueOf(hover));
				pieces.remove(hover);
				return true;
			}
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
		cmp.setInteger(TAG_TYPE_COUNT, typeCount);
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
		typeCount = cmp.getInteger(TAG_TYPE_COUNT);

		computeMatrix();
	}
	
	private void computeMatrix() {
		matrix = new int[MATRIX_WIDTH][MATRIX_HEIGHT];
		
		for(int i = 0; i < MATRIX_WIDTH; i++)
			for(int j = 0; j < MATRIX_HEIGHT; j++)
				matrix[i][j] = -1;
		
		for(Integer i : placedPieces) {
			Piece p = pieces.get(i);
			for(int[] b : p.blocks)
				matrix[p.x + b[0]][p.y + b[1]] = i;
		}
	}
	
	public boolean canPlace(Piece p, int x, int y) {
		for(int[] b : p.blocks) {
			int bx = b[0] + x;
			int by = b[1] + y;
			if(bx < 0 || by < 0 || bx >= MATRIX_WIDTH || by >= MATRIX_HEIGHT)
				return false;
			
			if(matrix[bx][by] != -1)
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
		List<Integer> list = new ArrayList<>(arr.length);
		for (int anArr : arr) list.add(anArr);
		
		return list;
	}
	
	public static class Piece {
		
		private static final int[][][] PIECE_TYPES = new int[][][] {
			{{0,0},	{-1,0},	{1,0},	{0,-1},	{0,1}}, // Plus
			{{0,0},	{-1,0},	{1,0},	{-1,-1},{0,-1}}, // Block
			{{0,0},	{-1,0},	{1,0},	{-1,1},	{1,1}}, // U
			{{0,0}, {-1,0},	{1,0},	{-1,-1},{1,1}}, // S
			{{0,0}, {-1,0},	{1,0},	{1,-1},	{1,1}}, // T
			{{0,0}, {-1,0},	{1,0},	{0,-1},	{1,1}}, // Twig
			{{0,0}, {-1,0},	{0,-1},	{-1,-1},{1,1}}, // Squiggle
			{{0,0},	{-1,0},	{1,0},	{0,-1},	{0,1},	{1,1}}, // Fish
			{{0,0}, {-1,0},	{0,-1},	{-1,-1},{-1,1},	{1,-1}}, // Stairs
			{{0,0},	{-1,0},	{0,-1},	{-1,-1},{-1,1},	{1,1}}, // J
			{{0,0},	{-1,0},	{1,0},	{-1,-1},{1,-1},	{1,1}}, // H
			{{0,0},	{-1,0},	{1,0},	{0,-1},	{-1,-1}, {1,1}} // weird block thing idk
		};
		
		private static final String TAG_COLOR = "color";
		private static final String TAG_TYPE = "type";
		private static final String TAG_ENCHANTMENT = "enchant";
		private static final String TAG_LEVEL = "level";
		private static final String TAG_BLOCK_COUNT = "blockCount";
		private static final String TAG_BLOCK = "block";
		private static final String TAG_X = "x";
		private static final String TAG_Y = "y";
		private static final String TAG_XP = "xp";
		private static final String TAG_MARKED = "marked";

		public Enchantment enchant;
		public int level, color, type, x, y, xp;
		public int[][] blocks;
		public boolean marked;
		
		Piece() { }
		
		Piece(Enchantment enchant, int level, int type, boolean marked) {
			this.enchant = enchant;
			this.level = level;
			this.type = type;
			this.marked = marked;
			
			Random rng = new Random(enchant.getRegistryName().toString().hashCode());
			float h = rng.nextFloat();
			float s = rng.nextFloat() * 0.2F + 0.8F;
			float b = rng.nextFloat() * 0.25F + 0.75F;
			this.color = Color.HSBtoRGB(h, s, b);
		}
		
		public void generateBlocks() {
			int type = (int) (Math.random() * PIECE_TYPES.length);
			int[][] copyPieces = PIECE_TYPES[type];
			blocks = new int[copyPieces.length][2];
			
			for(int i = 0; i < blocks.length; i++) {
				blocks[i][0] = copyPieces[i][0]; 
				blocks[i][1] = copyPieces[i][1];
			}
			
			int rotations = (int) (Math.random() * 4);
			for(int i = 0; i < rotations; i++)
				rotate();
		}
		
		public void rotate() {
			for (int[] b : blocks) {
				int x = b[0];
				int y = b[1];
				b[0] = y;
				b[1] = -x;
			}
		}
		
		public int getMaxXP() {
			if(level >= enchant.getMaxLevel())
				return 0;
			
			switch(enchant.getRarity()) {
			case COMMON:
				return (level - 1) + 1;
			case UNCOMMON:
				return level / 2 + 1;
			default:
				return 1;
			}
		}
		
		public void writeToNBT(NBTTagCompound cmp) {
			cmp.setInteger(TAG_COLOR, color);
			cmp.setInteger(TAG_TYPE, type);
			cmp.setString(TAG_ENCHANTMENT, enchant.getRegistryName().toString());
			cmp.setInteger(TAG_LEVEL, level);
			cmp.setInteger(TAG_X, x);
			cmp.setInteger(TAG_Y, y);
			cmp.setInteger(TAG_XP, xp);
			cmp.setBoolean(TAG_MARKED, marked);

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
			xp = cmp.getInteger(TAG_XP);
			marked = cmp.getBoolean(TAG_MARKED);
			
			blocks = new int[cmp.getInteger(TAG_BLOCK_COUNT)][2];
			for(int i = 0; i < blocks.length; i++)
				blocks[i] = cmp.getIntArray(TAG_BLOCK + i);
		}
	}
	
	private static class EnchantmentDataWrapper extends EnchantmentData {

		boolean marked;
		
		public EnchantmentDataWrapper(Enchantment enchantmentObj, int enchLevel) {
			super(enchantmentObj, enchLevel);
		}
		
		public void normalizeRarity(List<Piece> markedEnchants) {
			if(MatrixEnchanting.normalizeRarity) {
				itemWeight *= 100;
				switch(enchantment.getRarity()) {
				case COMMON:
					itemWeight = 800;
					break;
				case UNCOMMON:
					itemWeight = 400;
					break;
				case RARE:
					itemWeight = 250;
					break;
				case VERY_RARE:
					itemWeight = 50; 
				default: 
					break;
				}
				
				boolean mark = true;
				
				for(Piece other : markedEnchants) {
					if(other.enchant == enchantment) {
						itemWeight *= MatrixEnchanting.dupeMultiplier;
						mark = false;
						break;
					} else if(!other.enchant.isCompatibleWith(enchantment) || !enchantment.isCompatibleWith(other.enchant)) {
						itemWeight *= MatrixEnchanting.incompatibleMultiplier;
						mark = false;
						break;
					}
				}
				
				if(mark)
					marked = true;
			}
		}
		
	}
	
}


