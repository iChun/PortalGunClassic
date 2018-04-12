package me.ichun.mods.portalgunclassic.common.tileentity;

import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import me.ichun.mods.portalgunclassic.common.packet.PacketEntityLocation;
import me.ichun.mods.portalgunclassic.common.packet.PacketRequestTeleport;
import me.ichun.mods.portalgunclassic.common.portal.PortalInfo;
import me.ichun.mods.portalgunclassic.common.sounds.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityPortal extends TileEntity implements ITickable
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
    public void update()
    {
        if(top)
        {
            return; //The top never does anything and is there just to look pretty.
        }

        BlockPos pairLocation = BlockPos.ORIGIN;
        if(!world.isRemote)
        {
            if(!PortalGunClassic.eventHandlerServer.getSaveData(world).portalInfo.containsKey(world.provider.getDimension()))
            {
                return;
            }
            PortalInfo info = PortalGunClassic.eventHandlerServer.getSaveData(world).portalInfo.get(world.provider.getDimension()).get(orange ? "blue" : "orange");
            if(info == null)
            {
                return;
            }
            pairLocation = info.pos;
        }
        else
        {
            if(orange && !PortalGunClassic.eventHandlerClient.status.blue || !orange && !PortalGunClassic.eventHandlerClient.status.orange)
            {
                return;
            }
        }
        //Only hits here if we have a pair
        AxisAlignedBB aabbScan =   new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + (face.getAxis() != EnumFacing.Axis.Y ? 2 : 1), pos.getZ() + 1).expand(face.getFrontOffsetX() * 4, face.getFrontOffsetY() * 4, face.getFrontOffsetZ() * 4);
        AxisAlignedBB aabbInside = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + (face.getAxis() != EnumFacing.Axis.Y ? 2 : 1), pos.getZ() + 1).expand(face.getFrontOffsetX() * 9, face.getFrontOffsetY() * 9, face.getFrontOffsetZ() * 9).offset(-face.getFrontOffsetX() * 9.999D, -face.getFrontOffsetY() * 9.999D, -face.getFrontOffsetZ() * 9.999D);
        List<Entity> ents = world.getEntitiesWithinAABB(world.isRemote ? EntityPlayer.class : Entity.class, aabbScan);
        for(Entity ent : ents)
        {
            if(!world.isRemote && ent instanceof EntityPlayer)
            {
                continue; //we ignore players. They tell the server when they want a teleport.
            }

            if(ent.getEntityBoundingBox().offset(ent.motionX, ent.motionY, ent.motionZ).intersects(aabbInside))
            {
                if(world.isRemote)
                {
                    handleClientTeleport((EntityPlayer)ent);
                }
                else
                {
                    TileEntity te = world.getTileEntity(pairLocation);
                    if(te instanceof TileEntityPortal)
                    {
                        teleport(ent, (TileEntityPortal)te);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void handleClientTeleport(EntityPlayer player)
    {
        if(PortalGunClassic.eventHandlerClient.teleportCooldown <= 0 && player == Minecraft.getMinecraft().player)
        {
            PortalGunClassic.eventHandlerClient.teleportCooldown = 3;
            PortalGunClassic.channel.sendToServer(new PacketRequestTeleport(pos));
        }
    }

    //This code is bad. I know.
    public void teleport(Entity ent, TileEntityPortal pair)
    {
        ent.posX = pair.getPos().getX() + 0.5D - (0.5D - (ent.getEntityBoundingBox().maxX - ent.getEntityBoundingBox().minX) / 2D) * 0.99D * pair.face.getFrontOffsetX();
        ent.posZ = pair.getPos().getZ() + 0.5D - (0.5D - (ent.getEntityBoundingBox().maxZ - ent.getEntityBoundingBox().minZ) / 2D) * 0.99D * pair.face.getFrontOffsetZ();

        ent.posY = pair.getPos().getY() + (pair.face.getFrontOffsetY() < 0 ? -(ent.getEntityBoundingBox().maxY - ent.getEntityBoundingBox().minY) + 0.999D : 0.001D);

        if(face.getAxis() != EnumFacing.Axis.Y && pair.face.getAxis() != EnumFacing.Axis.Y) //horizontal
        {
            float yawDiff = face.getHorizontalAngle() - (pair.face.getOpposite().getHorizontalAngle());
            ent.prevRotationPitch -= yawDiff;
            ent.rotationYaw -= yawDiff;

            double mX = ent.motionX;
            double mZ = ent.motionZ;

            if(pair.face == face)
            {
                ent.motionX = -mX;
                ent.motionZ = -mZ;
            }
            else if(face.getAxis() == EnumFacing.Axis.X)
            {
                if(pair.face == EnumFacing.NORTH)
                {
                    ent.motionZ = -mX * - face.getFrontOffsetX();
                    ent.motionX = mZ * - face.getFrontOffsetX();
                }
                else if(pair.face == EnumFacing.SOUTH)
                {
                    ent.motionZ = mX * - face.getFrontOffsetX();
                    ent.motionX = -mZ * - face.getFrontOffsetX();
                }
            }
            else if(face.getAxis() == EnumFacing.Axis.Z)
            {
                if(pair.face == EnumFacing.EAST)
                {
                    ent.motionZ = -mX * - face.getFrontOffsetZ();
                    ent.motionX = mZ * - face.getFrontOffsetZ();
                }
                else if(pair.face == EnumFacing.WEST)
                {
                    ent.motionZ = mX * - face.getFrontOffsetZ();
                    ent.motionX = -mZ * - face.getFrontOffsetZ();
                }
            }
        }
        else if(face.getAxis() == EnumFacing.Axis.Y && pair.face.getAxis() != EnumFacing.Axis.Y) //from vertical to horizontal
        {
            ent.rotationPitch = 0F;
            ent.rotationYaw = pair.face.getHorizontalAngle();
            ent.motionX = Math.abs(ent.motionY) * pair.face.getFrontOffsetX();
            ent.motionZ = Math.abs(ent.motionY) * pair.face.getFrontOffsetZ();
            ent.motionY = 0D;
            ent.fallDistance = 0F;
        }
        else if(face.getAxis() != EnumFacing.Axis.Y && pair.face.getAxis() == EnumFacing.Axis.Y) //from horizontal to vertical
        {
            ent.motionY = Math.sqrt(ent.motionX * ent.motionX + ent.motionZ * ent.motionZ) * pair.face.getFrontOffsetY();
        }
        else //vertical only
        {
            if(pair.face == face)
            {
                ent.motionY = -ent.motionY;
            }
            ent.fallDistance = 0F;
        }
        ent.motionX += pair.face.getFrontOffsetX() * 0.2D;
        ent.motionY += pair.face.getFrontOffsetY() * 0.2D;
        ent.motionZ += pair.face.getFrontOffsetZ() * 0.2D;
        ent.setLocationAndAngles(ent.posX, ent.posY, ent.posZ, ent.rotationYaw, ent.rotationPitch);

        world.playSound(null, this.getPos().getX() + 0.5D, this.getPos().getY() + (face.getAxis() != EnumFacing.Axis.Y ? 1D : 0.5D), this.getPos().getZ() + 0.5D, SoundRegistry.enter, SoundCategory.BLOCKS, 0.1F, 1.0F);
        world.playSound(null, pair.getPos().getX() + 0.5D, pair.getPos().getY() + (pair.face.getAxis() != EnumFacing.Axis.Y ? 1D : 0.5D), pair.getPos().getZ() + 0.5D, SoundRegistry.exit, SoundCategory.BLOCKS, 0.1F, 1.0F);

        PortalGunClassic.channel.sendToAllAround(new PacketEntityLocation(ent), new NetworkRegistry.TargetPoint(ent.getEntityWorld().provider.getDimension(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 256));
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
