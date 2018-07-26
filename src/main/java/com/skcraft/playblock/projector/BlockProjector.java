package com.skcraft.playblock.projector;

import com.skcraft.playblock.GuiHandler;
import com.skcraft.playblock.PlayBlock;
import com.skcraft.playblock.PlayBlockCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * The projector block.
 */
public class BlockProjector extends Block {

    public static final String INTERNAL_NAME = "playblock.projector";
    public static final PropertyEnum<EnumFacing> facing = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.HORIZONTALS);

    public BlockProjector() {
        super(Material.IRON);
        setHardness(0.5F);
        setLightLevel(1.0F);
        setSoundType(SoundType.GLASS);
        setRegistryName("playblock:projector");
        setTranslationKey(INTERNAL_NAME);
        setCreativeTab(PlayBlockCreativeTab.tab);
        setDefaultState(this.blockState.getBaseState().withProperty(facing, EnumFacing.NORTH));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing against, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing blockFacing = placer.getHorizontalFacing().getOpposite();

        return this.getDefaultState().withProperty(facing, blockFacing);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);

        if (!(te instanceof TileEntityProjector) || playerIn.isSneaking()) {
            return false;
        }

        TileEntityProjector projector = (TileEntityProjector) te;

        // Show the GUI if it's the client
        playerIn.openGui(PlayBlock.instance, GuiHandler.PROJECTOR, worldIn, pos.getX(), pos.getY(), pos.getZ());

        if (!worldIn.isRemote) {
            projector.getAccessList().allow(playerIn);
        }

        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(facing).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(facing, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityProjector();
    }
}
