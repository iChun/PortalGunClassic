package me.ichun.mods.portalgunclassic.common.packet;

import io.netty.buffer.ByteBuf;
import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import me.ichun.mods.portalgunclassic.common.portal.PortalInfo;
import me.ichun.mods.portalgunclassic.common.tileentity.TileEntityPortal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestTeleport implements IMessage
{
    public BlockPos pos;

    public PacketRequestTeleport()
    {}

    public PacketRequestTeleport(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
    }

    public static class Handler implements IMessageHandler<PacketRequestTeleport, IMessage>
    {
        @Override
        public IMessage onMessage(PacketRequestTeleport message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            TileEntity te = player.world.getTileEntity(message.pos);
            if(te instanceof TileEntityPortal)
            {
                TileEntityPortal portalCurrent = (TileEntityPortal)te;
                if(PortalGunClassic.eventHandlerServer.getSaveData(player.world).portalInfo.containsKey(player.world.provider.getDimension()))
                {
                    PortalInfo info = PortalGunClassic.eventHandlerServer.getSaveData(player.world).portalInfo.get(player.world.provider.getDimension()).get(portalCurrent.orange ? "blue" : "orange");
                    if(info != null)
                    {
                        te = player.world.getTileEntity(info.pos);
                        if(te instanceof TileEntityPortal)
                        {
                            //There is a pair! We can teleport!
                            TileEntityPortal portalDest = (TileEntityPortal)te;

                            portalCurrent.teleport(player, portalDest);

//                            ObfuscationReflectionHelper.setPrivateValue(NetHandlerPlayServer.class, player.connection, ObfuscationReflectionHelper.getPrivateValue(NetHandlerPlayServer.class, player.connection, "field_147368_e" ,"networkTickCount"), "field_184343_A" ,"lastPositionUpdate");
//                            player.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                            player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                        }
                    }
                }
            }
            return null;
        }
    }
}
