package vazkii.quark.automation.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.DispenserContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import vazkii.quark.automation.module.FeedingTroughModule;
import vazkii.quark.base.handler.MiscUtil;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * @author WireSegal
 * Created at 9:39 AM on 9/20/19.
 */
public class FeedingTroughTileEntity extends LockableLootTileEntity {

    private static final GameProfile DUMMY_PROFILE = new GameProfile(UUID.randomUUID(), "[FeedingTrough]");

    private NonNullList<ItemStack> stacks;

    private FakePlayer foodHolder = null;

    protected FeedingTroughTileEntity(TileEntityType<? extends FeedingTroughTileEntity> type) {
        super(type);
        this.stacks = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    public FeedingTroughTileEntity() {
        this(FeedingTroughModule.tileEntityType);
    }

    public FakePlayer getFoodHolder(TemptGoal goal) {
        if (foodHolder == null && world instanceof ServerWorld)
            foodHolder = new FakePlayer((ServerWorld) world, DUMMY_PROFILE);

        if (foodHolder != null) {
            for (int i = 0; i < getSizeInventory(); i++) {
                ItemStack stack = getStackInSlot(i);
                if (goal.isTempting(stack)) {
                    foodHolder.inventory.mainInventory.set(foodHolder.inventory.currentItem, stack);
                    Vec2f angles = MiscUtil.getMinecraftAngles(goal.creature.getPositionVector()
                            .subtract(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));

                    foodHolder.moveToBlockPosAndAngles(pos, angles.x, angles.y);
                    return foodHolder;
                }
            }
        }

        return null;
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    @Nonnull
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("quark.container.feeding_trough");
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbt))
            ItemStackHelper.loadAllItems(nbt, this.stacks);

    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        if (!this.checkLootAndWrite(nbt))
            ItemStackHelper.saveAllItems(nbt, this.stacks);

        return nbt;
    }

    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(@Nonnull NonNullList<ItemStack> items) {
        this.stacks = items;
    }

    @Override
    @Nonnull
    protected Container createMenu(int id, @Nonnull PlayerInventory playerInventory) {
        return new DispenserContainer(id, playerInventory, this);
    }
}
