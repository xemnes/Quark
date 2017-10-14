package vazkii.quark.misc.feature;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.misc.block.BlockGlowstoneDust;
import vazkii.quark.misc.block.BlockGunpowder;

public class PlaceVanillaDusts extends Feature {

	public static Block glowstone_dust_block;
	public static Block gunpowder_block;
	
	boolean enableGlowstone, enableGunpowder;
	public static int gunpowderDelay;
	public static int gunpowderDelayNetherrack;
	
	@Override
	public void setupConfig() {
		enableGlowstone = loadPropBool("Enable Glowstone", "", true);
		enableGunpowder = loadPropBool("Enable Gunpowder", "", true);
		gunpowderDelay = loadPropInt("Gunpowder Delay", "Amount of ticks between each piece of gunpowder igniting the next", 10);
		gunpowderDelayNetherrack = loadPropInt("Gunpowder Delay on Netherrack", "Amount of ticks between each piece of gunpowder igniting the next, if on Netherrack", 5);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		if(enableGlowstone)
			glowstone_dust_block = new BlockGlowstoneDust();
		
		if(enableGunpowder)
			gunpowder_block = new BlockGunpowder();
	}
	
	@SubscribeEvent
	public void onRightClick(RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		EnumHand hand = event.getHand();
		EnumFacing face = event.getFace();
		ItemStack stack = event.getItemStack();
		
		if(enableGlowstone && stack.getItem() == Items.GLOWSTONE_DUST)
			setBlock(player, world, pos, hand, face, glowstone_dust_block);
		else if(enableGunpowder && stack.getItem() == Items.GUNPOWDER)
			setBlock(player, world, pos, hand, face, gunpowder_block);
	}
	
    private boolean setBlock(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, Block block) {
        boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        BlockPos blockpos = flag ? pos : pos.offset(facing);
        ItemStack itemstack = player.getHeldItem(hand);

        if(player.canPlayerEdit(blockpos, facing, itemstack) && worldIn.mayPlace(worldIn.getBlockState(blockpos).getBlock(), blockpos, false, facing, null) && block.canPlaceBlockAt(worldIn, blockpos)) {
            worldIn.setBlockState(blockpos, block.getDefaultState());

            if(player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, blockpos, itemstack);

            if(!player.capabilities.isCreativeMode)
            	itemstack.shrink(1);
            player.swingArm(hand);
            
            return true;
        }
        
        return false;
    }
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}

