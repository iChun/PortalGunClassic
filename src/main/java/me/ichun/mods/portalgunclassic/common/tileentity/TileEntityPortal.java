package me.ichun.mods.portalgunclassic.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class TileEntityPortal extends TileEntity
{
    public boolean setup;
    public boolean top;
    public boolean orange;
    public EnumFacing face;

    public TileEntityPortal()
    {
        top = false;
        orange = false;
        face = EnumFacing.DOWN;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.getNbtCompound());
    }

    public void setup(boolean top, boolean orange, EnumFacing face)
    {
        this.setup = true;

        this.top = top;
        this.orange = orange;
        this.face = face;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean("setup", setup);
        tag.setBoolean("top", top);
        tag.setBoolean("orange", orange);
        tag.setInteger("face", face.getIndex());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        setup = tag.getBoolean("setup");
        top = tag.getBoolean("top");
        orange = tag.getBoolean("orange");
        face = EnumFacing.getFront(tag.getInteger("face"));
    }
}
