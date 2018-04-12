package me.ichun.mods.portalgunclassic.common.portal;

import me.ichun.mods.portalgunclassic.common.sounds.SoundRegistry;
import me.ichun.mods.portalgunclassic.common.tileentity.TileEntityPortal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PortalInfo
{
    public boolean isOrange;
    public BlockPos pos;

    public PortalInfo(boolean o, BlockPos poss)
    {
        isOrange = o;
        pos = poss;
    }

    public void kill(World world)
    {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityPortal)
        {
            TileEntityPortal portal = (TileEntityPortal)te;

            world.setBlockToAir(pos);
            if(portal.face.getAxis() != EnumFacing.Axis.Y)
            {
                BlockPos offset = portal.top ? pos.down() : pos.up();
                if(world.getTileEntity(offset) instanceof TileEntityPortal)
                {
                    world.setBlockToAir(offset);
                }
            }

            world.playSound(null, pos.getX() + (portal.face.getAxis() != EnumFacing.Axis.Y ? 1D : 0.5D), pos.getY() + (portal.face.getAxis() == EnumFacing.Axis.Y ? 0.0D : 0.5D), pos.getZ() + (portal.face.getAxis() != EnumFacing.Axis.Y ? 1D : 0.5D), SoundRegistry.fizzle, SoundCategory.BLOCKS, 0.3F, 1F);
        }
        else
        {
            world.setBlockToAir(pos);
        }
    }

    public NBTTagCompound toNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("orange", isOrange);
        tag.setLong("pos", pos.toLong());
        return tag;
    }

    public static PortalInfo createFromNBT(NBTTagCompound tag)
    {
        return new PortalInfo(tag.getBoolean("orange"), BlockPos.fromLong(tag.getLong("pos")));
    }
}
