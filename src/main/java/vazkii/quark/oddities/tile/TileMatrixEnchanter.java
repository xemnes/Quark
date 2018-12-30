package vazkii.quark.oddities.tile;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import vazkii.arl.block.tile.TileSimpleInventory;
import vazkii.quark.oddities.inventory.ContainerMatrixEnchanting;

// mostly a copy of TileEntityEnchantmentTable
public class TileMatrixEnchanter extends TileSimpleInventory implements ITickable, IInteractionObject {

    public int tickCount;
    public float pageFlip, pageFlipPrev, flipT, flipA, bookSpread, bookSpreadPrev, bookRotation, bookRotationPrev, tRot;
    
    private static final Random rand = new Random();
    private String customName;
	
	@Override
	public int getSizeInventory() {
		return 3;
	}
	
	@Override
	public boolean isAutomationEnabled() {
		return false;
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if(hasCustomName())
            compound.setString("CustomName", customName);

        return compound;
    }

	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("CustomName", 8))
            customName = compound.getString("CustomName");
    }

	@Override
	public void update() {
		doVanillaUpdateyThings();
    }
	
	private void doVanillaUpdateyThings() {
		bookSpreadPrev = bookSpread;
        bookRotationPrev = bookRotation;
        EntityPlayer entityplayer = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, false);

        if(entityplayer != null) {
            double d0 = entityplayer.posX - pos.getX() + 0.5;
            double d1 = entityplayer.posZ - pos.getZ() + 0.5;
            tRot = (float) MathHelper.atan2(d1, d0);
            bookSpread += 0.1F;

            if(bookSpread < 0.5F || rand.nextInt(40) == 0) {
                float f1 = flipT;

                while(true) {
                    flipT += rand.nextInt(4) - rand.nextInt(4);

                    if(f1 != flipT)
                        break;
                }
            }
        } else {
            tRot += 0.02F;
            bookSpread -= 0.1F;
        }

        while(bookRotation >= Math.PI)
            bookRotation -= (Math.PI * 2);
        while(bookRotation < -Math.PI)
            bookRotation += (Math.PI * 2F);
        while (tRot >= Math.PI)
            tRot -= (Math.PI * 2F);
        while (tRot < Math.PI)
            tRot += (Math.PI * 2F);

        float f2;
        for(f2 = tRot - bookRotation; f2 >= Math.PI; f2 -= (Math.PI * 2F));

        while(f2 < -Math.PI)
            f2 += (Math.PI * 2F);

        bookRotation += f2 * 0.4F;
        bookSpread = MathHelper.clamp(bookSpread, 0.0F, 1.0F);
        ++tickCount;
        pageFlipPrev = pageFlip;
        float f = (flipT - pageFlip) * 0.4F;
        float f3 = 0.2F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        flipA += (f - flipA) * 0.9F;
        pageFlip += flipA;
	}

	public void dropItem(int i) {
		ItemStack stack = getStackInSlot(i);
		if(!stack.isEmpty())
			InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	}
	
	@Override
	public String getName() {
        return hasCustomName() ? customName : "container.enchant";
    }

	@Override
	public boolean hasCustomName() {
        return customName != null && !customName.isEmpty();
    }

    public void setCustomName(String customNameIn) {
        customName = customNameIn;
    }

    @Override
    public ITextComponent getDisplayName() {
        return hasCustomName() ? new TextComponentString(getName()) : new TextComponentTranslation(getName(), new Object[0]);
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerMatrixEnchanting(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return "minecraft:enchanting_table";
    }

}
