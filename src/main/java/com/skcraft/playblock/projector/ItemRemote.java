package com.skcraft.playblock.projector;

import com.skcraft.playblock.PlayBlock;
import com.skcraft.playblock.PlayBlockCreativeTab;
import com.skcraft.playblock.queue.ExposedQueue;
import com.skcraft.playblock.util.StringUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRemote extends Item {

    public static final String INTERNAL_NAME = "playblock.remote";

    public ItemRemote() {
        setTranslationKey(ItemRemote.INTERNAL_NAME);
        setRegistryName(PlayBlock.MOD_ID, "remote");
        setCreativeTab(PlayBlockCreativeTab.tab);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (world.isRemote) {
            ExposedQueue queuable = getLinked(world, stack);
            if (queuable == null) {
                player.sendStatusMessage(new TextComponentString("Not linked."), true);
            } else {
                PlayBlock.getClientRuntime().showRemoteGui(player, queuable);
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tileEntity = world.getTileEntity(pos);
        ItemStack item = player.getHeldItem(hand);

        if (!(tileEntity instanceof ExposedQueue)) {
            return EnumActionResult.PASS;
        }

        ExposedQueue queuable = (ExposedQueue) tileEntity;

        if (!item.hasTagCompound()) {
            item.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = item.getTagCompound();
        item.getTagCompound().setInteger("dim", world.provider.getDimension());
        item.getTagCompound().setInteger("x", pos.getX());
        item.getTagCompound().setInteger("y", pos.getY());
        item.getTagCompound().setInteger("z", pos.getZ());

        player.sendStatusMessage(new TextComponentString("Remote linked!"), true);

        return EnumActionResult.SUCCESS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(item, worldIn, tooltip, flagIn);

        NBTTagCompound tag = item.getTagCompound();

        if (tag != null && tag.hasKey("x")) {
            int x = item.getTagCompound().getInteger("x");
            int y = item.getTagCompound().getInteger("y");
            int z = item.getTagCompound().getInteger("z");

            tooltip.add(StringUtils.translate("remote.linkedTo") + " " + x + ", " + y + ", " + z);
        } else {
            tooltip.add(StringUtils.translate("remote.instruction"));
        }
    }

    /**
     * Get the {@link ExposedQueue} from an instance of an item.
     * 
     * @param world
     *            the current world
     * @param item
     *            the item
     * @return the linked object, otherwise null
     */
    public static ExposedQueue getLinked(World world, ItemStack item) {
        if (!item.hasTagCompound()) {
            return null;
        }

        NBTTagCompound tag = item.getTagCompound();
        int dim = tag.getInteger("dim");
        int x = tag.getInteger("x");
        int y = tag.getInteger("y");
        int z = tag.getInteger("z");

        if (world.provider.getDimension() != dim) {
            return null;
        }

        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (!(tileEntity instanceof ExposedQueue)) {
            return null;
        }

        return (ExposedQueue) tileEntity;
    }

}
